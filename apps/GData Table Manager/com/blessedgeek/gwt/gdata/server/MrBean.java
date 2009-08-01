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
implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 5015854859088660127L;

    public MrBean()
    {
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

            this.AuthToken = authToken;
            try
            {
                if (this.FeedsHdlr.getService()==null)
                    SpreadsheetFeedsSilo.SpreadsheetService =
                        new SpreadsheetService("Table Manager");
                
                this.FeedsHdlr.authenticateSession(authToken, this.authKey);
                SessionSilo.logMrBean.info("SessionAuthToken=" + this.FeedsHdlr.SessionAuthToken);
                SpreadsheetFeedsSilo.initSpreadsheetFeed(true);
                //if (this.FeedsHdlr.SessionAuthToken!=null)
                //    this.AuthToken = null;
                SessionSilo.logMrBean.info("SessionAuthToken=" + this.FeedsHdlr.SessionAuthToken);
                SessionSilo.logMrBean.info("hash=" + this.hashCode());
                SessionSilo.logMrBean.info("FeedsHdlr hash=" + this.FeedsHdlr.hashCode());
                return this.FeedsHdlr.SessionAuthToken;
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

    static public boolean logTokenInfo(
        String token, java.security.PrivateKey key)
    {
        SessionSilo.logMrBean.info("token:" + token);
        if (token == null)
        {
            SessionSilo.logMrBean.info("No token info: Token is null.");
            return false;
        }

        try
        {
            Map<String, String> tokenInfo =
                AuthSubUtil.getTokenInfo(token, key);
            SessionSilo.logMrBean.info("tokenInfo:" + tokenInfo);
            for (Map.Entry<String, String> info : tokenInfo.entrySet())
            {
                SessionSilo.logMrBean.info
                    (info.getKey() + ':' + info.getValue());
            }

            return true;
        }
        catch (Exception e)
        {
            SessionSilo.logMrBean.info(e.toString());
            return false;
        }
    }

    public HashVector<String, SpreadsheetEntry> listDocs(boolean refresh)
    {
        try
        {
            this.FeedsHdlr.mapDocs(refresh);
        }
        catch (Exception e)
        {
            SessionSilo.logMrBean.info(e.toString());
        }
        
        return this.FeedsHdlr.SpreadsheetEntries;
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
            SessionSilo.logMrBean.info(e.toString());
        }
    }

    public HashVector<String, TableEntry> listTables()
    {
        try
        {
            this.FeedsHdlr.mapTables();
            return this.FeedsHdlr.TableEntries;
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
        this.Table = this.FeedsHdlr.TableEntries.get(title);
        String pos = "" + this.FeedsHdlr.TableEntries.getKeyPosition(title);

        String recordsFeedUrlStr = this.FeedsHdlr.RecordsFeedUrl + pos;
        this.FeedsHdlr.TableRecordsFeedUrl = new URL(recordsFeedUrlStr);
    }

    public HashVector<String, WorksheetEntry> listWorksheets()
    {
        try
        {
            this.FeedsHdlr.mapWorksheets();
            return this.FeedsHdlr.WorksheetEntries;
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
        this.Worksheet = this.FeedsHdlr.WorksheetEntries.get(title);
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

    final public SpreadsheetFeedsHandler FeedsHdlr =
        new SpreadsheetFeedsHandler(
            new SpreadsheetService("Table Manager")
        );

    public Actions action;

    public String AuthToken;

    public String SpreadsheetKey;

    public TableEntry Table;
    
    public WorksheetEntry Worksheet;

    public List<RecordEntry> ResultRecords;

    public PrivateKey authKey;
    
    public String sessionId;
}
