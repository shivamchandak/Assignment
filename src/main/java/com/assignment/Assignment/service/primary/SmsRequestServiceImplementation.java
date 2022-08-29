package com.assignment.Assignment.service.primary;

import com.assignment.Assignment.entity.primary.SmsRequest;
import com.assignment.Assignment.entity.primary.SmsRequestStatus;
import com.assignment.Assignment.error.InvalidPhoneNumberException;
import com.assignment.Assignment.error.PhoneNumberMissingException;
import com.assignment.Assignment.error.SmsNotFoundException;
import com.assignment.Assignment.repository.primary.SmsRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class SmsRequestServiceImplementation implements SmsRequestService {

	@Autowired
	SmsRequestRepository smsRequestRepository;

	@Override
	public SmsRequest sendSmsRequest(SmsRequest smsRequest)
			throws PhoneNumberMissingException, InvalidPhoneNumberException {

		if (StringUtils.isEmpty(smsRequest.getPhoneNumber())) {
			throw new PhoneNumberMissingException("Phone number is required...");
		}
		checkValidPhoneNumber(smsRequest.getPhoneNumber());
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

	public void checkValidPhoneNumber (String phoneNumber) throws InvalidPhoneNumberException {
		int length = phoneNumber.length();
		int startIndex = 0, isValid = 1;
		if (!(length == 10 || length == 13)) {
			isValid = 0;
		}

		if (phoneNumber.charAt(0) == '+') {
			startIndex = 1;
			if (!phoneNumber.substring(1, 3).equals("91")) {
				isValid = 0;
			}
		}
		for (int i = startIndex; i < length; i++) {
			if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9') {
				isValid = 0;
			}
		}
		if (isValid == 0) {
			throw new InvalidPhoneNumberException("Please enter a valid phone number!");
		}
	}
}
