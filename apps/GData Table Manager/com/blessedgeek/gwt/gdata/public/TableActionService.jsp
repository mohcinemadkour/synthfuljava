<%
out = this.shuntJspOutput(pageContext);
%>
<%@page
language="java"
extends="com.blessedgeek.gwt.gdata.server.TableMgrServiceImplJspBeanable"
import="
com.blessedgeek.gwt.gdata.client.TableMgr,
com.blessedgeek.gwt.gdata.server.MrBean"
%>
<jsp:useBean id="mrBean" class="com.blessedgeek.gwt.gdata.server.MrBean" scope="session"/>
<%
String action = this.parameters.get("action");
String whatever = this.parameters.get("whatever");


mrBean.action  = TableMgr.getAction(action);

String pg = mrBean.action.toString();
System.out.println("pg=" + pg);
switch (mrBean.action)
{
case ListSheetDocs:
case ListTables:
case AddTable:
case DeleteTable:
case SetTable:
case AddRecord:
case ListRecords:
case DeleteRecord:
case Search4Record:
case Query4Record:
    pg = mrBean.action.toString() + ".jspf";
    break;
default:
    pageContext.include("Intro.jspf");
    return;
}

System.out.println("Action:" + pg);
%>

<table id="Conj" dir="rtl" border="0" cellpadding="3" cellspacing="0" cols="8" >
<tbody>
  <%-- @include file="TableHeader.jspf" --%>
	<jsp:include page="<%=pg%>" flush="true"/>
</tbody>
</table>
