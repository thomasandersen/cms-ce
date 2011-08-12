package com.enonic.cms.core.jcr;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.value.BinaryValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.jcr.JcrCallback;
import org.springframework.extensions.jcr.support.JcrDaoSupport;

import com.enonic.cms.domain.user.Gender;
import com.enonic.cms.domain.user.UserInfo;
import com.enonic.cms.domain.user.field.UserField;
import com.enonic.cms.domain.user.field.UserFieldHelper;
import com.enonic.cms.domain.user.field.UserFieldMap;
import com.enonic.cms.domain.user.field.UserFieldType;
import com.enonic.cms.domain.user.field.UserInfoTransformer;

import static com.enonic.cms.core.jcr.JcrCmsConstants.USERSTORES_PATH;
import static com.enonic.cms.core.jcr.JcrCmsConstants.USERS_NODE;
import static com.enonic.cms.core.jcr.JcrCmsConstants.USER_NODE_TYPE;

public class JcrAccountsImporter
    extends JcrDaoSupport
{

    @Autowired
    private JdbcAccountsRetriever jdbcAccountsRetriever;

    private Map<Integer, String> userStoreKeyName;

    public JcrAccountsImporter()
    {
        userStoreKeyName = new HashMap<Integer, String>();
    }

    public void importAccounts()
    {
        importUserStores();

        importUsers();
    }


    private void importUserStores()
    {
        jdbcAccountsRetriever.fetchUserStores( new ImportDataCallbackHandler()
        {
            public void processDataEntry( Map<String, Object> userstoreFields )
            {
                storeUserstore( userstoreFields );
            }
        } );
    }

    private void importUsers()
    {
        jdbcAccountsRetriever.fetchUsers( new ImportDataCallbackHandler()
        {
            public void processDataEntry( Map<String, Object> data )
            {
                storeUser( data );
            }
        } );
    }

    private void storeUser( final Map<String, Object> userFields )
    {
        getTemplate().execute( new JcrCallback()
        {
            public Object doInJcr( Session session )
                throws IOException, RepositoryException
            {
                addUser( session, userFields );

                session.save();

                return null;
            }
        } );
    }

    private void storeUserstore( final Map<String, Object> userstoreFields )
    {
        getTemplate().execute( new JcrCallback()
        {
            public Object doInJcr( Session session )
                throws IOException, RepositoryException
            {
                addUserstore( session, userstoreFields );

                session.save();

                return null;
            }
        } );
    }

    private void addUser( Session session, Map<String, Object> userFields )
        throws RepositoryException, UnsupportedEncodingException
    {
        String userName = (String) userFields.get( "USR_SUID" );
        Integer userStoreKey = (Integer) userFields.get( "USR_DOM_LKEY" );
        String userstoreNodeName = userStoreKeyName.get( userStoreKey );
        if ( userstoreNodeName == null )
        {
            logger.info( "Could not find userstore with key: " + userStoreKey + ". Skipping import of user " + userName );
            return;
        }

        String userParentNodePath = USERSTORES_PATH + userstoreNodeName + "/" + USERS_NODE;
        Node userNode = session.getRootNode().getNode( userParentNodePath ).addNode( userName, USER_NODE_TYPE );

        // common user properties
        String qualifiedName = (String) userFields.get( "USR_SUID" );
        String displayName = (String) userFields.get( "USR_SFULLNAME" );
        String email = (String) userFields.get( "USR_SEMAIL" );
        String key = (String) userFields.get( "USR_HKEY" );
        Date lastModified = (Date) userFields.get( "USR_DTETIMESTAMP" );
        String syncValue = (String) userFields.get( "USR_SSYNCVALUE" );
        byte[] photo = (byte[]) userFields.get( "USR_PHOTO" );

        userNode.setProperty( "qualifiedName", qualifiedName );
        userNode.setProperty( "displayname", displayName );
        userNode.setProperty( "email", email );
        userNode.setProperty( "key", key );
        userNode.setProperty( "lastModified", toCalendar( lastModified ) );
        userNode.setProperty( "syncValue", syncValue );
        userNode.setProperty( "photo", new BinaryValue( photo ) );

        // user info fields
        Map<String, Object> userInfoFields = (Map<String, Object>) userFields.get( JdbcAccountsRetriever.USER_INFO_FIELDS_MAP );
        addUserInfoFields( userNode, userInfoFields );
    }

    private void addUserInfoFields( Node userNode, Map<String, Object> userInfoFields )
        throws RepositoryException
    {
        UserFieldHelper userFieldHelper = new UserFieldHelper();

        UserFieldMap fieldMap = new UserFieldMap( true );
        for ( String userFieldName : userInfoFields.keySet() )
        {
            UserFieldType type = UserFieldType.fromName( userFieldName );
            if ( type != null )
            {
                Object value = userFieldHelper.fromString( type, userInfoFields.get( userFieldName ).toString() );
                UserField field = new UserField( type, value );
                fieldMap.add( field );
            }
        }
        UserInfoTransformer transformer = new UserInfoTransformer();
        UserInfo userInfo = transformer.toUserInfo( fieldMap );

        userInfoFieldsToNode( userInfo, userNode );

    }

    private void addUserstore( Session session, Map<String, Object> userstoreFields )
        throws RepositoryException, UnsupportedEncodingException
    {
        String userstoreName = (String) userstoreFields.get( "DOM_SNAME" );

        Node userstoresNode = session.getRootNode().getNode( JcrCmsConstants.USERSTORES_PATH );
        if ( ( userstoreName == null ) || userstoresNode.hasNode( userstoreName ) )
        {
            logger.info( "Skipping creation of existing user store: " + userstoreName );
            return;
        }

        Integer key = (Integer) userstoreFields.get( "DOM_LKEY" );
        boolean defaultUserstore = ( (Integer) userstoreFields.get( "DOM_BDEFAULTSTORE" ) == 1 );
        String connectorName = (String) userstoreFields.get( "DOM_SCONFIGNAME" );
        byte[] xmlBytes = (byte[]) userstoreFields.get( "DOM_XMLDATA" );
        String userStoreXmlConfig = new String( xmlBytes, "UTF-8" );

        Node userstoreNode = userstoresNode.addNode( userstoreName, JcrCmsConstants.USERSTORE_NODE_TYPE );
        userstoreNode.setProperty( "key", key );
        userstoreNode.setProperty( "default", defaultUserstore );
        userstoreNode.setProperty( "connector", connectorName );
        userstoreNode.setProperty( "xmlconfig", userStoreXmlConfig );

        userstoreNode.addNode( JcrCmsConstants.GROUPS_NODE, JcrCmsConstants.GROUPS_NODE_TYPE );
        userstoreNode.addNode( JcrCmsConstants.USERS_NODE, JcrCmsConstants.USERS_NODE_TYPE );

        userStoreKeyName.put( key, userstoreName );
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
