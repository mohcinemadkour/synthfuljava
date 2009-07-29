package z;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PrivateKey;
import java.util.List;

import org.synthful.gdata.SpreadsheetFeedsHandler;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.Field;
import com.google.gdata.data.spreadsheet.RecordEntry;
import com.google.gdata.data.spreadsheet.TableEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class MrBean4
{
    public MrBean4()
    {
        this.Service = new SpreadsheetService("Table Manager");
        this.FeedsHdlr = new SpreadsheetFeedsHandler(this.Service);
    }



    public void setSheetDoc(
        String sheetKey)
    {
        try
        {
            this.FeedsHdlr.setDoc(sheetKey);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void setTable(
        String title)
        throws MalformedURLException
    {
        this.Table = this.FeedsHdlr.TableEntries.get(title);
        String pos = "" + this.FeedsHdlr.TableEntries.getKeyPosition(title);

        String recordsFeedUrlStr = this.FeedsHdlr.RecordsFeedUrl + pos;
        this.FeedsHdlr.TableRecordsFeedUrl = new URL(recordsFeedUrlStr);
    }


    public void setWorksheet(
        String title)
        throws MalformedURLException
    {
        this.Worksheet = this.FeedsHdlr.WorksheetEntries.get(title);
    }

    // useless function
    public void listRecords()
        throws IOException, ServiceException
    {
        List<RecordEntry> entries = this.FeedsHdlr.listAllRecordEntries();
        for (RecordEntry entry : entries)
        {
            for (Field field : entry.getFields())
            {
                field.getName();
                field.getValue();
            }
        }
    }

    public String AuthToken;

    public SpreadsheetFeedsHandler FeedsHdlr;

    public SpreadsheetService Service;

    public String SpreadsheetKey;

    public TableEntry Table;
    
    public WorksheetEntry Worksheet;

    public List<RecordEntry> ResultRecords;

    public PrivateKey authKey;
}
