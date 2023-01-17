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
        MESSAGES
            - loading - ${label("ski", "ski-data.loading")}
            - no-js - ${label("ski", "ski-data.no-js")}
            - unavailable - ${label("ski", "ski-data.unavailable")}
        DATA PROVIDER -  ${label("ski", "ski-data.provider")}

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