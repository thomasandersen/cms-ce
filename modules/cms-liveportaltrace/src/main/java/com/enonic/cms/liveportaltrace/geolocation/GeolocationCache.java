package com.enonic.cms.liveportaltrace.geolocation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;

public class GeolocationCache
{
    private ConcurrentMap<String, GeolocationInfo> cache;

    GeolocationCache()
    {
        cache = new ConcurrentHashMap<String, GeolocationInfo>();
    }

    public void put( GeolocationInfo geolocationInfo )
    {
        String key = getKey( geolocationInfo );
        cache.putIfAbsent( key, geolocationInfo );
    }

    public GeolocationInfo get( String ipAddress )
    {
        return cache.get( getKey( ipAddress ) );
    }

    private String getKey( GeolocationInfo geolocationInfo )
    {
        return getKey( geolocationInfo.getIpAddress() );
    }

    private String getKey( String ipAddress )
    {
        return StringUtils.trimToEmpty( ipAddress );
    }
}
