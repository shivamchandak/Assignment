package com.assignment.Assignment.repository.primary;

import com.assignment.Assignment.entity.primary.SmsRequestElasticSearch;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SmsRequestESRepository extends ElasticsearchRepository<SmsRequestElasticSearch, Long> {
	List<SmsRequestElasticSearch> findByPhoneNumberAndCreatedAtBetween (String phoneNumber, Date startTime, Date endTime);

	List<SmsRequestElasticSearch> findByMessageContaining (String text, PageRequest pageable);

}
