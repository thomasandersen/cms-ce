Ext.define( 'CMS.view.user.EditUserPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.editUserPanel',

    requires: [
        'CMS.view.user.EditUserPropertiesPanel',
        'CMS.view.user.EditUserPreferencesPanel',
        'CMS.view.user.EditUserMembershipPanel'
    ],

    autoScroll: true,

    defaults: {
        bodyPadding: 10
    },



    modal: true,

    layout: {
        type: 'border'
    },

    initComponent: function()
    {

        this.items = [
        {
            xtype: 'tabpanel',
            region: 'center',
            items: [
                {
                    xtype: 'editUserFormPanel',
                    autoScroll: true,
                    currentUser: this.currentUser
                },
                {
                    xtype: 'editUserPropertiesPanel'
                },
                {
                    xtype: 'editUserMembershipPanel'
                },
                {
                    xtype: 'editUserPreferencesPanel'
                }
            ]
        }

    ],

        this.callParent( arguments );
    }

} );

