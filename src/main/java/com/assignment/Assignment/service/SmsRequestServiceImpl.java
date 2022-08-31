package com.assignment.Assignment.service;

import com.assignment.Assignment.entity.SmsRequest;
import com.assignment.Assignment.entity.SmsRequestStatus;
import com.assignment.Assignment.error.InvalidPhoneNumberException;
import com.assignment.Assignment.error.PhoneNumberMissingException;
import com.assignment.Assignment.error.SmsNotFoundException;
import com.assignment.Assignment.repository.SmsRequestRepository;
import com.assignment.Assignment.util.PhoneNumberCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class SmsRequestServiceImpl implements SmsRequestService {

	@Autowired
	SmsRequestRepository smsRequestRepository;

	PhoneNumberCheck phoneNumberCheck=new PhoneNumberCheck();

	@Override
	public SmsRequest sendSmsRequest(SmsRequest smsRequest)
			throws PhoneNumberMissingException, InvalidPhoneNumberException {

		if (StringUtils.isEmpty(smsRequest.getPhoneNumber())) {
			throw new PhoneNumberMissingException("Phone number is required...");
		}
		if(!phoneNumberCheck.isValidPhoneNumber(smsRequest.getPhoneNumber())) {
			throw new InvalidPhoneNumberException("Please enter a valid phone number!");
		}
		smsRequest.setStatus(SmsRequestStatus.valueOf("SMS_REQUESTED"));
		return smsRequestRepository.save(smsRequest);
	}


	@Override
	public SmsRequest fetchSmsFromRequestId (Long requestId) throws SmsNotFoundException {
		Optional<SmsRequest> smsRequest = smsRequestRepository.findById(requestId);
		if (!smsRequest.isPresent()) {
			throw new SmsNotFoundException("SmsRequest with given request id Does not exist");
		}
		return smsRequest.get();
	}

	@Override
	public List<SmsRequest> fetchAllSms () {
		return smsRequestRepository.findAll();
	}
}
