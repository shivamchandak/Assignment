package com.assignment.Assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "BlacklistRequest")
public class BlacklistRedis implements Serializable {

    @Id
    private String id;
    private String phoneNumber;
}
