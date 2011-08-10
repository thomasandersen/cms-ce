package com.enonic.cms.liveportaltrace.geolocation;


import java.util.ArrayList;
import java.util.List;

public class GeolocationInfoModel
{
    private int total = 0;

    private long lastId = 0;

    private List<GeolocationInfo> locationList = new ArrayList<GeolocationInfo>();

    GeolocationInfoModel()
    {
        this.locationList = new ArrayList<GeolocationInfo>();
    }

    public int getTotal()
    {
        return total;
    }

    void setTotal( int total )
    {
        this.total = total;
    }

    public List<GeolocationInfo> getLocations()
    {
        return locationList;
    }

    void setLocations( List<GeolocationInfo> locationList )
    {
        this.locationList = locationList;
    }

    void addLocation( GeolocationInfo location )
    {
        this.locationList.add( location );
    }

    public long getLastId()
    {
        return lastId;
    }

    public void setLastId( long lastId )
    {
        this.lastId = lastId;
    }
}
