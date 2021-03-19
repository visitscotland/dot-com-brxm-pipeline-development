<#macro quote authorImage content authorName authorTitle link="">
    <vs-quote>
        <#if authorImage?has_content>
            <vs-img
                alt="${(authorImage)!'${label("essentials.global", "default.alt-text")}'}"
                src="${authorImage}"
                slot="quoteImage">
            </vs-img>
        </#if>
        <template slot="quoteContent">
            <@hst.html hippohtml=content/>
        </template>
        <#if authorName?has_content && authorName != "">
            <template slot="quoteAuthorName">${authorName}</template>
        </#if>
        <#if authorTitle?has_content && authorTitle != "">
            <template slot="quoteAuthorTitle">${authorTitle}</template>
        </#if>
        <#if link?has_content>
            <vs-button
                href="${link.link}"
                slot="quoteLink">
                ${link.label}
            </vs-button>
        </#if>
    </vs-quote>
</#macro>