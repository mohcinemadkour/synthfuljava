package com.blessedgeek.gwt.examples.greet.client;

import org.synthful.gwt.http.servlet.client.HashedParameterService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService
extends HashedParameterService
{}
