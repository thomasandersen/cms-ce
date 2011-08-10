package com.enonic.cms.liveportaltrace.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enonic.cms.core.spring.PrototypeScope;
import com.enonic.cms.liveportaltrace.model.PortalRequestTraceListModel;
import com.enonic.cms.liveportaltrace.model.PortalRequestTraceModel;
import com.enonic.cms.liveportaltrace.model.PortalRequestTraceModelFactory;
import com.enonic.cms.portal.livetrace.LivePortalTraceService;
import com.enonic.cms.portal.livetrace.PastPortalRequestTrace;

@Path("/liveportaltrace/rest/portal-request-trace-history-list")
@PrototypeScope
@Component
@Produces("application/json")
public final class PortalRequestTraceHistoryListResource
{
    @Autowired
    private LivePortalTraceService livePortalTraceService;

    @GET
    public PortalRequestTraceListModel getNewRequestsFromHistory( @DefaultValue("0") @QueryParam("lastId") final long lastId )
    {
        final List<PastPortalRequestTrace> pastPortalRequestTraceList = livePortalTraceService.getHistoryOfPortalRequests();

        final List<PastPortalRequestTrace> newRequestsSinceLast = new ArrayList<PastPortalRequestTrace>();
        for ( PastPortalRequestTrace req : pastPortalRequestTraceList )
        {
            if ( req.getHistoryRecordNumber() > lastId )
            {
                newRequestsSinceLast.add( req );
            }
        }

        return PortalRequestTraceModelFactory.createListModel( newRequestsSinceLast );
    }

    @Path("{key}")
    public PortalRequestTraceModel getRequest( @PathParam("id") final String key )
    {
        throw new UnsupportedOperationException( "Not implemented" );
    }
}
