package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class TouristInformationMockBuilder {

    private TourismInformation mock;
    private ICentre iCentre;
    private Quote quote;
    private IKnow iKnow;

    public TouristInformationMockBuilder() {
        mock = Mockito.mock(TourismInformation.class);
    }


    public TourismInformation build() {
        return mock;
    }

    public TouristInformationMockBuilder addICentre(){
        iCentre = mock(ICentre.class);
        when(mock.getICentre()).thenReturn(iCentre);

        return this;
    }

    public TouristInformationMockBuilder addICentreTitle(String title){
        when(iCentre.getTitle()).thenReturn(title);

        return this;
    }

    public TouristInformationMockBuilder addQuote(){
        if (iCentre == null){
            addICentre();
        }
        this.quote = mock(Quote.class);
        when(iCentre.getQuote()).thenReturn(quote);

        return this;
    }


    public TouristInformationMockBuilder addICentreImage() {
        if (iCentre ==null){
            addICentre();
        }
        Image img = mock(Image.class);
        when(iCentre.getImage()).thenReturn(img);
        return this;
    }

    public TouristInformationMockBuilder addQuoteProduct() {
        if (quote == null){
            addQuote();
        }

        CMSLink cmsLink = mock(CMSLink.class);
        HippoBean sharedLink = mock(HippoBean.class, withSettings().extraInterfaces(Linkable.class));

        when(quote.getProduct()).thenReturn(cmsLink);
        when(cmsLink.getLink()).thenReturn(sharedLink);

        return this;
    }

    public TouristInformationMockBuilder addQuoteImage() {
        if (quote == null){
            addQuote();
        }

        when(quote.getImage()).thenReturn(mock(Image.class));
        return this;
    }

    public TouristInformationMockBuilder addQuoteText() {
        if (quote == null){
            addQuote();
        }

        when(quote.getQuote()).thenReturn(mock(HippoHtml.class));

        return this;
    }

    public TouristInformationMockBuilder addQuoteAuthor(String author) {
        if (quote == null){
            addQuote();
        }

        when(quote.getAuthor()).thenReturn(author);

        return this;
    }

    public TouristInformationMockBuilder addQuoteRole(String role) {
        if (quote == null){
            addQuote();
        }

        when(quote.getRole()).thenReturn(role);

        return this;
    }

    public TouristInformationMockBuilder addIKnow() {
        iKnow = mock(IKnow.class);
        when(mock.getIKnow()).thenReturn(iKnow);

        return this;
    }

    public TouristInformationMockBuilder addIKnowTitle(String title) {
        if (iKnow == null){
            addIKnow();
        }
        when(iKnow.getTitle()).thenReturn(title);
        return this;
    }

    public TouristInformationMockBuilder addIKnowDescription(String description) {
        if (iKnow == null){
            addIKnow();
        }
        HippoHtml hippoHtml = mock(HippoHtml.class);
        when(hippoHtml.getContent()).thenReturn(description);
        when(iKnow.getDescription()).thenReturn(hippoHtml);
        return this;
    }
}
