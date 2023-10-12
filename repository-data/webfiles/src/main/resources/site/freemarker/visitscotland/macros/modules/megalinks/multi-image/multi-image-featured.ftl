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
            theme="${theme}"
            link-type="${feature.type}"
            link-url="${feature.link}"
            error-message="${label('essentials.global', 'third-party-error')}"
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
            <#if feature.youtubeId??>
                video-id="${feature.youtubeId}"
                video-btn-text="${label('video', 'video.play-btn')}"
            </#if>
        >
            <template v-slot:vs-multi-image-heading>
                ${feature.label}</template>
            <#if feature.teaser?? && feature.label??>
                <template v-slot:vs-multi-image-content>
                    <p>
                        ${feature.teaser}
                    </p>
                </template>
            </#if>
        </vs-megalink-multi-image>
    </vs-col>
</#macro>