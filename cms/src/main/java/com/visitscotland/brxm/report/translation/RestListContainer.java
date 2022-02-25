package com.visitscotland.brxm.report.translation;

import java.util.List;

// ExtJS pagination requires the server to provide a total
// Configure the reader with `totalProperty: "total"; root: "data"`
// Only for use wth PageableHttpProxy

public class RestListContainer<T> {

    private final List<T> data;
    private final int total;

    public RestListContainer(List<T> data) {
        this.data = data;
        this.total = data.size();
    }

    public List<T> getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }
}
