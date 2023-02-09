<#include "../../../frontend/components/vs-quote.ftl">

<#macro quote quoteItem variant="narrow">    
    <vs-quote variant="${variant}">
        <#if quoteItem.image??>
            <#assign imageQuote>
                <@hst.link hippobean=quoteItem.image.cmsImage.original/>
            </#assign>
        <#else>
            <#assign imageQuote="" />
        </#if>
        <#if imageQuote?has_content>
            <vs-img
                alt=""
                src="${imageQuote}"
                sizes="25vw"
                srcset="${imageQuote}?size=xs 300w, 
                    ${imageQuote}?size=sm 600w,
                    ${imageQuote}?size=md 1200w, 
                    ${imageQuote}?size=lg 2048w"
                low-res-image="${imageQuote}?size=xxs"
                slot="quoteImage">
            </vs-img>
        </#if>
        <template slot="quoteContent">
            <@hst.html hippohtml=quoteItem.quote/>
        </template>
        <#if quoteItem.authorName?? && quoteItem.authorName?has_content>
            <template slot="quoteAuthorName">
                ${quoteItem.authorName}
            </template>
        </#if>
        <#if quoteItem.authorTitle?? && quoteItem.authorTitle?has_content>
            <template slot="quoteAuthorTitle">
                ${quoteItem.authorTitle}
            </template>
        </#if>
        <#if quoteItem.link?? && quoteItem.link?has_content>
            <vs-button href="${quoteItem.link.link}" slot="quoteLink">
                ${quoteItem.link.label}
            </vs-button>
        </#if>
    </vs-quote>
</#macro>