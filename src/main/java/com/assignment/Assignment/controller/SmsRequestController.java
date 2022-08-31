package com.assignment.Assignment.controller;

import com.assignment.Assignment.entity.*;
import com.assignment.Assignment.error.InvalidPhoneNumberException;
import com.assignment.Assignment.error.PhoneNumberMissingException;
import com.assignment.Assignment.error.SmsNotFoundException;
import com.assignment.Assignment.kafka.KafkaProducer;
import com.assignment.Assignment.service.SmsRequestESService;
import com.assignment.Assignment.service.SmsRequestService;
import com.assignment.Assignment.service.BlacklistRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SmsRequestController {

	@Autowired
	SmsRequestService smsRequestService;
	@Autowired
	BlacklistRedisService blacklistedService;
	@Autowired
	KafkaProducer kafkaProducer;
	@Autowired
	SmsRequestESService smsRequestESService;
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
	public SmsRequest fetchSmsFromRequestId (@PathVariable("requestId") Long requestId) throws SmsNotFoundException {
		return smsRequestService.fetchSmsFromRequestId(requestId);
	}

	@PostMapping("v1/blacklist")
	public ResponseEntity<String> addNumberToBlacklist (@RequestBody BlacklistRequest blacklistedNumber)
			throws InvalidPhoneNumberException {
		blacklistedService.addNumberToBlacklist(blacklistedNumber);
		return ResponseEntity.ok("Number added to blacklist.");
	}

	@GetMapping("v1/blacklist")
	public List<String> getAllBlacklistedNumbers () {
		return blacklistedService.getAllBlacklistedNumbers();
	}

	@DeleteMapping("v1/blacklist/{phoneNumber}")
	public ResponseEntity<String> deleteBlacklistedNumberFromId (@PathVariable("phoneNumber") String phoneNumber) {
		blacklistedService.deleteBlacklistedNumber(phoneNumber);
		return ResponseEntity.ok("Number deleted from blacklist successfully!");
	}

	@GetMapping("v1/getAllSmsInElasticSearch")
	public List<SmsRequestElasticSearch> getAllSms () {
		List<SmsRequestElasticSearch> smsRequest=new ArrayList<>();
		Iterable<SmsRequestElasticSearch> iterable= smsRequestESService.findAll();

		for(SmsRequestElasticSearch eachSms:iterable) {
			smsRequest.add(eachSms);
		}
		return smsRequest;
	}

	@PostMapping("v1/getAllSmsBetweenTimes")
	public List<SmsRequestElasticSearch> getAllSmsBetweenTimes (@RequestBody RequestBodyForTimeSearch requestBody) {
		LOGGER.info("{}", requestBody);
		return smsRequestESService.findByPhoneNumberAndCreatedAtBetween(
				requestBody.getPhoneNumber(), requestBody.getStartTime(), requestBody.getEndTime(),
				PageRequest.of(requestBody.getPageNumber(), requestBody.getPageSize()));
	}

	@PostMapping("v1/getAllSmsContainingText")
	public List<SmsRequestElasticSearch> getAllSmsContainingText (@RequestBody RequestBodyForTextSearch requestBody) {
		return smsRequestESService.findByMessageContaining(
				requestBody.getText(), PageRequest.of(requestBody.getPageNumber(), requestBody.getPageSize()));
	}
}

























