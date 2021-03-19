package com.visitscotland.brxm.config;

import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.services.LinkService;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HTMLtoVueTransformer {

    private final LinkService linkService;

    HTMLtoVueTransformer(LinkService linkService){
        this.linkService = linkService;
    }

    public String process(final String html){
        String output = processHeadings(html);
        output = processLinks(output);
        output = processLists(output);

        return output;
    }

    /**
     * Process the heading tags (h2, h3, etc) and transform them into Vue tags.
     */
    public String processHeadings(final String html){
        /*
         * Targets the heading tags
         *
         * Groups:
         * 1. Level of the header (i.e. "2" when the tag is "h2")
         * 2. Attributes for the heading tag
         * 3. Content of the tag
         */
        Pattern h2Tag = Pattern.compile("<h(\\d)(.*?)>(.*?)</h2>");
        Matcher matcher = h2Tag.matcher(html);
        String output = html;

        while (matcher.find()) {
            String id = toKebabCase(matcher.group(3));
            String vsHeading = String.format("<vs-heading level=\"%s\" id=\"%s\"%s>%s</vs-heading>",
                    matcher.group(1), id, matcher.group(2), matcher.group(3));
            output = output.replace(matcher.group(), vsHeading);
        }

        return output;
    }

    /**
     * Process the anchor tags and transform them into Vue tags. It also adds the attribute type depending on the type
     * of the link ({@code LinkType})
     *
     * @see LinkType
     */
    public String processLinks(final String html){

        /*
         * Targets the opening tag of anchor links (a tag).
         * - The tag must contain href attribute, otherwise it would be ignored
         *
         * Groups:
         * 1. Opening anchor including a trailing space:  "<a "
         * 2. The value of the attribute href
         * 3. Closing tag: "</a>"
         */
        Pattern aTag = Pattern.compile("(<a\\s)[.*?\\s]?href\\s?=\\s?\"(.*?)\".*?(</a>)");
        Matcher matcher = aTag.matcher(html);
        String output = html;

        while (matcher.find()) {
            String a = matcher.group();
            LinkType type = linkService.getType(matcher.group(2));
            String vsLink = a.replace(matcher.group(1), "<vs-link type=\""+type+"\" ")
                    .replace(matcher.group(3),"</vs-link>");
            output = output.replace(a, vsLink);
        }

        return output;
    }

    /**
     * Process the anchor tags and transform them into Vue tags. It also adds the attribute type depending on the type
     * of the link ({@code LinkType})
     *
     * @param html
     * @return
     */
    public String processLists(final String html){
        return html.replace("<ul>", "<vs-list>")
                .replace("</ul>", "</vs-list>")
                .replace("<ol>", "<vs-list ordered>")
                .replace("</ol>", "</vs-list>");
    }

    /**
     * Turns a text into kebab case by doing the followingIt does the folloing replacements:
     *
     * <li>Transform the text to lower case</li>
     * <li>Replace all non-alphanumeric characters with dash(-) </li>
     * <li>Replace consecutive dashes (-) with a single dash </li>
     * <li>Remove dashes at the end and beginning of the line</li>
     */
    private String toKebabCase(String text){
        if (text != null) {
            return text.toLowerCase().replaceAll("\\W", "-").replaceAll("-{2,}", "-").replaceAll("-$", "").replaceAll("^-", "");
        }
        return null;
    }


}
