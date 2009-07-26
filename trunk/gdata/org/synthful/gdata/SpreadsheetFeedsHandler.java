package org.synthful.gdata;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.synthful.gwt.gdata.client.FeedsBaseUrl;
import org.synthful.util.HashVector;

import com.google.common.collect.Maps;
import com.google.gdata.client.spreadsheet.RecordQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.Column;
import com.google.gdata.data.spreadsheet.Data;
import com.google.gdata.data.spreadsheet.Header;
import com.google.gdata.data.spreadsheet.RecordEntry;
import com.google.gdata.data.spreadsheet.RecordFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.TableEntry;
import com.google.gdata.data.spreadsheet.TableFeed;
import com.google.gdata.data.spreadsheet.Worksheet;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedsHandler.
 */
public class SpreadsheetFeedsHandler
    extends FeedsHandler
{
    
    /**
     * Instantiates a new feeds handler.
     */
    public SpreadsheetFeedsHandler() {}

    public SpreadsheetFeedsHandler(SpreadsheetService service)
    {
        super(service);
        this.SpreadsheetService = service;
    }

    /**
     * Instantiates a new feeds handler.
     * 
     * @param sheetKey the sheet key
     */
    public SpreadsheetFeedsHandler(String sheetKey, SpreadsheetService service)
    {
        super(sheetKey, service);
        this.SpreadsheetService = service;
    }

    public boolean initSpreadsheetFeed(boolean refresh)
    {
        if (this.SpreadsheetFeed!=null && !refresh)
            return true;
        URL feedUrl = this.FeedUrlFactory.getSpreadsheetsFeedUrl();
        
        try {
            this.SpreadsheetFeed =
                this.SpreadsheetService.getFeed(
                    feedUrl,
                    SpreadsheetFeed.class);
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
        catch (ServiceException e)
        {
            return false;            
        }
        catch (Exception e)
        {
            return false;            
        }
    }
        
    public URL getWorksheetFeed(String spreadsheetKey)
    {
        SpreadsheetEntry sentry =
            this.SpreadsheetMappedEntries.get(spreadsheetKey);
        return
            sentry.getWorksheetFeedUrl();
    }

    public List<SpreadsheetEntry> listDocs(boolean refresh)
    throws IOException, ServiceException
    {
        this.initSpreadsheetFeed(true);
        if (this.Spreadsheets == null || refresh)
        {
            this.Spreadsheets = this.SpreadsheetFeed.getEntries();
            this.SpreadsheetMappedEntries =
                Collections.synchronizedMap(
                    new HashMap<String, SpreadsheetEntry>(this.Spreadsheets.size()));
            
            for(SpreadsheetEntry entry: Spreadsheets)
                this.SpreadsheetMappedEntries.put(entry.getKey(), entry);
        }
        
        return this.Spreadsheets;
    }
        
    
    /**
     * Sets the sheet urls.
     * 
     * @param sheetKey construct the feed urls based on the sheet key.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    public void setDoc(String sheetKey)
    throws IOException
    {
        super.setDoc(sheetKey);
        this.SheetFeedUrlStr = FeedsBaseUrl.SpreadSheets + sheetKey;            
        this.TablesFeedUrl = new URL(this.SheetFeedUrlStr + "/tables/");            
        this.RecordsFeedUrl = new URL(this.SheetFeedUrlStr + "/records/");        
        this.WorkSheetsFeedUrl = this.getWorksheetFeed(sheetKey);
    }

    /**
     * Sets the table to use to construct the record feed.
     * 
     * @param id the table to use to construct the record feed. A record feed
     * is a feed of records from a table. A table can be constructed
     * using the TableDemo.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setTableRecordsFeedUrl(
        String id)
        throws IOException
    {
        this.TableId = Integer.parseInt(id);
        this.TableRecordsFeedUrl =
            new java.net.URL(
                this.RecordsFeedUrl.toString() + TableId);
    }

    /**
     * Lists the tables currently available in the sheet.
     * 
     * @return the list< table entry>
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServiceException the service exception
     */
    public List<TableEntry> listTables()
        throws IOException, ServiceException
    {
        TableFeed feed = this.Service.getFeed(this.TablesFeedUrl, TableFeed.class);
        return feed.getEntries();
    }

    /**
     * Lists the tables currently available in the sheet.
     * 
     * @return HashMap<table title String, TableEntry>
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServiceException the service exception
     */
    public HashMap<String, TableEntry> listTables(HashMap<String, TableEntry> nullObj)
        throws IOException, ServiceException
    {
        nullObj = new HashMap<String, TableEntry>();
        TableFeed feed = this.Service.getFeed(this.TablesFeedUrl, TableFeed.class);
        List<TableEntry> entries = feed.getEntries();
        nullObj = new  HashMap<String, TableEntry>(entries.size());
        for(TableEntry entry: entries)
        {
            nullObj.put(entry.getTitle().getPlainText(), entry);
        }
        return nullObj;
    }
    
    /**
     * Lists the tables currently available in the sheet.
     * 
     * @return HashVector<table title String, TableEntry>
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServiceException the service exception
     */
    public HashVector<String, TableEntry> listTables(HashVector<String, TableEntry> nullObj)
        throws IOException, ServiceException
    {
        nullObj = new HashVector<String, TableEntry>();
        TableFeed feed = this.Service.getFeed(this.TablesFeedUrl, TableFeed.class);
        List<TableEntry> entries = feed.getEntries();
        nullObj = new  HashVector<String, TableEntry>(entries.size());
        for(TableEntry entry: entries)
        {
            nullObj.put(entry.getTitle().getPlainText(), entry);
        }
        return nullObj;
    }
    
    
    /**
     * Adds a new table entry, based on the user-specified name value pairs.
     * Note that the following parameters must be specified:
     * title, startrow, numrows, columns.
     * 
     * @param csNameValuePairs the comma separated name value pairs
     * 
     * title=Table1, summary=Test Table, worksheet=SheetName, header=1,
     * startrow=2, numrows=5, columns=a:Name;b:Phone;c:Address
     * 
     * @throws ServiceException when the request causes an error in the Google
     * Spreadsheets service.
     * @throws IOException when an error occurs in communication with the Google
     * Spreadsheets service.
     */
    public void addTable(
        String csNameValuePairs)
        throws IOException, ServiceException
    {
        TableEntry newEntry =
            setEntryContentsFromString(new TableEntry(), csNameValuePairs);
        this.Service.insert(this.TablesFeedUrl, newEntry);
    }

    /**
     * Parses the command line arguments given in the format
     * "title=Table1,summary=This is a test", and sets the corresponding values
     * on the given TableEntry.
     * 
     * Values that are not specified are left alone. That is, if the entry already
     * contains "title=Table1", then setting summary will retain
     * the title as Table1.
     * 
     * @param entryToUpdate the entry to change based on the parameters
     * @param csNameValuePairs the list of name/value pairs, containing the name of
     * the title.
     * 
     * @return the table entry
     */
    private TableEntry setEntryContentsFromString(
        TableEntry entryToUpdate, String csNameValuePairs)
    {
        Map<String, String> dataParams = Maps.newHashMap();
        Map<String, String> dataCols = Maps.newHashMap();

        // Split first by the commas between the different fields.
        for (String nameValuePair : csNameValuePairs.split(","))
        {

            // Then, split by the equal sign. Attributes are specified as
            // key=value.
            String[] parts = nameValuePair.split("=", 2);
            if (parts.length < 2)
            {
                System.out.println("Attributes are specified as key=value, "
                    + "for example, title=Table1");
            }

            String param = parts[0].trim();
            String value = parts[1].trim();

            setTableEntryValues(
                entryToUpdate, dataParams, dataCols,
                param, value);
        }

        // Update table data.
        Data data =
            dataFromParams(entryToUpdate.getData(), dataParams, dataCols);
        entryToUpdate.setData(data);

        return entryToUpdate;
    }


    /**
     * Lists all rows in the spreadsheet.
     * 
     * @return the list< record entry>
     * 
     * @throws ServiceException when the request causes an error in the Google Spreadsheets
     * service.
     * @throws IOException when an error occurs in communication with the Google
     * Spreadsheets service.
     */
    public List<RecordEntry> listAllRecordEntries()
        throws IOException, ServiceException
    {
        RecordFeed feed = this.Service.getFeed(this.TableRecordsFeedUrl, RecordFeed.class);

        return feed.getEntries();
    }
    
    /**
     * Search.
     * 
     * @param fullTextSearchString the full text search string
     * 
     * @return the list< record entry>
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServiceException the service exception
     */
    public List<RecordEntry> search(
        String fullTextSearchString)
        throws IOException, ServiceException
    {
        RecordQuery query = new RecordQuery(this.TableRecordsFeedUrl);
        query.setFullTextQuery(fullTextSearchString);
        query.setReverse(ReverseQueryResults);
        RecordFeed feed = this.Service.query(query, RecordFeed.class);

        return feed.getEntries();
    }
    
    /**
     * Performs a full database-like query on the records.
     * 
     * @param structuredQuery a query like: name = "Bob" and phone != "555-1212"
     * 
     * @return the list< record entry>
     * 
     * @throws ServiceException when the request causes an error in the Google Spreadsheets
     * service.
     * @throws IOException when an error occurs in communication with the Google
     * Spreadsheets service.
     */
    public List<RecordEntry> query(
        String structuredQuery)
        throws IOException, ServiceException
    {
        RecordQuery query = new RecordQuery(this.TableRecordsFeedUrl);
        query.setSpreadsheetQuery(structuredQuery);
        query.setReverse(ReverseQueryResults);
        RecordFeed feed = this.Service.query(query, RecordFeed.class);

        return feed.getEntries();
    }

    public void addWorksheet(
        String title, String rowCount, String colCount)
        throws IOException, ServiceException
    {
        try {
            this.addWorksheet(title, Integer.parseInt(rowCount), Integer.parseInt(colCount));
        }
        catch(NumberFormatException e)
        {
            this.addWorksheet(title, 2, 2);
        }
    }
    
    public void addWorksheet(
        String title, int rowCount, int colCount)
        throws IOException, ServiceException
    {
        WorksheetEntry newWorksheet = new WorksheetEntry();
        newWorksheet.setTitle(new PlainTextConstruct(title));
        newWorksheet.setRowCount(rowCount);
        newWorksheet.setColCount(colCount);
        this.Service.insert(this.WorkSheetsFeedUrl, newWorksheet);
    }
    
    public void addNewTableEntry(
        Map<String, String> params)
        throws IOException, ServiceException
    {
        TableEntry newEntry =
            entryContentsFromParams(new TableEntry(), params);
        this.Service.insert(this.TablesFeedUrl, newEntry);
    }

    public TableEntry entryContentsFromParams(
        TableEntry entryToUpdate, Map<String, String> params)
    {
        Map<String, String> dataParams = Maps.newHashMap();
        Map<String, String> dataCols = Maps.newHashMap();

        for (Entry<String, String> entry : params.entrySet())
        {
            String tag = entry.getKey();
            String value = entry.getValue();
            setTableEntryValues(
                entryToUpdate, dataParams, dataCols,
                tag, value);
        }

        // Update table data.
        Data data =
            dataFromParams(entryToUpdate.getData(), dataParams, dataCols);
        entryToUpdate.setData(data);

        return entryToUpdate;
    }
    
    static private void setTableEntryValues(
        TableEntry entryToUpdate,
        Map<String, String> dataParams,
        Map<String, String> dataCols,
        String tag, String value)
    {
        if (tag.equals("title"))
        {
            entryToUpdate.setTitle(new PlainTextConstruct(value));
        }
        else if (tag.equals("summary"))
        {
            entryToUpdate.setSummary(new PlainTextConstruct(value));
        }
        else if (tag.equals("worksheet"))
        {
            entryToUpdate.setWorksheet(new Worksheet(value));
        }
        else if (tag.equals("header"))
        {
            entryToUpdate.setHeader(new Header(Integer.parseInt(value)));
        }
        else if (tag.equals("startRow") || tag.equals("insertionMode")
            || tag.equals("numRows"))
        {
            dataParams.put(tag, value);
        }
        else if (tag.equals("columns"))
        {
            String[] columns = value.split(";");
            for (int i = 0; i < columns.length; i++)
            {
                String[] colInfo = columns[i].split(":");
                if (colInfo.length < 2)
                {
                    System.out
                        .println(COLUMN_ENTRY_INSTR);
                }
                String index = colInfo[0];
                String name = colInfo[1];
                dataCols.put(index, name);
            }
        }
    }

    /**
     * Create a new data object from any data params that were specified.
     * Data params are numrows, startrow, insertionmode and columns.
     * Any attributes that are not specified are left alone.  So, if data already
     * has insertionmode=insert, specifying numrows=4 and startrow=12 will leave
     * insertionmode as insert.
     * 
     * @param data original data object.
     * @param dataParams new data params to use to update the original
     * data object.
     * @param columnMap map of column index to value used to update the column
     * headers of the table.
     * 
     * @return the data from params
     */

    public Data dataFromParams(
        Data data,
        Map<String, String> dataParams,
        Map<String, String> columnMap)
    {
        Data newData = new Data();
        if (data == null)
        {
            data = new Data();
        }

        if (dataParams.get("numRows") != null)
        {
            newData
                .setNumberOfRows(Integer.parseInt(dataParams.get("numRows")));
        }
        else
        {
            newData.setNumberOfRows(data.getNumberOfRows());
        }

        if (dataParams.get("startRow") != null)
        {
            newData.setStartIndex(Integer.parseInt(dataParams.get("startRow")));
        }
        else
        {
            newData.setStartIndex(data.getStartIndex());
        }

        String insertionMode = dataParams.get("insertionMode");
        if (insertionMode != null && insertionMode.equals("insert"))
        {
            newData.setInsertionMode(Data.InsertionMode.INSERT);
        }

        List<Column> existing = data.getColumns();
        // Add existing column data to column map.
        for (Column existingCol : existing)
        {
            String index = existingCol.getIndex();
            String name = existingCol.getName();
            // If column is being updated, set value, else add a new one.
            if (columnMap.get(index) == null)
            {
                columnMap.put(index, name);
            }
        }

        // Set columns on new data object.
        for (String key : columnMap.keySet())
        {
            newData.addColumn(new Column(key, columnMap.get(key)));
        }
        return newData;
    }
    
    public HashVector<String, WorksheetEntry> listWorksheets()
    throws IOException, ServiceException
{
    HashVector<String, WorksheetEntry> whash = new HashVector<String, WorksheetEntry>();
    WorksheetFeed feed = this.Service.getFeed(this.WorkSheetsFeedUrl, WorksheetFeed.class);
    List<WorksheetEntry> entries = feed.getEntries();
    whash = new  HashVector<String, WorksheetEntry>(entries.size());
    
    for(WorksheetEntry entry: entries)
    {
        whash.put(entry.getTitle().getPlainText(), entry);
    }
    return whash;
}


    /** Base feed url of spreadsheets to use to construct record feed urls. */
    final static public String BaseFeedUrlStr =
        "http://spreadsheets.google.com/feeds/";

    /** The Sheet feed url str. */
    public String SheetFeedUrlStr;

    /** Our view of Google Spreadsheets as an authenticated Google user. */
    public SpreadsheetService SpreadsheetService;
    
    public List<SpreadsheetEntry> Spreadsheets;
    public Map<String, SpreadsheetEntry> SpreadsheetMappedEntries;
    
    public SpreadsheetFeed SpreadsheetFeed;

    /** The URL of the record feed for the specified table. */
    public URL TableRecordsFeedUrl;
    
    /** The Records feed url. */
    public URL RecordsFeedUrl;

    /** The URL of the table feed for the selected spreadsheet. */
    public URL TablesFeedUrl;

    public URL WorkSheetsFeedUrl;
    /** Spreadsheet key of the loaded sheet. */
    public String SpreadsheetKey;

    /** Table id to use to construct the record feed. */
    public int TableId;
    
    /** Reverse query results if true. */
    public boolean ReverseQueryResults = false;
    
    final static public String COLUMN_ENTRY_INSTR =
        "Columns are specified as semicolon delimited column:value pairs,<br/>"+
        "for example, A:Apple; B:Banana; C:Cantaloupe; D:Durian";
}
