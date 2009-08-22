<%@
page language="java"
 extends="com.blessedgeek.gwt.gdata.server.TableMgrServiceImplJspBeanable"
 import="
 com.blessedgeek.gwt.gdata.client.TableMgr,
 com.blessedgeek.gwt.gdata.server.MrBean,
 com.google.gdata.util.InvalidEntryException,
 com.blessedgeek.gwt.gdata.server.SessionSilo,
 java.util.List,
 com.google.gdata.data.spreadsheet.RecordEntry,
 com.google.gdata.data.spreadsheet.TableEntry,
 com.google.gdata.data.spreadsheet.Data,
 com.google.gdata.data.spreadsheet.Column,
 com.google.gdata.data.spreadsheet.Field"
%><%
class RecordsLister
{
    public RecordsLister (MrBean mrBean, JspWriter out)
    {
        this.mrBean = mrBean;
        this.out = out;
    }
    
    public void list()
    throws Exception
    {
        TableEntry entry = mrBean.getTableEntry();
        Data data = entry.getData();
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
        List<RecordEntry> entries = resultRecords;
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
        </table><%
    }
    public List<RecordEntry> resultRecords;
    MrBean mrBean;
    JspWriter out;
}

String action = this.parameters.get("action");
String whatever = this.parameters.get("whatever");

MrBean mrBean = SessionSilo.initSessionBean(session.getId());

mrBean.action = TableMgr.getAction(action);
String sheetKey = request.getParameter("sheetKey");
RecordsLister recs = new RecordsLister(mrBean, out);
String pg = mrBean.action.toString();
switch (mrBean.action)
{
    case UpdateTable:
        mrBean.updateTableEntry(parameters);
        //break;
    case ShowUpdateTable:
        pg = "ListTableInfo.jsp";
        break;
    case AddTable:
        try{
            mrBean.addNewTableEntry(parameters);
        }
        catch (Exception e)
        {
            %>Invalid/insufficient parameters entered<%
            return;
        }
        pg = "ListTables.jsp";
        break;
    case AddWorksheet:
        String title = parameters.get("title");
        String numrows = parameters.get("numrows");
        String numcols = parameters.get("numcols");
        mrBean.addWorksheet(title, numrows, numcols);
        pg = "ListWorksheets.jsp";
        break;
    case DeleteTable:
    case AddRecord:
    case DeleteRecord:
        
    case Search4Record:
        String queryStr = parameters.get("search");
        try{
            recs.resultRecords = mrBean.search(queryStr);
            recs.list();
            return;
        }
        catch(InvalidEntryException e)
        {
            %>Invalid Search:<br/><%=queryStr%><%
        }
        return;
    case Query4Record:
        queryStr = parameters.get("query");
        try{
            recs.resultRecords = mrBean.query(queryStr);
            recs.list();
            return;
        }
        catch(InvalidEntryException e)
        {
            %>Invalid Query:<br/><%=queryStr%><%
        }
        return;
    case ListTableRecords:
        recs.resultRecords = mrBean.listAllRecordEntries();
        recs.list();
        break;
    case ListWorksheets:
    case ListTableInfo:
    case ListTables:
        pg = mrBean.action.toString() + ".jsp";
        break;
    case ListSheetDocs:
        String refresh = parameters.get("refresh");
        mrBean.listDocs(refresh!=null);
        SessionSilo.logTableMgr.info("entries=" + mrBean.getSpreadsheetDescrs());
        pg = mrBean.action.toString() + ".jsp";
        break;
    case SetSheetDoc:
        mrBean.setSheetDoc(parameters.get("sheetKey"));
        SessionSilo.storeSessionBean(mrBean);
        %><script type="text/javascript">location.replace("TableMgr.jsp");</script><%
        return;
    case SetTable:
        mrBean.setTable(parameters.get("table"));
        SessionSilo.storeSessionBean(mrBean);
        %><script type="text/javascript">location.replace("TableMgr.jsp");</script><%
        return;
    case SetWorksheet:
        mrBean.setWorksheet(parameters.get("worksheet"));
        SessionSilo.storeSessionBean(mrBean);
        %><script type="text/javascript">location.replace("TableMgr.jsp");</script><%
        return;

    default:
        pg = "About.jsp";
        break;
}

SessionSilo.storeSessionBean(mrBean);
SessionSilo.logTableAction.info("Action:" + pg);
%><jsp:include page="<%=pg%>" flush="true" />

