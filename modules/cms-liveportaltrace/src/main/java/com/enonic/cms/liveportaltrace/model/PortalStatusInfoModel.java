package com.enonic.cms.liveportaltrace.model;

public class PortalStatusInfoModel
{
    private long maxMemoryHeap;

    private long usedMemoryHeap;

    private long maxMemoryNonHeap;

    private long usedMemoryNonHeap;

    private long dbOpenConnections;

    public long getMaxMemoryHeap()
    {
        return maxMemoryHeap;
    }

    public void setMaxMemoryHeap( long maxMemoryHeap )
    {
        this.maxMemoryHeap = maxMemoryHeap;
    }

    public long getUsedMemoryHeap()
    {
        return usedMemoryHeap;
    }

    public void setUsedMemoryHeap( long usedMemoryHeap )
    {
        this.usedMemoryHeap = usedMemoryHeap;
    }

    public long getMaxMemoryNonHeap()
    {
        return maxMemoryNonHeap;
    }

    public void setMaxMemoryNonHeap( long maxMemoryNonHeap )
    {
        this.maxMemoryNonHeap = maxMemoryNonHeap;
    }

    public long getUsedMemoryNonHeap()
    {
        return usedMemoryNonHeap;
    }

    public void setUsedMemoryNonHeap( long usedMemoryNonHeap )
    {
        this.usedMemoryNonHeap = usedMemoryNonHeap;
    }

    public long getDbOpenConnections()
    {
        return dbOpenConnections;
    }

    public void setDbOpenConnections( long dbOpenConnections )
    {
        this.dbOpenConnections = dbOpenConnections;
    }
}
