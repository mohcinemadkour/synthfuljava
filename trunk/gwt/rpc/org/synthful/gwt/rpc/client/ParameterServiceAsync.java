package org.synthful.gwt.rpc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * The async counterpart of ParameterService.
 */
public interface ParameterServiceAsync<S2C, C2S>
{
    void doServiceResponse(
    	C2S parameters,
        AsyncCallback<S2C> callback);
}
