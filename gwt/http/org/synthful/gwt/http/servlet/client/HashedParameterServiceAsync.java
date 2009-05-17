package org.synthful.gwt.http.servlet.client;


import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * The async counterpart of HashedParameterServiceAsync.
 * @gwt.TypeArgs parameters <java.util.HashMap<java.lang.String,java.lang.String>>
 */
public interface HashedParameterServiceAsync
{
    void doServiceResponse(
        HashMap<String, String> parameters,
        AsyncCallback<String> callback);
}
