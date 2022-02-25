Ext.ns('Hippo.Reports');


Hippo.Reports.languageComboConfig = {
    xtype: "combo",
    width: 195,
    autoSelect: true,
    editable: false,
    triggerAction: 'all',
    remoteSort: true,
    lazyRender:true,
    mode: 'local',
    value: INITIAL_LOCALE,
    store: new Ext.data.ArrayStore({
        id: 0,
        fields: [
            'locale',
            'displayText'
        ],
        data: [["fr", "French"], ["de", "German"], ["nl", "Dutch"], ["it", "Italian"], ["es", "Spanish"]]
    }),
    valueField: 'locale',
    displayField: 'displayText',
    listeners: {
        select: function (combo, record, index) {
            const locale = record["id"]
            const gridStore = Ext.getCmp(GRID_ID).getStore()
            gridStore.proxy.setParams({locale: locale});
            gridStore.load();
        }
    }
}

Hippo.Reports.publishStatusComboConfig = {
    xtype: 'combo',
    width: 195,
    autoSelect: true,
    editable: false,
    triggerAction: 'all',
    lazyRender: true,
    mode: 'local',
    value: "all",
    store: new Ext.data.ArrayStore({
        id: 0,
        fields: [
            'code',
            'displayText'
        ],
        data: [["all", "All documents"], ["published", "Published"], ["offline", "Offline"]]
    }),
    valueField: 'code',
    displayField: 'displayText',
    listeners: {
        select: function (combo, record, index) {
            const selected = record["id"]
            const proxy = Ext.getCmp(GRID_ID).getStore().proxy;
            if (selected === "published") {
                proxy.addFilter("publishStatus", (record, value) => {
                    return value === "CURR_VERSION_LIVE" || value === "PREV_VERSION_LIVE"
                })
            } else if (selected === "offline") {
                proxy.addFilter("publishStatus", (record, value) => {
                    return value === "NOT_LIVE";
                })
            } else {
                proxy.clearFilter("publishStatus")
            }
            Ext.getCmp(GRID_ID).getStore().load();
        }
    }
};


// Combo box used for filtering records, shown in the left hand side
Hippo.Reports.priorityFilterComboConfig = {
    xtype: "combo",
    width: 195,
    autoSelect: true,
    editable: false,
    triggerAction: 'all',
    lazyRender:true,
    mode: 'local',
    value: "All documents",
    store: new Ext.data.ArrayStore({
        id: 0,
        autoLoad: true,
        fields: [
            'value',
            'name',
            'sortOrder'
        ],
        proxy: new Ext.data.HttpProxy({
            // Get possible priority values from the server. Configured in com.visitscotland.brxm.translation.report.TranslationPriority
            url: "/cms/translation/priority",
            method: "GET"
        }),

        listeners: {
            load: (store) => {
                store.insert(0, new Ext.data.Record({
                    value: "all",
                    name: "All documents",
                    sortOrder: 0
                }, "all"))
            }
        }
    }),

    valueField: 'value',
    displayField: 'name',
    listeners: {
        select: (combo, record, index) => {
            console.log("Filter translationPriority", record);
            const proxy = Ext.getCmp(GRID_ID).getStore().proxy;
            if (record["id"] === "all") {
                proxy.clearFilter("translationPriority");
            } else {
                proxy.addFilter("translationPriority", (rec, value) => value === record["id"])
            }

            Ext.getCmp(GRID_ID).getStore().load();
        }
    }
}

// Combo box used for filtering between Pages and modules
// Gets possible page and module types from api
Hippo.Reports.pageOrModuleFilterComboConfig = {
    xtype: 'combo',
    width: 195,
    autoSelect: true,
    editable: false,
    triggerAction: 'all',
    lazyRender: true,
    mode: 'local',
    value: "all",
    store: new Ext.data.ArrayStore({
        id: 0,
        fields: [
            'code',
            'displayText'
        ],
        data: [["all", "All documents"], ["pages", "Pages"], ["modules", "Modules"]]
    }),
    valueField: 'code',
    displayField: 'displayText',
    listeners: {
        select: function (combo, record, index) {
            const store = Ext.getCmp(GRID_ID).getStore();
            const panel = Ext.getCmp(PANEL_ID);

            if (record["id"] === "all") {
                store.proxy.clearFilter("type");
                panel.removePageSubtypeFilters()
            }
            if ( record["id"] === "pages") {
                panel.showPageFilter()
            } else if (record["id"] === "modules") {
                panel.showModuleFilter()
            }
            if (panel.moduleTypes.length === 0 || panel.pageTypes.length === 0) return;
            const allowedTypes = record["id"] === "pages" ? panel.pageTypes : panel.moduleTypes;
            store.proxy.addFilter("type", (rec, val) => allowedTypes.includes(val));
            store.load();
        }
    }
};

