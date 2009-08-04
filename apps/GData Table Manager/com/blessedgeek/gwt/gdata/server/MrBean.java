package com.blessedgeek.gwt.gdata.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.synthful.gdata.SpreadsheetFeedsHandler;
import org.synthful.gdata.SpreadsheetFeedsSilo;
import org.synthful.gdata.SpreadsheetFeedsSilo.SpreadsheetDescr;
import org.synthful.gdata.SpreadsheetFeedsSilo.TableDescr;
import org.synthful.gdata.SpreadsheetFeedsSilo.WorksheetDescr;
import org.synthful.util.HashVector;

import com.blessedgeek.gwt.gdata.client.Actions;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.Field;
import com.google.gdata.data.spreadsheet.RecordEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.TableEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class MrBean
extends SpreadsheetFeedsHandler
implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 5015854859088660127L;

    public MrBean()
    {
        super(
            new SpreadsheetService("Table Manager")
        );
    }

    public String readAuthToken(
        HttpServletRequest request)
    {
        String authToken = request.getParameter("token");
        SessionSilo.logMrBean.info("authToken=" + authToken);

        if (authToken != null && authToken.length() > 5)
        {
            // String authToken0 =
            // AuthSubUtil.getTokenFromReply(request.getQueryString());
            // System.out.println("authToken0=" + authToken0);

            this.authToken = authToken;
            try
            {
                if (this.getService()==null)
                    SpreadsheetFeedsSilo.SpreadsheetService =
                        new SpreadsheetService("Table Manager");
                
                this.authenticateSession(authToken, this.authKey);
                SessionSilo.logMrBean.info("SessionAuthToken=" + this.sessionAuthToken);
                SpreadsheetFeedsSilo.initSpreadsheetFeed(true);
                //if (this.FeedsHdlr.SessionAuthToken!=null)
                //    this.AuthToken = null;
                SessionSilo.logMrBean.info("SessionAuthToken=" + this.sessionAuthToken);
                SessionSilo.logMrBean.info("hash=" + this.hashCode());
                
                this.updated = true;
                return this.sessionAuthToken;
            }
            catch (AuthenticationException e)
            {
                SessionSilo.logMrBean.info(e.toString());
                return e.toString();
            }
            catch (IOException e)
            {
                SessionSilo.logMrBean.info(e.toString());
                return e.toString();
            }
            catch (GeneralSecurityException e)
            {
                SessionSilo.logMrBean.info(e.toString());
                return e.toString();
            }
            catch (ServiceException e)
            {
                SessionSilo.logMrBean.info(e.toString());
                return e.toString();
            }
            catch (Exception e)
            {
                SessionSilo.logMrBean.info(e.toString());
                return e.toString();
            }

        }
        return authToken;
    }
    
    public boolean revokeSessionAuth()
    {
        try{
            AuthSubUtil.revokeToken(this.sessionAuthToken,null);
            this.sessionAuthToken = null;
            this.authToken = null;
            this.updated = true;
            
            return true;
           }
           catch (Exception ex)
           {
               return false;
           }        
    }


    public HashVector<String, SpreadsheetDescr> listDocs(boolean refresh)
    {
        try
        {
            this.mapDocs(refresh);
        }
        catch (Exception e)
        {
            SessionSilo.logMrBean.info(e.toString());
        }
        
        return getSpreadsheetDescrs();
    }

    public void setSheetDoc(
        String sheetKey)
    {
        try
        {
            this.setDoc(sheetKey);
        }
        catch (IOException e)
        {
            SessionSilo.logMrBean.info(e.toString());
        }
    }

    public HashVector<String, TableDescr> listTables()
    {
        try
        {
            this.mapTables();
            return getTableDescrs();
        }
        catch (Exception e)
        {
            SessionSilo.logMrBean.info(e.toString());
            return null;
        }
    }

    public void setTable(
        String title)
        throws MalformedURLException
    {
        this.setTable(getTableDescrs().get(title));
        String pos = "" + getTableDescrs().getKeyPosition(title);

        String recordsFeedUrlStr = this.RecordsFeedUrl + pos;
        this.TableRecordsFeedUrl = new URL(recordsFeedUrlStr);
        
        this.updated = true;
    }

    public HashVector<String, WorksheetDescr> listWorksheets()
    {
        try
        {
            this.mapWorksheets();
            return getWorksheetDescrs();
        }
        catch (Exception e)
        {
            SessionSilo.logMrBean.info(e.toString());
            return null;
        }
    }

    public void setWorksheet(
        String title)
        throws MalformedURLException
    {
        this.Worksheet = getWorksheetDescrs().get(title);
        this.updated = true;
    }
    
    public String setSessionId(HttpSession sess)
    {
        SessionSilo.logMrBean.info(sess.getId());
        if (this.sessionId==null)
            this.sessionId = sess.getId();
        SessionSilo.logMrBean.info(this.sessionId);
        return this.sessionId;
    }

    // useless function
    public void listRecords()
        throws IOException, ServiceException
    {
        List<RecordEntry> entries = this.listAllRecordEntries();
        for (RecordEntry entry : entries)
        {
            for (Field field : entry.getFields())
            {
                field.getName();
                field.getValue();
            }
        }
    }

    public TableEntry getTableEntry()
    {
        try {            
            return this.getTableEntry(this.table);
        }
        catch (Exception e)
        {
            SessionSilo.logMrBean.warning(e.toString());
            return null;
        }
    }

    public Actions action;

    public String authToken;

    public String SpreadsheetKey;

    public TableDescr getTable()
    {
        return this.table;
    }
    
    public void setTable(TableDescr t)
    {
        this.table = t;
    }
    
    public WorksheetDescr Worksheet;
    
    public TableDescr table;

    //public List<RecordEntry> ResultRecords;

    public PrivateKey authKey;
    
    public String sessionId;
}
