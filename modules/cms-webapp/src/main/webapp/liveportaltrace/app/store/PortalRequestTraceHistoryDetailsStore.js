Ext.define('LPT.store.PortalRequestTraceHistoryDetailsStore', {
    extend: 'Ext.data.TreeStore',

    model: 'LPT.model.TraceTreeTableNodeModel',
    storeId: 'PortalRequestTraceHistoryDetailsStore',

    nodeParam: 'id',

    proxy: {
        type: 'ajax',
        url: '/liveportaltrace/rest/portal-request-trace-history/detail',
        reader: {
            type: 'json'
        }
    },

    folderSort: true
} );
