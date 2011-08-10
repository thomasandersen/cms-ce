package com.enonic.cms.liveportaltrace.model;

public final class PortalRequestTraceModel
{
    private long id;

    private PortalRequestTraceType requestType;

    private SiteModel site;

    private URLModel url;

    private DurationModel duration;

    private String remoteAddress;

    private String username;


    void setId( long id )
    {
        this.id = id;
    }

    void setRequestType( PortalRequestTraceType requestType )
    {
        this.requestType = requestType;
    }

    void setUrl( URLModel url )
    {
        this.url = url;
    }

    void setRemoteAddress( String remoteAddress )
    {
        this.remoteAddress = remoteAddress;
    }

    void setUsername( String username )
    {
        this.username = username;
    }

    void setSite( SiteModel site )
    {
        this.site = site;
    }

    void setDuration( DurationModel duration )
    {
        this.duration = duration;
    }

    public long getId()
    {
        return id;
    }

    public PortalRequestTraceType getRequestType()
    {
        return requestType;
    }

    public SiteModel getSite()
    {
        return site;
    }

    public URLModel getUrl()
    {
        return url;
    }

    public DurationModel getDuration()
    {
        return duration;
    }

    public String getRemoteAddress()
    {
        return remoteAddress;
    }

    public String getUsername()
    {
        return username;
    }
}
