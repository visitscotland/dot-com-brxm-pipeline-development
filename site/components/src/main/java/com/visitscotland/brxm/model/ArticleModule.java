package com.visitscotland.brxm.model;

import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.List;

public class ArticleModule extends Module {
    private FlatImage image;
    private String title;
    private HippoHtml introduction;
    private List<ArticleModuleSection> sections;
    private EnhancedLink video;

    public FlatImage getImage() {
        return image;
    }

    public void setImage(FlatImage image) {
        this.image = image;
    }

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

    public List<ArticleModuleSection> getSections() {
        return sections;
    }

    public void setSections(List<ArticleModuleSection> sections) {
        this.sections = sections;
    }

    public EnhancedLink getVideo() {
        return video;
    }

    public void setVideo(EnhancedLink video) {
        this.video = video;
    }
}
