package com.enonic.cms.liveportaltrace.model;

import com.enonic.cms.portal.livetrace.PortalRequestTrace;

public class URLModel
{
    private String originalURL;

    private String internalURL;

    private String siteLocalUrl;

    public URLModel( String originalURL, String internalURL, String siteLocalUrl )
    {
        this.originalURL = originalURL;
        this.internalURL = internalURL;
        this.siteLocalUrl = siteLocalUrl;
    }

    public static URLModel create( PortalRequestTrace portalRequestTrace )
    {
        return new URLModel( portalRequestTrace.getUrl(), portalRequestTrace.getUrl(), portalRequestTrace.getSiteLocalUrl() );
    }

    public String getOriginalURL()
    {
        return originalURL;
    }

    public String getInternalURL()
    {
        return internalURL;
    }

    public String getSiteLocalUrl()
    {
        return siteLocalUrl;
    }
}
