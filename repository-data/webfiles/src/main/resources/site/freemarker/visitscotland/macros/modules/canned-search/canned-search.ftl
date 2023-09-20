<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-tag.ftl">
<#include "../../../../frontend/components/vs-link.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-canned-search.ftl">
<#include "../../../../frontend/components/vs-button.ftl">

<#include "../../global/preview-warning.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.CannedSearchModule" -->
<#-- @ftlvariable name="editMode" type="java.lang.Boolean"-->

<#macro cannedSearch module themeName="">
    <@previewWarning editMode module module.errorMessages/>

    <vs-canned-search
        api-url="${module.cannedSearchEndpoint}"
        search-type="${module.productType}"
        carousel-next-text="${label('essentials.pagination', 'page.next')}"
        carousel-previous-text="${label('essentials.pagination', 'page.previous')}"
        heading="${module.title}"
    >
        <template v-slot:vs-canned-search-intro>
            <@hst.html hippohtml=module.description/>
        </template>

        <template v-slot:vs-canned-search-buttons>
            <vs-button
                href="${module.viewAllLink.link}">
                ${module.viewAllLink.label}
            </vs-button>
        </template>

        <#if module.credit??>
            <template v-slot:vs-canned-search-credit>
                ${module.credit}
            </template>
        </#if>

        <template v-slot:vs-canned-search-of>
            ${label('essentials.pagination', 'page.of')}
        </template>
    </vs-canned-search>
</#macro>