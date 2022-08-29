package com.assignment.Assignment.kafka;

import com.assignment.Assignment.entity.primary.SmsRequest;
import com.assignment.Assignment.entity.primary.SmsRequestElasticSearch;
import com.assignment.Assignment.entity.primary.SmsRequestStatus;
import com.assignment.Assignment.entity.primary.requestJson.Channels;
import com.assignment.Assignment.entity.primary.requestJson.Destination;
import com.assignment.Assignment.entity.primary.requestJson.RequestJsonForThirdPartyApi;
import com.assignment.Assignment.entity.primary.requestJson.Sms;
import com.assignment.Assignment.entity.primary.responseJson.ResponseJsonFromThirdPartyApi;
import com.assignment.Assignment.repository.primary.SmsRequestESRepository;
import com.assignment.Assignment.repository.primary.SmsRequestRepository;
import com.assignment.Assignment.repository.secondary.BlacklistedRepositoryRedisImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class KafkaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

	@Autowired
	SmsRequestRepository smsRequestRepository;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	SmsRequestESRepository smsRequestESRepository;

	@Autowired
	BlacklistedRepositoryRedisImplementation blacklistedRepository;
	private RestTemplate restTemplate = new RestTemplate();
	private static final String KEY = "BLACKLISTED";

//	@Value("${URL}")
//	private String URL;
// all constants in app.properties

	@KafkaListener(topics = "notification.send_sms", groupId = "myConsumerGroup")
	public void consume (String message) {

		LOGGER.info(String.format("Request Id received -> %s", message));
		SmsRequest smsRequest = smsRequestRepository.findById(Long.parseLong(message)).get();
		System.out.println(smsRequest.toString());

		String phoneNumber = smsRequest.getPhoneNumber();
		LOGGER.info(String.format("checking if number is blacklisted %s", phoneNumber));
		LOGGER.info(String.format("blacklisted or not-> %s", redisTemplate.opsForHash().hasKey(KEY, phoneNumber)));

		if (blacklistedRepository.isPresentInBlacklist(phoneNumber)) {
			smsRequest.setStatus(SmsRequestStatus.valueOf("NUMBER_BLACKLISTED"));
			smsRequestRepository.save(smsRequest);
			LOGGER.info(String.format("the current sms status is: %s", smsRequest.toString()));
			return;
		}
		LOGGER.info(String.format("the current sms status is: %s", smsRequest.toString()));

		// Add sms to elastic search only if it is not blacklisted.
		SmsRequestElasticSearch smsRequestElasticSearch = SmsRequestElasticSearch.builder()
				.id(smsRequest.getRequestId())
				.message(smsRequest.getMessage())
				.phoneNumber(smsRequest.getPhoneNumber())
				.createdAt(smsRequest.getCreatedAt())
				.build();
		LOGGER.info(String.format("the SmsRequest entity is %s", smsRequest.toString()));
		LOGGER.info(String.format("the ES entity is %s", smsRequestElasticSearch.toString()));
		smsRequestESRepository.save(smsRequestElasticSearch);

		// Third party API call
		ResponseJsonFromThirdPartyApi response = callThirdPartyApi(smsRequest);
		String description = response.getResponse().get(0).getDescription();
		smsRequest.setFailureCode(response.getResponse().get(0).getCode());
		if (description.equalsIgnoreCase("queued")) {
			smsRequest.setStatus(SmsRequestStatus.valueOf("QUEUED"));
		} else {
			smsRequest.setStatus(SmsRequestStatus.valueOf("ERROR_IN_CALLING_3P_API"));
			smsRequest.setFailureComments(description);
		}
		smsRequestRepository.save(smsRequest);
		LOGGER.info(String.format("the current sms status is: %s", smsRequest.toString()));
		LOGGER.info(String.format("the response from 3P api is %s", response.toString()));

	}

	public ResponseJsonFromThirdPartyApi callThirdPartyApi (SmsRequest smsRequest) {

		String url = "https://api.imiconnect.in/resources/v1/messaging";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("key", "93ceffda-5941-11ea-9da9-025282c394f2");

		Sms sms = Sms.builder()
				.text(smsRequest.getMessage())
				.build();

		Channels channels = Channels.builder()
				.sms(sms)
				.build();

		ArrayList<String> msisdn = new ArrayList<String>();
		msisdn.add(smsRequest.getPhoneNumber());

		Destination dest = Destination.builder()
				.msisdn(msisdn)
				.correlationid("some_unique_id")
				.build();
		ArrayList<Destination> destination = new ArrayList<Destination>();
		destination.add(dest);


		RequestJsonForThirdPartyApi requestJson = RequestJsonForThirdPartyApi.builder()
				.deliverychannel("sms")
				.channels(channels)
				.destination(destination)
				.build();

		HttpEntity<RequestJsonForThirdPartyApi> entity =
				new HttpEntity<RequestJsonForThirdPartyApi>(requestJson, headers);
		ResponseJsonFromThirdPartyApi response =
				restTemplate.postForEntity(url, entity,
						ResponseJsonFromThirdPartyApi.class).getBody();
		return response;
	}
}
