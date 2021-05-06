Ext.ns('Hippo.Reports');


Hippo.Reports.PageableHttpProxy = function(conn, additionalParams) {
    this.additionalParams = additionalParams
    this.filters = {}
    Hippo.Reports.PageableHttpProxy.superclass.constructor.call(this, conn);
}

Ext.extend(Hippo.Reports.PageableHttpProxy, Ext.data.HttpProxy, {

    doRequest : function(action, rs, params, reader, cb, scope, arg) {
        this.api = undefined;
        Ext.data.Api.prepare(this);
        this.api["read"]["method"] = "GET"

        console.log("doRequest: params=", params, "additionalParams=", this.additionalParams)
        cb = cb.bind(Ext.getCmp(GRID_ID).getStore());
        if (this.records !== undefined && this.records.length !== 0) {
            let operation = this.buildJsonObject(this.records, params, true);
            params["sort"] = undefined
            params["dir"] = undefined
            operation["reader"] = reader
            operation["scope"] = this
            operation["request"] = {"callback": cb, arg: arg}

            console.log("Returning subset of records from store, operation=", operation)
            this.onRead("read", operation, {
                method: "GET",
                status: 200,
                responseText: JSON.stringify({
                    total: operation.totalRecords,
                    data: operation.records.map(function(record) {return record.data})
                }),
                params: params,
            })
        } else {
            console.log("Making HTTP request to obtain records")
            Hippo.Reports.PageableHttpProxy.superclass.doRequest.call(this, action, rs, Ext.apply(params, this.additionalParams), reader, this.onRequestCompleteCallback(cb) , scope, arg);
        }
    },


    getFilteredRecords(params) {
        if (this.records === undefined) return [];
        params.start = 0
        params.limit = this.records.length;
        return this.buildJsonObject(this.records, params, true).records;
    },

    setParams: function(params) {
        this.additionalParams = params;
        // When params are changed, force a refresh of server data
        this.invalidateCache();
    },

    onRequestCompleteCallback: function(loadStoreCb) {
        let self = this;
        return function(o, params, success) {
            console.log("Returned records, success=" + success, o)
            if (success) {
                self.records = o.records;
                return loadStoreCb(self.buildJsonObject(o.records, params, success), params, success);
            }

            loadStoreCb(o, params, success);
        }
    },

    invalidateCache: function() {
        this.records = undefined;
    },

    buildJsonObject: function(records, params, success) {
        if (params["params"] !== undefined) params = params.params;
        const start = params.start === undefined ? 0 : params.start;
        const limit = params.limit === undefined ? PAGE_SIZE : params.limit;
        records = this.filterRecords(records);
        const recordLength = records.length;
        records = this.sortRecords(records,params)
        const obj =  {records: records.slice(start, start + limit), success: success, totalRecords: recordLength}
        console.log("o=", obj, start, limit, params)
        return obj;
    },

    sortRecords: function(records, params) {
        if (params.sort === undefined) return records;
        let self = this;
        records = records.sort(function (a, b) {
            a = a.data[params.sort]
            b = b.data[params.sort]
            if (self.sortTransformations[params.sort] !== undefined) {
                a = self.sortTransformations[params.sort](a)
                b = self.sortTransformations[params.sort](b)
            }
            return a < b ? -1 : 1;
        })

        return params.dir === "ASC"? records : records.reverse();
    },

    filterRecords: function (records) {
        this.filters = this.filters === undefined ? {} : this.filters;
        return records.filter(function (record) {
            for (const [key, value] of Object.entries(this.filters)) {
                if (value === undefined) continue;
                const fn = value["scope"] === undefined ? value["fn"] : value["fn"].bind(value["scope"])
                if (!fn(record.data, record.data[key])) return false;
            }
            return true
        }, this)
    },

    addSortTransformation: function(field, f) {
        this.sortTransformations[field] = f
    },

    clearFilters() {
        this.filters = {}
    },

    clearFilter(property) {
        this.filters[property] = undefined
    },

    addFilter(property, fn, scope = undefined) {
        this.filters[property] = {fn: fn, scope: scope}
    },

    sortTransformations: {},
    filters: {}
});
