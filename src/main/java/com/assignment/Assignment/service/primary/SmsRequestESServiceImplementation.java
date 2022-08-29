package com.assignment.Assignment.service.primary;

import com.assignment.Assignment.entity.primary.SmsRequestElasticSearch;
import com.assignment.Assignment.repository.primary.SmsRequestESRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SmsRequestESServiceImplementation implements SmsRequestESService {
	SmsRequestESRepository smsRequestESRepository;

	@Override
	public void save (SmsRequestElasticSearch smsRequestElasticSearch) {
		smsRequestESRepository.save(smsRequestElasticSearch);
	}

	@Override
	public List<SmsRequestElasticSearch> findByPhoneNumberAndCreatedAtBetween (String phoneNumber, Date startTime, Date endTime) {
		return smsRequestESRepository.findByPhoneNumberAndCreatedAtBetween(phoneNumber, startTime, endTime);
	}

	@Override
	public List<SmsRequestElasticSearch> findByMessageContaining (String text, PageRequest pageRequest) {
		return smsRequestESRepository.findByMessageContaining(text, pageRequest);
	}

	@Override
	public Iterable<SmsRequestElasticSearch> findAll () {

		return smsRequestESRepository.findAll();
	}
}
