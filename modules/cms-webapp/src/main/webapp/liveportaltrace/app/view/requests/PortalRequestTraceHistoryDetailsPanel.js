Ext.define( 'LPT.view.requests.PortalRequestTraceHistoryDetailsPanel', {
    extend: 'Ext.panel.Panel',
    alias : 'widget.portalRequestTraceHistoryDetailsPanel',


    layout: 'fit',
    loadMask: true,
    columnLines: true,
    frame: false,
    collapsible: true,
    store: 'PortalRequestTraceHistoryListStore',

    initComponent: function() {

        this.tpl = new Ext.XTemplate(
                '<div class="request-info">',
                '<h3>{url}</h3>',
                '<dl>',
                '<dt>Type</dt><dd>{requestType}</dd>',
                '<dt>User</dt><dd>{username}</dd>',
                '<dt>URL</dt><dd>{url}</dd>',
                '<dt>Time</dt><dd>{timestamp:this.formatDate}</dd>',
                '<dt>Remote Address</dt><dd>{remoteAddress}</dd>',
                '</dl>',
                '</div>', {

            formatDate: function(value) {
                if (!value) {
                    return '';
                }
                return Ext.Date.format(value, 'j M Y, H:i:s');
            }

        });

        this.callParent(arguments);
    }

});
