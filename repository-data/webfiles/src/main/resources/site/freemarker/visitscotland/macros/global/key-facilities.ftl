<#include "../../../frontend/components/vs-icon-list.ftl">
<#include "../../../frontend/components/vs-icon-list-item.ftl">

<#macro keyFacilities facilitiesList>
    <vs-icon-list title="${label('essentials.global', 'keyfacilities.title')}">
        <#list facilitiesList as facility>
            <vs-icon-list-item
                icon="${facility.id}"
                label="${facility.name}">
            </vs-icon-list-item>
        </#list>
    </vs-icon-list>
</#macro>

