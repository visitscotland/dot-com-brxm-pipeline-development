package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Article;
import com.visitscotland.brxm.hippobeans.ArticleSection;
import com.visitscotland.brxm.hippobeans.VideoLink;
import com.visitscotland.brxm.model.ArticleModule;
import com.visitscotland.brxm.model.ArticleModuleSection;
import com.visitscotland.brxm.services.LinkService;
import org.hippoecm.hst.core.component.HstRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleFactory {

    private final ImageFactory imageFactory;
    private final LinkService linkService;
    private final QuoteFactory quoteEmbedder;

    public ArticleFactory(ImageFactory imageFactory, QuoteFactory quoteEmbedder, LinkService linkService){
        this.imageFactory = imageFactory;
        this.quoteEmbedder = quoteEmbedder;
        this.linkService = linkService;
    }

    public ArticleModule getModule(HstRequest request, Article doc){
        ArticleModule module = new ArticleModule();
        List<ArticleModuleSection> sections = new ArrayList<>();
          if (doc.getMediaItem() != null) {
            if (doc.getMediaItem() instanceof VideoLink){
                VideoLink videoLink = ((VideoLink)doc.getMediaItem());
                if (videoLink.getVideoLink() != null) {
                    module.setVideo(linkService.createVideo(videoLink.getVideoLink(), module, request.getLocale()));
                }
            }else {
                module.setImage(imageFactory.getImage(doc.getMediaItem(), module, request.getLocale()));
            }
        }
        module.setTitle(doc.getTitle());
        module.setIntroduction(doc.getCopy());
        module.setHippoBean(doc);
        module.setAnchor(doc.getAnchor());

        for (ArticleSection paragraph: doc.getParagraph()){
            ArticleModuleSection section = new ArticleModuleSection();
            section.setCopy(paragraph.getCopy());

            if (paragraph.getMediaItem() != null) {
                if (paragraph.getMediaItem() instanceof VideoLink){
                    section.setVideo(linkService.createVideo(((VideoLink)paragraph.getMediaItem()).getVideoLink(), module, request.getLocale()));
                }else {
                    section.setImage(imageFactory.getImage(paragraph.getMediaItem(), module, request.getLocale()));
                }
            }

            if (paragraph.getQuote() != null) {
                section.setQuote(quoteEmbedder.getQuote(paragraph.getQuote(), module, request.getLocale()));
            }

            sections.add(section);
        }
        module.setSections(sections);

        return module;
    }
}
