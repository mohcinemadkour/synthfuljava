<%@ page
language="java"
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
import="
com.google.gdata.client.http.AuthSubUtil"
%>
<jsp:useBean id="mrBean" class="com.blessedgeek.gwt.gdata.server.MrBean" scope="session"/>
<%
String callBackLocation = request.getParameter("callBackLocation");
System.out.println(mrBean.SessionAuthToken);
try{
 AuthSubUtil.revokeToken(mrBean.SessionAuthToken,null);
 mrBean.SessionAuthToken = null;
 mrBean.AuthToken = null;
}
catch (Exception ex)
{}
//pageContext.forward(callBackLocation);
%>
Logged out successfully. <br>
Return to <a href="<%=callBackLocation%>">Table Manager</a>