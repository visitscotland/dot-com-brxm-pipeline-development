<#-- This Template will collect a list of methods or macros related to dataLayer operations.

The specification of the events can be found on Airtable which needs to match with the front-end specification for them
to be included or excluded. The front-end specification can be found in:
- frontend/src/utils/data-layer-templates.js
-->
<#function pageViewDLEvent document>
    <#assign searchType = (psrWidget.category.name())!'none'>
    <#assign searchLocation = (psrWidget.location.name)!''>

    <#assign url = hstRequest.request.pathInfo >
    <#assign event = "{
        'site_language': '${language}',
        'content_language' : '${document.locale.language}',"
    >
    <#if searchLocation != ''>
        <#assign event = event + "'content_region': '${searchLocation}',">
    <#elseif location??>
        <#if location.isRegion() >
            <#assign event = event + "'content_region': '${location.name}',">
        <#else >
            <#assign event = event + "'content_city': '${location.name}',">
        </#if>
    </#if>
    <#assign category = "homepage">
    <#list url?split("/") as x>
        <#if x?index gt 0>
            <#if x?index = 1>
                <#assign category = x>
            </#if>
            <#assign event = event + "'page_category_${x?index}':  '${x}',">
        </#if>
    </#list>
    <#assign event = event + "'content_category':  '${category}',">
    <#assign event = event + "'content_page_type':  '${documentType(document)}',">
    <#assign event = event + "'search_category':  '${searchType}',">
    <#assign event = event + "}">
    <#return event>
</#function>

<#function documentType document>
    <#if document.theme??>
        <#return document.theme>
    <#else>
        <#return document.contentType>
    </#if>
</#function>