package com.blessedgeek.gwt.gdata.client;

import java.util.Map;

import org.synthful.gwt.rpc.client.HashedParameterServiceAsync;
import org.synthful.gwt.rpc.client.ParameterServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TableMgrServiceAsync
extends ParameterServiceAsync<String, Map<String, String>>{
}
