package com.assignment.Assignment.service.secondary;

import com.assignment.Assignment.entity.secondary.BlacklistRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;
import com.assignment.Assignment.repository.secondary.BlacklistedRepositoryRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlacklistedServiceImplementation implements BlacklistedService {

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
