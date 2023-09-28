<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-ski-scotland-status.ftl">
<#include "../../../../frontend/components/vs-heading.ftl">
<#include "../../../../frontend/components/vs-icon.ftl">
<#include "../../../../frontend/components/vs-link.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-list.ftl">
<#include "../../../../frontend/components/vs-img.ftl">
<#include "../../global/address.ftl">
<#include "../../global/preview-warning.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.SkiModule" -->
<#macro skiCentre module>
    <@previewWarning editMode module module.errorMessages />

    <vs-module-wrapper theme="light">
        <template v-slot:vs-module-wrapper-heading>
            ${module.title}
        </template>

        <template v-slot:vs-module-wrapper-intro>
            <@hst.html hippohtml=module.introduction/>
        </template>

        <vs-container>
            <vs-row>
                <vs-col
                    cols="12"
                    md="10"
                    offset-md="1"
                >
                    <vs-ski-scotland-status
                        ski-status-url="${module.feedURL}"
                        locale="${locale}"
                        runs-lifts-status-label="${label("ski", "ski-centre.run-lift-status")}"
                        status-label="${label("ski", "ski-centre.status")}"
                        runs-label="${label("ski", "ski-centre.runs")}"
                        lifts-label="${label("ski", "ski-centre.lifts")}"
                        summary-open-label="${label("ski", "ski-centre.summary.open")}"
                        summary-opening-label="${label("ski", "ski-centre.summary.expected-to-open")}"
                        summary-closed-label="${label("ski", "ski-centre.summary.closed")}"
                        last-updated-label="${label("ski", "ski-centre.last-updated")}"
                        detailed-status-label="${label("ski", "ski-centre.detailed-status")}"
                        snow-conditions-label="${label("ski", "ski-centre.snow-conditions.full-report")}"
                        current-weather-label="${label("ski", "ski-centre.snow-conditions.current-weather")}"
                        weather-forecast-label="${label("ski", "ski-centre.snow-conditions.weather-forecast")}"
                        roads-label="${label("ski", "ski-centre.snow-conditions.road-status")}"
                        news-label="${label("ski", "ski-centre.snow-conditions.news-from-the-slopes")}"
                        run-status-label="${label("ski", "ski-centre.run-status")}"
                        lift-status-label="${label("ski", "ski-centre.lift-status")}"
                        run-label="${label("ski", "ski-centre.run")}"
                        lift-label="${label("ski", "ski-centre.lift")}"
                        easy-label="${label("ski", "ski-centre.run.easy")}"
                        intermediate-label="${label("ski", "ski-centre.run.intermediate")}"
                        difficult-label="${label("ski", "ski-centre.run.difficult")}"
                        very-difficult-label="${label("ski", "ski-centre.run.very-difficult")}"
                        itineraries-label="${label("ski", "ski-centre.run.itineraries")}"
                        other-label="${label("ski", "ski-centre.run.other")}"
                        green-label="${label("ski", "ski-centre.run.green")}"
                        blue-label="${label("ski", "ski-centre.run.blue")}"
                        red-label="${label("ski", "ski-centre.run.red")}"
                        black-label="${label("ski", "ski-centre.run.black")}"
                        orange-label="${label("ski", "ski-centre.run.orange")}"
                        grey-label="${label("ski", "ski-centre.run.grey")}"
                        status-open-label="${label("ski", "ski-centre.status.open")}"
                        status-opening-label="${label("ski", "ski-centre.status.expected-to-open")}"
                        status-closed-label="${label("ski", "ski-centre.status.closed")}"
                    >
                        <template v-slot:data-loading>
                            <div>
                                ${label("ski", "ski-data.loading")}
                            </div>
                        </template>
                        <template v-slot:js-required>
                            <div>
                                ${label("ski", "ski-data.ski-centre.no-js")}
                            </div>
                        </template>
                        <template v-slot:data-unavailable>
                            <div>
                                ${label("ski", "ski-data.unavailable")}
                            </div>
                        </template>

                        <template v-slot:centre-information>
                            <div>
                                <vs-heading
                                    level="3"
                                    override-style-level="6"
                                >
                                    ${label("ski", "ski-centre.centre-information")}
                                </vs-heading>
                                <vs-list unstyled>
                                    <#if module.phone??>
                                        <li class="mb-2">
                                            <vs-icon
                                                name="phone"
                                                size="xs"
                                                class="mr-2"
                                            ></vs-icon>
                                            ${module.phone}
                                        </li>
                                    </#if>
                                    <#if module.website.link??>
                                        <li class="mb-2">
                                            <vs-icon
                                                name="globe"
                                                size="xs"
                                                class="mr-2"
                                            ></vs-icon>
                                            <vs-link
                                                type="external"
                                                href="${module.website.link?eval}"
                                            >
                                                ${module.website.link?eval}
                                            </vs-link>
                                        </li>
                                    </#if>
                                    <#if module.address??>
                                        <li class="mb-2">
                                            <vs-icon
                                                name="map-marker"
                                                size="xs"
                                                class="mr-2"
                                            ></vs-icon>
                                            <@address module.address true />
                                        </li>
                                    </#if>
                                    <#if module.pisteMap??>
                                        <li class="mb-2">
                                            <vs-icon
                                                name="landscape"
                                                size="xs"
                                                class="mr-2"
                                            ></vs-icon>
                                            <vs-link
                                                type="download"
                                                href="${module.pisteMap}"
                                            >
                                                ${label("ski", "ski-centre.view-piste-map")}
                                            </vs-link>
                                        </li>
                                    </#if>
                                    <#if module.openingLink?? && module.openingLink.label??>
                                        <li class="mb-2">
                                            <vs-icon
                                                name="clock"
                                                size="xs"
                                                class="mr-2"
                                            ></vs-icon>
                                            <vs-link
                                                type="${module.openingLink.type}"
                                                href="${module.openingLink.link}"
                                            >
                                                ${module.openingLink.label}
                                            </vs-link>
                                        </li>
                                    </#if>
                                    <#if module.webcam??>
                                        <li class="mb-2">
                                            <vs-icon
                                                name="webcam"
                                                size="xs"
                                                class="mr-2"
                                            ></vs-icon>
                                            <span>${label("ski", "ski-centre.webcam")}</span>
                                        </li>
                                    </#if>
                                </vs-list>
                                <#list module.socialChannels as channel>
                                    <vs-link
                                        <#if channel.label?eval == 'Twitter'>
                                            <#if channel.link?contains("twitter.com")>
                                                href="${channel.link?eval}"
                                            <#else>
                                                href="https://twitter.com/${channel.link?eval}"
                                            </#if>
                                        <#else>
                                            href="${channel.link?eval}"
                                        </#if>
                                        class="d-inline-block mt-4"
                                    >
                                        <div class="d-inline-block mr-4">
                                            <span class="sr-only">${channel.label?eval}</span>
                                            <vs-icon
                                                size="sm"
                                                name="${channel.label?eval?lower_case}"
                                                <#switch channel.label?eval>
                                                    <#case 'Twitter'>
                                                        custom-colour="#55ACEE"
                                                    <#break>
                                                    <#case 'Facebook' >
                                                        custom-colour="#3A5A99"
                                                    <#break>
                                                    <#case 'Instagram'>
                                                        custom-colour="#E1306C"
                                                    <#break>
                                                </#switch>
                                            ></vs-icon>
                                        </div>
                                    </vs-link>
                                </#list>
                            </div>
                        </template>
                    </vs-ski-scotland-status>
                </vs-col>
            </vs-row>
        </vs-container>
        <vs-container class="mt-8">
            <vs-row>
                <vs-col
                    cols="6"
                    md="3"
                    offset-md="3"
                    lg="2"
                    offset-lg="4"
                    class="d-flex"
                    style="align-items: center;"
                >
                    <vs-img
                        src="https://static.visitscotland.com/img/ski-scotland/ski-scotland.png"
                        class="w-100 p-4"
                    />
                </vs-col>
                <vs-col
                    cols="6"
                    md="3"
                    lg="2"
                    class="d-flex"
                    style="align-items: center;"
                >
                    <vs-img
                        src="https://static.visitscotland.com/img/ski-scotland/cairngorm-mountain.svg"
                        class="w-100 p-4"
                    />
                </vs-col>
            </vs-row>
            <vs-row>
                <vs-col
                    cols="12"
                    md="6"
                    offset-md="3"
                >
                    <p>${label("ski", "ski-data.provider")}</p>
                </vs-col>
            </vs-row>
        </vs-container>
    </vs-module-wrapper>
</#macro>
