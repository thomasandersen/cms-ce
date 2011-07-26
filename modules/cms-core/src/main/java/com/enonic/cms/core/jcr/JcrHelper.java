package com.enonic.cms.core.jcr;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcrHelper
{
    private static final Logger LOG = LoggerFactory.getLogger( JcrHelper.class );

    public static void printNode( Node node )
        throws RepositoryException
    {
        String nodeStr = node.getPath() + " [" + node.getPrimaryNodeType().getName() + "]";
        LOG.info( nodeStr );
        NodeIterator nodeIterator = node.getNodes();
        while ( nodeIterator.hasNext() )
        {
            Node childNode = nodeIterator.nextNode();
            printNode( childNode );
        }
    }
}
