package org.synthful.gwt.http.servlet.client;

import com.google.gwt.user.client.rpc.RemoteService;

/*
 * The client side stub for the RPC service.
 */
public interface ParameterService<S2C, C2S>
    extends RemoteService
{
	S2C doServiceResponse(C2S input);
}
