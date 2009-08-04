<%@ page
contentType="text/html; charset=utf-8"
language="java"
extends="com.blessedgeek.gwt.gdata.server.TableMgrJspBeanable"
import="
java.util.List,
com.google.gdata.data.spreadsheet.SpreadsheetEntry,
com.blessedgeek.gwt.gdata.server.SessionSilo,
com.blessedgeek.gwt.gdata.server.MrBean,
org.synthful.gdata.SpreadsheetFeedsSilo.SpreadsheetDescr"
%>[{"message":"Select a document"}<%
MrBean mrBean = SessionSilo.initSessionBean(session.getId());
response.setContentType("text/json");
for (SpreadsheetDescr entry : mrBean.getSpreadsheetDescrs().toVector())
{
%>,{
    "key":"<%=entry.key%>",
    "title":"<%=entry.title%>"
}
<%
}
%>]