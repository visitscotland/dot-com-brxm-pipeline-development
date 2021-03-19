package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Quote;
import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import com.visitscotland.brxm.model.FlatQuote;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.brxm.services.CommonUtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class QuoteFactory {

    private static final Logger logger = LoggerFactory.getLogger(QuoteFactory.class);

    private ImageFactory imageFactory;

    private LinkModulesFactory linkFactory;

    public QuoteFactory(ImageFactory imageFactory, LinkModulesFactory linkFactory){
        this.imageFactory = imageFactory;
        this.linkFactory = linkFactory;
    }

    public FlatQuote getQuote(Quote doc, Module module, Locale locale){
        FlatQuote quote = new FlatQuote();
        quote.setQuote(doc.getQuote());
        quote.setAuthorName(doc.getAuthor());
        quote.setAuthorTitle(doc.getRole());

        if (doc.getImage() != null) {
            quote.setImage(imageFactory.createImage(doc.getImage(), module, locale));
        }

        if (doc.getProduct() instanceof Linkable) {
            quote.setLink(linkFactory.createEnhancedLink((Linkable) doc.getProduct(), locale, false));
        } else if (doc.getProduct() != null){
            CommonUtilsService.contentIssue("The Product for this iCentre (%s) is not a valid link.", doc.getPath());
            logger.warn("The Product for this iCentre ({})is not a valid link.", doc.getPath());
        }
        return quote;
    }

}
