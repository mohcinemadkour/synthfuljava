package com.blessedgeek.gwt.gdata.client;

import org.synthful.gwt.http.servlet.client.HashedParameterService;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/*
 * The client side stub for the RPC service.
 * Why does GWT require us perform an extensionless subclass
 * just so that we could specify the servlet path?
 * TODO: Suggest to google that the servlet path should be
 * a parameter in rpc interface.
 */
@RemoteServiceRelativePath("TableActionService.jsp")
public interface TableMgrService
    extends HashedParameterService
{
}
