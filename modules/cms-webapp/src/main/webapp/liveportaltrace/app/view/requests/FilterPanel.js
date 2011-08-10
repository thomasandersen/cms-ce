Ext.define( 'LPT.view.requests.FilterPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.requestsFilterPanel',

    title: 'Filter',
    split: true,
    collapsible: true,

    layout: {
        type: 'vbox',
        padding: 10,
        align: 'stretch'
    },
    border: true,
    bodyPadding: 10,

    defaults: {
        margins: '0 0 0 0'
    },

    items: [
        {
            xtype: 'fieldcontainer',
            layout: 'hbox',

            items: [
                {
                    xtype: 'textfield',
                    name: 'filter',
                    flex: 1
                },
                {
                    xtype: 'button',
                    icon: 'resources/images/find.png',
                    action: 'search',
                    margins: '0 0 0 5'
                }
            ]
        },
        {
            id: 'pageFilterButton',
            xtype: 'checkbox',
            checked: true,
            boxLabel: 'Page requests',
            listeners: {
                change: function()
                {
                    return this.ownerCt.applyFilters( this );
                }
            }
        },
        {
            id: 'windowFilterButton',
            xtype: 'checkbox',
            checked: true,
            boxLabel: 'Window requests',
            listeners: {
                change: function()
                {
                    return this.ownerCt.applyFilters( this );
                }
            }

        },
        {
            id: 'imageFilterButton',
            xtype: 'checkbox',
            checked: true,
            boxLabel: 'Image requests',
            listeners: {
                change: function()
                {
                    return this.ownerCt.applyFilters( this );
                }
            }
        },
        {
            id: 'attachmentFilterButton',
            xtype: 'checkbox',
            checked: true,
            boxLabel: 'Attachment requests',
            listeners: {
                change: function()
                {
                    return this.ownerCt.applyFilters( this );
                }
            }
        }
    ],

    initComponent: function()
    {
        this.callParent( arguments );
    },

    applyFilters: function( button )
    {
        var store = Ext.data.StoreManager.lookup( 'PortalRequestTraceHistoryListStore' );

        var pageFilterButton = this.getComponent( 'pageFilterButton' );
        var windowFilterButton = this.getComponent( 'windowFilterButton' );
        var attachmentFilterButton = this.getComponent( 'attachmentFilterButton' );
        var imageFilterButton = this.getComponent( 'imageFilterButton' );

        var requestTypeFilter = new Ext.util.Filter( {
                                                         filterFn: function( item )
                                                         {
                                                             var dontAccept = false;
                                                             if ( !pageFilterButton.getValue() )
                                                             {
                                                                 dontAccept = item.data.requestType === 'Page';
                                                             }
                                                             if ( dontAccept )
                                                             {
                                                                 return false;
                                                             }

                                                             if ( !windowFilterButton.getValue() )
                                                             {
                                                                 dontAccept = item.data.requestType === 'Window';
                                                             }
                                                             if ( dontAccept )
                                                             {
                                                                 return false;
                                                             }

                                                             if ( !attachmentFilterButton.getValue() )
                                                             {
                                                                 dontAccept = item.data.requestType === 'Attachment';
                                                             }
                                                             if ( dontAccept )
                                                             {
                                                                 return false;
                                                             }

                                                             if ( !imageFilterButton.getValue() )
                                                             {
                                                                 dontAccept = item.data.requestType === 'Image';
                                                             }
                                                             if ( dontAccept )
                                                             {
                                                                 return false;
                                                             }

                                                             return true;
                                                         }
                                                     } );

        store.clearFilter();
        store.filter( requestTypeFilter );
    }

} );
