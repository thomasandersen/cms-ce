/*
 * Copyright 2000-2011 Enonic AS
 * http://www.enonic.com/license
 */
package com.enonic.cms.business.resolver.deviceclass;

import javax.servlet.http.HttpServletResponse;

import com.enonic.cms.domain.resolver.ForcedResolverValueLifetimeSettings;
import com.enonic.cms.domain.resolver.ResolverContext;

public interface DeviceClassResolverService
{
    String getDeviceClass( ResolverContext context );

    void setForcedDeviceClass( ResolverContext context, HttpServletResponse response, ForcedResolverValueLifetimeSettings setting,
                               String deviceClass );

    void resetDeviceClass( ResolverContext context, HttpServletResponse response );
}
