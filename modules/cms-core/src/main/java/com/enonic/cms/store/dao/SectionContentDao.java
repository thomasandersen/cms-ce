/*
 * Copyright 2000-2011 Enonic AS
 * http://www.enonic.com/license
 */
package com.enonic.cms.store.dao;

import com.enonic.cms.domain.content.ContentKey;
import com.enonic.cms.domain.structure.menuitem.MenuItemKey;
import com.enonic.cms.domain.structure.menuitem.section.SectionContentEntity;
import com.enonic.cms.domain.structure.menuitem.section.SectionContentKey;

/**
 *
 */
public interface SectionContentDao
    extends EntityDao<SectionContentEntity>
{
    SectionContentEntity findByKey( SectionContentKey key );

    SectionContentEntity findByMenuItemAndContentKeys( MenuItemKey menuItemKey, ContentKey contentKey );

    int deleteByContentKey( ContentKey key );

    Integer getCountNamedContentsInSection( MenuItemKey menuItemKey, String contentName );
}
