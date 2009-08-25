package com.blessedgeek.gwt.gdata.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.gdata.client.http.AuthSubUtil;

public class SessionSilo
{
    final static public Logger logSessionSilo =
        Logger.getLogger(SessionSilo.class.getName());

    final static public Logger logMrBean =
        Logger.getLogger(MrBean.class.getName());

    final static public Logger logTableAction =
        Logger.getLogger("TableActionService");

    final static public Logger logTableMgr =
        Logger.getLogger("TableMgr");

    /**
     * Create new instance of static cache, beanCache.
     * GAE memcache is configurable thro certain reserved keys.
     * A reserved key and its value can be injected into the
     * cache thro a hashmap.
     * If a config hashmap is not injected into the cache,
     * the cache would take on default values.
     * 
     */
    static public void initBeanCache()
    {
        if (beanCache!=null)
            return;
        Map cfgMap = new HashMap();
        cfgMap.put(GCacheFactory.EXPIRATION_DELTA, 900);
        try
        {
            CacheFactory cacheFactory =
                CacheManager.getInstance().getCacheFactory();
            beanCache = cacheFactory.createCache(cfgMap);
        }
        catch (CacheException e)
        {
        }
    }
    
    /**
     * Call this at start of a JSP to get the session bean
     * If bean does not exist, create and put it in the cache.
     */
    static public MrBean initSessionBean(String sessId)
    {
        MrBean mrBean = getBean(sessId);
        if (mrBean==null)
        {
            mrBean = new MrBean();
            mrBean.sessionId = sessId;
            putBean(sessId, mrBean);
        }
        
        return mrBean;
    }
    
    /**
     * Call this at the end of a JSP.
     * If any routine updates the bean,
     * those routines would need to set updated flag to true.
     * If updated is true, then put the bean back into cache
     * to over-write its existing blob in the cache.
     * 
     * @param mrBean
     */
    static public void storeSessionBean(MrBean mrBean)
    {
        if(mrBean.isUpdated())
            putBean(mrBean.sessionId, mrBean);
    }
    
    static public boolean logTokenInfo(
        String token, java.security.PrivateKey key)
    {
        SessionSilo.logSessionSilo.info("token:" + token);
        if (token == null)
        {
            SessionSilo.logSessionSilo.info("No token info: Token is null.");
            return false;
        }

        try
        {
            Map<String, String> tokenInfo =
                AuthSubUtil.getTokenInfo(token, key);
            SessionSilo.logSessionSilo.info("tokenInfo:" + tokenInfo);
            for (Map.Entry<String, String> info : tokenInfo.entrySet())
            {
                SessionSilo.logSessionSilo.info
                    (info.getKey() + ':' + info.getValue());
            }

            return true;
        }
        catch (Exception e)
        {
            SessionSilo.logSessionSilo.info(e.toString());
            return false;
        }
    }
    
    static public MrBean putBean(String key, MrBean value)
    {
        if (beanCache==null)
            initBeanCache();
        Object o = beanCache.put(key, value);
        if (o==null)
            return null;
        
        if (o instanceof MrBean)
            return (MrBean)o;
        
        return null;
        
    }
    
    static public MrBean getBean(Serializable key)
    {
        if (beanCache==null)
            initBeanCache();
        Object o = beanCache.get(key);
        
        if (o==null)
            return null;
        
        if (o instanceof MrBean)
            return (MrBean)o;
        
        return null;
    }
    
    static public MrBean removeBean(String key)
    {
        if (beanCache==null)
            return null;
        Object o = beanCache.remove(key);
        
        if (o==null)
            return null;
        
        if (o instanceof MrBean)
            return (MrBean)o;
        
        return null;
        
    }
    
    static public boolean containsKey(Serializable key)
    {
        if (beanCache==null)
            initBeanCache();
        return beanCache.containsKey(key);
    }

    static Cache beanCache;
    
}
