package com.enonic.cms.liveportaltrace.geolocation;

/**
 * Service that handles resolving of ip address to global location (longitude, latitude).
 */
public interface GeolocationService
{

    /**
     * Search the location corresponding to the ip address specified.
     *
     * @param ipAddress in the format 123.123.123.123
     */
    GeolocationInfo findLocation( String ipAddress );

}
