package com.assignment.Assignment.entity.secondary;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistDB {

	@Id
	private String id;
	private String phoneNumber;
}
