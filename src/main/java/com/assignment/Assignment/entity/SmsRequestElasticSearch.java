package com.assignment.Assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.Date;

@Document(indexName = "smsrequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsRequestElasticSearch {

	@Id
	private Long id;

	@Field(type = FieldType.Keyword, name = "phone_number")
	private String phoneNumber;

	@Field(type = FieldType.Text, name = "message")
	private String message;

	@Field(type = FieldType.Date, name = "created_at")
	private Date createdAt;
}
