package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.SkiCentreList;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.List;

public class SkiListModule extends Module<SkiCentreList> {

    private String title;
    private HippoHtml introduction;
    private List<SkiModule> skiCentres;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HippoHtml getIntroduction() {
        return introduction;
    }

    public void setIntroduction(HippoHtml introduction) {
        this.introduction = introduction;
    }

    public List<SkiModule> getSkiCentres() {
        return skiCentres;
    }

    public void setSkiCentres(List<SkiModule> skiCentres) {
        this.skiCentres = skiCentres;
    }
}
