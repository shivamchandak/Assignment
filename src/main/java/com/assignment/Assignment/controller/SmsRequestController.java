package com.assignment.Assignment.controller;

import com.assignment.Assignment.entity.RequestBodyForSearch;
import com.assignment.Assignment.entity.SmsRequest;
import com.assignment.Assignment.entity.SmsRequestElasticSearch;
import com.assignment.Assignment.entity.SuccessResponse;
import com.assignment.Assignment.entity.BlacklistRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;
import com.assignment.Assignment.error.PhoneNumberMissingException;
import com.assignment.Assignment.error.SmsNotFoundException;
import com.assignment.Assignment.kafka.KafkaProducer;
import com.assignment.Assignment.repository.SmsRequestESRepository;
import com.assignment.Assignment.service.SmsRequestService;
import com.assignment.Assignment.service.BlacklistedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SmsRequestController {

	@Autowired
	SmsRequestService smsRequestService;
	@Autowired
	BlacklistedService blacklistedService;
	@Autowired
	KafkaProducer kafkaProducer;
	@Autowired
	SmsRequestESRepository smsRequestESRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(SmsRequestController.class);

	@PostMapping("v1/sms/send")
	public ResponseEntity<SuccessResponse> sendSmsRequest(
			@RequestBody SmsRequest smsRequest) throws PhoneNumberMissingException, InvalidPhoneNumberException {

		SmsRequest smsRequestWithId = smsRequestService.sendSmsRequest(smsRequest);
		SuccessResponse successResponse = new SuccessResponse();
		Long requestId = smsRequestWithId.getRequestId();
		successResponse.setRequestId(requestId);
		successResponse.setComments("Successfully Sent");
		kafkaProducer.sendMessage(requestId.toString());
		return ResponseEntity.ok()
				.header("data")
				.body(successResponse);
	}

	@GetMapping("v1/sms/getAll")
	public List<SmsRequest> fetchAllSms () {
		return smsRequestService.fetchAllSms();
	}

	@GetMapping("v1/sms/{requestId}")
	public SmsRequest fetchSmsFromRequestId (
			@PathVariable("requestId") Long requestId) throws SmsNotFoundException {
		return smsRequestService.fetchSmsFromRequestId(requestId);
	}

	@PostMapping("v1/blacklist")
	public ResponseEntity<String> addNumberToBlacklist (
			@RequestBody BlacklistRequest blacklistedNumber) throws InvalidPhoneNumberException {
		blacklistedService.addNumberToBlacklistRedis(blacklistedNumber);
		return ResponseEntity.ok("Number added to blacklist.");
	}

	@GetMapping("v1/blacklist")
	public List<String> getAllBlacklistedNumbers () {
		return blacklistedService.getAllBlacklistedNumbersRedis();
	}

	@DeleteMapping("v1/blacklist/{phoneNumber}")
	public ResponseEntity<String> deleteBlacklistedNumberFromId (
			@PathVariable("phoneNumber") String phoneNumber) {
		blacklistedService.deleteBlacklistedNumberFromId(phoneNumber);
		return ResponseEntity.ok("Number deleted from blacklist successfully!");
	}

	//  read on transactional.
//    @DeleteMapping("v1/blacklist/delete/{phoneNumber}")
//    @Transactional
//    public String removeNumberFromBlacklist(
//            @PathVariable("phoneNumber") String phoneNumber) {
//        blacklistedService.removeNumberFromBlacklist(phoneNumber);
//        return "Number successfully deleted from blacklist!";
//    }

//	public void buildEntityForElasticSearch (SmsRequest smsRequest) {
//		SmsRequestElasticSearch smsRequestElasticSearch = SmsRequestElasticSearch.builder()
//				.id(smsRequest.getRequestId())
//				.message(smsRequest.getMessage())
//				.phoneNumber(smsRequest.getPhoneNumber())
//				.createdAt(smsRequest.getCreatedAt())
//				.build();
//		LOGGER.info(String.format("the SmsRequest entity is %s", smsRequest.toString()));
//		LOGGER.info(String.format("the ES entity is %s", smsRequestElasticSearch.toString()));
//		smsRequestESRepository.save(smsRequestElasticSearch);
//	}

//    @GetMapping("v1/getAllSmsInElasticSearch")
//    public List<SmsRequestElasticSearch> getAllSms() {
//
//        return smsRequestESRepository.findAll(Pageable.unpaged()).toList();
//    }

	// Iterable to list
	@GetMapping("v1/getAllSmsInElasticSearch")
	public Iterable<SmsRequestElasticSearch> getAllSms () {
		return smsRequestESRepository.findAll();
	}

	@PostMapping("v1/getAllSmsBetweenTimes")
	public List<SmsRequestElasticSearch> getAllSmsBetweenTimes (
			@RequestBody RequestBodyForSearch requestBody) {
		//LOGGER.info(String.format("Request body is %s", requestBody.toString()));
		LOGGER.info("{}", requestBody);
		return smsRequestESRepository.findByPhoneNumberAndCreatedAtBetween(
				requestBody.getPhoneNumber(), requestBody.getStartTime(),
				requestBody.getEndTime());
	}

	// set default pageSize and offset. if user doesnt enter, you need to have default values.
	@GetMapping("v1/getAllSmsContainingText/{text}/{offset}/{pageSize}")
	public List<SmsRequestElasticSearch> getAllSmsContainingText (
			@PathVariable("text") String text,
			@PathVariable("offset") int offset,
			@PathVariable("pageSize") int pageSize) {
		return smsRequestESRepository.findByMessageContaining(
				text, PageRequest.of(offset, pageSize));
	}
}

























