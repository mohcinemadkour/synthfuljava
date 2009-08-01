package org.synthful.gdata;

import java.io.IOException;
import java.net.URL;

import com.google.gdata.client.GoogleService;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
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
        
    static public SpreadsheetService SpreadsheetService;
    static public SpreadsheetFeed SpreadsheetFeed;
}
