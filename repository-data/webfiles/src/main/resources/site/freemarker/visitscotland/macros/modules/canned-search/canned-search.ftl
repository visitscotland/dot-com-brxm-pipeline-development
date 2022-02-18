<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-tag.ftl">
<#include "../../../../frontend/components/vs-link.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-canned-search.ftl">
<#include "../../../../frontend/components/vs-button.ftl">

<#include "../../global/cms-errors.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.CannedSearchModule" -->

<#macro cannedSearch module themeName="">
    <@cmsErrors errors=module.errorMessages!"" editMode=editMode />

    <vs-canned-search
        api-url="${module.cannedSearchEndpoint}"
        search-type="${module.productType}"
        carousel-next-text="${label('essentials.pagination', 'page.next')}"
        carousel-previous-text="${label('essentials.pagination', 'page.previous')}"
        heading="${module.title}"
    >
        <template slot="vsCannedSearchIntro">
            <@hst.html hippohtml=module.description/>
        </template>

        <template slot="vsCannedSearchButtons">
            <vs-button
                href="${module.viewAllLink.link}">
                ${module.viewAllLink.label}
            </vs-button>
        </template>

        <#if module.credit??>
            <template slot="vsCannedSearchCredit">
                ${module.credit}
            </template>
        </#if>

        <template slot="vsCannedSearchOf">
            ${label('essentials.pagination', 'page.of')}
        </template>
    </vs-canned-search>
</#macro>