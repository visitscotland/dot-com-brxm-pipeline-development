<#include "../../../include/imports.ftl">
<#include "../../functions/helpers.ftl">
<#include "../../../frontend/components/vs-carbon-calculator.ftl">
<#include "../../../frontend/components/vs-module-wrapper.ftl">

<#macro experiencesCalculator >
    <#assign labels =  ResourceBundle.getAllLabels("experiences-calculator", locale) />

    <vs-module-wrapper
        class="text-left"
    >
        <vs-experiences-calculator
            :labels-map="{
                <#list labels?keys as key>
                    '${key}': '${escapeJSON(labels[key], false)}',
                </#list>
            }"
            language="${locale}"
        >
        </vs-experiences-calculator>
    </vs-module-wrapper>

</#macro>