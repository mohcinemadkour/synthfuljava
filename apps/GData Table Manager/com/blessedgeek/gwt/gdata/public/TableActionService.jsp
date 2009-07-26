<%
    out = this.shuntJspOutput(pageContext);
%>
<%@page language="java"
 extends="com.blessedgeek.gwt.gdata.server.TableMgrServiceImplJspBeanable"
 import="com.blessedgeek.gwt.gdata.client.TableMgr,com.blessedgeek.gwt.gdata.server.MrBean"%>

<%@page import="com.google.gdata.util.InvalidEntryException"%><jsp:useBean id="mrBean" class="com.blessedgeek.gwt.gdata.server.MrBean"
 scope="session" />
<%
String action = this.parameters.get("action");
String whatever = this.parameters.get("whatever");

mrBean.action = TableMgr.getAction(action);
String sheetKey = request.getParameter("sheetKey");

String pg = mrBean.action.toString();
switch (mrBean.action)
{
    case AddTable:
        mrBean.FeedsHdlr.addNewTableEntry(parameters);
        pg = "ListTables.jspf";
        break;
    case AddWorksheet:
        String title = parameters.get("title");
        String numrows = parameters.get("numrows");
        String numcols = parameters.get("numcols");
        mrBean.FeedsHdlr.addWorksheet(title, numrows, numcols);
        pg = "ListWorksheets.jspf";
        break;
    case DeleteTable:
    case AddRecord:
    case DeleteRecord:
        
    case Search4Record:
        String queryStr = parameters.get("search");
        try{
            mrBean.ResultRecords = mrBean.FeedsHdlr.search(queryStr);
        }
        catch(InvalidEntryException e)
        {
            %>Invalid Search:<br/><%=queryStr%><%
            return;
        }
        pg = "ListRecords.jspf";
        break;
    case Query4Record:
        queryStr = parameters.get("query");
        try{
            mrBean.ResultRecords = mrBean.FeedsHdlr.query(queryStr);
        }
        catch(InvalidEntryException e)
        {
            %>Invalid Query:<br/><%=queryStr%><%
            return;
        }
        pg = "ListRecords.jspf";
        break;
    case ListTableRecords:
        mrBean.ResultRecords = mrBean.FeedsHdlr.listAllRecordEntries();
        pg = "ListRecords.jspf";
        break;
    case ListWorksheets:
    case ListTableInfo:
    case ListTables:
        pg = mrBean.action.toString() + ".jspf";
        break;
    case ListSheetDocs:
        String refresh = parameters.get("refresh");
        mrBean.listDocs(refresh!=null);
        pg = mrBean.action.toString() + ".jspf";
        break;
    case ShowUpdateTable:
        pg = "ListTableInfo.jspf";
    case SetSheetDoc:
        mrBean.setSheetDoc(parameters.get("sheetKey"));
        %><script type="text/javascript">location.replace("TableMgr.jsp");</script><%
        return;
    case SetTable:
        mrBean.setTable(parameters.get("table"));
        %><script type="text/javascript">location.replace("TableMgr.jsp");</script><%
        return;
    case SetWorksheet:
        mrBean.setWorksheet(parameters.get("worksheet"));
        %><script type="text/javascript">location.replace("TableMgr.jsp");</script><%
        return;

    default:
        pg = "About.jspf";
        break;
}

System.out.println("Action:" + pg);
%><jsp:include page="<%=pg%>" flush="true" />

