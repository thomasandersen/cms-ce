/*
 * Copyright 2000-2011 Enonic AS
 * http://www.enonic.com/license
 */
package com.enonic.cms.server.service.webdav;

import com.enonic.cms.store.resource.FileResourceService;

import com.enonic.cms.business.core.resource.access.ResourceAccessResolver;
import com.enonic.cms.business.core.security.SecurityService;

/**
 * This class implements the dav configuration.
 */
public class DavConfiguration
{
    private FileResourceService fileResourceService;

    /**
     * Security service.
     */
    private SecurityService securityService;

    private ResourceAccessResolver resourceAccessResolver;

    public FileResourceService getFileResourceService()
    {
        return this.fileResourceService;
    }

    public void setFileResourceService( FileResourceService value )
    {
        this.fileResourceService = value;
    }

    /**
     * Return the login service.
     */
    public SecurityService getSecurityService()
    {
        return this.securityService;
    }

    /**
     * Set the login service.
     */
    public void setSecurityService( SecurityService securityService )
    {
        this.securityService = securityService;
    }

    public ResourceAccessResolver getResourceAccessResolver()
    {
        return resourceAccessResolver;
    }

    public void setResourceAccessResolver( ResourceAccessResolver resourceAccessResolver )
    {
        this.resourceAccessResolver = resourceAccessResolver;
    }
}
