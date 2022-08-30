package com.assignment.Assignment.service;

import com.assignment.Assignment.entity.BlacklistDB;
import com.assignment.Assignment.repository.BlacklistDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistDbServiceImpl implements BlacklistDbService{

    @Autowired
    BlacklistDbRepository blacklistDbRepository;
    @Override
    public boolean existsById(String phoneNumber) {
        return blacklistDbRepository.existsById(phoneNumber);
    }

    @Override
    public void save(BlacklistDB blacklistDB) {
        blacklistDbRepository.save(blacklistDB);
    }

    @Override
    public void deleteById(String phoneNumber) {
        blacklistDbRepository.deleteById(phoneNumber);
    }
}
