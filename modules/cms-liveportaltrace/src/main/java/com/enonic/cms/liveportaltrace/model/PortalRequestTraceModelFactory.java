package com.enonic.cms.liveportaltrace.model;

import java.util.List;

import com.enonic.cms.portal.livetrace.PastPortalRequestTrace;
import com.enonic.cms.portal.livetrace.PortalRequestTrace;

public class PortalRequestTraceModelFactory
{

    public static PortalRequestTraceModel createModel( final PastPortalRequestTrace pastPortalRequestTrace )
    {
        final PortalRequestTrace portalRequestTrace = pastPortalRequestTrace.getPortalRequestTrace();

        final PortalRequestTraceModel model = new PortalRequestTraceModel();
        model.setId( pastPortalRequestTrace.getHistoryRecordNumber() );
        model.setSite( SiteModel.create( portalRequestTrace ) );
        model.setUrl( URLModel.create( portalRequestTrace ) );
        model.setRemoteAddress( portalRequestTrace.getHttpRequestRemoteAddress() );
        model.setUsername( portalRequestTrace.getRequester().toString() );
        model.setRequestType( toPortalRequestType( portalRequestTrace.getType() ) );
        model.setDuration( DurationModel.create( portalRequestTrace.getDuration() ) );

        return model;
    }

    public static PortalRequestTraceListModel createListModel( final List<PastPortalRequestTrace> list )
    {
        PortalRequestTraceListModel model = new PortalRequestTraceListModel();
        for ( PastPortalRequestTrace trace : list )
        {
            model.addRequest( createModel( trace ) );
        }
        model.setTotal( list.size() );
        return model;
    }

    public static PortalRequestTraceListModel createEmptyListModel( int total )
    {
        PortalRequestTraceListModel model = new PortalRequestTraceListModel();
        model.setTotal( total );
        return model;
    }

    private static PortalRequestTraceType toPortalRequestType( String type )
    {
        if ( type.equals( "P" ) )
        {
            return PortalRequestTraceType.Page;
        }
        else if ( type.equals( "W" ) )
        {
            return PortalRequestTraceType.Window;
        }
        else if ( type.equals( "A" ) )
        {
            return PortalRequestTraceType.Attachment;
        }
        else
        {
            return PortalRequestTraceType.Unknown;
        }
    }
}
