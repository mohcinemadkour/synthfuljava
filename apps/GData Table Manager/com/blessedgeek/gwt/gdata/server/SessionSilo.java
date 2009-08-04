package com.blessedgeek.gwt.gdata.server;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheEntry;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import org.synthful.util.HashVector;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.TableEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

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
    
    static public boolean containsKey(Serializable key)
    {
        if (beanCache==null)
            initBeanCache();
        return beanCache.containsKey(key);
    }

    static Cache beanCache;
    
}
