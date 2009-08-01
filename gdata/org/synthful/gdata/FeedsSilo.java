package org.synthful.gdata;

import java.util.logging.Logger;

import com.google.gdata.client.spreadsheet.FeedURLFactory;

public class FeedsSilo
{
    final static public FeedURLFactory FeedUrlFactory = FeedURLFactory.getDefault();
    
    final static public Logger logFeedsHdlr = Logger.getLogger(FeedsHandler.class.getName());
    final static public Logger logSpreadsheetFeedsHdlr = Logger.getLogger(SpreadsheetFeedsHandler.class.getName());
    final static public Logger logSpreadsheetFeedsSilo = Logger.getLogger(SpreadsheetFeedsSilo.class.getName());
}
