Ext.define('Pactera.widget.upload.Multiupload', {
    extend: 'Ext.flash.Component',
    requires: [
        'Pactera.widget.upload.UploadManager'
    ],
    alias: 'widget.uploader',
    width: 101,
    height: 22,
    wmode: 'transparent',
    url: 'app1/widget/upload/Upload.swf',
    statics: {
        instanceId: 0
    },
    constructor: function (config) {
        config = config || {};
        config.instanceId = Ext.String.format('upload-{0}', ++Pactera.widget.upload.Multiupload.instanceId);
        config.flashVars = config.flashVars || {};
        config.flashVars = Ext.apply({
            instanceId: config.instanceId,
            buttonImagePath: 'app1/widget/upload/button.png',
            buttonImageHoverPath: 'app1/widget/upload/button_hover.png',
            fileFilters: 'Images (*.jpg)|*.jpg',
            uploadUrl: '/upload/url',
            maxFileSize: 0,
            maxQueueLength: 0,
            maxQueueSize: 0,
            callback: 'Pactera.widget.upload.UploadManager.uploadCallback'
        }, config.uploadConfig);

        this.addEvents(
            'fileadded',
            'uploadstart',
            'uploadprogress',
            'uploadcomplete',
            'uploaddatacomplete',
            'queuecomplete',
            'queuedatacomplete',
            'uploaderror'
        );

        this.callParent([config]);
    },
    initComponent: function () {
    	Pactera.widget.upload.UploadManager.register(this);
        this.callParent(arguments);
    },
    onDestroy: function () {
    	Pactera.widget.upload.UploadManager.unregister(this);
        this.callParent(arguments);
    }
});
