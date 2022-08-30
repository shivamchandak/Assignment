package com.assignment.Assignment.service;

import com.assignment.Assignment.entity.SmsRequestElasticSearch;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

public interface SmsRequestESService {
    List<SmsRequestElasticSearch> findByPhoneNumberAndCreatedAtBetween(String phoneNumber, Date startTime, Date endTime, PageRequest of);

    Iterable<SmsRequestElasticSearch> findAll();

    List<SmsRequestElasticSearch> findByMessageContaining(String text, PageRequest of);
}
