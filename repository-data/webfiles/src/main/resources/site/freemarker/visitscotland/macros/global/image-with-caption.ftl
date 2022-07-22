<#include "../../../frontend/components/vs-svg.ftl">
<#include "../../../frontend/components/vs-social-credit-link.ftl">
<#include "../../../frontend/components/vs-image-with-caption.ftl">
<#include "../../../frontend/components/vs-caption.ftl">

<#macro imageWithCaption imageSrc imageDetails variant="fullwidth" isHero="false" mobileOverlap="false" alignment="left" isVideo="false" videoId="" videoTitle="" videoBtn="" useLazyLoading="true">
    <vs-image-with-caption
        latitude="<#if variant != 'fullwidth'>${(imageDetails.coordinates.latitude)!''}</#if>"
        longitude="<#if variant != 'fullwidth'>${(imageDetails.coordinates.longitude)!''}</#if>"
        variant="${variant}"
        toggle-button-text="${label('essentials.global', 'image.toggle.text')}"
        :is-hero-image="${isHero}"
        :mobile-overlap="${mobileOverlap}"
        :is-video="${isVideo}"
        video-id="${videoId}"
        no-cookies-message="${label('video', 'video.no-cookies')}"
        no-js-message="${label('video', 'video.no-js')}"
        cookie-link-text="${label('essentials.global', 'cookie.link-message')}"
        :use-lazy-loading="${useLazyLoading}"
        <#if videoBtn?? && videoBtn != "">
            play-button-text="${videoBtn}"
        <#else>
            play-button-text="${label('video', 'video.play-btn')}"
        </#if>
    >
        <template slot="video-title">
            ${videoTitle}
        </template>

        <vs-img
            src="${imageSrc}"
            alt="${(imageDetails.altText)!'${label("essentials.global", "default.alt-text")}'}"
            srcset="${imageSrc}?size=xs 300w, 
                    ${imageSrc}?size=sm 600w,
                    ${imageSrc}?size=md 1200w, 
                    ${imageSrc}?size=lg 2048w"
            sizes="(min-width: 768px) 75vw, 100vw"
            low-res-image="${imageSrc}?size=xxs"
            :use-lazy-loading="${useLazyLoading}"
        >
        </vs-img>

        <vs-caption
            slot="img-caption"
            latitude="<#if variant != 'fullwidth'>${(imageDetails.coordinates.latitude)!''}</#if>"
            longitude="<#if variant != 'fullwidth'>${(imageDetails.coordinates.longitude)!''}</#if>"
            variant="${variant}"
            text-align="${alignment}"
        >
            <template slot="caption">
                ${label('essentials.global', 'image.title')}: ${(imageDetails.description)!''}
            </template>

            <#if imageDetails.source?has_content>
                <vs-svg slot="toggle-icon" path="${imageDetails.source + '-bg'}" height="24" width="24"></vs-svg>

                <vs-social-credit-link
                    slot="credit"
                    credit="<#if imageDetails.credit??>${imageDetails.credit}<#else>${label('essentials.global', 'image.no.credit')}</#if>"
                    social-post-url="${imageDetails.postUrl}"
                    source="${imageDetails.source}"
                ></vs-social-credit-link>
            <#else>
                <#if imageDetails.credit?has_content>
                    <template slot="credit">
                        &copy; ${imageDetails.credit}
                    </template>
                </#if>
            </#if>
        </vs-caption>
    </vs-image-with-caption>
</#macro>	