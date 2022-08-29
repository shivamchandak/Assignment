package com.assignment.Assignment.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

	private KafkaTemplate<String, String> kafkaTemplate;
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

	public KafkaProducer (KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage (String message) {
		kafkaTemplate.send("notification.send_sms", message);
		LOGGER.info(String.format("Request Id sent -> %s", message));
	}
}
