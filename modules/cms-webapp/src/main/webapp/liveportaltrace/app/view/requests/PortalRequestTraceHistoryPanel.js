Ext.define('LPT.view.requests.PortalRequestTraceHistoryPanel', {
    extend: 'Ext.panel.Panel',
    alias : 'widget.portalRequestTraceHistoryPanel',

    requires: [
        'LPT.view.requests.PortalRequestTraceHistoryGrid'
    ],

    layout: 'border',
    border: false,

    initComponent: function() {
        this.items = [
            {
                region: 'center',
                xtype: 'portalRequestTraceHistoryGrid',
                flex: 3
            },
            {
                region: 'south',
                xtype: 'portalRequestTraceHistoryDetailsPanel',
                flex: 1,
                hidden: true
            }
        ];

        this.callParent(arguments);
    }

});
