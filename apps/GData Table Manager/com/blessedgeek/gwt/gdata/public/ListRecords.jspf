<%@ page
contentType="text/html; charset=utf-8"
language="java"
extends="com.blessedgeek.gwt.gdata.server.TableMgrJspBeanable"
import="
java.util.List,
com.google.gdata.data.spreadsheet.TableEntry,
com.google.gdata.data.spreadsheet.Data,
com.google.gdata.data.spreadsheet.Column,
com.google.gdata.data.spreadsheet.RecordEntry,
com.google.gdata.data.spreadsheet.Field"
%>
<jsp:useBean id="mrBean" class="com.blessedgeek.gwt.gdata.server.MrBean" scope="session"/>
<%
Data data = mrBean.Table.getData();
%>
<table border='1'>
<tr>
<%
for (Column col : data.getColumns())
{
    %><th><%=col.getIndex()%> <%=col.getName()%></th><%
}
%>
</tr>
<%
List<RecordEntry> entries = mrBean.ResultRecords;
for (RecordEntry rentry : entries)
{
    %><tr><%
    for (Field field : rentry.getFields())
    {
        %><td><%=field.getName()%>:<%=field.getValue()%></td><%
    }
    %></tr><%
}
%>
</table>
