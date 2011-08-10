package com.enonic.cms.liveportaltrace.model;

import java.util.ArrayList;
import java.util.List;

public class PortalRequestTraceListModel
{
    private int total = 0;

    private List<PortalRequestTraceModel> requests = new ArrayList<PortalRequestTraceModel>();

    PortalRequestTraceListModel()
    {
        this.requests = new ArrayList<PortalRequestTraceModel>();
    }

    public int getTotal()
    {
        return total;
    }

    void setTotal( int total )
    {
        this.total = total;
    }

    public List<PortalRequestTraceModel> getRequests()
    {
        return requests;
    }

    void setRequests( List<PortalRequestTraceModel> requests )
    {
        this.requests = requests;
    }

    void addRequest( PortalRequestTraceModel request )
    {
        this.requests.add( request );
    }
}
