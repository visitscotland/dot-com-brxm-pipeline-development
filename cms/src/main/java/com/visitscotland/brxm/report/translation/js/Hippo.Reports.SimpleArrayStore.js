Ext.ns('Hippo.Reports');

// Simply loads records from a simple list, such as [1, 3, 6, 7]
// Normal ArrayReader requires each list item to be in a sublist
Hippo.Reports.SimpleArrayReader = Ext.extend(Ext.data.JsonReader,{
    readRecords: function (list) {
        return {
            success : true,
            records : list.map((item) => new this.recordType({value: item}, item)),
            totalRecords : list.length
        };
    }
});

Hippo.Reports.SimpleArrayStore = Ext.extend(Ext.data.Store, {
    constructor: function (config) {
        Hippo.Reports.SimpleArrayStore.superclass.constructor.call(this, Ext.apply(config, {
            reader: new Hippo.Reports.SimpleArrayReader(config)
        }));
    }
});