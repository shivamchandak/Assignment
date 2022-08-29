package com.assignment.Assignment.entity.primary;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SuccessResponse {
	private Long requestId;
	private String comments;
}
