package com.assignment.Assignment.service.primary;

import com.assignment.Assignment.entity.primary.SmsRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;
import com.assignment.Assignment.error.PhoneNumberMissingException;
import com.assignment.Assignment.error.SmsNotFoundException;

import java.util.List;

public interface SmsRequestService {
	public SmsRequest sendSmsRequest(SmsRequest contact) throws PhoneNumberMissingException, InvalidPhoneNumberException;

	public SmsRequest fetchSmsFromRequestId (Long requestId) throws SmsNotFoundException;

	public List<SmsRequest> fetchAllSms ();

}
