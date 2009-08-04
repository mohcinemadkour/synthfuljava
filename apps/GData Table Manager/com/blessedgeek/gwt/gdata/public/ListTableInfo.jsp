<%@ page
contentType="text/html; charset=utf-8"
language="java"
extends="com.blessedgeek.gwt.gdata.server.TableMgrJspBeanable"
import="
java.util.List,
com.google.gdata.data.spreadsheet.TableEntry,
com.google.gdata.data.spreadsheet.Data,
com.google.gdata.data.spreadsheet.Column,
com.blessedgeek.gwt.gdata.server.SessionSilo,
com.blessedgeek.gwt.gdata.server.MrBean"
%>
<%
MrBean mrBean = SessionSilo.initSessionBean(session.getId());
TableEntry entry = mrBean.getTableEntry();
Data data = entry.getData();
%>{
"id":"<%=entry.getId()%>",
"title":"<%=entry.getTitle().getPlainText()%>",
"summary":"<%=entry.getSummary().getPlainText()%>",
"worksheet":"<%=entry.getWorksheet().getName()%>",
"header":"<%=entry.getHeader().getRow()%>",
"insertionMode":"<%=data.getInsertionMode().name()%>",
"startRow":"<%=data.getStartIndex()%>",
"numRows":"<%=data.getNumberOfRows()%>",
"columns":"<%
int i=0;
for (Column col : data.getColumns())
{
    %><%=i>0?";":""%><%=col.getIndex()%>:<%=col.getName()%><%
    i++;
}
%>"
}