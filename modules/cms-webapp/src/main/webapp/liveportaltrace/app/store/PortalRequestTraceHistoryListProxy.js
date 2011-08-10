Ext.define('LPT.store.PortalRequestTraceHistoryListProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'widget.cachingproxy',

    read: function(operation, callback, scope) {
        // append loaded items, instead of replacing current contents
        if (operation && (operation.action === 'read')) {
            operation.addRecords = true;
        }
        LPT.store.PortalRequestTraceHistoryListProxy.superclass.read.apply(this, [operation, callback, scope]);
    }

});