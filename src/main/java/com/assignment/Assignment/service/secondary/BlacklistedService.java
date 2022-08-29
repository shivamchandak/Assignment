package com.assignment.Assignment.service.secondary;

import com.assignment.Assignment.entity.secondary.BlacklistRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;

import java.util.List;

public interface BlacklistedService {

	void addNumberToBlacklistRedis (BlacklistRequest blacklistedNumber) throws InvalidPhoneNumberException;

	List<String> getAllBlacklistedNumbersRedis ();

	void deleteBlacklistedNumberFromId (String phoneNumber);
}
