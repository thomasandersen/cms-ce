/*
 * Copyright 2000-2011 Enonic AS
 * http://www.enonic.com/license
 */
package com.enonic.cms.business.portal.rendering;

import org.springframework.beans.factory.annotation.Autowired;

import com.enonic.vertical.VerticalProperties;

import com.enonic.cms.core.service.DataSourceService;

import com.enonic.cms.business.SitePropertiesService;
import com.enonic.cms.business.SiteURLResolver;
import com.enonic.cms.business.core.resource.ResourceService;
import com.enonic.cms.business.core.security.SecurityService;
import com.enonic.cms.business.portal.cache.PageCacheService;
import com.enonic.cms.business.portal.cache.SiteCachesService;
import com.enonic.cms.business.portal.datasource.DatasourceExecutorFactory;
import com.enonic.cms.business.portal.instruction.PostProcessInstructionExecutor;
import com.enonic.cms.business.portal.livetrace.LivePortalTraceService;
import com.enonic.cms.business.portal.rendering.viewtransformer.PortletXsltViewTransformer;

/**
 * Apr 20, 2009
 */
public class WindowRendererFactory
{
    @Autowired
    private SiteCachesService siteCachesService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private DatasourceExecutorFactory datasourceExecutorFactory;

    @Autowired
    private PortletXsltViewTransformer portletXsltViewTransformer;

    @Autowired
    private VerticalProperties verticalProperties;

    @Autowired
    private SitePropertiesService sitePropertiesService;

    @Autowired
    private SiteURLResolver siteURLResolver;

    @Autowired
    private PostProcessInstructionExecutor postProcessInstructionExecutor;

    @Autowired
    private LivePortalTraceService livePortalTraceService;

    @Autowired
    private DataSourceService dataSourceService;

    public WindowRenderer createPortletRenderer( WindowRendererContext windowRendererContext )
    {
        PageCacheService pageCacheService = siteCachesService.getPageCacheService( windowRendererContext.getSite().getKey() );

        WindowRenderer windowRenderer = new WindowRenderer( windowRendererContext );

        windowRenderer.setDataSourceExecutorFactory( datasourceExecutorFactory );
        windowRenderer.setPageCacheService( pageCacheService );
        windowRenderer.setPortletXsltViewTransformer( portletXsltViewTransformer );
        windowRenderer.setResourceService( resourceService );
        windowRenderer.setSiteURLResolver( siteURLResolver );
        windowRenderer.setSitePropertiesService( sitePropertiesService );
        windowRenderer.setVerticalProperties( verticalProperties );
        windowRenderer.setPostProcessInstructionExecutor( postProcessInstructionExecutor );
        windowRenderer.setLiveTraceService( livePortalTraceService );
        windowRenderer.setDataSourceService( dataSourceService );

        return windowRenderer;
    }

}
