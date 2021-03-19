package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.factory.QuoteFactory;
import com.visitscotland.brxm.hippobeans.Image;
import com.visitscotland.brxm.hippobeans.Quote;
import com.visitscotland.brxm.hippobeans.SharedLink;
import com.visitscotland.brxm.model.FlatQuote;
import com.visitscotland.brxm.factory.ImageFactory;
import com.visitscotland.brxm.factory.LinkModulesFactory;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.util.Locale;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteFactoryTest {

    @Mock
    ImageFactory imageFactory;

    @Mock
    LinkModulesFactory linkFactory;

    @InjectMocks
    @Resource
    QuoteFactory embedder;

    @Test
    @DisplayName("Happy path - All fields are mapped correctly")
    void getQuote() {
        Quote quote = mock(Quote.class);
        Image image = mock(Image.class);
        SharedLink link = mock(SharedLink.class);
        HippoHtml copy = mock(HippoHtml.class);
        when(quote.getQuote()).thenReturn(mock(HippoHtml.class));
        when(quote.getAuthor()).thenReturn("Author");
        when(quote.getRole()).thenReturn("Role");
        when(quote.getQuote()).thenReturn(copy);
        when(quote.getImage()).thenReturn(image);
        when(quote.getProduct()).thenReturn(link);

        FlatQuote flat = embedder.getQuote(quote, null, Locale.UK);

        verify(linkFactory).createEnhancedLink(link, Locale.UK, false);
        verify(imageFactory).createImage(image, null, Locale.UK);
        Assertions.assertEquals("Author", flat.getAuthorName());
        Assertions.assertEquals("Role", flat.getAuthorTitle());
        Assertions.assertNotNull(flat.getQuote());
    }
}
