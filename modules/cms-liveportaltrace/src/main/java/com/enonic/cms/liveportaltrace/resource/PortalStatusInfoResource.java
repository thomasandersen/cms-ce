package com.enonic.cms.liveportaltrace.resource;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.enonic.cms.core.spring.PrototypeScope;
import com.enonic.cms.liveportaltrace.model.PortalStatusInfoModel;

@Path("/liveportaltrace/rest/statusInfo")
@PrototypeScope
@Component
@Produces("application/json")
public final class PortalStatusInfoResource
{
    private MemoryMXBean memoryMbean;

    public PortalStatusInfoResource()
    {
        memoryMbean = ManagementFactory.getMemoryMXBean();
    }

    @GET
    public PortalStatusInfoModel getStatusInfo()
    {
        PortalStatusInfoModel statusInfo = new PortalStatusInfoModel();
        statusInfo.setDbOpenConnections( 33 );

        MemoryUsage heapMemUsage = memoryMbean.getHeapMemoryUsage();
        statusInfo.setMaxMemoryHeap( heapMemUsage.getMax() );
        statusInfo.setUsedMemoryHeap( heapMemUsage.getUsed() );

        MemoryUsage nonHeapMemUsage = memoryMbean.getNonHeapMemoryUsage();
        statusInfo.setMaxMemoryNonHeap( nonHeapMemUsage.getMax() );
        statusInfo.setUsedMemoryNonHeap( nonHeapMemUsage.getUsed() );

        return statusInfo;
    }
}
