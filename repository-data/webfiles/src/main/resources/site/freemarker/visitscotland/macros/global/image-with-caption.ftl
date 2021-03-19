<#include "../../../frontend/components/vs-svg.ftl">
<#include "../../../frontend/components/vs-social-credit-link.ftl">
<#include "../../../frontend/components/vs-image-with-caption.ftl">

<#macro imageWithCaption imageSrc imageDetails variant>
    <vs-image-with-caption
        alt-text="${(imageDetails.altText)!'${label("essentials.global", "default.alt-text")}'}"
        image-src="${imageSrc}"
        latitude="<#if variant != 'fullwidth'>${(imageDetails.coordinates.latitude)!''}</#if>"
        longitude="<#if variant != 'fullwidth'>${(imageDetails.coordinates.longitude)!''}</#if>"
        variant="${variant}"
    >
        <span slot="caption">
            ${(imageDetails.description)!''}
        </span>

        <#if imageDetails.source?has_content>
            <vs-svg slot="toggle-icon" path="${imageDetails.source + '-bg'}" height="24" width="24"></vs-svg>

            <vs-social-credit-link
                slot="social-link"
                credit="${(imageDetails.credit)!'No credit'}"
                social-post-url="${imageDetails.postUrl}"
                source="${imageDetails.source}"
            ></vs-social-credit-link>
        <#else>
            <#if imageDetails.credit?has_content>
                <span slot="credit">
                    &copy; ${imageDetails.credit}
                </span>
            </#if>
        </#if>
    </vs-image-with-caption>
</#macro>