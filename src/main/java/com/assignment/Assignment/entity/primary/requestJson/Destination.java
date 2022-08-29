package com.assignment.Assignment.entity.primary.requestJson;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class Destination {
	public ArrayList<String> msisdn;
	public String correlationid;
}
