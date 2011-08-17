Ext.define( 'LPT.model.TraceTreeTableNodeModel', {
    extend: 'Ext.data.Model',

    fields: [
     //   'id',
        'text',
        'duration.startTime',
        'duration.milliseconds',
        'duration.humanReadable'
    ]
} );
