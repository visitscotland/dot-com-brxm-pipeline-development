<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-carousel.ftl">
<#include "../../../../frontend/components/vs-carousel-slide.ftl">

<#macro carousel item>
    <vs-carousel
        next-text="${label('essentials.pagination', 'page.next')}"
        prev-text="${label('essentials.pagination', 'page.previous')}"
        :slides-xs="1"
        :slides-sm="2"
        :slides-md="3"
        :slides-lg="4"
    >
        <#list item.links as carouselItem>
            <#if carouselItem.image.cmsImage??>
                <#assign image>
                    <@hst.link hippobean=carouselItem.image.cmsImage.original/>
                </#assign>
            <#else>
                <#assign image = carouselItem.image.externalImage!'' />
            </#if>
            <vs-carousel-slide
                slide-index="${carouselItem?index}"
                link-url="${carouselItem.link}"
                link-type="${carouselItem.type}"
                img-src="${image}"
                img-alt="${(carouselItem.image.altText)!'${label("essentials.global", "default.alt-text")}'}"
                <#if carouselItem.category??>
                    category-label="${label("otyml", "otyml.category")}"
                    category="${carouselItem.category}"
                </#if>
                <#if carouselItem.itineraryTransport??>
                    transport="${carouselItem.itineraryTransport}"
                    transport-name="${label('transports', carouselItem.itineraryTransport)}"
                </#if>
                <#if carouselItem.itineraryDays??>
                    <#if carouselItem.itineraryDays = 1>
                        days-label="${label('itinerary', 'day')}"
                    <#else>
                        days-label="${label('itinerary', 'days')}"
                    </#if>
                    days="${carouselItem.itineraryDays}"
                <#else>
                    days-label="${label('itinerary', 'day')}"
                </#if>
            >
                <template v-slot:vs-carousel-slide-heading>
                    ${carouselItem.label}
                </template>
            </vs-carousel-slide>
        </#list>

        <template v-slot:vs-carousel-navigate>
            ${label("essentials.pagination", "page.navigate-to-page")}
        </template>

        <template v-slot:vs-carousel-of>
            ${label("essentials.pagination", "page.of")}
        </template>
    </vs-carousel>
</#macro>
