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
                v-slot:quote-image>
            </vs-img>
        </#if>
        <template v-slot:quote-content>
            <@hst.html hippohtml=quoteItem.quote/>
        </template>
        <#if quoteItem.authorName?? && quoteItem.authorName?has_content>
            <template v-slot:quote-author-name>
                ${quoteItem.authorName}
            </template>
        </#if>
        <#if quoteItem.authorTitle?? && quoteItem.authorTitle?has_content>
            <template v-slot:quote-author-title>
                ${quoteItem.authorTitle}
            </template>
        </#if>
        <#if quoteItem.link?? && quoteItem.link?has_content>
            <vs-button href="${quoteItem.link.link}" v-slot:quote-link>
                ${quoteItem.link.label}
            </vs-button>
        </#if>
    </vs-quote>
</#macro>