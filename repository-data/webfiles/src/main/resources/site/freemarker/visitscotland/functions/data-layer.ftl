<#-- This Template will collect a list of methods or macros related to dataLayer operations.

The specification of the events can be found on Airtable which needs to match with the front-end specification for them
to be included or excluded. The front-end specification can be found in:
- frontend/src/utils/data-layer-templates.js
-->

<#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.Page" -->
<#function pageViewDLEvent document>
    <#assign url = hstRequest.request.pathInfo >
    <#assign event = "{
        'site_language': '${language}',
        'content_language' : '${document.locale.language}',"
    >
    <#if location??>
        <#if region??>
            <#assign event = event + "'content_region': '${region.name}',">
        </#if>
        <#if !location.isRegion() >
            <#assign event = event + "'content_city': '${location.name}',">
        </#if>
    </#if>
    <#assign category = "homepage">
    <#list url?split("/") as urlSegment>
        <#if urlSegment?index gt 0>
            <#if urlSegment?index = 1 && urlSegment?length gt 0>
                <#assign category = urlSegment>
            </#if>
            <#assign event = event + "'page_category_${urlSegment?index}':  '${urlSegment}',">
        </#if>
    </#list>
    <#assign event = event + "'content_category':  '${category}',">
    <#assign event = event + "'content_page_type':  '${documentType(document)}',">
    <#assign event = event + "}">
    <#return event>
</#function>

<#function documentType document>
<#-- Remove "visitscotland:" from the jcrType -->
    <#assign pageType = document.contentType[14..]>
    <#if pageType = "General">
        <#return document.theme>
    <#else>
        <#return pageType>
    </#if>
</#function>