package com.enonic.cms.core.jcr;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

public class JcrHelper
{
    public static void printNode( Node node )
        throws RepositoryException
    {
        String nodeStr = node.getPath() + " [" + node.getPrimaryNodeType().getName() + "]";
        System.out.println( nodeStr );
        NodeIterator nodeIterator = node.getNodes();
        while ( nodeIterator.hasNext() )
        {
            Node childNode = nodeIterator.nextNode();
            printNode( childNode );
        }
    }
}
