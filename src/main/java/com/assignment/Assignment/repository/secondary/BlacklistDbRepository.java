package com.assignment.Assignment.repository.secondary;

import com.assignment.Assignment.entity.secondary.BlacklistDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistDbRepository extends JpaRepository<BlacklistDB, String> {
}
