package com.assignment.Assignment.repository.primary;

import com.assignment.Assignment.entity.primary.SmsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRequestRepository extends JpaRepository<SmsRequest, Long> {

}
