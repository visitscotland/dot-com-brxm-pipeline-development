package com.visitscotland.brxm.model.navigation;

import com.visitscotland.brxm.hippobeans.FeaturedWidget;
import com.visitscotland.brxm.model.Module;

public class FeaturedEvent extends Module<FeaturedWidget> implements NavigationWidget{

    private String apiUrl;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
