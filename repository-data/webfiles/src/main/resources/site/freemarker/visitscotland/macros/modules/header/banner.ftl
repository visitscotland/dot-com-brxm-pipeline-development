<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-banner.ftl">
<#include "../../../../frontend/components/vs-link.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.BannerModule"-->
<#macro emergencyBanner module>
    <vs-banner>
        <template slot="bannerTitle">
            ${module.title} 
        </template>

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

        <template slot="closeBtnText">
            ${label("essentials.global", "close")}
        </template>
    </vs-banner>
</#macro>
