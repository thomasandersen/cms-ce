Ext.define( 'LPT.view.requests.PerformancePanel', {
    extend: 'Ext.panel.Panel',
    alias : 'widget.performancePanel',

    requires: [
        'LPT.view.requests.PerformanceGauge'
    ],

    title: 'Filter',
    split: true,
    collapsible: true,

    layout: {
        type: 'hbox',
        padding: 10
    },

    items: [{
        xtype: 'requestsGauge',
        name: 'gauge1'
    },{
        xtype: 'requestsGauge',
        name: 'gauge2'
    }],


    initComponent: function() {
        this.callParent(arguments);
    }

});
