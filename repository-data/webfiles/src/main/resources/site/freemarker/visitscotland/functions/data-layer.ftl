<#-- This Template will collect a list of methods or macros related to dataLayer operations.

The specification of the events can be found on Airtable which needs to match with the front-end specification for them
to be included or excluded. The front-end specification can be found in:
- frontend/src/utils/data-layer-templates.js
-->

<#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.Page" -->
<#function pageViewDLEvent document>
    <#assign searchType = (psrWidget.category.pathVariable)!'none'>

    <#assign url = hstRequest.request.pathInfo >
    <#assign event = "{
        'site_language': '${language}',
        'content_language' : '${document.locale.language}',"
    >
    <#if location??>
        <#if location.isRegion() >
            <#assign event = event + "'content_region': '${location.name}',">
        <#else >
            <#assign event = event + "'content_city': '${location.name}',">
        </#if>
    </#if>
    <#assign category = url?split("/")[1]>
    <#if !category?has_content>
        <#assign category = "homepage" />
    </#if>
    <#assign event = event + "'content_category':  '${category}',">
    <#assign event = event + "'content_page_type':  '${documentType(document)}',">
    <#assign event = event + "'search_category':  '${searchType}',">
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