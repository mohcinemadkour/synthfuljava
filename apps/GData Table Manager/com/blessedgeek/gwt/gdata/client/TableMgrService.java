package com.blessedgeek.gwt.gdata.client;

import java.util.Map;

import org.synthful.gwt.rpc.client.HashedParameterService;
import org.synthful.gwt.rpc.client.ParameterService;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/*
 * The client side stub for the RPC service.
 * Why does GWT require us perform an extensionless subclass
 * just so that we could specify the servlet path?
 * TODO: Suggest to google that the servlet path should be
 * a parameter in rpc interface.
 */
@RemoteServiceRelativePath("TableActionService.gwtrpc.jsp")
public interface TableMgrService
extends ParameterService<String, Map<String, String>>
{
}
