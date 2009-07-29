package z;



import java.security.PrivateKey;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.RecordEntry;
import com.google.gdata.data.spreadsheet.TableEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

public class MrBean5
{
    public MrBean5()
    {
        this.Service = new SpreadsheetService("Table Manager");
    }







    public String AuthToken;


    public SpreadsheetService Service;

    public String SpreadsheetKey;

    public TableEntry Table;
    
    public WorksheetEntry Worksheet;

    public List<RecordEntry> ResultRecords;

    public PrivateKey authKey;
}
