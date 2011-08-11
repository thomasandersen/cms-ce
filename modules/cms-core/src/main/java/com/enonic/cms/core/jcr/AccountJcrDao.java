package com.enonic.cms.core.jcr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.qom.Column;
import javax.jcr.query.qom.Constraint;
import javax.jcr.query.qom.DynamicOperand;
import javax.jcr.query.qom.Ordering;
import javax.jcr.query.qom.QueryObjectModel;
import javax.jcr.query.qom.QueryObjectModelConstants;
import javax.jcr.query.qom.QueryObjectModelFactory;
import javax.jcr.query.qom.Selector;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.jcr.JcrCallback;
import org.springframework.extensions.jcr.support.JcrDaoSupport;

import com.enonic.cms.core.security.user.QualifiedUsername;
import com.enonic.cms.core.security.user.UserEntity;
import com.enonic.cms.core.security.user.UserKey;
import com.enonic.cms.core.security.userstore.UserStoreEntity;
import com.enonic.cms.core.security.userstore.UserStoreKey;

import com.enonic.cms.domain.EntityPageList;
import com.enonic.cms.domain.user.Gender;
import com.enonic.cms.domain.user.UserInfo;

import static com.enonic.cms.core.jcr.JcrCmsConstants.USERSTORES_ABSOLUTE_PATH;
import static com.enonic.cms.core.jcr.JcrCmsConstants.USERSTORES_PATH;
import static com.enonic.cms.core.jcr.JcrCmsConstants.USERS_NODE;
import static com.enonic.cms.core.jcr.JcrCmsConstants.USER_NODE_TYPE;
import static javax.jcr.query.qom.QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO;

