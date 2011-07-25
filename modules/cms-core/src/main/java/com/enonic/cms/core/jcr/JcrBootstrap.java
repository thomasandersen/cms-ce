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

import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.commons.cnd.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import com.google.common.io.Files;

public final class JcrBootstrap
{
    private static final Logger LOG = LoggerFactory.getLogger( JcrBootstrap.class );

    private static final String ENONIC_CMS_NAMESPACE = "http://www.enonic.com/cms";

    private static final String ENONIC_CMS_NAMESPACE_PREFIX = "cms";

    private org.springframework.extensions.jcr.JcrSessionFactory sessionFactory;

    private Resource compactNodeDefinitionFile;

    public JcrBootstrap()
    {
    }

    private File homeDir;

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

        Node enonic = root.addNode( "enonic", "nt:unstructured" );
        Node userstores = enonic.addNode( "userstores", "cms:userstores" );

        Node enonicUserstore = userstores.addNode( "enonic", "cms:userstore" );
        Node enonicGroupsRoles = enonicUserstore.addNode( "groups", "cms:groups" );
        Node enonicUsersRoles = enonicUserstore.addNode( "users", "cms:users" );

        Node systemUserstore = userstores.addNode( "system", "cms:userstore" );
        Node groupsRoles = systemUserstore.addNode( "groups", "cms:groups" );
        Node usersRoles = systemUserstore.addNode( "users", "cms:users" );
        Node systemRoles = systemUserstore.addNode( "roles", "cms:roles" );

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

        registerNamespace( reg, registeredPrefixes, ENONIC_CMS_NAMESPACE_PREFIX, ENONIC_CMS_NAMESPACE );
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
}
