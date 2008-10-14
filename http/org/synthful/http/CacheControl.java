/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 * CacheControl.java
 *
 * Created on December 30, 2003, 7:11 PM
 */

package org.synthful.http;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import org.synthful.jdbc.JUtil;
import org.synthful.util.HashTreeNode;
import org.synthful.io.SynFile;

// TODO: Auto-generated Javadoc
/**
 * CacheControl Class.
 * 
 * @author Blessed Geek
 */
public class CacheControl
{
    
    /**
	 * Creates a new instance of CacheControl.
	 */
    public CacheControl()
    {
    }
    
    /**
	 * Instantiates a new cache control based on the request.
	 * 
	 * @param request
	 *            the request
	 */
    public CacheControl(HttpServletRequest request)
    {
        setRequest(request);
    }
    
    /**
	 * Sets the request.
	 * 
	 * @param request
	 *            the request
	 */
    public void setRequest(HttpServletRequest request)
    {
        Request = request;
        CacheRootURL = Request.getParameter("CacheRootURL");
    }
    
    /**
	 * Gets the threshold time.
	 * 
	 * @param jdbcConnection
	 *            the jdbc connection
	 * @param sql
	 *            the sql
	 * 
	 * @return the threshold time
	 */
    public static long getThresholdTime(java.sql.Connection jdbcConnection, String sql)
    {
        long sigTime = 0;
        
        if (jdbcConnection!=null && sql != null || sql.length() > 0)
        {                
            Object[][] xxSigTime
                = JUtil.SqlFetch(jdbcConnection, sql);
            try{
            sigTime = ((java.util.Date)xxSigTime[0][0]).getTime();
            }
            catch (NullPointerException e){e.printStackTrace();}
            catch (ClassCastException e){e.printStackTrace();}
            catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}
        }
        
        return sigTime;
    }
    
    /**
	 * Gets the cache signature string.
	 * 
	 * @param jdbcConnection
	 *            the jdbc connection
	 * @param sql
	 *            the sql
	 * 
	 * @return the cache signature string
	 */
    public static String getSigString(java.sql.Connection jdbcConnection, String sql)
    {
        String sigStr = "";
        
        if (jdbcConnection!=null && sql != null || sql.length() > 0)
        {        
            Object[][] xxSigStr
                = JUtil.SqlFetch(jdbcConnection, sql);
            try{
            sigStr = xxSigStr[0][0].toString().trim();
            }
            catch (NullPointerException e){e.printStackTrace();}
            catch (ClassCastException e){e.printStackTrace();}
            catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}
        }
        
        return sigStr;
    }
    
    
    /**
	 * Check validity cache file.
	 * 
	 * @param jdbcConnection
	 *            the jdbc connection
	 * @param sql
	 *            the sql
	 * 
	 * @return true, if up-to-date
	 */
    public boolean ValidCacheFile(java.sql.Connection jdbcConnection, String sql)
    {
        long threshholdtime = getThresholdTime(jdbcConnection,  sql);
        boolean cached = CacheFile.exists() && CacheFile.lastModified() > threshholdtime;
        return cached;
    }


    /**
	 * Create cache file.
	 * 
	 * @param subpath
	 *            the sub-path
	 * @param filename
	 *            the filename
	 */
    public void createCacheFile(String subpath, String filename)
    {
        createCacheFile(Request, subpath, filename);
    }
    
    /**
	 * Create cache file.
	 * 
	 * @param request
	 *            the request
	 * @param subpath
	 *            the sub-path
	 * @param filename
	 *            the filename
	 */
    public void createCacheFile(HttpServletRequest request, String subpath, String filename)
    {
        if (request!=null)
        {
            String usesig = 
                (""+request.getParameter("UseSignatureName")).trim().toUpperCase();
            if (usesig.equals("TRUE"))
            {
                Object signame = request.getParameter("SignatureFileName");
                if (signame!=null & signame.toString().length()>0)
                    filename = signame.toString().trim();
            }
        }
        
        setCacheFile(subpath, filename);
    }
    
    /**
	 * Sets the cache file.
	 * 
	 * @param subpath
	 *            the sub-path
	 * @param filename
	 *            the filename
	 * 
	 * @return the string
	 */
    public String setCacheFile(String subpath, String filename)
    {
      setCacheFileURL(subpath, filename);
      CacheSubPath = subpath;
      String realcpath =  org.synthful.net.ReadURL.ReadURL(CacheRootURL + GetContextJsp);
      CacheFile = new SynFile (realcpath +'/'+ subpath, filename);
      CacheFileDir = CacheFile.getParentFile();
      CacheFileDir.mkdirs();
      return CacheFileURL;
    }

    /**
	 * Sets the cache file url.
	 * 
	 * @param subpath
	 *            the subpath
	 * @param filename
	 *            the filename
	 * 
	 * @return the string
	 */
    protected String setCacheFileURL(String subpath, String filename)
    {
      CacheFileURL = CacheRootURL +'/'+ subpath +'/'+ filename;
      return CacheFileURL;
    }

    /**
	 * Write cache file.
	 * 
	 * @param text
	 *            the text
	 * 
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public void writeCacheFile(StringBuffer text)
    throws 
        FileNotFoundException,
        IOException
    {
        if (text==null)return;
        writeCacheFile(text.toString(), true);
    }
    
    /**
	 * Write cache file.
	 * 
	 * @param text
	 *            the text
	 * @param compact
	 *            the compact
	 * 
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public void writeCacheFile(StringBuffer text, boolean compact)
    throws 
        java.io.FileNotFoundException,
        java.io.IOException
    {
        if (text==null)return;
        writeCacheFile(text.toString(), compact);
    }
    
    /**
	 * Write cache file.
	 * 
	 * @param text
	 *            the text
	 * @param compact
	 *            the compact
	 * 
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public void writeCacheFile(String text, boolean compact)
    throws 
        java.io.FileNotFoundException,
        java.io.IOException
    {
        CacheFile.delete();
        java.io.FileOutputStream cfout = new FileOutputStream(CacheFile);
        java.io.BufferedOutputStream cfbuf = new BufferedOutputStream(cfout);
        java.io.PrintWriter cfprn = new PrintWriter(cfbuf);
        
        if (compact)
        text = text.toString()
              .replaceAll("[\t ][\t ]+"," ")
              .replaceAll("[\t ]*[\n\r][\n\r\t ]+","\n");

        cfprn.println(text);
        cfprn.close();
        cfout.close();
    }

    /** The Get context jsp. */
    public String GetContextJsp = "/GetPhysicalDirectory.jsp";
    
    /** The Cache root url. */
    public String CacheRootURL;
    
    /** The Cache file url. */
    public String CacheFileURL;
    
    /** The Cache sub-path. */
    public String CacheSubPath;
    
    /** The Cache file dir. */
    public File CacheFileDir;
    
    /** The Cache file. */
    public SynFile CacheFile;
    
    /** The Page sig. */
    public PageSignature PageSig;
//    public org.synthful.util.HashTreeNode RequestParams;
    /** The Request. */
public HttpServletRequest Request;
    
    /** The Sig hash. */
    public HashTreeNode SigHash = new HashTreeNode();
}
