<%@ page
language="java"
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
import="
com.google.gdata.client.http.AuthSubUtil,
com.blessedgeek.gwt.gdata.server.SessionSilo,
com.blessedgeek.gwt.gdata.server.MrBean"
%><%
MrBean mrBean = SessionSilo.initSessionBean(session.getId());
String callBackLocation = request.getParameter("callBackLocation");
SessionSilo.logTableMgr.info(mrBean.sessionAuthToken);
mrBean.revokeSessionAuth();
//pageContext.forward(callBackLocation);
%>
Logged out successfully. <br>
Return to <a href="<%=callBackLocation%>">Table Manager</a>