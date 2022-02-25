<#include "../../../frontend/components/vs-container.ftl">

<#macro cmsErrors errors editMode>
    <#if errors?? && errors?has_content && editMode>
        <vs-container
            class="py-4"
        >
            <#list errors as error>
                <h1
                    class="text-danger pb-2"
                    role="alert"
                >
                    ${error?upper_case}
                </h1>
            </#list>
        </vs-container>
    </#if>
</#macro>