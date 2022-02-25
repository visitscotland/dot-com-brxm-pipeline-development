package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Article;
import com.visitscotland.brxm.hippobeans.ArticleSection;
import com.visitscotland.brxm.model.ArticleModule;
import com.visitscotland.brxm.model.ArticleModuleSection;
import org.hippoecm.hst.core.component.HstRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class ArticleFactory {

    private ImageFactory imageFactory;

    private QuoteFactory quoteEmbedder;

    public ArticleFactory(ImageFactory imageFactory, QuoteFactory quoteEmbedder){
        this.imageFactory = imageFactory;
        this.quoteEmbedder = quoteEmbedder;
    }

    public ArticleModule getModule(HstRequest request, Article doc){
        ArticleModule module = new ArticleModule();
        List<ArticleModuleSection> sections = new ArrayList<>();
        if (doc.getImage() != null) {
            module.setImage(imageFactory.createImage(doc.getImage(), module, Locale.UK));
        }
        module.setTitle(doc.getTitle());
        module.setIntroduction(doc.getCopy());
        module.setHippoBean(doc);
        module.setAnchor(doc.getAnchor());

        for (ArticleSection paragraph: doc.getParagraph()){
            ArticleModuleSection section = new ArticleModuleSection();
            section.setCopy(paragraph.getCopy());

            if (paragraph.getMediaItem() != null) {
                section.setImage(imageFactory.getImage(paragraph.getMediaItem(), module, request.getLocale()));
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
