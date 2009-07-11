package com.blessedgeek.gwt.gdata.server;

import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.synthful.gdata.SpreadsheetFeedsHandler;

import com.blessedgeek.gwt.gdata.client.TableMgr;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.RecordEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.AuthenticationException;

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
    
    public void setSheetDoc(HttpServletRequest request)
    {
        String sheetKey = request.getParameter("sheetKey");
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
    
    public TableMgr.Actions action;
    public String AuthToken, SessionAuthToken;
    public SpreadsheetFeedsHandler FeedsHdlr;
    public SpreadsheetService Service;
    public Map<String, RecordEntry> EntriesCached;
    public String SpreadsheetKey;
    public List<SpreadsheetEntry> SpreadsheetEntries;
    //public SpreadsheetEntry SelectedSpreadsheetEntry;
}
