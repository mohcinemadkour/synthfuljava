<%@ page
contentType="text/html; charset=utf-8"
language="java"
extends="com.blessedgeek.gwt.gdata.server.TableMgrJspBeanable"
import="
java.util.List,
com.google.gdata.data.spreadsheet.SpreadsheetEntry"
%><jsp:useBean id="mrBean" class="com.blessedgeek.gwt.gdata.server.MrBean" scope="session"/>
[{"message":"Select a document"}
<%
System.out.println("entries=" + mrBean.FeedsHdlr.SpreadsheetEntries);
response.setContentType("text/json");
for (SpreadsheetEntry entry : mrBean.FeedsHdlr.SpreadsheetEntries.toVector())
{
%>,{
    "key":"<%=entry.getKey()%>",
    "title":"<%=entry.getTitle().getPlainText()%>"
}
<%
}
%>]