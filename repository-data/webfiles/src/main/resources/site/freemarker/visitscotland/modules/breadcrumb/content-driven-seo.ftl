<#include "../../../include/imports.ftl">
<#--
    This macro takes all the relevant information with SEO purposes from a document. Therefore, this macro
    has no use for Channel Manager pages

    TODO: SEO for Channel Manager pages (at the moment 0 CM pages have been created))

-->

<#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.Page" -->
<#-- @ftlvariable name="orderedTranslations" type="java.util.List<com.visitscotland.brxm.hippobeans.BaseDocument>" -->

<#if document??>
    <#-- NO INDEX -->
    <#if document.seoNoIndex >
        <@hst.headContribution category="seo">
            <meta name="robots" content="noindex,follow" />
        </@hst.headContribution>
    </#if>
    <#-- CANONICAL TAG -->
    <@hst.headContribution category="seo">
        <link rel="canonical" href="<@hst.link hippobean=document canonical=true fullyQualified=true/>" />
    </@hst.headContribution>

    <#-- HREFLANG TAG -->
    <#if orderedTranslations?size gt 1>
        <#list orderedTranslations as translation>
            <@hst.headContribution category="seo">
                <link rel="alternate" href="<@hst.link hippobean=translation fullyQualified=true/>"  hreflang="${translation.locale.language}"/>
            </@hst.headContribution>
        </#list>
    </#if>

    <#-- TITLE TAG -->
    <@hst.headContribution category="seo">
        <title>${document.seoTitle?html} ${property("seo.title-suffix")}</title>
    </@hst.headContribution>

    <#-- META DESCRIPTION TAG -->
    <@hst.headContribution category="opengraph">
        <meta name="description" content="${document.seoDescription?html}" />
    </@hst.headContribution>


    <#-- OPEN GRAPH MARKUP  -->
    <@hst.link var="ogImage" hippobean=document.heroImage.original fullyQualified=true/>
    <@hst.headContribution category="opengraph">
        <meta property="og:title" content="${document.seoTitle?html}" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta property="og:description" content="${document.seoDescription?html}" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta property="og:type" content="article" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta property="og:url" content="<@hst.link hippobean=document canonical=true fullyQualified=true/>" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta property="og:site_name" content="${property("seo.og.site-name")}" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta property="og:locale" content="${document.locale.toLanguageTag()?lower_case}" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta property="og:image" content="${ogImage}" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta name="twitter:card" content="summary_large_image" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta name="twitter:site" content="${property("seo.og.twitter.site")}" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta name="twitter:title" content="${document.seoTitle?html}" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta name="twitter:description" content="${document.seoDescription?html}" />
    </@hst.headContribution>
    <@hst.headContribution category="opengraph">
        <meta property="twitter:image" content="${ogImage}" />
    </@hst.headContribution>
</#if>