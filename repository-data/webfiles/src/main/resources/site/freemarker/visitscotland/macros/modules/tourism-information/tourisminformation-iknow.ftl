<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-link.ftl">
<#include "../../../../frontend/components/vs-heading.ftl">
<#include "../../../../frontend/components/vs-iknow-partner-item.ftl">

<#macro iknow module>

	<vs-container slot="upper" class="py-lg-4" >
        <vs-iknow-partner-item>
            <span slot="iknowHeading">
                <vs-heading level="2">${module.title}</vs-heading>
            </span>
            <span slot="iknowText">
                ${module.description}
            </span>
            <span slot="iknowCta">
                <vs-link href="${module.link.link}">${module.link.label}</vs-link>
            </span>
        </vs-iknow-partner-item>
    </vs-container>

</#macro>