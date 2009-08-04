<%@ page
contentType="text/html; charset=utf-8"
language="java"
extends="com.blessedgeek.gwt.gdata.server.TableMgrJspBeanable"
import="
java.util.List,
com.google.gdata.data.spreadsheet.TableEntry,
com.blessedgeek.gwt.gdata.server.SessionSilo,
com.blessedgeek.gwt.gdata.server.MrBean,
org.synthful.gdata.SpreadsheetFeedsSilo.TableDescr"
%>[{"message":"Select a table"}<%
MrBean mrBean = SessionSilo.initSessionBean(session.getId());
mrBean.listTables();
SessionSilo.storeSessionBean(mrBean);
SessionSilo.logTableMgr.info("entries=" + mrBean.getTableDescrs());
response.setContentType("text/json");
for (TableDescr d : mrBean.getTableDescrs().toVector())
{
%>,{
    "table":"<%=d.title%>",
    "title":"<%=d.title%>"
}
<%
}
%>]