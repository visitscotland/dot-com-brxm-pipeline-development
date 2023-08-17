<#include "../../../../frontend/components/vs-tab-item.ftl">
<#include "../../../../frontend/components/vs-accordion.ftl">
<#include "../../../../frontend/components/vs-accordion-item.ftl">
<#include "../../../../frontend/components/vs-icon.ftl">
<#include "../../../../frontend/components/vs-heading.ftl">
<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.TravelInformationModuleTab" -->

<#macro travelInformationTab module>
    <vs-tab-item title="${module.title}">
        <div class="px-3 px-md-6 px-lg-9 px-xl-10 pt-0 pt-lg-2 pb-0 pb-md-5">
            <vs-accordion>
                <#list module.travelInformationTransportRows as row>
                    <vs-accordion-item 
                        :open-by-default="<#if row?is_first>true<#else>false</#if>" 
                        variant="transparent" 
                        control-id="accordion-item-${row.transport.key}-${row?index}"
                        class="<#if row?is_first>border-0</#if>"
                    >
                        <template v-slot:title>
                            <vs-icon name="${row.transport.key}" variant="dark" size="sm" class="mr-2"></vs-icon> 
                            ${row.transport.label}
                        </template>

                        <template v-slot:icon-open>
                            <vs-icon name="chevron" variant="dark" size="sm" />
                        </template>

                        <template v-slot:icon-closed>
                            <vs-icon name="chevron" orientation="down" variant="dark" size="sm" />
                        </template>

                        <div class="p-3">
                            <@hst.html hippohtml=row.copy/>
                        </div>
                    </vs-accordion-item>
                </#list>
            </vs-accordion>
        </div>
    </vs-tab-item>
</#macro>