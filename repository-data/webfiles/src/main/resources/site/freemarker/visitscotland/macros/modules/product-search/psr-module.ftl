<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-product-search.ftl">
<#include "../../shared/theme-calculator.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.PSModule" -->

<#macro productSearchWidget module="" position="bottom" >
    <#if module?has_content>
        <vs-product-search
            default-prod="${module.category.productTypes}"
            default-locale="${locale[0..1]}"
            no-js-message="${label('product-search-widget', 'no-js-message')}"
            <#if module.location??>
                default-location="${module.location.key}"
            </#if>
            <#if module.position=="top" && themeCalculator(introTheme) != "light">
                class="mb-9 mb-lg-12 pt-9"
            <#elseif module.position=="bottom">
                class="mt-9 mt-lg-12"
            </#if>
        >
            <template v-slot:vs-module-heading>
                ${module.title}
            </template>

            <template v-slot:vs-module-intro>
                ${module.description}
            </template>
        </vs-product-search>
    </#if>
</#macro>
