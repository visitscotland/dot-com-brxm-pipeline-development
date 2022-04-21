
<#macro otymlModule module editMode>
    <#if module?? >
        <#if module.getType()== "HorizontalListLinksModule" >
            <@horizontalList module />
        <#else>
            <@previewWarning editMode module module.errorMessages true "There is no valid links for the OTYML module and it has been removed from the page:"/>
        </#if>
    </#if>
</#macro>