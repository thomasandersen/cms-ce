package com.enonic.cms.admin.user;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.core.InjectParam;

import com.enonic.cms.core.jcr.AccountDaoJcrImpl;
import com.enonic.cms.core.security.user.UserEntity;
import com.enonic.cms.store.dao.UserDao;

import com.enonic.cms.domain.EntityPageList;

@Component
@Path("/admin/data/user")
@Produces("application/json")
public final class UsersResource
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDaoJcrImpl userJcrDao;

    @Autowired
    private UserPhotoService photoService;

    @GET
    @Path("list")
    public UsersModel getAll( @InjectParam final UserLoadRequest req )
    {
        userJcrDao.findAll( req.getStart(), req.getLimit(), req.getQuery(), req.getSort() );

        final EntityPageList<UserEntity> list =
            this.userDao.findAll( req.getStart(), req.getLimit(), req.buildHqlQuery(), req.buildHqlOrder() );
        return UserModelHelper.toModel( list );
    }

    @GET
    @Path("detail")
    public UserModel getUser( @QueryParam("key") final String key )
    {
        final UserEntity entity = findEntity( key );
        return UserModelHelper.toModel( entity );
    }

    @GET
    @Path("photo")
    @Produces("image/png")
    public byte[] getPhoto( @QueryParam("key") final String key, @QueryParam("thumb") @DefaultValue("false") final boolean thumb )
        throws Exception
    {
        final UserEntity entity = findEntity( key );
        return this.photoService.renderPhoto( entity, thumb ? 40 : 100 );
    }

    private UserEntity findEntity( final String key )
    {
        if ( key == null )
        {
            throw new NotFoundException();
        }

        final UserEntity entity = this.userDao.findByKey( key );
        if ( entity == null )
        {
            throw new NotFoundException();
        }

        return entity;
    }

}
