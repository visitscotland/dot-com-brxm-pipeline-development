<#include "../../../frontend/components/vs-container.ftl">
<#include "../../../frontend/components/vs-alert.ftl">
<#include "../../../frontend/components/vs-list.ftl">

<#macro previewWarning editMode module errorMessages=[] hidden=false message="">
    <#if editMode && errorMessages?has_content >
        <vs-container
            class="py-4"
        >
            <vs-alert>
                <div>
                    <p class="text-danger pb-2"><strong>CMS ERROR! </strong></p>
                    <#if message?? && message != "">
                        <p>${message}</p>
                    <#elseif hidden>
                        <p>There is an issue with the document <strong>${module.hippoBean.displayName}</strong> and It was hidden from the page:</p>
                    <#else>
                        <p>The following issues have been detected in the document ${module.hippoBean.displayName}:</p>
                    </#if>
                    <vs-list>
                        <#list errorMessages as error>
                            <li>${error}</li>
                        </#list>
                    </vs-list>
                </div>
            </vs-alert>
        </vs-container>
    </#if>
</#macro>