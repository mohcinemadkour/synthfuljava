package org.synthful.gwt.rpc.client;


import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * The async counterpart of HashedParameterService.
 * @gwt.TypeArgs parameters <java.util.Map<java.lang.String,java.lang.String>>
 */
public interface HashedParameterServiceAsync
	extends ParameterServiceAsync<Map<String, String>, Map<String, String>>
{
    //void doServiceResponse(
    //    HashMap<String, String> parameters,
    //    AsyncCallback<HashMap<String, String>> callback);
}
