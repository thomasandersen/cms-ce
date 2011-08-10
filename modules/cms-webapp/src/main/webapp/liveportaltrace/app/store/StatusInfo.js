Ext.define('LPT.store.StatusInfo', {
    extend: 'Ext.data.Store',

    model: 'LPT.model.StatusInfo',

    pageSize: 100,
    autoLoad: true,

    proxy: {
        type: 'ajax',
        url: '/liveportaltrace/rest/statusInfo',
        reader: {
            type: 'json'
        }
    }
});
