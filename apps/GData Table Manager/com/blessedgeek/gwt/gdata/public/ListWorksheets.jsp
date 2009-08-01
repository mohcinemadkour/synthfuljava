<%@ page
contentType="text/html; charset=utf-8"
language="java"
extends="com.blessedgeek.gwt.gdata.server.TableMgrJspBeanable"
import="
java.util.List,
com.google.gdata.data.spreadsheet.WorksheetEntry"
%><jsp:useBean id="mrBean" class="com.blessedgeek.gwt.gdata.server.MrBean" scope="session"
/>[{"message":"Select a worksheet"}
<%
mrBean.listWorksheets();
System.out.println("entries=" + mrBean.FeedsHdlr.TableEntries);
response.setContentType("text/json");
for (WorksheetEntry entry : mrBean.FeedsHdlr.WorksheetEntries.toVector())
{
%>,{
    "worksheet":"<%=entry.getId()%>",
    "title":"<%=entry.getTitle().getPlainText()%>"
}
<%
}
%>]