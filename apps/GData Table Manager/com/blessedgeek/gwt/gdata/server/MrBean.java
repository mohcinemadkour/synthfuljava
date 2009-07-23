package com.blessedgeek.gwt.gdata.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.synthful.gdata.SpreadsheetFeedsHandler;
import org.synthful.util.HashVector;

import com.blessedgeek.gwt.gdata.client.TableMgr;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.Field;
import com.google.gdata.data.spreadsheet.RecordEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.TableEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class MrBean
{
    public MrBean()
    {
        this.Service = new SpreadsheetService("Table Manager");
        this.FeedsHdlr = new SpreadsheetFeedsHandler(this.Service);
        this.EntriesCached = new HashMap<String, RecordEntry>();
    }
    
    
    public void readAuthToken(HttpServletRequest request)
    {
        String authToken = request.getParameter("token");
        System.out.println(authToken);
        
        if (authToken != null && authToken.length()>5)
        {
            //String authToken0 =
            //    AuthSubUtil.getTokenFromReply(request.getQueryString());
            //System.out.println("authToken0=" + authToken0);
            
            this.AuthToken = authToken;
            try
            {
                this.SessionAuthToken =
                    AuthSubUtil.exchangeForSessionToken(
                        this.AuthToken, null);
                
                this.Service.setAuthSubToken(this.SessionAuthToken);
                this.FeedsHdlr.initSpreadsheetFeed(true);
            }
            catch (AuthenticationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (GeneralSecurityException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    
    public void setSheetDoc(String sheetKey)
    {
        try
        {
            this.FeedsHdlr.setDoc(sheetKey);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    static public boolean logTokenInfo(String token, java.security.PrivateKey key)
    {
        if (token==null)
        {
            System.out.println("No token info: Token is null.");
            return false;
        }
        
        try {
            Map<String, String> tokenInfo = AuthSubUtil.getTokenInfo(token, key);
            System.out.println("tokenInfo:"+ tokenInfo);
            for(Map.Entry<String, String> info: tokenInfo.entrySet())
            {
                System.out.println(info.getKey()+':'+info.getValue());
            }
            
            return true;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
    
    public void listDocs()
    {
        try {
            this.SpreadsheetEntries =  this.FeedsHdlr.listDocs(false);
        }
        catch (Exception e)
        {
        }
    }
    
    public void listTables()
    {
        try {
            this.TableEntries =  this.FeedsHdlr.listTables(this.TableEntries);
        }
        catch (Exception e)
        {
        }
    }
    
    public void setTable(String title)
    throws MalformedURLException
    {
        this.Table = this.TableEntries.get(title);
        String pos = "" + this.TableEntries.getKeyPosition(title);
        
        String recordsFeedUrlStr = this.FeedsHdlr.RecordsFeedUrl + pos;
        this.FeedsHdlr.TableRecordsFeedUrl = new URL(recordsFeedUrlStr);
    }
    
    public void listRecords ()
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
    
    public TableMgr.Actions action;
    public String AuthToken, SessionAuthToken;
    public SpreadsheetFeedsHandler FeedsHdlr;
    public SpreadsheetService Service;
    public Map<String, RecordEntry> EntriesCached;
    public String SpreadsheetKey;
    public List<SpreadsheetEntry> SpreadsheetEntries;
    public HashVector<String, TableEntry> TableEntries;
    public TableEntry Table;
    public List<RecordEntry> ResultRecords;
    //public SpreadsheetEntry SelectedSpreadsheetEntry;
}
