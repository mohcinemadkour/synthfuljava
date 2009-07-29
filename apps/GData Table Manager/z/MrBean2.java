package z;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.synthful.gdata.SpreadsheetFeedsHandler;

import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.Field;
import com.google.gdata.data.spreadsheet.RecordEntry;
import com.google.gdata.data.spreadsheet.TableEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class MrBean2
{
    public MrBean2()
    {
        this.Service = new SpreadsheetService("Table Manager");
        this.FeedsHdlr = new SpreadsheetFeedsHandler(this.Service);
    }

    public void readAuthToken(
        HttpServletRequest request)
    {
        String authToken = request.getParameter("token");
        System.out.println(authToken);

        if (authToken != null && authToken.length() > 5)
        {
            // String authToken0 =
            // AuthSubUtil.getTokenFromReply(request.getQueryString());
            // System.out.println("authToken0=" + authToken0);

            this.AuthToken = authToken;
            try
            {
                this.FeedsHdlr.authenticateSession(authToken, this.authKey);
                this.FeedsHdlr.initSpreadsheetFeed(true);
            }
            catch (AuthenticationException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (GeneralSecurityException e)
            {
                e.printStackTrace();
            }

        }
    }

    static public boolean logTokenInfo(
        String token, java.security.PrivateKey key)
    {
        if (token == null)
        {
            System.out.println("No token info: Token is null.");
            return false;
        }

        try
        {
            Map<String, String> tokenInfo =
                AuthSubUtil.getTokenInfo(token, key);
            System.out.println("tokenInfo:" + tokenInfo);
            for (Map.Entry<String, String> info : tokenInfo.entrySet())
            {
                System.out.println(info.getKey() + ':' + info.getValue());
            }

            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
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
