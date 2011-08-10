Ext.define('LPT.store.GeolocationStore', {
    extend: 'Ext.data.Store',

    model: 'LPT.model.GeolocationModel',
    storeId: 'GeolocationStore',

    pageSize: 100,
    autoLoad: false,

    proxy: {
        type: 'ajax',
        url: '/liveportaltrace/rest/locations',
        reader: Ext.create('Ext.data.reader.Json', {
            type: 'json',
            root: 'locationList',
            totalProperty : 'total'
        })
    }

});
