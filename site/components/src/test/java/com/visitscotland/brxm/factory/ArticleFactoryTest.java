package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.ArticleModule;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleFactoryTest {

    @InjectMocks
    @Resource
    ArticleFactory factory;

    @Mock
    ImageFactory imageFactory;

    @Mock
    QuoteFactory embedder;

    @Test
    @DisplayName("Main Article - Populates all fields")
    void getModule() {
        HstRequest request = new MockHstRequest();
        Article article = mock(Article.class);
        when(article.getMediaItem()).thenReturn(mock(Image.class));
        when(article.getTitle()).thenReturn("Title");
        when(article.getAnchor()).thenReturn("Anchor");
        when(article.getCopy()).thenReturn(mock(HippoHtml.class));

        ArticleModule module = factory.getModule(request, article);

        verify(imageFactory, only()).getImage(any(Image.class), any(), any());
        assertEquals("Title", module.getTitle());
        assertEquals("Anchor", module.getAnchor());
        assertEquals(HippoHtml.class, module.getIntroduction().getClass());
        assertEquals(article, module.getHippoBean());
    }

    @Test
    @DisplayName("Article Sections - Populates all fields")
    void getModuleSections() {
        HstRequest request = new MockHstRequest();
        ArticleSection section = mock(ArticleSection.class);
        Article article = mock(Article.class);

        when(section.getCopy()).thenReturn(mock(HippoHtml.class));
        when(section.getMediaItem()).thenReturn(mock(Image.class));
        when(section.getQuote()).thenReturn(mock(Quote.class));
        when(article.getParagraph()).thenReturn(Arrays.asList(section, section));

        ArticleModule module = factory.getModule(request, article);

        verify(imageFactory, times(2)).getImage(any(Image.class), any(), any());
        verify(embedder, times(2)).getQuote(any(Quote.class), any(), any());
        assertEquals(2, module.getSections().size());
    }
}
