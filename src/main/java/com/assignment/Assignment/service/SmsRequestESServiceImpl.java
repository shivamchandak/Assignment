package com.assignment.Assignment.service;

import com.assignment.Assignment.entity.SmsRequestElasticSearch;
import com.assignment.Assignment.repository.SmsRequestESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SmsRequestESServiceImpl implements SmsRequestESService {

    @Autowired
    SmsRequestESRepository smsRequestESRepository;

    @Override
    public List<SmsRequestElasticSearch> findByPhoneNumberAndCreatedAtBetween(
            String phoneNumber, Date startTime, Date endTime, PageRequest of) {
        return smsRequestESRepository.findByPhoneNumberAndCreatedAtBetween(phoneNumber, startTime, endTime, of);
    }

    @Override
    public Iterable<SmsRequestElasticSearch> findAll() {
        return smsRequestESRepository.findAll();
    }

    @Override
    public List<SmsRequestElasticSearch> findByMessageContaining(String text, PageRequest of) {
        return smsRequestESRepository.findByMessageContaining(text, of);
    }
}
