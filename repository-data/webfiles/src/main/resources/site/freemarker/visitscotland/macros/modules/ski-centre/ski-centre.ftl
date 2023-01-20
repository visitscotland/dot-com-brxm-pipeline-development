<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-button.ftl">
<#include "../../../../frontend/components/vs-img.ftl">
<#include "../../../../frontend/components/vs-rich-text-wrapper.ftl">
<#include "../../global/address.ftl">
<#include "../../global/preview-warning.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.SkiModule" -->
<#macro skiCentre module>
    <@previewWarning editMode module module.errorMessages />

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