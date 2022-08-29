package com.assignment.Assignment.service.primary;

import com.assignment.Assignment.entity.primary.SmsRequestElasticSearch;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

public interface SmsRequestESService {
	void save (SmsRequestElasticSearch smsRequestElasticSearch);

	List<SmsRequestElasticSearch> findByPhoneNumberAndCreatedAtBetween (String phoneNumber, Date startTime, Date endTime);

	List<SmsRequestElasticSearch> findByMessageContaining (String text, PageRequest pageRequest);

	Iterable<SmsRequestElasticSearch> findAll ();
}
