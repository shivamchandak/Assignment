package com.assignment.Assignment.repository;

import com.assignment.Assignment.entity.SmsRequestElasticSearch;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SmsRequestESRepository extends ElasticsearchRepository<SmsRequestElasticSearch, Long> {
	List<SmsRequestElasticSearch> findByPhoneNumberAndCreatedAtBetween (String phoneNumber, Date startTime, Date endTime, PageRequest of);

	List<SmsRequestElasticSearch> findByMessageContaining (String text, PageRequest pageable);

}
