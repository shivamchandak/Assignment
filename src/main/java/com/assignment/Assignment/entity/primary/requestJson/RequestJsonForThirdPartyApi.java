package com.assignment.Assignment.entity.primary.requestJson;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class RequestJsonForThirdPartyApi {
	public String deliverychannel;
	public Channels channels;
	public ArrayList<Destination> destination;
}
