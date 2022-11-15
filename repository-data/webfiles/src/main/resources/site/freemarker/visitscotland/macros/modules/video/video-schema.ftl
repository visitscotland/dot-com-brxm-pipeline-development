<#include "../../../../include/imports.ftl">

<#macro videoSchema video>
    <@hst.headContribution category="seo">
        <script type="application/ld+json">
            {
                "@context": "https://schema.org",
                "@type": "VideoObject",
                "name": "${video.label}",
                "description": "${video.teaser}",
                "contentUrl": "https://youtube.com/watch?v=${video.youtubeId}",
                "thumbnailUrl":  "<@hst.link hippobean=video.image.cmsImage.original fullyQualified=true/>"
                <#if video.publishedDate??>
                    ,"uploadDate": "${video.publishedDate?date?string("iso")}"
                </#if>
            }
        </script>
    </@hst.headContribution>
</#macro>
