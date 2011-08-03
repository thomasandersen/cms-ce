package com.enonic.cms.core.jcr;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NodeType;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.commons.cnd.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.extensions.jcr.JcrSessionFactory;

import com.google.common.io.Files;

import com.enonic.cms.core.security.userstore.UserStoreService;
import com.enonic.cms.store.dao.UserDao;

public class JcrBootstrap
{
    private static final Logger LOG = LoggerFactory.getLogger( JcrBootstrap.class );

    private JcrSessionFactory sessionFactory;

    private Resource compactNodeDefinitionFile;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserStoreService userstoreService;

    @Autowired
    private AccountJcrDao userJcrDao;

    @Autowired
    private JcrBootstrapImporter importer;

    public JcrBootstrap()
    {
    }

    private File homeDir;

    @PostConstruct
    public void afterPropertiesSet()
        throws Exception
    {
        initialize();
    }

    private void print( Session jcrSession )
        throws RepositoryException
    {
        Node root = jcrSession.getRootNode();
        JcrHelper.printNode( root );
    }

    public void initialize()
    {
        LOG.info( "Initializing JCR repository..." );

        resetLocalRepository();
        Session jcrSession = null;
        try
        {
            jcrSession = sessionFactory.getSession();

            registerNamespaces( jcrSession );
            registerCustomNodeTypes( jcrSession );

            createTreeStructure( jcrSession );

            jcrSession.save();

            importer.importUserstores();

            importer.importUsers();

            LOG.info( JcrHelper.sessionViewToXml( jcrSession, "/enonic" ) );

            jcrSession.save();

            print( jcrSession );
        }
        catch ( Exception e )
        {
            throw new RepositoryRuntimeException( "Error while initializing JCR repository", e );
        }
        finally
        {
            if ( jcrSession != null )
            {
                jcrSession.logout();
            }
        }
        LOG.info( "JCR repository initialized" );
    }

    private void createTreeStructure( Session jcrSession )
        throws RepositoryException
    {
        Node root = jcrSession.getRootNode();

        if ( root.hasNode( JcrCmsConstants.ROOT_NODE ) )
        {
            root.getNode( JcrCmsConstants.ROOT_NODE ).remove();
            jcrSession.save();
        }
        Node enonic = root.addNode( JcrCmsConstants.ROOT_NODE, JcrConstants.NT_UNSTRUCTURED );
        Node userstores = enonic.addNode( JcrCmsConstants.USERSTORES_NODE, JcrCmsConstants.USERSTORES_NODE_TYPE );

        Node systemUserstore = userstores.addNode( JcrCmsConstants.SYSTEM_USERSTORE_NODE, JcrCmsConstants.USERSTORE_NODE_TYPE );
        Node groupsRoles = systemUserstore.addNode( JcrCmsConstants.GROUPS_NODE, JcrCmsConstants.GROUPS_NODE_TYPE );
        Node usersRoles = systemUserstore.addNode( JcrCmsConstants.USERS_NODE, JcrCmsConstants.USERS_NODE_TYPE );
        Node systemRoles = systemUserstore.addNode( JcrCmsConstants.ROLES_NODE, JcrCmsConstants.ROLES_NODE_TYPE );

        systemRoles.addNode( "ea", "cms:role" );
        systemRoles.addNode( "developer", "cms:role" );
        systemRoles.addNode( "administrator", "cms:role" );
        systemRoles.addNode( "contributor", "cms:role" );
        systemRoles.addNode( "expert", "cms:role" );
        systemRoles.addNode( "everyone", "cms:role" );
        systemRoles.addNode( "authenticated", "cms:role" );
    }

    private void registerCustomNodeTypes( Session jcrSession )
        throws RepositoryException, IOException, ParseException
    {
        Reader fileReader = new InputStreamReader( compactNodeDefinitionFile.getInputStream() );
        try
        {
            NodeType[] nodeTypes = CndImporter.registerNodeTypes( fileReader, jcrSession );
            for ( NodeType nt : nodeTypes )
            {
                LOG.info( "Registered node type: " + nt.getName() );
            }

        }
        catch ( Exception e )
        {
            LOG.error( e.getMessage(), e );
        }
    }

    private void registerNamespaces( Session jcrSession )
        throws RepositoryException
    {
        Workspace workspace = jcrSession.getWorkspace();
        NamespaceRegistry reg = workspace.getNamespaceRegistry();

        String[] prefixes = reg.getPrefixes();
        Set<String> registeredPrefixes = new HashSet( Arrays.<String>asList( prefixes ) );

        registerNamespace( reg, registeredPrefixes, JcrCmsConstants.ENONIC_CMS_NAMESPACE_PREFIX, JcrCmsConstants.ENONIC_CMS_NAMESPACE );
    }

    private void registerNamespace( NamespaceRegistry reg, Set<String> registeredPrefixes, String prefix, String uri )
        throws RepositoryException
    {
        if ( !registeredPrefixes.contains( prefix ) )
        {
            reg.registerNamespace( prefix, uri );
            LOG.info( "JCR namespace registered " + prefix + ":" + uri );
        }
        else
        {
            String registeredUri = reg.getURI( prefix );
            if ( !uri.equals( registeredUri ) )
            {
                throw new RepositoryRuntimeException(
                    "Namespace prefix is already registered with a different URI: " + prefix + ":" + registeredUri );
            }
        }
    }

    public void setSessionFactory( org.springframework.extensions.jcr.JcrSessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    public void setCompactNodeDefinitionFile( Resource compactNodeDefinitionFile )
    {
        this.compactNodeDefinitionFile = compactNodeDefinitionFile;
    }

    private void resetLocalRepository()
    {
        if ( this.homeDir.exists() )
        {
            try
            {
                Files.deleteRecursively( this.homeDir );
            }
            catch ( IOException e )
            {
                // DO NOTHING
            }
        }
    }

    @Value("${cms.home}/jackrabbit")
    public void setHomeDir( final File homeDir )
    {
        this.homeDir = homeDir;
    }

}
