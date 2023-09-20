<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-tabs.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">

<#include "travel-information-tab.ftl">
<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.TravelInformationModule" -->

<#macro travelInformation module>
    <vs-module-wrapper theme="dark">
        <template v-slot:vs-module-wrapper-heading>
            ${module.title}
        </template>

        <template v-slot:vs-module-wrapper-intro>
            <@hst.html hippohtml=module.copy/>
        </template>

        <vs-container>
            <vs-row>
                <vs-col cols="12" sm="10" offset-sm="1">
                    <vs-tabs>
                        <@travelInformationTab module.gettingTo />
                        <@travelInformationTab module.gettingAround />
                    </vs-tabs>
                </vs-col>
            </vs-row>
        </vs-container>
    </vs-module-wrapper>
</#macro>