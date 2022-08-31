package com.assignment.Assignment.repository;

import com.assignment.Assignment.entity.BlacklistDB;
import com.assignment.Assignment.entity.BlacklistRequest;
import com.assignment.Assignment.error.InvalidPhoneNumberException;
import com.assignment.Assignment.service.BlacklistDbService;
import com.assignment.Assignment.util.PhoneNumberCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Vector;

@Repository
public class BlacklistRedisRepositoryImpl implements BlacklistRedisRepository {

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private BlacklistDbService blacklistDbService;
	private PhoneNumberCheck phoneNumberCheck=new PhoneNumberCheck();
	private static final String KEY = "BLACKLISTED";
	private static final Logger LOGGER = LoggerFactory.getLogger(BlacklistRedisRepositoryImpl.class);

	@Override
	public void addNumberToBlacklist(BlacklistRequest blacklistRequest) throws InvalidPhoneNumberException {
		Vector invalidIndices=new Vector();
		int index=-1, flag=0;
		for (String phoneNumber : blacklistRequest.getPhoneNumbers()) {
			index++;
			boolean isValid= phoneNumberCheck.isValidPhoneNumber(phoneNumber);
			if(!isValid) {
				flag=1;
				invalidIndices.add(index);
				continue;
			}

			if (isPresentInBlacklist(phoneNumber)) {
				continue;
			}

			// Adds blacklisted number to Redis.
			redisTemplate.opsForHash().put(KEY, phoneNumber, phoneNumber);
			LOGGER.info(String.format("the phone number being added to blacklist redis is %s", phoneNumber));

			// Adds blacklisted number to mysql db only if it is not already present.
			if (!blacklistDbService.existsById(phoneNumber)) {
				BlacklistDB blacklistDB = BlacklistDB.builder().id(phoneNumber).phoneNumber(phoneNumber).build();
				blacklistDbService.save(blacklistDB);
			}
		}
		if(flag==1) {
			throw new InvalidPhoneNumberException("Please enter valid phone number(s) at indices: "+invalidIndices);
		}
	}

	@Override
	public List<String> getAllBlacklistedNumbers() {
		return redisTemplate.opsForHash().values(KEY);
	}

	@Override
	public void deleteBlacklistedNumber(String phoneNumber) {
		// Delete from redis.
		redisTemplate.opsForHash().delete(KEY, phoneNumber);

		// Delete from mysql db.
		blacklistDbService.deleteById(phoneNumber);
	}

	public boolean isPresentInBlacklist(String phoneNumber) {

		return redisTemplate.opsForHash().hasKey(KEY, phoneNumber);
	}
}
