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
        true
    </#assign>

    <#if module.mapType = 'destinations'>
        <#assign hideToggle>
            false
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

            <template v-slot:close-side-panel-text>
                <span class="sr-only">
                    ${label('map', 'map.close-panel')}
                </span>
            </template>
            <template v-slot:open-side-panel-text>
                ${label('map', 'map.open-panel')}
            </template>
            <template v-slot:back-btn-text>
                ${label('map', 'map.step-back')}
            </template>
            <template v-slot:reset-side-panel-text>
                ${label('map', 'map.reset-filters')}
            </template>
            <template v-slot:load-more-text>
                ${label('map', 'map.load-more')}
            </template>
            <template v-slot:no-js>
                ${label('map', 'map.no-js')}
            </template>
            <template v-slot:map-loading-text>
                ${label('map', 'map.loading')}
            </template>
            <template v-slot:panel-loading-message>
                ${label('map', 'map.loading-results')}
            </template>
            <template v-slot:zoom-too-close>
                ${label('map', 'map.zoom-too-close')}
            </template>
            <template v-slot:zoom-too-far>
                ${label('map', 'map.zoom-too-far')}
            </template>
        </vs-main-map-wrapper>
    </vs-module-wrapper>
</#macro>