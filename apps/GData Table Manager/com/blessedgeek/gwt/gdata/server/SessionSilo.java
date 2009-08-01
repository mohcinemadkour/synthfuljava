package com.blessedgeek.gwt.gdata.server;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheEntry;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

public class SessionSilo
{
    final static public Logger logMrBean =
        Logger.getLogger(MrBean.class.getName());

    final static public Logger logTableAction =
        Logger.getLogger("TableActionService");

    final static public Logger logTableMgr =
        Logger.getLogger("TableMgr");

    static public void initCache()
    {
        if (cache!=null)
            return;
        Map store = Collections.emptyMap();
        store.put(GCacheFactory.EXPIRATION_DELTA, 900);
        try
        {
            CacheFactory cacheFactory =
                CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(store);
        }
        catch (CacheException e)
        {
        }
    }
    
    static public Object put(Serializable key, Serializable value)
    {
        if (cache==null)
            initCache();
        return cache.put(key, value);
    }
    
    static public Object get(Serializable key)
    {
        if (cache==null)
            initCache();
        return cache.get(key);
    }
    
    static public boolean containsKey(Serializable key)
    {
        if (cache==null)
            initCache();
        return cache.containsKey(key);
    }

    static Cache cache;
}
