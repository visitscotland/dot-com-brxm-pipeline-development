<#include "../../../../../include/imports.ftl">
<#include "../../../../../frontend/components/vs-megalink-multi-image.ftl">

<#macro multiImageThreeItems megalink showTeaser theme>
    <#if megalink.image.cmsImage??>
        <#assign image>
            <@hst.link hippobean=megalink.image.cmsImage.original/>
        </#assign>
    <#else>
        <#assign image>
            ${megalink.image.externalImage}
        </#assign>
    </#if>
    <vs-col
        cols="12"
        md="6"
        xl="4"
    >
        <vs-megalink-multi-image
            img-src="${image}"
            link-type="${megalink.type}"
            theme="${theme}"
            link-url="${megalink.link}"
            <#if megalink.itineraryTransport??>
                transport="${megalink.itineraryTransport}"
                transport-name="${label('transports', megalink.itineraryTransport)}"
            </#if>
            <#if megalink.itineraryDays??>
                <#if megalink.itineraryDays = 1>
                    days-label="${label('itinerary', 'day')}"
                <#else>
                    days-label="${label('itinerary', 'days')}"
                </#if>
                days="${megalink.itineraryDays}"
            <#else>
                days-label="${label('itinerary', 'day')}"
            </#if>
        >
            <template slot="vsMultiImageHeading">
                ${megalink.label}</template>
            <#if showTeaser == 'true'>
                <template slot="vsMultiImageContent">
                    <p>${megalink.teaser}</p>
                </template>
            </#if>
        </vs-megalink-multi-image>
    </vs-col>
</#macro>