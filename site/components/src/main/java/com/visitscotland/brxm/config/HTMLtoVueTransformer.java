package com.visitscotland.brxm.config;

import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.services.LinkService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
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
        output = processInfoAlert(output);

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
        Pattern headingTag = Pattern.compile("<h(\\d)(.*?)>(.*?)</h\\1>");
        Matcher matcher = headingTag.matcher(html);
        String output = html;

        while (matcher.find()) {
            String id = toKebabCase(matcher.group(3));
            String level = matcher.group(1);
            String vsHeading;
            if (level.equals("6")){
                /* TODO: This is a workaround for VS-3489 and needs to be removed when that ticket is completed
                 * Since this a temporarily fix no unit tests have been written
                 */
                vsHeading = String.format("<vs-heading level=\"4\" override-style-level=\"6\" id=\"%s\"%s>%s</vs-heading>",
                        id, matcher.group(2), matcher.group(3));
            } else {
                vsHeading = String.format("<vs-heading level=\"%s\" id=\"%s\"%s>%s</vs-heading>",
                        level, id, matcher.group(2), matcher.group(3));
            }
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
        final Pattern aTag = Pattern.compile("(<a\\s)(?:.+\\s)?href\\s?=\\s?\"(.*?)\".*?(</a>)");
        final int OPEN = 1;
        final int HREF = 2;
        final int CLOSE = 3;

        Matcher matcher = aTag.matcher(html);
        String output = html;

        while (matcher.find()) {
            String closingTag;
            String a = matcher.group();
            FlatLink link = linkService.createExternalLink(matcher.group(HREF));

            if (link.getType().equals(LinkType.DOWNLOAD)){
                closingTag = linkService.getDownloadText(matcher.group(HREF))+ "</vs-link>";
            } else {
                closingTag = "</vs-link>";
            }

            String vsLink = a.replace(matcher.group(OPEN), "<vs-link type=\""+ link.getType().getRichTextType()+"\" ")
                    .replace(matcher.group(HREF), link.getLink())
                    .replace(matcher.group(CLOSE), closingTag);
            output = output.replace(a, vsLink);
        }

        return output;
    }

    /**
     * Process info alerts
     *
     * These are warnings that can appear in a General page introduction. They are inserted by the CKEditor as
     * <span class="info-alert">...</span>, and must be converted into <vs-alert>...</vs-alert>
     */
    public String processInfoAlert(final String html) {
        String output = html;
        final Pattern infoSpanRegex = Pattern.compile("<span\\s+class=\\\"info-text\\\">(.*?)</span>");
        Matcher matcher = infoSpanRegex.matcher(html);
        while (matcher.find()) {
            String alertHtml = "<vs-alert>" + matcher.group(1) + "</vs-alert>";
            output = output.replace(matcher.group(0), alertHtml);
        }
        return output;
    }

    /**
     * Process ul and ol lists.
     *
     * Due to a bug in Vue, nested ul and ol tags are not correctly processed. In order to overcome that issue, only the most
     * external tags will be converted to a Vue component
     *
     * This text will come from a CKEditor so it is assumed that the HTML is well constructed.
     */
    public String processLists(final String html){
        final String UL = "<ul>";
        final String UL_END = "</ul>";
        final String OL = "<ol>";
        final String OL_END = "</ol>";

        String output = html;

        Map<Integer, String> tags = new TreeMap<>(Collections.reverseOrder());

        //Calculate all the position for the list tags
        for (String tag : new String[]{UL, UL_END, OL,OL_END}){
            int index = html.indexOf(tag);
            while (index != -1){
                tags.put(index, tag);
                index = html.indexOf(tag, index + 1);
            }
        }

        //Transform only most external list tags into Vue component. Internal list will remain as ul or ol
        String closeTag = null;
        Integer closeTagIndex = null;
        int depth = 1;
        for (Map.Entry<Integer, String> entry : tags.entrySet()){
            if (closeTag == null){
                closeTagIndex = entry.getKey();
                closeTag = entry.getValue();
            } else if (closeTag.equals(entry.getValue())){
                depth++;
            } else if (UL.equals(entry.getValue()) && UL_END.equals(closeTag)
                || OL.equals(entry.getValue()) && OL_END.equals(closeTag)){
                if (depth > 1){
                    depth--;
                } else {
                    closeTag = null;
                    output = convertToVsList(output, entry.getKey(), closeTagIndex, UL.equals(entry.getValue()));
                }
            }
        }

        return output;
    }

    /**
     * Convert the ol or ul tag into a the vs-list Vue component.
     */
    private String convertToVsList(String html, int openTagIndex, int closeTagIndex, boolean ul){
        String vsList = ul?"<vs-list>":"<vs-list ordered>";
        return html.substring(0,openTagIndex) + vsList
                + html.substring(openTagIndex+4, closeTagIndex) + "</vs-list>"
                + html.substring(closeTagIndex + 5);
    }

    /**
     * Turns a text into kebab case by doing the followingIt does the following replacements:
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
