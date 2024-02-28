<#include "../../../include/imports.ftl">
<#include "../../functions/helpers.ftl">
<#include "../../../frontend/components/vs-carbon-calculator.ftl">
<#include "../../../frontend/components/vs-module-wrapper.ftl">

<#macro carbonCalculator >
    <#assign labels =  ResourceBundle.getAllLabels("carbon-calculator", locale) />

    <vs-module-wrapper
        class="text-start"
    >
        <vs-carbon-calculator
            :labels-map="{
                <#list labels?keys as key>
                    '${key}': '${escapeJSON(labels[key], false)}',
                </#list>
            }"
            language="${locale}"
        >
        </vs-carbon-calculator>
    </vs-module-wrapper>

</#macro>