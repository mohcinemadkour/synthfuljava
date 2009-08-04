package org.synthful.gdata;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.synthful.util.HashVector;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.TableEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class SpreadsheetFeedsSilo
extends FeedsSilo
{

    static public boolean initSpreadsheetFeed(boolean refresh)
    throws IOException, ServiceException
    {
        if (SpreadsheetFeedsSilo.SpreadsheetFeed!=null && !refresh)
            return true;
        URL feedUrl = FeedsSilo.FeedUrlFactory.getSpreadsheetsFeedUrl();
        FeedsSilo.logSpreadsheetFeedsSilo.info("feedUrl=" + feedUrl);
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            FeedsSilo.logSpreadsheetFeedsSilo.warning(e.toString());
        }
        
        SpreadsheetFeedsSilo.SpreadsheetFeed =
            SpreadsheetService.getFeed(
                feedUrl,
                SpreadsheetFeed.class);
        
        FeedsSilo.logSpreadsheetFeedsSilo.info("SpreadsheetFeed=" + SpreadsheetFeed);
        return true;
    }

    static public class SpreadsheetDescr
    implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 792714907464237866L;
        public SpreadsheetDescr(
            String key,
            String title,
            URL worksheetFeedURL
            )
        {
            this.key = key;
            this.title = title;
            this.worksheetFeedURL = worksheetFeedURL;
        }
        
        public SpreadsheetDescr(SpreadsheetEntry entry)
        {
            this.key = entry.getKey();
            this.title = entry.getTitle().getPlainText();
            this.worksheetFeedURL = entry.getWorksheetFeedUrl();
        }
        
        public String key;
        public String title;
        public URL worksheetFeedURL;
    }
    
    static public class WorksheetDescr
    implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 8562036861812708871L;
        public WorksheetDescr(WorksheetEntry entry)
        {
            this.title = entry.getTitle().getPlainText();
            this.name = this.title;
            this.cellFeedURL = entry.getCellFeedUrl();
            this.listFeedURL = entry.getListFeedUrl();
        }
        
        public String name;
        public String title;
        public URL cellFeedURL;
        public URL listFeedURL;
    }

    static public class TableDescr
    implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 8594762104667681497L;
        public TableDescr(TableEntry entry)
        {
            this.title = entry.getTitle().getPlainText();
            this.worksheet = entry.getWorksheet().getName();
        }
        
        public String title;
        public String worksheet;
    }

    //static final public HashVector<String, TableEntry> tableEntries =
    //    new  HashVector<String, TableEntry>();

    static public SpreadsheetService SpreadsheetService;
    static public SpreadsheetFeed SpreadsheetFeed;
    static public TableEntry Table;
    
}
