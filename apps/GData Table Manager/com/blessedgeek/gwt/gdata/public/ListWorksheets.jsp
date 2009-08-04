<%@ page
contentType="text/html; charset=utf-8"
language="java"
extends="com.blessedgeek.gwt.gdata.server.TableMgrJspBeanable"
import="
java.util.List,
com.google.gdata.data.spreadsheet.WorksheetEntry,
com.blessedgeek.gwt.gdata.server.SessionSilo,
com.blessedgeek.gwt.gdata.server.MrBean,
org.synthful.gdata.SpreadsheetFeedsSilo.WorksheetDescr"
%>[{"message":"Select a worksheet"}<%
   
MrBean mrBean = SessionSilo.initSessionBean(session.getId());
mrBean.listWorksheets();
SessionSilo.storeSessionBean(mrBean);
response.setContentType("text/json");
for (WorksheetDescr d : mrBean.getWorksheetDescrs().toVector())
{
%>,
{
    "worksheet":"<%=d.name%>",
    "title":"<%=d.name%>"
}
<%
}
%>]