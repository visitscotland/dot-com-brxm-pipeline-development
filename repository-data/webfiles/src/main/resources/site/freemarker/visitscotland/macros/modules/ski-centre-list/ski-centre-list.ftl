<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-ski-scotland-card.ftl">
<#include "../../global/preview-warning.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.SkiListModule" -->
<#macro skiCentreList module>
    <@previewWarning editMode module module.errorMessages />

    <vs-module-wrapper theme="light">
        <template slot="vsModuleWrapperHeading">
            ${module.title}
        </template>

        <template slot="vsModuleWrapperIntro">
            <@hst.html hippohtml=module.introduction/>
        </template>

        <vs-container>
            <vs-row
                class="mx-n4 mx-lg-n8"
            >
                <#list module.skiCentres as centre>
                    <@hst.link var="imageSrc" hippobean=centre.cmsPage.image.cmsImage.original/>
                    <vs-col
                        cols="12"
                        sm="6"
                        lg="4"
                        class="px-4 px-lg-8 text-left"
                    >
                        <vs-ski-scotland-card
                            centre-info-url="${centre.feedURL}"
                            locale="${locale}"
                            timeout-duration="${property('ski.timeout')}"
                            img-src="${imageSrc}"
                            img-alt="${centre.cmsPage.image.altText}"
                            more-details-link="${centre.cmsPage.link}"
                            piste-map-link="${centre.pisteMap}"
                            last-updated-label="${label('ski', 'ski-centre.last-updated')}"
                            lifts-label="${label('ski', 'ski-centre.lifts')}"
                            runs-label="${label('ski', 'ski-centre.runs')}"
                            runs-lifts-status-label="${label('ski', 'ski-centre.run-lift-status')}"
                            status-label="${label('ski', 'ski-centre.status')}"
                            summary-closed-label="${label('ski', 'ski-centre.summary.closed')}"
                            summary-open-label="${label('ski', 'ski-centre.summary.open')}"
                            summary-opening-label="${label('ski', 'ski-centre.summary.expected-to-open')}"
                            summary-limited-patrol-label="${label('ski', 'ski-centre.summary.limited-patrol')}"
                            summary-on-hold-label="${label('ski', 'ski-centre.summary.on-hold')}"
                            piste-map-label="${label('ski', 'ski-centre.view-piste-map')}"
                            more-details-label="${label('ski', 'ski-centre.more-details')}"
                        >
                            <template slot="centre-name">${centre.title}</template>
                            <div slot="data-loading">
                                ${label("ski", "ski-data.loading")}
                            </div>
                            <div slot="data-unavailable">
                                ${label("ski", "ski-data.unavailable")}
                            </div>
                            <div slot="js-required">
                                ${label("ski", "ski-data.no-js")}
                            </div>
                        </vs-ski-scotland-card>
                    </vs-col>
                </#list>
            </vs-row>
        </vs-container>
    </vs-module-wrapper>
</#macro>