<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-banner.ftl">
<#include "../../../../frontend/components/vs-link.ftl">

<#include "../../global/preview-warning.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.BannerModule"-->
<#macro emergencyBanner module>
    <@previewWarning editMode module module.errorMessages />
    <#if module.ctaLink??>
    <vs-banner
        close-btn-text="${label('essentials.global', 'close')}"
    >
        <template slot="bannerText">
            <@hst.html hippohtml=module.copy />
        </template>

        <template slot="bannerCta">
            <vs-link 
                href="${module.ctaLink.link}"
                <#if module.ctaLink.type != "internal">type="${module.ctaLink.type}"</#if>
            >
                ${module.ctaLink.label}
            </vs-link>
        </template>
    </vs-banner>
    </#if>
</#macro>
