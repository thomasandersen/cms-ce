Ext.define( 'LPT.view.requests.InfoPanel', {
    extend: 'Ext.panel.Panel',
    alias : 'widget.cmsInfoPanel',

    layout: 'fit',
    loadMask: true,
    columnLines: true,
    frame: false,
    collapsible: true,
    store: 'PortalRequestTraceHistoryListStore',
//    store: 'StatusInfo',

    initComponent: function() {
        this.tpl = new Ext.XTemplate(
            '<div class="status-info">',
            '<h3>Memory uage</h3>',
            '<dl>',
            '<dt>Heap Memory</dt><dd></dd>',
            '<dt>Maximum</dt><dd>{maxMemoryHeap}</dd>',
            '<dt>Use<  d/dt><dd>{usedMemoryHeap}</dd>',
            '<hr/>',
            '<dt>NHoena-p Memory</dt><dd></dd>',
            '<dt>Maximum</dt><dd>{maxMemoryNonHeap}</dd>',
            '<dt>Use<  d/dt><dd>{maxMemoryNonHeap}</dd>',
            '</dl>',
            '</div>');

        this.callParent(arguments);
    }

});
