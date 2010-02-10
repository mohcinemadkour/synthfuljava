package com.blessedgeek.gwt.examples.greet.server;

import java.util.HashMap;
import java.util.Map;

import com.blessedgeek.gwt.examples.greet.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SendGreeting
	extends RemoteServiceServlet
	implements GreetingService
{

	@Override
	public Map<String, String> doServiceResponse(
		Map<String, String> input)
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("serverInfo", getServletContext().getServerInfo());
		params.put("userAgent", getThreadLocalRequest().getHeader("User-Agent"));
		params.put("name", input.get("name"));
		return params;
	}
}
