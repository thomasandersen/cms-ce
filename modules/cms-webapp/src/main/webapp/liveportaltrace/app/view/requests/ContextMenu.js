Ext.define('LPT.view.requests.ContextMenu', {
    extend: 'Ext.menu.Menu',
    alias: 'widget.requestContextMenu',

    items: [
        {
            text: 'View request details',
            iconCls: 'request-details',
            action: 'details'
        },
        '-',
        {
            text: 'Auto-refresh on',
            xtype: 'menucheckitem',
            action: 'toggle-autorefresh'
        }
    ]
});

