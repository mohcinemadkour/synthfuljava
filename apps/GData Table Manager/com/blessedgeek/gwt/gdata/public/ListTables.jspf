<%@ page
contentType="text/html; charset=utf-8"
language="java"
extends="com.blessedgeek.gwt.gdata.server.TableMgrJspBeanable"
import="
java.util.List,
com.google.gdata.data.spreadsheet.TableEntry"
%><jsp:useBean id="mrBean" class="com.blessedgeek.gwt.gdata.server.MrBean" scope="session"
/>[{"message":"Select a table"}
<%
mrBean.listTables();
System.out.println("entries=" + mrBean.FeedsHdlr.TableEntries);
response.setContentType("text/json");
for (TableEntry entry : mrBean.FeedsHdlr.TableEntries.toVector())
{
%>,{
    "table":"<%=entry.getId()%>",
    "title":"<%=entry.getTitle().getPlainText()%>"
}
<%
}
%>]