package org.synthful.gwt.http.servlet.client;


import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;

/*
 * The client side stub for the RPC service.
 */
public interface HashedParameterService
    extends RemoteService
{
    String doServiceResponse(HashMap<String, String> input);
}
