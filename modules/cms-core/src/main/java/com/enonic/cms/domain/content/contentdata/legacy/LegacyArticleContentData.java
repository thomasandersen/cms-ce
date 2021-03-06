/*
 * Copyright 2000-2011 Enonic AS
 * http://www.enonic.com/license
 */
package com.enonic.cms.domain.content.contentdata.legacy;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;

import com.enonic.cms.domain.content.ContentKey;
import com.enonic.cms.domain.content.binary.BinaryDataAndBinary;
import com.enonic.cms.domain.content.binary.BinaryDataKey;


public class LegacyArticleContentData
    extends AbstractBaseLegacyContentData
{
    public LegacyArticleContentData( Document contentDataXml )
    {
        super( contentDataXml );
    }

    protected String resolveTitle()
    {
        final Element nameEl = contentDataEl.getChild( "heading" );
        return nameEl.getText();
    }

    protected List<BinaryDataAndBinary> resolveBinaryDataAndBinaryList()
    {
        return null;
    }

    public void replaceBinaryKeyPlaceholders( List<BinaryDataKey> binaryDatas )
    {
        // nothing to do for this type
    }

    public void turnBinaryKeysIntoPlaceHolders( Map<BinaryDataKey, Integer> indexByBinaryDataKey )
    {
        // nothing to do for this type
    }

    @Override
    public Set<ContentKey> resolveRelatedContentKeys()
    {
        final Set<ContentKey> contentKeys = new HashSet<ContentKey>();

        contentKeys.addAll( resolveContentKeysByXPath( "/contentdata/files/file/@key" ) );
        contentKeys.addAll( resolveContentKeysByXPath( "/contentdata/body/image/@key" ) );
        contentKeys.addAll( resolveContentKeysByXPath( "/contentdata/teaser/image/@key" ) );

        return contentKeys;
    }
}
