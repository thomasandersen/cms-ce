package com.enonic.cms.liveportaltrace.resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enonic.cms.core.spring.PrototypeScope;
import com.enonic.cms.liveportaltrace.model.PortalRequestTraceListModel;
import com.enonic.cms.liveportaltrace.model.PortalRequestTraceModelFactory;
import com.enonic.cms.liveportaltrace.model.treetable.TraceTreeTableNodeModel;
import com.enonic.cms.liveportaltrace.model.treetable.TraceTreeTableNodeModelFactory;
import com.enonic.cms.portal.livetrace.LivePortalTraceService;
import com.enonic.cms.portal.livetrace.PastPortalRequestTrace;

@Path("/liveportaltrace/rest/portal-request-trace-history")
@PrototypeScope
@Component
@Produces("application/json")
public final class PortalRequestTraceHistoryListResource
{
    @Autowired
    private LivePortalTraceService livePortalTraceService;

    private LinkedHashMap<Long, PastPortalRequestTrace> history = new LinkedHashMap<Long, PastPortalRequestTrace>();

    @GET
    @Path("list")
    public PortalRequestTraceListModel getNewRequestsFromHistory( @DefaultValue("0") @QueryParam("lastId") final long lastId )
    {
        final List<PastPortalRequestTrace> pastPortalRequestTraceList = livePortalTraceService.getHistoryOfPortalRequests();

        final List<PastPortalRequestTrace> newSinceLast = new ArrayList<PastPortalRequestTrace>();
        for ( PastPortalRequestTrace pastPortalRequestTrace : pastPortalRequestTraceList )
        {
            if ( pastPortalRequestTrace.getHistoryRecordNumber() > lastId )
            {
                newSinceLast.add( pastPortalRequestTrace );
                history.put( pastPortalRequestTrace.getHistoryRecordNumber(), pastPortalRequestTrace );
            }
        }

        return PortalRequestTraceModelFactory.createListModel( newSinceLast );
    }

    @GET
    @Path("detail")
    public TraceTreeTableNodeModel getRequest( @QueryParam("id") final String id )
    {

        Long historyId = new Long( id );

        final List<PastPortalRequestTrace> pastPortalRequestTraceList = livePortalTraceService.getHistoryOfPortalRequests();

        for( PastPortalRequestTrace trace : pastPortalRequestTraceList )
        {
            if( historyId.equals( trace.getHistoryRecordNumber() ) )
            {
                TraceTreeTableNodeModel model = TraceTreeTableNodeModelFactory.create( trace );
                System.out.println("returning root node for: " + trace.getHistoryRecordNumber());
                return model;
            }
        }

        return TraceTreeTableNodeModelFactory.createEmpty();

        /*
        PastPortalRequestTrace pastPortalRequestTrace = history.get( historyId );
        if ( pastPortalRequestTrace != null )
        {
            return PortalRequestTraceTreeTableModelFactory.createModel( pastPortalRequestTrace );
        }
        else
        {
            return null;
        } */
    }
}
