package com.assignment.Assignment.repository.secondary;

import com.assignment.Assignment.entity.secondary.BlacklistRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;

import java.util.List;

public interface BlacklistedRepositoryRedis {
	void addNumberToBlacklistRedis (BlacklistRequest blacklistedNumber) throws InvalidPhoneNumberException;

	List<String> getAllBlacklistedNumbersRedis ();

	void deleteBlacklistedNumberFromId (String phoneNumber);
}
