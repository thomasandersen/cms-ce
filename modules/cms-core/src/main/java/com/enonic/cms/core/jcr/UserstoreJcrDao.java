package com.enonic.cms.core.jcr;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.jcr.JcrCallback;
import org.springframework.extensions.jcr.support.JcrDaoSupport;

import com.enonic.cms.core.security.userstore.UserStoreEntity;

public class UserstoreJcrDao
    extends JcrDaoSupport
{
    private static final Logger LOG = LoggerFactory.getLogger( UserstoreJcrDao.class );

    public void store( final UserStoreEntity userstore )
    {
        getTemplate().execute( new JcrCallback()
        {
            public Object doInJcr( Session session )
                throws IOException, RepositoryException
            {

                String userstoreNodeName = userstore.getName();
                Node userstoresNode = session.getRootNode().getNode( JcrCmsConstants.USERSTORES_PATH );
                if ( !userstoresNode.hasNode( userstoreNodeName ) )
                {
                    createNewUserstore( session, userstore );
                }

                session.save();

                return null;
            }
        } );
    }

    private void createNewUserstore( Session session, final UserStoreEntity userstore )
        throws RepositoryException
    {
        String userstoreNodeName = userstore.getName();
        Node userstoresNode = session.getRootNode().getNode( JcrCmsConstants.USERSTORES_PATH );
        Node userstoreNode = userstoresNode.addNode( userstoreNodeName, JcrCmsConstants.USERSTORE_NODE_TYPE );

        Node enonicGroupsRoles = userstoreNode.addNode( JcrCmsConstants.GROUPS_NODE, JcrCmsConstants.GROUPS_NODE_TYPE );
        Node enonicUsersRoles = userstoreNode.addNode( JcrCmsConstants.USERS_NODE, JcrCmsConstants.USERS_NODE_TYPE );
    }

}
