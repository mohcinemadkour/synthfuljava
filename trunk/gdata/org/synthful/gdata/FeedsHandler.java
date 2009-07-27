package org.synthful.gdata;

import java.io.IOException;
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
public class FeedsHandler
{
    
    /**
     * Instantiates a new feeds handler.
     */
    public FeedsHandler() {}

    public FeedsHandler(GoogleService service)
    {
        this.Service = service;
    }

    /**
     * Instantiates a new feeds handler.
     * 
     * @param docKey the document key
     */
    public FeedsHandler(String docKey, GoogleService service)
    {
        this.Service = service;
        
        try {
            this.setDoc(docKey);
        }
        catch (IOException ex)
        {            
        }
    }

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
      this.Service.setUserCredentials(username, password);
    }
    
    public void authenticateSession(String authToken, PrivateKey key)
    throws AuthenticationException, IOException, GeneralSecurityException
    {
        this.SessionAuthToken =
            AuthSubUtil.exchangeForSessionToken(authToken, key);

        this.Service.setAuthSubToken(this.SessionAuthToken);
    }

    public void restoreSessionAuth(String sessionAuthToken)
    throws AuthenticationException, IOException, GeneralSecurityException
    {
        this.SessionAuthToken = sessionAuthToken;
        this.Service.setAuthSubToken(this.SessionAuthToken);
    }

    public void setDoc(
        String docKey)
        throws IOException
    {
        this.CurrentDocKey = docKey;
    }

    /** Base feed url of spreadsheets to use to construct record feed urls. */
    final static public String BaseFeedUrlStr = "";

    public GoogleService Service;
    protected FeedURLFactory FeedUrlFactory = FeedURLFactory.getDefault();
    public String SessionAuthToken;
    public URL ListDocsFeedUrl;
    public String CurrentDocKey;
}
