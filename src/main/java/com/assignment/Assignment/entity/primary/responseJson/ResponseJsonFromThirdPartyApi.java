package com.assignment.Assignment.entity.primary.responseJson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseJsonFromThirdPartyApi {
	public ArrayList<Response> response;
}
