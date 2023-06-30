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

    <#assign toggleValues>
        [
            {
                text: '${placesText?trim}',
                value: 'places',
                icon: 'map-marker'
            },
            {
                text: '${regionsText?trim}',
                value: 'regions',
                icon: 'map',
            },
        ]
    </#assign>

    <#if module.mapType = 'regional'>
        <#assign toggleValues>
            [
                {
                    text: '${placesText?trim}',
                    value: 'places-regional',
                    icon: 'pin'
                },
                {
                    text: 'iCentres',
                    value: 'icentres',
                    icon: 'serv',
                },
            ]
        </#assign>
    </#if>

    <#assign hideToggle>
        false
    </#assign>

    <#if module.mapType = 'cities' || module.mapType = 'towns'>
        <#assign hideToggle>
            true
        </#assign>
    </#if>

    <vs-module-wrapper>
        <template v-slot:vs-module-wrapper-heading>
            ${module.title}
        </template>

        <template v-slot:vs-module-wrapper-intro>
            <@hst.html hippohtml=module.introduction/>
        </template>
        <vs-main-map-wrapper
            :main-heading-exists="${mainHeadingExists}"
            category-heading="${module.tabTitle}"
            :filters="${escapeJSON(module.filters,true)}"
            :places-data="${escapeJSON(module.geoJson.features,true)}"
            map-id="vs-map-${module.id}"
            :toggle-data="${toggleValues}"
            buttons-label="${label('map', 'map.buttons-label')}"
            clear-selection-text="${label('map', 'map.clear')}"
            apply-filters-text="${label('map', 'map.show-results')}"
            filters-applied-text="${label('map', 'map.filters-applied')}"
            clear-filters-text="${label('map', 'map.clear')}"
            :region-bounds="${escapeJSON(module.mapPosition,true)}"
            <#if module.detailsEndpoint??>
                details-endpoint="${module.detailsEndpoint}"
            </#if>
            <#if module.mapType = 'regional'>
                panel-message="${label('map', 'map.panel-bottom-msg')}"
            </#if>
            map-filter-message="${label('map', 'map.apply-filters')}"
            map-no-results-message="${label('map', 'map.no-results')}"
            :hide-mobile-toggle="${hideToggle}"
        >

            <template slot="closeSidePanelText">
                <span class="sr-only">
                    ${label('map', 'map.close-panel')}
                </span>
            </template>
            <template slot="openSidePanelText">
                ${label('map', 'map.open-panel')}
            </template>
            <template slot="backBtnText">
                ${label('map', 'map.step-back')}
            </template>
            <template slot="resetSidePanelText">
                ${label('map', 'map.reset-filters')}
            </template>
            <template slot="loadMoreText">
                ${label('map', 'map.load-more')}
            </template>
            <template slot="noJs">
                ${label('map', 'map.no-js')}
            </template>
            <template slot="mapLoadingText">
                ${label('map', 'map.loading')}
            </template>
            <template slot="panelLoadingMessage">
                ${label('map', 'map.loading-results')}
            </template>
            <template slot="zoomTooClose">
                ${label('map', 'map.zoom-too-close')}
            </template>
            <template slot="zoomTooFar">
                ${label('map', 'map.zoom-too-far')}
            </template>
        </vs-main-map-wrapper>
    </vs-module-wrapper>
</#macro>