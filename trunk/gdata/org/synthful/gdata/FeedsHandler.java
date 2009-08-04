package org.synthful.gdata;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

import com.google.gdata.client.GoogleService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.util.AuthenticationException;

/**
 * The Class FeedsHandler.
 */
abstract public class FeedsHandler
implements Serializable
{
    
    /**
     * Instantiates a new feeds handler.
     */
    public FeedsHandler() {}

    /**
     * Set the user credentials for the given user name and password.
     * 
     * @param username the username of the user toauthenticate 
     *     (e.g. yourname@gmail.com)
     @ @param password the password of the user to authenticate.
     * @throws AuthenticationException if the service is unable to validate the
     *         username and password.
     */
    public void login(String username, String password)
        throws AuthenticationException {
      // Authenticate
        this.getService().setUserCredentials(username, password);
    }
    
    public void authenticateSession(String authToken, PrivateKey key)
    throws AuthenticationException, IOException, GeneralSecurityException
    {
        this.sessionAuthToken =
            AuthSubUtil.exchangeForSessionToken(authToken, key);

        FeedsSilo.logFeedsHdlr.info("SessionAuthToken=" + this.sessionAuthToken);
        FeedsSilo.logFeedsHdlr.info("service=" + this.getService());
        this.getService().setAuthSubToken(this.sessionAuthToken);
        this.updated = true;
        FeedsSilo.logFeedsHdlr.info("service=" + this.getService());
    }

    public void restoreSessionAuth(String sessionAuthToken)
    throws AuthenticationException, IOException, GeneralSecurityException
    {
        this.sessionAuthToken = sessionAuthToken;
        this.getService().setAuthSubToken(this.sessionAuthToken);
        this.updated = true;
    }

    public void setDoc(
        String docKey)
        throws IOException
    {
        this.CurrentDocKey = docKey;
        this.updated = true;
    }
    
    abstract public GoogleService getService();
    
    public boolean isUpdated() {return this.updated;};

    /** Base feed url of spreadsheets to use to construct record feed urls. */
    final static public String BaseFeedUrlStr = "";

    public String sessionAuthToken;
    public URL ListDocsFeedUrl;
    public String CurrentDocKey;
    protected boolean updated;
}
