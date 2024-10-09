package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Article;
import com.visitscotland.brxm.hippobeans.ArticleBSH;
import com.visitscotland.brxm.hippobeans.ArticleSection;
import com.visitscotland.brxm.hippobeans.VideoLink;
import com.visitscotland.brxm.model.ArticleModule;
import com.visitscotland.brxm.model.ArticleModuleSection;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.core.component.HstRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class ArticleFactory {

    private static final String STANDARD = "standard";
    private static final String ACCORDION = "accordion";
    private static final String BULLET_LIST = "bullet-list";
    private static final String HORIZONTAL_LIST = "horizontal-list";
    private static final String IMAGE_LIST = "image-list";
    private static final String NUMBERED_LIST = "numbered-list";

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

        module.setTitle(doc.getTitle());
        module.setIntroduction(doc.getCopy());
        module.setHippoBean(doc);

        addSpecialFields(doc, module);

        setImage(module, doc, request.getLocale());

        module.setAnchor(getAnchor(doc));

        setSections(module, doc, request.getLocale());

        if (isEditMode(request)) {
            // This validation is only required for Edit Mode
            validate(module);
        }

        return module;
    }

    private void addSpecialFields(Article document, ArticleModule module) {
        if (document instanceof ArticleBSH){
            module.setLayout(((ArticleBSH) document).getTheme() == null? STANDARD: ((ArticleBSH) document).getTheme());
            module.setNested(Boolean.TRUE.equals(((ArticleBSH) document).getNested()));
        }
    }

    private String getAnchor(Article doc){
        String anchor = Contract.isEmpty(doc.getAnchor())? doc.getTitle() : doc.getAnchor();

        return anchor.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();
    }

    private void setImage(ArticleModule module, Article doc, Locale locale){
        if (doc.getMediaItem() != null) {
            if (doc.getMediaItem() instanceof VideoLink){
                VideoLink videoLink = ((VideoLink)doc.getMediaItem());
                if (videoLink.getVideoLink() != null) {
                    module.setVideo(linkService.createVideo(videoLink.getVideoLink(), module, locale));
                }
            }else {
                module.setImage(imageFactory.getImage(doc.getMediaItem(), module, locale));
            }
        }
    }

    private void setSections(ArticleModule module, Article doc, Locale locale){
        List<ArticleModuleSection> sections = new ArrayList<>();

        for (ArticleSection paragraph: doc.getParagraph()){
            ArticleModuleSection section = new ArticleModuleSection();
            section.setCopy(paragraph.getCopy());

            if (paragraph.getMediaItem() != null) {
                if (paragraph.getMediaItem() instanceof VideoLink){
                    section.setVideo(linkService.createVideo(((VideoLink)paragraph.getMediaItem()).getVideoLink(), module, locale));
                }else {
                    section.setImage(imageFactory.getImage(paragraph.getMediaItem(), module, locale));
                }
            }

            if (paragraph.getQuote() != null) {
                section.setQuote(quoteEmbedder.getQuote(paragraph.getQuote(), module, locale));
            }
            sections.add(section);
        }

        module.setSections(sections);
    }

    private void validate(ArticleModule module){


        if (!module.getLayout().equals(STANDARD) && module.getImage() != null) {
            module.addErrorMessage("The current Article layout doesn't allow a main Image");
        }
        if (in(module.getLayout(), IMAGE_LIST, BULLET_LIST)) {
            module.addErrorMessage("The current Article layout doesn't allow any Copy text");
        }
        switch (module.getLayout()) {
            case HORIZONTAL_LIST:
                if (module.getSections().size() > 4) {
                    module.addErrorMessage("The current Article layout should not contain more than 4 paragraph. The extra items will be ignored.");
                }
                break;
            case NUMBERED_LIST:
                if (module.getSections().size() > 5) {
                    module.addErrorMessage("The current Article layout should not contain more than 5 paragraph.");
                }
                break;
            case STANDARD:
            case BULLET_LIST:
            case IMAGE_LIST:
                if (module.getSections().size() > 9) {
                    module.addErrorMessage("The current Article layout should not contain more than 9 paragraph.");
                }
                break;
        }

        if (!module.getLayout().equals(STANDARD)) {
            for (int i=0; i<module.getSections().size(); i++){
                ArticleModuleSection section = module.getSections().get(i);

                if (section.getCopy().getContent().indexOf("<h3>") != section.getCopy().getContent().lastIndexOf("<h3>")) {
                    module.addErrorMessage(String.format("The paragraph #%s contains multiple headings and only one is allowed.", i));
                } else if (section.getCopy().getContent().indexOf("</h3>") - section.getCopy().getContent().indexOf("</h3>") > 84) {
                    module.addErrorMessage(String.format("The heading line in paragraph #%s is too long. It should not be longer than 80 characters.", i));
                }
                if (module.getLayout().equals(BULLET_LIST)) {
                    if (section.getCopy().getContent().indexOf("<p>") - section.getCopy().getContent().lastIndexOf("</p>") > 204) {
                        module.addErrorMessage(String.format("The copy in paragraph #%s is too long. It should not be longer than 200 characters, excluding the heading.", i));
                    }
                } else if (module.getLayout().equals(IMAGE_LIST)) {
                    if (section.getCopy().getContent().indexOf("<p>") - section.getCopy().getContent().lastIndexOf("</p>") > 304) {
                        module.addErrorMessage(String.format("The copy in paragraph #%s is too long. It should not be longer than 300 characters, excluding the heading.", i));
                    }
                }

                if (module.getLayout().equals(BULLET_LIST) && module.getImage() != null) {
                    module.addErrorMessage(String.format("The paragraph #%s contains an image and it will not be displayed.", i));
                }
                if (section.getQuote() != null) {
                    module.addErrorMessage(String.format("The paragraph #%s contains an quote and it will not be displayed", i));
                }
            }
        }
    }

    private boolean in (String field, String... values) {
        for (String value: values) {
            if (field.equals(value)) {
                return true;
            }
        }
        return false;
    }

    boolean isEditMode(HstRequest request) {
        return Boolean.TRUE.equals(request.getAttribute("editMode"));
    }
}
