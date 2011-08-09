package com.enonic.cms.core.jcr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.jdom.Document;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.enonic.cms.framework.util.JDOMUtil;

import com.enonic.cms.core.security.user.UserEntity;
import com.enonic.cms.core.security.user.UserKey;
import com.enonic.cms.core.security.user.UserType;
import com.enonic.cms.core.security.userstore.UserStoreEntity;
import com.enonic.cms.core.security.userstore.UserStoreKey;
import com.enonic.cms.core.security.userstore.config.UserStoreConfig;
import com.enonic.cms.core.security.userstore.config.UserStoreConfigParser;

@Component
public class JdbcUserImporterDao
{
    private static final Logger LOG = LoggerFactory.getLogger( JdbcUserImporterDao.class );

    private JdbcTemplate jdbcTemplate;

    public List<UserEntity> getUsers()
    {
        String query = "SELECT USR_HKEY, USR_SUID, USR_SFULLNAME, USR_DTETIMESTAMP, USR_UT_LKEY, " +
            "USR_DOM_LKEY, USR_SSYNCVALUE, USR_SEMAIL, USR_SPASSWORD, USR_GRP_HKEY, USR_PHOTO, USR_BISDELETED FROM TUSER WHERE USR_BISDELETED = 0";

        List<UserEntity> users = jdbcTemplate.query( query, new RowMapper<UserEntity>()
        {
            public UserEntity mapRow( ResultSet rs, int i )
                throws SQLException
            {
                UserEntity user = new UserEntity();
                UserKey key = new UserKey( rs.getString( "USR_HKEY" ) );
                user.setKey( key );
                user.setName( rs.getString( "USR_SUID" ) );
                user.setDisplayName( rs.getString( "USR_SFULLNAME" ) );
                user.setEmail( rs.getString( "USR_SEMAIL" ) );
                user.setTimestamp( new DateTime( rs.getDate( "USR_DTETIMESTAMP" ) ) );
                user.setSyncValue( rs.getString( "USR_SSYNCVALUE" ) );
                user.setType( UserType.getByKey( rs.getInt( "USR_UT_LKEY" ) ) );
                user.setDeleted( rs.getInt( "USR_BISDELETED" ) );
                user.setPhoto( rs.getBytes( "USR_PHOTO" ) );

                UserStoreKey userStoreKey = new UserStoreKey( rs.getInt( "USR_DOM_LKEY" ) );
                UserStoreEntity userStore = getUserStore( userStoreKey );
                user.setUserStore( userStore );

                return user;
            }
        } );

        LOG.info( users.size() + " users retrieved" );
        return users;
    }

    public UserStoreEntity getUserStore( UserStoreKey userStoreKey )
    {
        String query = "SELECT DOM_LKEY, DOM_BISDELETED, DOM_SNAME, DOM_BDEFAULTSTORE, DOM_SCONFIGNAME, DOM_XMLDATA " +
            "FROM TDOMAIN WHERE DOM_BISDELETED = ?";

        List<UserStoreEntity> userStore = jdbcTemplate.query( query, new RowMapper<UserStoreEntity>()
        {
            public UserStoreEntity mapRow( ResultSet rs, int i )
                throws SQLException
            {
                UserStoreEntity userStore = new UserStoreEntity();
                UserStoreKey key = new UserStoreKey( rs.getString( "DOM_LKEY" ) );
                userStore.setKey( key );
                userStore.setDeleted( rs.getInt( "DOM_BISDELETED" ) == 1 );
                userStore.setName( rs.getString( "DOM_SNAME" ) );
                userStore.setDefaultStore( rs.getInt( "DOM_BDEFAULTSTORE" ) == 1 );
                userStore.setConnectorName( rs.getString( "DOM_SCONFIGNAME" ) );

                byte[] bytes = rs.getBytes( "DOM_XMLDATA" );
                Document xmlDoc = null;
                try
                {
                    xmlDoc = JDOMUtil.parseDocument( new String( bytes, "UTF-8" ) );
                }
                catch ( Exception e )
                {
                    throw new RuntimeException( e );
                }
                UserStoreConfig userStoreConfig = UserStoreConfigParser.parse( xmlDoc.getRootElement() );
                userStore.setConfig( userStoreConfig );

                return userStore;
            }
        }, userStoreKey.integerValue() );

        if ( userStore.isEmpty() )
        {
            return null;
        }
        else
        {
            return userStore.get( 0 );
        }
    }

    public void setDataSource( DataSource dataSource )
    {
        this.jdbcTemplate = new JdbcTemplate( dataSource );
    }

}
