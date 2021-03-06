/*
 * Copyright 2000-2011 Enonic AS
 * http://www.enonic.com/license
 */
package com.enonic.cms.domain.content.index;

import java.util.List;
import java.util.Map;

import com.enonic.cms.domain.content.ContentVersionEntity;
import com.enonic.cms.domain.content.ContentVersionKey;

/**
 * This interface defines the content entity fetcher.
 */
public interface ContentVersionEntityFetcher
{

    Map<ContentVersionKey, ContentVersionEntity> fetch( List<ContentVersionKey> keys );

}
