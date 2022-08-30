package com.assignment.Assignment.service;

import com.assignment.Assignment.entity.BlacklistRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;
import com.assignment.Assignment.repository.BlacklistRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlacklistRedisServiceImpl implements BlacklistRedisService {

	@Autowired
	BlacklistRedisRepository blacklistRedisRepository;

	@Override
	public void addNumberToBlacklist(BlacklistRequest blacklistedNumber) throws InvalidPhoneNumberException {
		blacklistRedisRepository.addNumberToBlacklist(blacklistedNumber);
	}

	@Override
	public List<String> getAllBlacklistedNumbers() {
		return blacklistRedisRepository.getAllBlacklistedNumbers();
	}

	@Override
	public void deleteBlacklistedNumber(String phoneNumber) {
		blacklistRedisRepository.deleteBlacklistedNumber(phoneNumber);
	}
}
