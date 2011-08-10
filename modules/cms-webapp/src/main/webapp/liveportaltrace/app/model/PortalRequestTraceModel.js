Ext.define( 'LPT.model.PortalRequestTraceModel', {
    extend: 'Ext.data.Model',

    fields: [
        'id',
        'url.originalURL',
        'url.siteLocalUrl',
        'url.internalURL',
        'requestType',
        'site.key',
        'site.name',
        'username',
        'duration.startTime',
        'duration.milliseconds',
        'duration.humanReadable',
        'remoteAddress',
        {name: 'timestamp', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ],

    idProperty: 'id'
} );
