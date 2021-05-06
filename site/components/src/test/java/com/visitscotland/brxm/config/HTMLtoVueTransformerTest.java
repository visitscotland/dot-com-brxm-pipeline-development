package com.visitscotland.brxm.config;

import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.services.LinkService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HTMLtoVueTransformerTest {

    @Mock
    LinkService linkService;

    @Resource
    @InjectMocks
    HTMLtoVueTransformer transformer;

    @ParameterizedTest
    @DisplayName("VS-2383 - Internal links return type default ")
    @CsvSource({"INTERNAL,default", "EXTERNAL,external", "DOWNLOAD, download"})
    void links_external(LinkType type, String output){
        final String HTML = "<p>Take a look at the " +
                "<a href=\"https://community.visitscotland.com/\" title=\"iKnow Community\" target=\"_blank\">iKnow Scotland Community</a>" +
                " to share your tips and pick up a couple more for your next break or day out in Scotland.</p>";

        when(linkService.getType("https://community.visitscotland.com/")).thenReturn(type);

        String result = transformer.processLinks(HTML);

        Assertions.assertTrue(result.contains("<vs-link "));
        Assertions.assertTrue(result.contains(" type=\""+output+"\""));
    }

    @Test
    @DisplayName("Identifies multiples a tags")
    void links_multiple(){
        final String HTML = "<p>Random Links usefull for this test" +
                "<a href=\"https://www.gov.scot/\">Link 1</a>, " +
                "<a href=\"https://www.gov.scot/coronavirus-covid-19/\">Link 2</a> & " +
                "<a href=\"https://protect.scot/\">Link 3</a> </p>";

        when(linkService.getType(any())).thenReturn(LinkType.EXTERNAL);

        String result = transformer.processLinks(HTML);
        Matcher matcher = Pattern.compile("<vs-link ").matcher(result);
        int count = 0;

        while (matcher.find()) {
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    @DisplayName("The a tag expression does not get confused by tags that start with a (i.e article)")
    void links_no_article_tag(){
        final String HTML = "<article>The a tag expression does not get confused by tags that start with a</article>";
        String result = transformer.processLinks(HTML);

        Assertions.assertFalse(result.contains("data-type="));
    }

    @Test
    @DisplayName("The tags must contain an href")
    void links_no_href(){
        final String HTML = "<p>The following links does not contain href attribute" +
                "<a data-href=\"https://dms.visitscotland.com/\">Link 1</a> " +
                "<a>Link 2</a></p>";
        String result = transformer.processLinks(HTML);

        Assertions.assertFalse(result.contains("data-type="));
    }

    @Test
    @DisplayName("Headings convert heading to vue componentsonvert headings")
    void heading(){
        String HTML = "<h2>Heading 2</h2>";

        String result = transformer.processHeadings(HTML);

        assertTrue(result.startsWith("<vs-heading level=\"2\""));
        assertTrue(result.endsWith("</vs-heading>"));
    }

    @Test
    @DisplayName("Create anchor links for h2 headings")
    void h2_multiple(){
        String HTML = "<h2>Heading 1</h2><p>Blah</p><h2>Heading 2</h2><p>Blah</p><h2>Heading 3</h2><p>Blah</p>";

        String result = transformer.processHeadings(HTML);
        Matcher matcher = Pattern.compile("id=").matcher(result);
        int count = 0;

        while (matcher.find()) {
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    @DisplayName("The conversion to kebab case reduce the noise as much")
    void h2_kebabCase(){
        String HTML = "<h2> Heading for ---- OMG!  </h2>";

        assertTrue(transformer.processHeadings(HTML).contains("id=\"heading-for-omg\""));
    }

    @Test
    @DisplayName("Transform ordered lists into vue components")
    void lists_ol(){
        String HTML = "<ol><li>Item 1</li><li>Item 2</li></ol>";

        assertTrue(transformer.processLists(HTML).endsWith("</vs-list>"));
        assertTrue(transformer.processLists(HTML).startsWith("<vs-list ordered>"));
    }

    @Test
    @DisplayName("Transform unordered lists into vue components")
    void lists_ul(){
        String HTML = "<ul><li>Item 1</li><li>Item 2</li></ul>";

        assertTrue(transformer.processLists(HTML).endsWith("</vs-list>"));
        assertTrue(transformer.processLists(HTML).startsWith("<vs-list>"));
    }

//    @ParameterizedTest
//    @CsvSource({
//            "<ul><li>Item 1</li><li>Item 2</li></ul><ul><li>Item 3</li><li>Item 4</li></ul>," +
//                    "<vs-list><li>Item 1</li><li>Item 2</li></vs-list><vs-list><li>Item 3</li><li>Item 4</li></vs-list>",
//            "<ul><li>Item 1</li><li>Item 2</li><li><ul><li>Item 3</li><li>Item 4</li></ul></li></ul>," +
//                    "<vs-list><li>Item 1</li><li>Item 2</li><li><ul><li>Item 3</li><li>Item 4</li></ul></li></vs-list>",
//            "<ul><ol><ul></ul></ol><ol></ol></ul> <ol><ol></ol></ol><ul><ul></ul></ul>," +
//                    "<vs-list><ol><ul></ul></ol><ol></ol></vs-list> <vs-list ordered><ol></ol></vs-list><vs-list><ul></ul></vs-list>"
//    })
//    @DisplayName("VS-2488 - Consecutive lists will be turned into vs-link components")
//    void lists_nestedListProcess(String input, String expectedOutput){
//        assertEquals(expectedOutput, transformer.processLists(input));
//    }

    @Test
    @DisplayName("VS-2488 - Consecutive lists will be turned into vs-link components")
    void lists_severalConsecutiveLists(){
        String output = transformer.processLists("<ul><li>Item 1</li><li>Item 2</li></ul><ul><li>Item 3</li><li>Item 4</li></ul>");
        assertEquals("<vs-list><li>Item 1</li><li>Item 2</li></vs-list><vs-list><li>Item 3</li><li>Item 4</li></vs-list>", output);
    }

    @Test
    @DisplayName("VS-2488 - Nested list will only alter the most external item")
    void lists_nestedLists(){
        String output = transformer.processLists("<ul><li>Item 1</li><li>Item 2</li><li><ul><li>Item 3</li><li>Item 4</li></ul></li></ul>");
        assertEquals("<vs-list><li>Item 1</li><li>Item 2</li><li><ul><li>Item 3</li><li>Item 4</li></ul></li></vs-list>", output);
    }

    @Test
    @DisplayName("VS-2488 - Complex scenario that combines consecutive and nested lists")
    void lists_complex(){
        String output = transformer.processLists("<ul><ol><ul></ul></ol><ol></ol></ul> <ol><ol></ol></ol><ul><ul></ul></ul>");
        assertEquals("<vs-list><ol><ul></ul></ol><ol></ol></vs-list> <vs-list ordered><ol></ol></vs-list><vs-list><ul></ul></vs-list>", output);
    }
}
