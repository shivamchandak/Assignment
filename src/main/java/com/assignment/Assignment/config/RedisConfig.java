package com.assignment.Assignment.config;

import com.assignment.Assignment.entity.secondary.BlacklistRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

	// read on difference between lettuce and jedis.
	@Bean
	public RedisConnectionFactory redisConnectionFactory () {
		return new LettuceConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, BlacklistRequest> redisTemplate () {
		RedisTemplate<String, BlacklistRequest> userRedisTemplate = new RedisTemplate<>();
		userRedisTemplate.setConnectionFactory(redisConnectionFactory());
		return userRedisTemplate;
	}
}

