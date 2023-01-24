<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-ski-scotland-status.ftl">
<#include "../../../../frontend/components/vs-heading.ftl">
<#include "../../../../frontend/components/vs-icon.ftl">
<#include "../../../../frontend/components/vs-link.ftl">
<#include "../../global/address.ftl">
<#include "../../global/preview-warning.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.SkiModule" -->
<#macro skiCentre module themeName="">
    <@previewWarning editMode module module.errorMessages />

    <vs-module-wrapper theme="<#if themeName?has_content>${themeName}<#else>light</#if>">
        <template slot="vsModuleWrapperHeading">
            ${module.title}
        </template>

        <template slot="vsModuleWrapperIntro">
            <@hst.html hippohtml=module.introduction/>
        </template>

        <vs-ski-scotland-status
            ski-status-url="http://glencoe.infonet-online.fr/json/snowreport.json"
            locale="en-gb"
        >
            <div slot="data-loading">
                Data is currently loading, please wait...
            </div>
            <div slot="js-required">
                JavaScript is required to load more ski data.
            </div>

            <div slot="centre-information">
                <vs-heading
                    level="3"
                    override-style-level="6"
                >
                    ${label("ski", "ski-centre.centre-information")}
                </vs-heading>
                <#if module.phone??>
                    <div class="mb-2">
                        <vs-icon
                            name="phone"
                            size="xs"
                            class="mr-2"
                        ></vs-icon>
                        ${module.phone}
                    </div>
                </#if>
                <#if module.website.link??>
                    <div class="mb-2">
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
                    </div>
                </#if>
                <#if module.address??>
                    <div class="mb-2">
                        <vs-icon
                            name="map-marker"
                            size="xs"
                            class="mr-2"
                        ></vs-icon>
                        <@address module.address true />
                    </div>
                </#if>
                <#if module.pisteMap??>
                    <div class="mb-2">
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
                    </div>
                </#if>
                <#if module.openingLink?? && module.openingLink.label??>
                    <div class="mb-2">
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
                    </div>
                </#if>
                <#if module.webcam??>
                    <div class="mb-2">
                        <vs-icon
                            name="webcam"
                            size="xs"
                            class="mr-2"
                        ></vs-icon>
                        <span>${label("ski", "ski-centre.webcam")}</span>
                    </div>
                </#if>
                <#list module.socialChannels as channel>
                    <vs-link
                        href="${channel.link?eval}"
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
        </vs-ski-scotland-status>
    </vs-module-wrapper>

<!--
    TITLE : ${module.title}
    INTRO : <@hst.html hippohtml=module.introduction/>

    FEED: Config:
        URL - ${module.feedURL}
        Timeout - ${property("ski.timeout")}
        locale - ${locale}
        MESSAGES
            ${label("ski", "ski-centre.run-lift-status")} // Run/Lift Status
            <#-- Note that ski-centre.summary.open and ski-centre.status.open have the same value but they have different
                purposes. In other languages the texts don't match-->
            <#-- "ski-centre.summary.*" Are to be used in the "Run/Lift Status" section -->
            ${label("ski", "ski-centre.summary.open")} //Open
            ${label("ski", "ski-centre.summary.closed")} //Closed
            ${label("ski", "ski-centre.summary.expected-to-open")} //Expected to Open
            ${label("ski", "ski-centre.summary.on-hold")} //On Hold
            ${label("ski", "ski-centre.summary.limited-patrol")} //Limited Patrol
            ${label("ski", "ski-centre.summary.open-soon")} //Open soon
            ${label("ski", "ski-centre.status")} // Status
            ${label("ski", "ski-centre.lifts")} // Runs
            ${label("ski", "ski-centre.lifts")} // Lifts
            ${label("ski", "ski-centre.detailed-status")} // Detailed Status
            <#-- "ski-centre.status.*" Are to be used in the "Detailed Status" section -->
            ${label("ski", "ski-centre.status.open")} // Open
            ${label("ski", "ski-centre.status.closed")} // Closed
            ${label("ski", "ski-centre.status.expected-to-open")} // Expected to Open
            ${label("ski", "ski-centre.status.on-hold")} // On Hold
            ${label("ski", "ski-centre.status.limited-patrol")} // Limited Patrol
            ${label("ski", "ski-centre.status.open-soon")} // Open soon
            ${label("ski", "ski-centre.run.green")} // Green
            ${label("ski", "ski-centre.run.easy")} // Easy
            ${label("ski", "ski-centre.run.blue")} // Blue
            ${label("ski", "ski-centre.run.intermediate")} // Intermediate
            ${label("ski", "ski-centre.run.red")} // Red
            ${label("ski", "ski-centre.run.difficult")} // Difficult
            ${label("ski", "ski-centre.run.black")} // Black
            ${label("ski", "ski-centre.run.very-difficult")} // Very Difficult
            ${label("ski", "ski-centre.run.orange")} // Orange
            ${label("ski", "ski-centre.run.itineraries")} // Itineraries
            ${label("ski", "ski-centre.run.back-country")} // Back Country
            ${label("ski", "ski-centre.run.grey")} // Grey
            ${label("ski", "ski-centre.run.other")} // Other
            ${label("ski", "ski-centre.lift-status")} // Lift Status
            ${label("ski", "ski-centre.run-status")} // Run Status
            ${label("ski", "ski-centre.lift")} // Lift
            ${label("ski", "ski-centre.centre-information")} // Centre Information
            ${label("ski", "ski-centre.piste-map")} // Piste Map
            ${label("ski", "ski-centre.webcam")} // WebCam
            ${label("ski", "ski-centre.view-piste-map")} // View Piste Map
            ${label("ski", "ski-centre.more-details")} // More Details
            ${label("ski", "ski-centre.last-updated")} // Last updated
            ${label("ski", "ski-data.no-js")} // JavaScript needs to be enabled to see Run and Lift status
            ${label("ski", "ski-data.ski-centre.no-js")} // JavaScript needs to be enabled to see the latest conditions at this ski centre
            ${label("ski", "ski-data.loading")} // Data is currently loading, please wait…
            ${label("ski", "ski-data.unavailable")} // Data is currently unavailable, please try again later.
            ${label("ski", "ski-centre.snow-conditions.news-from-the-slopes")} // News from the slopes
            ${label("ski", "ski-centre.snow-conditions.weather")} // Weather
            ${label("ski", "ski-centre.snow-conditions.current-weather")} // Current Weather
            ${label("ski", "ski-centre.snow-conditions.weather-forecast")} // Weather Forecast
            ${label("ski", "ski-centre.snow-conditions.full-report")} // Snow conditions full report
            ${label("ski", "ski-centre.snow-conditions.road-status")} // Road Status
            ${label("ski", "ski-data.provider")}


            CENTRE INFORMATION
                - PHONE - ${module.phone}
                - URL - ${module.website.link?eval}
                - ADDRESS - <@address module.address true />
                - PISTE MAP - ${module.pisteMap}
                - OPENING TIMES
                    - LABEL - ${module.openingLink.label}
                    - LINK - ${module.openingLink.link}
                    - TYPE - ${module.openingLink.type}
                - SOCIAL CHANNELS
            <#list module.socialChannels as channel>
                - LINK - ${channel.link?eval}
                - ICON - (${channel.label?eval}) - //Possible values (Twitter, Facebook, Instagram)
            </#list>
-->


</#macro>