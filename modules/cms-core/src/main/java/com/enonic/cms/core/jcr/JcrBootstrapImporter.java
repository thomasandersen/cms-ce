package com.enonic.cms.core.jcr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enonic.cms.core.security.user.UserEntity;
import com.enonic.cms.core.security.userstore.UserStoreEntity;
import com.enonic.cms.core.security.userstore.UserStoreService;
import com.enonic.cms.store.dao.UserDao;

public class JcrBootstrapImporter
{
    private static final Logger LOG = LoggerFactory.getLogger( JcrBootstrapImporter.class );

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserStoreService userstoreService;


    @Autowired
    private AccountJcrDao userJcrDao;

    @Autowired
    private UserstoreJcrDao userstoreJcrDao;


    @Autowired
    private JdbcUserImporterDao jdbcUserImporterDao;


    public JcrBootstrapImporter()
    {
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void importUserstores()
    {
        List<UserStoreEntity> userstores = userstoreService.findAll();

        int userstoresImported = 0;
        for ( UserStoreEntity userstore : userstores )
        {
            LOG.info( "Creating userstore: " + userstore.getName() );
            try
            {
                userstoreJcrDao.store( userstore );
                userstoresImported++;
            }
            catch ( RepositoryRuntimeException e )
            {
                LOG.warn( "Exception while importing user " + userstore.getName(), e );
            }
        }

        LOG.info( userstoresImported + " userstores have been created" );
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void importUsers()
    {
        List<UserEntity> users = jdbcUserImporterDao.getUsers();
//        List<UserEntity> users = this.userDao.findAll( false );

        int usersImported = 0;
        for ( UserEntity user : users )
        {
            if ( user.getUserStore() == null )
            {
                continue;
            }

            LOG.info( "Importing user: " + user.getDisplayName() );
            try
            {
                userJcrDao.store( user );
                usersImported++;
            }
            catch ( RepositoryRuntimeException e )
            {
                LOG.warn( "Exception while importing user " + user.getQualifiedName(), e );
            }
        }

        LOG.info( usersImported + " users have been imported" );
    }
}
