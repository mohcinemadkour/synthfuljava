/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org
 * 
 * PageSignature.java
 *
 * Created on December 30, 2003, 9:36 AM
 */

package org.synthful.http;
import javax.servlet.http.HttpServletRequest;
import org.synthful.util.HashTreeNode;

// TODO: Auto-generated Javadoc
/**
 * The Class PageSignature.
 * 
 * @author Blessed Geek
 */
public class PageSignature
extends HashTreeNode
{
    
    /**
	 * Instantiates a new page signature.
	 * 
	 * @param request
	 *            the request
	 * @param sigkeys
	 *            the sigkeys
	 */
    public PageSignature(HttpServletRequest request, String[] sigkeys)
    {
        Request     = request;
        
        cache_generator_url = Request.getParameter("cache_generator_url");
        cache_init_url      = Request.getParameter("cache_init_url");
        cache_page   = Request.getParameter("cache_page");
        cache_dir    = Request.getParameter("cache_dir");
        
        if (cache_generator_url==null || cache_generator_url.length()==0)
            cache_generator_url = "/Default";
        else
            cache_generator_url = cache_generator_url.trim();

        if (cache_page!=null)
            cache_page = cache_page.trim();

        if (cache_dir==null || cache_page.length()==0)
            cache_dir = "/Default";
        else
            cache_dir = cache_dir.trim();
        
        String[] sigvals = setSignatureKeys(sigkeys);
        Signature = mkSignature(sigkeys, sigvals);
        put("Signature", Signature);
        
        SignatureName = mkSignatureName(sigvals);
        put("SignatureFileName", SignatureName+cache_page);
    }
    
    /**
	 * Creates cache signature.
	 * 
	 * @param keys
	 *            the keys
	 * @param signatureComponents
	 *            the Components contributing to construction of signature
	 * 
	 * @return the signature StringBuffer
	 */
    private StringBuffer mkSignature(String[] keys, String[] signatureComponents)
    {
        StringBuffer sig = new StringBuffer();
        
        if (signatureComponents!=null)
            for(int i=0; i<signatureComponents.length; i++)
                if (signatureComponents[i]!=null && signatureComponents[i].length()>0)
                {
                    sig.append('/').append(signatureComponents[i]);
                    put(keys[i], signatureComponents[i]);
                }
        return sig;
    }
    
    /**
	 * Sets the signature keys.
	 * 
	 * @param sigkeys
	 *            the sigkeys
	 * 
	 * @return the string[]
	 */
    public String[] setSignatureKeys(String[] sigkeys)
    {
        SignatureKeys = sigkeys;
        if (sigkeys!=null)
        {
            String[] sigvals = new String[sigkeys.length];
            for (int i=0; i<sigkeys.length; i++)
            {
                if (sigkeys[i]!=null)
                {
                    sigkeys[i] = sigkeys[i].trim();
                    if (sigkeys[i].trim().length()>0)
                    {
                        sigvals[i] = Request.getParameter(sigkeys[i]);
//                        if (sigvals[i] == null)
//                            sigvals[i] = sigkeys[i];
                     }
                }
                
                if (sigvals[i] == null)
                    sigvals[i] = "";
            }
            
            return sigvals;
        }
        
        return new String[]{""};
    }
    
    /**
	 * Create signature name.
	 * 
	 * @param ss
	 *            the ss
	 * 
	 * @return the string buffer
	 */
    public StringBuffer mkSignatureName(String[] ss)
    {
        StringBuffer ssb = new StringBuffer();
        
        if (ss!=null)
            for(int i=0; i<ss.length; i++)
                if (ss[i]!=null && ss[i].length()>0)
                   ssb.append('_').append(ss[i]);
        return ssb;
    }

    /** The Signature keys. */
    public String[] SignatureKeys;
    
    /** The Request. */
    public HttpServletRequest Request;
    
    /** The Signature. */
    public StringBuffer Signature;
    
    /** The Signature name. */
    public StringBuffer SignatureName;
        
    // *Cache parameters
    /**
	 * URL of jsp/generator generating the page, relative to /Eval/Navigate or
	 * relative to context root.
	 */
    public String cache_generator_url ;
    
    /** URL of jsp to init the check for cache aging. */
    public String cache_init_url ;
    // .
    
    /** Name of cache file generated. */
    public String cache_page ;
    
    /**
	 * The cache_dir. Different applications should deposit into respective
	 * cache directory
	 */
    public String cache_dir  ;
    //     // End - Parameters in IndexForm in /Eval/Navigate/index.jsp
}