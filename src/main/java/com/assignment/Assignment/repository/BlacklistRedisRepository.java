package com.assignment.Assignment.repository;

import com.assignment.Assignment.entity.BlacklistRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;

import java.util.List;

public interface BlacklistRedisRepository {
	void addNumberToBlacklist(BlacklistRequest blacklistedNumber) throws InvalidPhoneNumberException;

	List<String> getAllBlacklistedNumbers();

	void deleteBlacklistedNumber(String phoneNumber);
}
