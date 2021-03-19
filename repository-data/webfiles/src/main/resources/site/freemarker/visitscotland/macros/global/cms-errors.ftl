<#macro cmsErrors errors editMode>
    <#if errors?? && errors?has_content && editMode>
        <#list errors as error>
            <h1 class="text-danger">${error?upper_case}</h1>
        </#list>
    </#if>
</#macro>