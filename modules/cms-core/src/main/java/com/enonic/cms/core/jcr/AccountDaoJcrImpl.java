package com.enonic.cms.core.jcr;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.jcr.JcrCallback;
import org.springframework.extensions.jcr.support.JcrDaoSupport;

import com.enonic.cms.core.security.user.QualifiedUsername;
import com.enonic.cms.core.security.user.UserEntity;
import com.enonic.cms.core.security.user.UserKey;

import com.enonic.cms.domain.EntityPageList;

public class AccountDaoJcrImpl
    extends JcrDaoSupport
{
    private static final Logger LOG = LoggerFactory.getLogger( AccountDaoJcrImpl.class );

    public UserEntity findByKey( String key )
    {
        return null;
    }

    public UserEntity findByKey( UserKey key )
    {
        return null;
    }

    public UserEntity findByQualifiedUsername( final QualifiedUsername qualifiedUsername )
    {
        return null;
    }

    public EntityPageList<UserEntity> findAll( int index, int count, String query, String order )
    {
        @SuppressWarnings("unchecked") EntityPageList<UserEntity> users =
            (EntityPageList<UserEntity>) getTemplate().execute( new JcrCallback()
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    NodeIterator nodeIterator = session.getRootNode().getNode( "enonic/userstores/" ).getNodes( "*" );
                    LOG.info( nodeIterator.getSize() + " users found" );
                    while ( nodeIterator.hasNext() )
                    {
                        Node childNode = nodeIterator.nextNode();
                        LOG.info( "" + childNode.getPath() );
                    }

                    return null;
                }
            } );
        return users;
    }

}