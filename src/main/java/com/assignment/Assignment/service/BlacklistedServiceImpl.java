package com.assignment.Assignment.service;

import com.assignment.Assignment.entity.BlacklistRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;
import com.assignment.Assignment.repository.BlacklistedRepositoryRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlacklistedServiceImpl implements BlacklistedService {

	@Autowired
	BlacklistedRepositoryRedis blacklistedRepositoryRedis;

	@Override
	public void addNumberToBlacklistRedis (BlacklistRequest blacklistedNumber) throws InvalidPhoneNumberException {
		blacklistedRepositoryRedis.addNumberToBlacklistRedis(blacklistedNumber);
	}

	@Override
	public List<String> getAllBlacklistedNumbersRedis () {
		return blacklistedRepositoryRedis.getAllBlacklistedNumbersRedis();
	}

	@Override
	public void deleteBlacklistedNumberFromId (String phoneNumber) {
		blacklistedRepositoryRedis.deleteBlacklistedNumberFromId(phoneNumber);
	}
}
