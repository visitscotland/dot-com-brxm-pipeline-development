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
            <#assign imageParameter>
                ${addParameter(imageQuote)}
            </#assign>
            <template v-slot:quote-image>
                <vs-img
                    alt=""
                    src="${imageQuote}"
                    sizes="25vw"
                    srcset="${imageQuote}${imageParameter}size=xs 300w,
                        ${imageQuote}${imageParameter}size=sm 600w,
                        ${imageQuote}${imageParameter}size=md 1200w,
                        ${imageQuote}${imageParameter}size=lg 2048w"
                    low-res-image="${imageQuote}${imageParameter}size=xxs"
                >
                </vs-img>
            </template>
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
            <template v-slot:quote-link>
                <vs-button href="${quoteItem.link.link}">
                    ${quoteItem.link.label}
                </vs-button>
            </template>
        </#if>
    </vs-quote>
</#macro>