Ext.define( 'LPT.view.requests.PortalRequestTraceHistoryDetailsPanel', {
    extend: 'Ext.tree.Panel',
    alias : 'widget.portalRequestTraceHistoryDetailsPanel',

    layout: 'fit',

    title: 'Details',

    collapsible: true,
    useArrows: true,
    rootVisible: false,
    store: 'PortalRequestTraceHistoryDetailsStore',
    multiSelect: false,
    singleExpand: false,

    // The 'columns' property is now 'headers'
    columns: [{
        xtype: 'treecolumn', //this is so we know which column will show the tree
        text: 'Trace',
        flex: 2,
        sortable: true,
        dataIndex: 'text'
    },
    {
        text: 'Duration',
        dataIndex: 'duration.humanReadable',
        sortable: false,
        align: 'right'
    }
    ],

    initComponent: function() {

        this.callParent(arguments);
    }

});
