package com.assignment.Assignment.repository;

import com.assignment.Assignment.entity.BlacklistDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistDbRepository extends JpaRepository<BlacklistDB, String> {
}
