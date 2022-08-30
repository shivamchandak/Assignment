package com.assignment.Assignment.kafka;

import com.assignment.Assignment.entity.SmsRequest;
import com.assignment.Assignment.entity.SmsRequestElasticSearch;
import com.assignment.Assignment.entity.SmsRequestStatus;
import com.assignment.Assignment.entity.requestJson.Channels;
import com.assignment.Assignment.entity.requestJson.Destination;
import com.assignment.Assignment.entity.requestJson.RequestJsonForThirdPartyApi;
import com.assignment.Assignment.entity.requestJson.Sms;
import com.assignment.Assignment.entity.responseJson.ResponseJsonFromThirdPartyApi;
import com.assignment.Assignment.repository.SmsRequestESRepository;
import com.assignment.Assignment.repository.SmsRequestRepository;
import com.assignment.Assignment.repository.BlacklistRedisRepositoryImpl;
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
	BlacklistRedisRepositoryImpl blacklistedRepository;
	private RestTemplate restTemplate = new RestTemplate();
	private static final String KEY = "BLACKLISTED";

	@Value("${topicName}")
	private String topicName;

	@Value("${URL}")
	private String URL;

	@Value("${apiKey}")
	private String apiKey;


	@KafkaListener(topics = "notification.send_sms", groupId = "myConsumerGroup")
	public void consume (String message) {

		LOGGER.info(String.format("Request Id received -> %s", message));
		LOGGER.info(String.format("Topic Name -> %s", topicName));

		SmsRequest smsRequest = smsRequestRepository.findById(Long.parseLong(message)).get();
		LOGGER.info(String.format("the current sms status is: %s", smsRequest));

		String phoneNumber = smsRequest.getPhoneNumber();
		LOGGER.info(String.format("checking if number is blacklisted %s", phoneNumber));
		LOGGER.info(String.format("blacklisted or not-> %s", redisTemplate.opsForHash().hasKey(KEY, phoneNumber)));

		if (blacklistedRepository.isPresentInBlacklist(phoneNumber)) {
			smsRequest.setStatus(SmsRequestStatus.valueOf("NUMBER_BLACKLISTED"));
			smsRequestRepository.save(smsRequest);
			LOGGER.info(String.format("the current sms status is: %s", smsRequest));
			return;
		}
		LOGGER.info(String.format("the current sms status is: %s", smsRequest));

		// Add sms to elastic search only if it is not blacklisted.
		SmsRequestElasticSearch smsRequestElasticSearch = SmsRequestElasticSearch.builder()
				.id(smsRequest.getRequestId())
				.message(smsRequest.getMessage())
				.phoneNumber(smsRequest.getPhoneNumber())
				.createdAt(smsRequest.getCreatedAt())
				.build();
		LOGGER.info(String.format("the SmsRequest entity is %s", smsRequest));
		LOGGER.info(String.format("the ES entity is %s", smsRequestElasticSearch));
		smsRequestESRepository.save(smsRequestElasticSearch);

		// Third party API call
		ResponseJsonFromThirdPartyApi response = callThirdPartyApi(smsRequest);
		String description = response.getResponse().get(0).getDescription();
		smsRequest.setStatusCode(response.getResponse().get(0).getCode());
		if (description.equalsIgnoreCase("queued")) {
			smsRequest.setStatus(SmsRequestStatus.valueOf("QUEUED"));
		} else {
			smsRequest.setStatus(SmsRequestStatus.valueOf("ERROR_IN_CALLING_3P_API"));
			smsRequest.setFailureComments(description);
		}
		smsRequestRepository.save(smsRequest);
		LOGGER.info(String.format("the current sms status is: %s", smsRequest));
		LOGGER.info(String.format("the response from 3P api is %s", response));

	}

	public ResponseJsonFromThirdPartyApi callThirdPartyApi (SmsRequest smsRequest) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("key", apiKey);

		Sms sms = Sms.builder()
				.text(smsRequest.getMessage())
				.build();

		Channels channels = Channels.builder()
				.sms(sms)
				.build();

		ArrayList<String> msisdn = new ArrayList<>();
		msisdn.add(smsRequest.getPhoneNumber());

		Destination dest = Destination.builder()
				.msisdn(msisdn)
				.correlationid("some_unique_id")
				.build();
		ArrayList<Destination> destination = new ArrayList<>();
		destination.add(dest);


		RequestJsonForThirdPartyApi requestJson = RequestJsonForThirdPartyApi.builder()
				.deliverychannel("sms")
				.channels(channels)
				.destination(destination)
				.build();

		HttpEntity<RequestJsonForThirdPartyApi> entity =
				new HttpEntity<>(requestJson, headers);
		ResponseJsonFromThirdPartyApi response =
				restTemplate.postForEntity(URL, entity,
						ResponseJsonFromThirdPartyApi.class).getBody();
		return response;
	}
}
