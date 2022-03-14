<#include "../../../../../include/imports.ftl">
<#include "../../../../../frontend/components/vs-megalink-multi-image.ftl">

<#macro multiImageFeatured feature lastFeatured theme>
    <#if feature.image.cmsImage??>
        <#assign image>
            <@hst.link hippobean=feature.image.cmsImage.original/>
        </#assign>
    <#else>
        <#assign image>
            ${feature.image.externalImage}
        </#assign>
    </#if>
    <vs-col
        cols="12"
        md="6"
        xl="12"
    >
        <vs-megalink-multi-image
            featured
            <#if lastFeatured == 'true'>last-featured</#if>
            img-src="${image}"
            link-type="${feature.type}"
            link-url="${feature.link}"
            theme="${theme}"
            <#if feature.itineraryTransport??>
                transport="${feature.itineraryTransport}"
                transport-name="${label('transports', feature.itineraryTransport)}"
            </#if>
            <#if feature.itineraryDays??>
                <#if feature.itineraryDays = 1>
                    days-label="${label('itinerary', 'day')}"
                <#else>
                    days-label="${label('itinerary', 'days')}"
                </#if>
                days="${feature.itineraryDays}"
            <#else>
                days-label="${label('itinerary', 'day')}"
            </#if>
        >
            <template slot="vsMultiImageHeading">
                ${feature.label}</template>
            <#if feature.teaser?? && feature.label??>
                <p slot="vsMultiImageContent">
                    ${feature.teaser}
                </p>
            </#if>
        </vs-megalink-multi-image>
    </vs-col>
</#macro>