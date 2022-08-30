package com.assignment.Assignment.service;

import com.assignment.Assignment.entity.BlacklistRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;

import java.util.List;

public interface BlacklistRedisService {

	void addNumberToBlacklist(BlacklistRequest blacklistedNumber) throws InvalidPhoneNumberException;

	List<String> getAllBlacklistedNumbers();

	void deleteBlacklistedNumber(String phoneNumber);
}
