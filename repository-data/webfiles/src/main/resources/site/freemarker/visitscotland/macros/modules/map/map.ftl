<#include "../../../../include/imports.ftl">
<#include "../../global/preview-warning.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-main-map-wrapper.ftl">


<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.MapsModule" -->
<#-- @ftlvariable name="editMode" type="java.lang.Boolean"-->

<#macro map module>
    <@hst.manageContent hippobean=module.hippoBean />
    <@previewWarning editMode module module.errorMessages />

    <#assign regionsText>
        ${label('map', 'map.regions')}
    </#assign>

    <#assign placesText>
        ${label('map', 'map.places')}
    </#assign>

    <#if module.title??>
        <#assign mainHeadingExists>
            true
        </#assign>
    <#else>
        <#assign mainHeadingExists>
            false
        </#assign>
    </#if>

    <vs-module-wrapper>
        <template slot="vsModuleWrapperHeading">
            ${module.title}
        </template>

        <template slot="vsModuleWrapperIntro">
            <@hst.html hippohtml=module.introduction/>
        </template>

        <vs-main-map-wrapper
            :main-heading-exists="${mainHeadingExists}"
            category-heading="${module.tabTitle}"
            :filters="${escapeJSON(module.filters,true)}"
            :places-data="${escapeJSON(module.geoJson.features,true)}"
            map-id="vs-map-${module.id}"
            :toggle-data="[
                {
                    text: '${regionsText?trim}',
                    value: 'regions',
                    icon: 'map',
                },
                {
                    text: '${placesText?trim}',
                    value: 'places',
                    icon: 'pin'
                },
            ]"
        >

            <template slot="closeSidePanelText">
                <span class="sr-only">
                    ${label('map', 'map.close-panel')}
                </span>
            </template>
            <template slot="openSidePanelText">
                <span class="sr-only">
                    ${label('map', 'map.open-panel')}
                </span>
            </template>
            <template slot="backBtnText">
                ${label('map', 'map.step-back')}
            </template>
            <template slot="resetSidePanelText">
                ${label('map', 'map.reset-filters')}
            </template>
            <template slot="noJs">
                ${label('map', 'map.no-js')}
            </template>
        </vs-main-map-wrapper>
    </vs-module-wrapper>
</#macro>