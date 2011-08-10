Ext.define( 'LPT.view.requests.PerformanceGauge', {
    extend: 'Ext.panel.Panel',
    alias : 'widget.requestsGauge',

    requires: [
        'Ext.chart.*', 'Ext.chart.axis.Gauge', 'Ext.chart.series.*'
    ],

    layout: {
        type: 'hbox',
        padding: 2
    },
    width: 320,
    height: 150,
    minWidth: 320,
    minHeight: 150,

    items: [{
        xtype: 'chart',
        store: Ext.create('Ext.data.JsonStore', {
            fields: ['name', 'val'],
            data: [{name:'%', 'val':55}]
        }),
        style: 'background:#fff',
        animate: {
            easing: 'bounceOut',
            duration: 500
        },
        insetPadding: 25,
        flex: 1,
        axes: [{
            type: 'gauge',
            position: 'gauge',
            minimum: 0,
            maximum: 100,
            steps: 10,
            margin: 7
        }],
        series: [{
            type: 'gauge',
            field: 'val',
            donut: 30,
            colorSet: ['#82B525', '#ddd']
        }]
    }],

    initComponent: function() {
        this.callParent(arguments);
    }

});
