package com.enonic.cms.liveportaltrace.model;


import com.enonic.cms.portal.livetrace.PortalRequestTrace;

public class SiteModel
{
    private String key;

    private String name;

    public SiteModel( String key, String name )
    {
        this.key = key.toString();
        this.name = name;
    }

    public static SiteModel create( PortalRequestTrace portalRequestTrace )
    {
        return new SiteModel( portalRequestTrace.getSiteKey().toString(), portalRequestTrace.getSiteName() );
    }

    public String getKey()
    {
        return key;
    }

    public String getName()
    {
        return name;
    }
}
