package com.assignment.Assignment.repository;

import com.assignment.Assignment.entity.BlacklistRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;

import java.util.List;

public interface BlacklistedRepositoryRedis {
	void addNumberToBlacklistRedis (BlacklistRequest blacklistedNumber) throws InvalidPhoneNumberException;

	List<String> getAllBlacklistedNumbersRedis ();

	void deleteBlacklistedNumberFromId (String phoneNumber);
}