Hippo.Reports.moduleFilterComboConfig = {
    xtype: "combo",
    width: 195,
    id: MODULE_FILTER_ID,
    autoSelect: true,
    editable: false,
    triggerAction: 'all',
    lazyRender:true,
    mode: 'local',
    value: "All documents",
    store: new Hippo.Reports.SimpleArrayStore({
        id: 0,
        autoLoad: true,
        fields: [
            'value',
        ],
        proxy: new Ext.data.HttpProxy({
            url: "/cms/translation/modules",
            method: "GET"
        }),

        listeners: {
            load: (store) => {
                store.insert(0, new Ext.data.Record({
                    value: "All documents",
                }, "All documents"))
            }
        }
    }),

    valueField: 'value',
    displayField: 'value',
    listeners: {
        select: (combo, record, index) => {
            const proxy = Ext.getCmp(GRID_ID).getStore().proxy;
            const panel = Ext.getCmp(PANEL_ID)
            if (record["id"] === "All documents") {
                proxy.addFilter("type", (rec, value) => panel.moduleTypes.includes(value))
            } else {
                proxy.addFilter("type", (rec, value) => value === record["id"])
            }

            Ext.getCmp(GRID_ID).getStore().load();
        }
    }
}

Hippo.Reports.pageFilterComboConfig = {
    xtype: "combo",
    width: 195,
    id: PAGE_FILTER_ID,
    autoSelect: true,
    editable: false,
    triggerAction: 'all',
    lazyRender:true,
    mode: 'local',
    value: "All documents",
    store: new Hippo.Reports.SimpleArrayStore({
        id: 0,
        autoLoad: true,
        fields: [
            'value',
        ],
        proxy: new Ext.data.HttpProxy({
            url: "/cms/translation/pages",
            method: "GET"
        }),

        listeners: {
            load: (store) => {
                store.insert(0, new Ext.data.Record({
                    value: "All documents",
                }, "All documents"))
            }
        }
    }),

    valueField: 'value',
    displayField: 'value',
    listeners: {
        select: (combo, record, index) => {
            const panel = Ext.getCmp(PANEL_ID)
            const proxy = Ext.getCmp(GRID_ID).getStore().proxy;
            if (record["id"] === "All documents") {
                proxy.addFilter("type", (rec, value) => panel.pageTypes.includes(value))
            } else {
                proxy.addFilter("type", (rec, value) => value === record["id"])
            }

            Ext.getCmp(GRID_ID).getStore().load();
        }
    }
}

Hippo.Reports.typeFilterComboConfig = {
    xtype: "combo",
    width: 195,
    id: STATUS_FILTER_ID,
    autoSelect: true,
    editable: false,
    triggerAction: 'all',
    lazyRender:true,
    mode: 'local',
    value: "All documents",
    store: new Hippo.Reports.SimpleArrayStore({
        id: 0,
        autoLoad: true,
        fields: [
            'value',
        ],
        proxy: new Ext.data.HttpProxy({
            url: "/cms/translation/status",
            method: "GET"
        }),

        listeners: {
            load: (store) => {
                store.insert(0, new Ext.data.Record({
                    value: "All documents",
                }, "All documents"))
                // Remove Translated filter as it will always return no results
                const translated = store.find("value", "Translated")
                if (translated > -1) {
                    store.removeAt(translated)
                }
            }
        }
    }),

    valueField: 'value',
    displayField: 'value',
    listeners: {
        select: (combo, record, index) => {
            const panel = Ext.getCmp(PANEL_ID)
            const proxy = Ext.getCmp(GRID_ID).getStore().proxy;
            if (record["id"] === "All documents") {
                proxy.addFilter("translationStatus", (rec, value) => panel.statusTypes.includes(value))
            } else {
                proxy.addFilter("translationStatus", (rec, value) => value === record["id"])
            }

            Ext.getCmp(GRID_ID).getStore().load();
        }
    }
}