public class AccountJcrDao
    extends JcrDaoSupport
{
    private static final Logger LOG = LoggerFactory.getLogger( AccountJcrDao.class );

    public UserEntity findByKey( String key )
    {
        return findByKey( new UserKey( key ) );
    }

    public UserEntity findByKey( final UserKey key )
    {
        UserEntity user = (UserEntity) getTemplate().execute( new JcrCallback()
        {
            public Object doInJcr( Session session )
                throws IOException, RepositoryException
            {
                return queryUserByKey( session, key );
            }
        } );
        return user;
    }

    public UserEntity findByQualifiedUsername( final QualifiedUsername qualifiedUsername )
    {
        return null;
    }

    public EntityPageList<UserEntity> findAll( final int index, final int count, final String query, final String order )
    {
        @SuppressWarnings("unchecked") EntityPageList<UserEntity> users =
            (EntityPageList<UserEntity>) getTemplate().execute( new JcrCallback()
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    return queryAllUsers( session, index, count, query, order );
                }
            } );
        return users;
    }

    private EntityPageList<UserEntity> queryAllUsers( Session session, int index, int count, String query, String order )
        throws RepositoryException
    {
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        QueryObjectModelFactory factory = queryManager.getQOMFactory();

        Selector source = factory.selector( USER_NODE_TYPE, "userNodes" );
        Column[] columns = null;
        Constraint constraint = factory.descendantNode( "userNodes", USERSTORES_ABSOLUTE_PATH );
        Ordering[] orderings = null;

        QueryObjectModel queryObj = factory.createQuery( source, constraint, orderings, columns );
        QueryResult result = queryObj.execute();

        NodeIterator nodeIterator = result.getNodes();

        LOG.info( nodeIterator.getSize() + " users found" );

        List<UserEntity> userList = new ArrayList<UserEntity>();
        while ( nodeIterator.hasNext() )
        {
            UserEntity user = new UserEntity();
            Node userNode = nodeIterator.nextNode();
            nodePropertiesToUserFields( userNode, user );

            userList.add( user );

            LOG.info( user.toString() );
        }

        return new EntityPageList<UserEntity>( index, (int) nodeIterator.getSize(), userList );
    }

    private UserEntity queryUserByKey( Session session, UserKey key )
        throws RepositoryException
    {
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        QueryObjectModelFactory factory = queryManager.getQOMFactory();
        ValueFactory vf = session.getValueFactory();

        Selector source = factory.selector( USER_NODE_TYPE, "userNodes" );
        Column[] columns = null;
        Constraint constrUserstoresDescendant = factory.descendantNode( "userNodes", USERSTORES_ABSOLUTE_PATH );

        Constraint constrUserKey = factory.comparison( factory.propertyValue( "userNodes", "key" ), JCR_OPERATOR_EQUAL_TO,
                                                       factory.literal( vf.createValue( key.toString() ) ) );

        Constraint constraint = factory.and( constrUserstoresDescendant, constrUserKey );

        Ordering[] orderings = null;
        QueryObjectModel queryObj = factory.createQuery( source, constraint, orderings, columns );
        QueryResult result = queryObj.execute();

        NodeIterator nodeIterator = result.getNodes();

        LOG.info( nodeIterator.getSize() + " users found" );

        UserEntity user = null;
        if ( nodeIterator.hasNext() )
        {
            user = new UserEntity();
            Node userNode = nodeIterator.nextNode();
            nodePropertiesToUserFields( userNode, user );
        }

        return user;
    }

    public void store( final UserEntity user )
    {
        getTemplate().execute( new JcrCallback()
        {
            public Object doInJcr( Session session )
                throws IOException, RepositoryException
            {
                createNewUser( session, user );

                session.save();

                return null;
            }
        } );
    }

    private void createNewUser( Session session, final UserEntity user )
        throws RepositoryException
    {
        String userstoreNodeName = user.getUserStore().getName();
        String userParentNodePath = USERSTORES_PATH + userstoreNodeName + "/" + USERS_NODE;

        Node userNode = session.getRootNode().getNode( userParentNodePath ).addNode( user.getName(), USER_NODE_TYPE );
        userFieldsToNode( user, userNode );
    }

    private void userFieldsToNode( UserEntity user, Node userNode )
        throws RepositoryException
    {
        userNode.setProperty( "qualifiedName", user.getQualifiedName().toString() );
        userNode.setProperty( "displayname", user.getDisplayName() );
        userNode.setProperty( "email", user.getEmail() );
        userNode.setProperty( "key", user.getKey().toString() );
        userNode.setProperty( "lastModified", toCalendar( user.getLastModified() ) );

        userInfoFieldsToNode( user.getUserInfo(), userNode );
    }

    private void userInfoFieldsToNode( UserInfo userInfo, Node userNode )
        throws RepositoryException
    {
        userNode.setProperty( "birthday", toCalendar( userInfo.getBirthday() ) );
        userNode.setProperty( "country", userInfo.getCountry() );
        userNode.setProperty( "description", userInfo.getDescription() );
        userNode.setProperty( "fax", userInfo.getFax() );
        userNode.setProperty( "firstname", userInfo.getFirstName() );
        userNode.setProperty( "globalposition", userInfo.getGlobalPosition() );
        userNode.setProperty( "homepage", userInfo.getHomePage() );
        Boolean htmlEmail = userInfo.getHtmlEmail();
        if ( htmlEmail != null )
        {
            userNode.setProperty( "htmlemail", htmlEmail );
        }
        userNode.setProperty( "initials", userInfo.getInitials() );
        userNode.setProperty( "lastname", userInfo.getLastName() );
        Locale locale = userInfo.getLocale();
        if ( locale != null )
        {
            userNode.setProperty( "locale", locale.getISO3Language() );
        }
        userNode.setProperty( "memberid", userInfo.getMemberId() );
        userNode.setProperty( "middlename", userInfo.getMiddleName() );
        userNode.setProperty( "mobile", userInfo.getMobile() );
        userNode.setProperty( "organization", userInfo.getOrganization() );
        userNode.setProperty( "personalid", userInfo.getPersonalId() );
        userNode.setProperty( "phone", userInfo.getPhone() );
        userNode.setProperty( "prefix", userInfo.getPrefix() );
        userNode.setProperty( "suffix", userInfo.getSuffix() );
        TimeZone timezone = userInfo.getTimeZone();
        if ( timezone != null )
        {
            userNode.setProperty( "timezone", timezone.getID() );
        }
        userNode.setProperty( "title", userInfo.getTitle() );
        Gender gender = userInfo.getGender();
        if ( gender != null )
        {
            userNode.setProperty( "gender", gender.toString() );
        }
        userNode.setProperty( "organization", userInfo.getOrganization() );
    }

    private void nodePropertiesToUserFields( Node userNode, UserEntity user )
        throws RepositoryException
    {
        user.setName( userNode.getName() );
        user.setDisplayName( userNode.getProperty( "displayname" ).getString() );
        if ( userNode.hasProperty( "email" ) )
        {
            user.setEmail( userNode.getProperty( "email" ).getString() );
        }
        user.setKey( new UserKey( userNode.getProperty( "key" ).getString() ) );

        Calendar lastmodified = userNode.getProperty( "lastModified" ).getDate();
        if ( lastmodified != null )
        {
            DateTime timestamp = new DateTime( lastmodified );
            user.setTimestamp( timestamp );
        }

        UserStoreEntity userstore = new UserStoreEntity();
        nodeToUserstore( userNode.getParent().getParent(), userstore );
        user.setUserStore( userstore );
    }

    private void nodeToUserstore( Node userStoreNode, UserStoreEntity userStoreEntity )
        throws RepositoryException
    {
        userStoreEntity.setName( userStoreNode.getName() );
        userStoreEntity.setKey( new UserStoreKey( userStoreNode.getProperty( "cms:key" ).getString() ) );
    }

    private Calendar toCalendar( Date date )
    {
        if ( date == null )
        {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        return cal;
    }

}
