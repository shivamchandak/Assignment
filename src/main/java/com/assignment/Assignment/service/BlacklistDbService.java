package com.assignment.Assignment.service;

import com.assignment.Assignment.entity.BlacklistDB;

public interface BlacklistDbService {
    boolean existsById(String phoneNumber);

    void save(BlacklistDB blacklistDB);

    void deleteById(String phoneNumber);
}
