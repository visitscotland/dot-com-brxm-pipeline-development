/*
 * Copyright 2011-2019 Hippo B.V. (http://www.onehippo.com)
 */
Ext.ns('Hippo.Reports');


Hippo.Reports.TranslationListPanel = Ext.extend(Hippo.Reports.Portlet, {

    constructor: function(config) {

        let ajaxStore = new Ext.data.JsonStore({
            storeId: "myStore",
            autoLoad: true,
            // NOT sorted on server. This just delegates sorting to Hippo.Reports.PageableHttpProxy
            remoteSort: true,
            // Default sort from high to low priority
            sortInfo: {field: "translationPriority", direction: "DESC"},
            idProperty: "handleId",
            id: "myStore",
            root: "data",
            totalProperty: "total",
            method: "GET",
            proxy: new Hippo.Reports.PageableHttpProxy({url: GET_UNTRANSLATED_FILES_ENDPOINT, api: {}}, {locale: INITIAL_LOCALE}),
            fields: ["displayName", "translatedLocales", "sentForTranslationLocales", "path", "translationStatus",
                "translationPriority", "handleId", "lastModified", "lastModifiedBy", "publishStatus", "type"]
        })

        var self = this;

        this.store = ajaxStore;
        this.pageSize = PAGE_SIZE;
        this.paging = true;
        this.pageTypes =[]
        this.moduleTypes = []



        // Combo box used to edit each record's priority
        // Configured in the editor grid as a column editor component
        // Requests for editing made in the editor grid edit event callback
        const priorityComboConfig = {
            xtype: "combo",
            autoSelect: true,
            editable: false,
            triggerAction: 'all',
            lazyRender:true,
            mode: 'remote',
            store: new Ext.data.ArrayStore({
                id: 0,
                fields: [
                    'value',
                    'name',
                    'sortOrder'
                ],
                proxy: new Ext.data.HttpProxy({
                    // Get possible priority values from the server. Configured in com.visitscotland.brxm.translation.report.TranslationPriority
                    url: "/cms/translation/priority",
                    method: "GET"
                })
            }),
            valueField: 'value',
            displayField: 'name'
        }


        const editorGridPanelConfig = {
            xtype: "editorgrid",
            id: GRID_ID,
            store: ajaxStore,
            stateful: false,
            colModel: new Ext.grid.ColumnModel({
                defaults: {
                    menuDisabled: true,
                    sortable: false,
                    renderer: Ext.util.Format.htmlEncode
                },
                columns: [
                    {name: "publishStatus", dataIndex: "publishStatus", header: "", sortable: true, renderer: this.renderPublishStatus, width: 5},
                    {name: "name", dataIndex: "displayName", header: "Document", sortable: true, width: 25},
                    {name: "type", dataIndex: "type", header: "Type", sortable: true, width: 10},
                    {name: "lastModified", dataIndex: "lastModified", header: "Last modified", sortable: true, renderer: {fn: this.renderDateTime, scope: self}, width: 5},
                    {name: "lastModifiedBy", dataIndex: "lastModifiedBy", header: "Modified by", sortable: true, width: 5},
                    {name: "translatedLocales", dataIndex: "translatedLocales", header: "Translated", renderer: this.renderFlags, width: 10},
                    {name: "sentForTranslationLocales", dataIndex: "sentForTranslationLocales", header: "Sent for translation", renderer: this.renderFlags, width: 10},
                    {name: "translationStatus", dataIndex: "translationStatus", header: "Translation status", sortable: true, width: 15},
                    {name: "translationPriority", dataIndex: "translationPriority", header: "Priority", sortable: true, editor: priorityComboConfig, renderer: {fn: this.renderTranslationPriority, scope: self}, width: 15},
                ]
            }),
            loadMask: false,
            border: false,
            disableSelection: true,
            enableColumnMove: false,
            viewConfig: {
                forceFit: true,
                scrollOffset: 0,
                markDirty: false
            },
            bbar: self.paging ? new Ext.PagingToolbar({
                pageSize: self.pageSize,
                store: self.store,
                displayInfo: true,
                emptyMsg: '',
                afterPageText: '',
                listeners: {
                    afterrender: function(bbar) {
                        bbar.last.hideParent = true;
                        bbar.last.hide();
                        bbar.refresh.hideParent = true;
                        bbar.refresh.hide();
                    }
                }
            }) : null,

            listeners: {
                cellclick: (grid, rowIndex, columnIndex, event) => {
                    // Only redirect user to page if title is clicked
                    if (columnIndex !== 1) return;
                    const record = grid.getStore().getAt(rowIndex);
                    self.fireEvent('documentSelected', record.data.handleId);
                },

                afteredit: (e) =>  {
                    if (e.originalValue === e.value) return;
                    if (e.field !== "translationPriority") return;
                    console.log("afterEdit", e.record.id, e.record.json.handleId, e.originalValue, e.value, e)

                    self.updatePriority(e.record.json.handleId, e.value)
                        .then(() => {
                            console.log("Success!");
                            this.store.proxy.invalidateCache();
                            this.store.reload();
                        }).catch(err =>  {
                            console.error("Failure to update priority. Restoring previous priority to " + e.originalValue, err);
                            // Restore previous value
                            e.record.reject()
                        })
                }
            }
        }

        this.moduleLabel = {
            xtype: "label",
            text: "Module type",
            id: MODULE_LABEL_ID,
            style: SIDEBAR_LABEL_STYLE
        }

        this.pageLabel = {
            xtype: "label",
            text: "Page type",
            id: PAGE_LABEL_FILTER_ID,
            style: SIDEBAR_LABEL_STYLE
        }


        config = Ext.apply(config, {
            // bodyCssClass: 'hippo-reports-document-list',
            id: PANEL_ID,
            layout: "border",
            items: [
                {
                    region: "center",
                    layout: "fit",
                    items: [editorGridPanelConfig]
                },
                {
                    layout: "anchor",
                    region: 'west',
                    id: FILTER_SIDEBAR_ID,
                    anchorSize: {width:200, height:600},
                    width: 200,
                    items: [
                        {
                            xtype: "label",
                            text: "Language",
                            anchor: "100%"
                        },
                        Hippo.Reports.languageComboConfig,
                        {
                            xtype: "label",
                            text: "Publish status",
                            style: SIDEBAR_LABEL_STYLE
                        },
                        Hippo.Reports.publishStatusComboConfig,
                        {
                            xtype: "label",
                            text: "Priority",
                            style: SIDEBAR_LABEL_STYLE
                        },
                        Hippo.Reports.priorityFilterComboConfig,
                        {
                            xtype: "label",
                            text: "Document type",
                            style: SIDEBAR_LABEL_STYLE
                        },
                        Hippo.Reports.pageOrModuleFilterComboConfig,
                        {
                            xtype: "button",
                            text: "Download",
                            style: SIDEBAR_LABEL_STYLE,
                            listeners: {
                                click: self.downloadRecordsAsCsv,
                                scope: self
                            }
                        }
                    ]

                }
            ]
        });

        Hippo.Reports.TranslationListPanel.superclass.constructor.call(this, config);
    },

    removePageSubtypeFilters() {
        const cmp = Ext.getCmp(FILTER_SIDEBAR_ID)
        cmp.remove(MODULE_FILTER_ID);
        cmp.remove(MODULE_LABEL_ID);
        cmp.remove(PAGE_FILTER_ID);
        cmp.remove(PAGE_LABEL_FILTER_ID);
        cmp.doLayout()
    },

    showModuleFilter() {
        this.removePageSubtypeFilters();
        const cmp = Ext.getCmp(FILTER_SIDEBAR_ID)
        cmp.add(this.moduleLabel)
        cmp.add(Hippo.Reports.moduleFilterComboConfig);
        cmp.doLayout()
    },

    showPageFilter() {
        this.removePageSubtypeFilters();
        const cmp = Ext.getCmp(FILTER_SIDEBAR_ID)
        cmp.add(this.pageLabel)
        cmp.add(Hippo.Reports.pageFilterComboConfig);
        cmp.doLayout()
    },

    getPriorityData: function() {
        return new Promise((resolve, reject) => {
            fetch("/cms/translation/priority")
                .then((response) => {
                    response.json().then((priorityData) => {
                        resolve(priorityData)
                    })
                })
                .catch((err) => reject(err))
        })
    },

    getPageTypes() {
        // TODO clean this up
        return new Promise((resolve, reject) => {
            fetch("/cms/translation/pages")
                .then((response) => {
                    response.json().then((priorityData) => {
                        resolve(priorityData)
                    })
                })
                .catch((err) => reject(err))
        })
    },
    getModuleTypes() {
        // TODO clean this up
        return new Promise((resolve, reject) => {
            fetch("/cms/translation/modules")
                .then((response) => {
                    response.json().then((priorityData) => {
                        resolve(priorityData)
                    })
                })
                .catch((err) => reject(err))
        })
    },

    renderFlags: function(flags, metaData, record, rowIndex, colIndex, store) {
        // Ensure flags are always shown in the same order, and English comes up first
        const uuid = record.data.handleId;
        flags = flags.sort((a, b) => {
            if (a === "en") return -1;
            if (b === "en") return 1;
            return a.localeCompare(b);
        })

        return flags.map(function(locale) {
            locale = locale === "en" ? "uk" : locale;
            const url = "./wicket/resource/org.hippoecm.frontend.translation.LocaleProviderPlugin/icons/flags/flag-16_" + locale + ".png"
            return '<img src="' + url + '" style="display: inline" />'
        }).join(" ");
    },

    renderDateTime: function(iso) {
        const onHover = new Date(iso).toLocaleString()
        return '<p title="' + onHover + '">' + this.isoDateTimeToDayMonthYear(iso) + '</p>'
    },

    isoDateTimeToDayMonthYear(iso) {
        if (iso === undefined) return undefined;
        return new Date(iso).dateFormat("d M y")
    },

    renderPublishStatus: function(status) {
        let topIcon = "hi-empty"
        let bottomIcon = "hi-empty"
        if (status === "NOT_LIVE") {
            topIcon = "hi-minus-circle"
        } else if (status === "PREV_VERSION_LIVE") {
            topIcon = "hi-check-circle";
            bottomIcon = "hi-exclamation-triangle"
        } else if (status === "CURR_VERSION_LIVE") {
            topIcon = "hi-check-circle";
            bottomIcon = "hi-empty";
        }

        return "<div class=\"hi hi-stack hi-l\">" +
            " <svg class=\"hi hi-file-text hi-l\"><use xlink:href=\"#hi-file-text-l\"></use></svg>" +
            " <svg class=\"hi " + topIcon + " hi-m hi-left hi-top\"><use xlink:href=\"#"+ topIcon +"-m\"></use></svg>" +
            " <svg class=\"hi " + bottomIcon +  " hi-m hi-left hi-bottom\"><use xlink:href=\"#" + bottomIcon + "-m\"></use></svg></div>"
    },

    publishStatusToString: function(status) {
        switch(status) {
            case "NOT_LIVE":
                return "Not live"
            case "PREV_VERSION_LIVE":
                return "Previous version live"
            case "CURR_VERSION_LIVE":
                return "Live"
            default:
                return status
        }
    },

    renderTranslationPriority: function(priority) {
        // If the priority map does not exist, request it and force a reload of the store once it has completed
        // If data does exist, then map the given priority onto its display value
        if (this.priorityDropdownData === undefined) {
            this.priorityDropdownData = []
            this.getPriorityData()
                .then((priorityData) => {
                    this.priorityDropdownData = priorityData;
                    const gridStore = Ext.getCmp(GRID_ID).getStore()
                    gridStore.load()
                    gridStore.proxy.addSortTransformation("translationPriority", (value) => {
                        const item = priorityData.find((item) => item[0] === value);
                        return item === undefined ? value : item[2];
                    })
                })
            return "";
        } else {
            const item = this.priorityDropdownData.find((item) => item[0] === priority);
            if (item !== undefined) return item[1];
        }
        return "";
    },

    updatePriority: function(handleId, priority) {
        return fetch("/cms/translation/" + handleId + "/priority", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({priority: priority})
        });
    },

    loadStore: function() {
        this.store.load({
            params: {
                start: 0,
                limit: this.pageSize
            }
        });
    },

    initComponent: function() {
        Hippo.Reports.TranslationListPanel.superclass.initComponent.call(this);
        Hippo.Reports.RefreshObservableInstance.addListener("refresh", this.loadStore, this);
        this.loadStore();
        var self = this;
        this.getModuleTypes()
            .then((types) => self.moduleTypes = types)
            .catch((err) => console.error(err));

        this.getPageTypes()
            .then((types) => self.pageTypes = types)
            .catch((err) => console.error(err));

    },

    destroy: function() {
        Hippo.Reports.TranslationListPanel.superclass.destroy.call(this);
        Hippo.Reports.RefreshObservableInstance.removeListener("refresh", this.loadStore, this);
    },

    downloadRecordsAsCsv: function() {
        const gridStore = Ext.getCmp(GRID_ID).getStore();
        const sortInfo = gridStore.sortInfo;
        const records = gridStore.proxy.getFilteredRecords({sort: sortInfo.field, dir: sortInfo.direction});
        let self = this;
        records.forEach((record) => {
            record.data.lastModified = self.isoDateTimeToDayMonthYear(record.data.lastModified);
            record.data.publishStatus = self.publishStatusToString(record.data.publishStatus);
        });
        const csv = Hippo.Reports.recordsToCsv(records, ["displayName", "translatedLocales", "sentForTranslationLocales", "translationStatus",
            "translationPriority", "lastModified", "lastModifiedBy", "publishStatus", "type"],
            ["Document", "Translated Locales", "Sent for Translation Locales", "Translation Status", "Translation Priority", "Last Modified", "Last Modified By", "Publish Status", "Type"])
        Hippo.Reports.downloadFile("translation-report.csv", "text/csv", csv);
    }

});

Ext.reg('Hippo.Reports.TranslationListPanel', Hippo.Reports.TranslationListPanel);
