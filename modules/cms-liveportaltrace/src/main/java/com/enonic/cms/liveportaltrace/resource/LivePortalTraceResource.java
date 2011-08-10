package com.enonic.cms.liveportaltrace.resource;

import java.net.URI;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.springframework.stereotype.Component;

import com.enonic.cms.core.spring.PrototypeScope;

@Path("/liveportaltrace")
@PrototypeScope
@Component
public final class LivePortalTraceResource
{
    @Context
    private ServletContext context;

    @GET
    public Response handleGet()
    {
        final URI uri = UriBuilder.fromResource( LivePortalTraceResource.class ).segment( "index.html" ).build();
        return Response.seeOther( uri ).build();
    }

    @GET
    @Path("{path:.+}")
    public StaticResource handleResource( @PathParam("path") final String path )
    {
        return new StaticResource( this.context, "/liveportaltrace/" + path );
    }
}
