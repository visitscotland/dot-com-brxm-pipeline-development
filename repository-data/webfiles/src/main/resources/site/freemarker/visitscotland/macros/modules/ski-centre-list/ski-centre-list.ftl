<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-button.ftl">
<#include "../../../../frontend/components/vs-img.ftl">
<#include "../../../../frontend/components/vs-rich-text-wrapper.ftl">
<#include "../../global/preview-warning.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.SkiListModule" -->
<#macro skiCentreList module>
    <@previewWarning editMode module module.errorMessages />
    <!-- TODO THIS IS JUST A PLACE HOLDER

    TITLE : ${module.title}
    INTRO : ${module.introduction?html}

    <#list module.skiCentres as centre>
    - CENTRE = ${centre.title}
        - JSON = ${centre.feedURL}
        - PISTE_MAP = ${centre.pisteMap}
        - CMS_PAGE = ${centre.cmsPage.link}
    </#list>

    CONFIGURATION
        Timeout - ${property("ski.timeout")}
        locale - ${locale}

    LABELS FOR THE CARDS
            ${label("ski", "ski-centre.status")} // Status
            ${label("ski", "ski-centre.lifts")} // Runs
            ${label("ski", "ski-centre.lifts")} // Lifts
            ${label("ski", "ski-centre.summary.open")} //Open
            ${label("ski", "ski-centre.summary.closed")} //Closed
            ${label("ski", "ski-centre.summary.expected-to-open")} //Expected to Open
            ${label("ski", "ski-centre.summary.on-hold")} //On Hold
            ${label("ski", "ski-centre.summary.limited-patrol")} //Limited Patrol
            ${label("ski", "ski-centre.summary.open-soon")} //Open soon
            ${label("ski", "ski-centre.view-piste-map")} // View Piste Map
            ${label("ski", "ski-centre.more-details")} // More Details
            ${label("ski", "ski-centre.last-updated")} // Last updated
            ${label("ski", "ski-data.no-js")} // JavaScript needs to be enabled to see Run and Lift status
            ${label("ski", "ski-data.loading")} // Data is currently loading, please wait…
            ${label("ski", "ski-data.unavailable")} // Data is currently unavailable, please try again later.
    -->
</#macro>