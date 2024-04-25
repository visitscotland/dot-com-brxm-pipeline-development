<#include "../modules/horizontal-list/horizontal-list.ftl">
<#include "preview-warning.ftl">

<#macro otymlModule module editMode>
    <#if module?? >
        <#if module.getType()== "HorizontalListLinksModule" >
            <@horizontalList module />
        <#else>
            <@previewWarning editMode module module.errorMessages true label("cms-message", "otyml.no-links")/>
        </#if>
    </#if>
</#macro>