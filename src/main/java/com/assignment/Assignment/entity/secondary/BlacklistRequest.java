package com.assignment.Assignment.entity.secondary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistRequest {
	private List<String> phoneNumbers;
}
