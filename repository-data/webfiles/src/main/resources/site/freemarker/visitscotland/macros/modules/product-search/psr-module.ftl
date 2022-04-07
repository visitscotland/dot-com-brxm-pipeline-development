<#include "../../../../include/imports.ftl">

<#include "../../../../frontend/components/vs-psr-module.ftl">

<#include "../../shared/theme-calculator.ftl">


<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.PSModule" -->

<#macro productSearchWidget module="" position="bottom" >
    <#if module?has_content>

        

        <vs-psr-module
            :config-arr="[
                {'subSearchType': '${module.category.productTypes}'},
                <#if module.location??>
                    <#assign isPolygon = (module.location.type == "POLYGON")>
                    {'type': '${module.location.type}'}, <#-- TODO: Is this parameter required -->
                    {'${isPolygon?then('locpoly', 'locplace')}': '${module.location.key}'},
                </#if>
                {'domain' : '${module.domain}'},
                {'lang':'${locale[0..1]}'},
            ]"

            <#if position=="top" && themeCalculator(introTheme) != "light">
                class="mb-9 mb-lg-12"
            <#elseif position=="bottom">
                class="mt-9 mt-lg-12"
            </#if>
        >
            <template slot="vsModuleHeading">
                ${module.title}
            </template>

            <template slot="vsModuleIntro">
                ${module.description}
            </template>
        </vs-psr-module>
    </#if>
</#macro>