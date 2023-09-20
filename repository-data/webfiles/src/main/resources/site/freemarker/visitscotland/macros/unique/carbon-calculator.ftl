<#include "../../../include/imports.ftl">
<#include "../../../frontend/components/vs-carbon-calculator.ftl">
<#include "../../../frontend/components/vs-module-wrapper.ftl">

<#macro carbonCalculator >
    <#assign labels =  ResourceBundle.getAllLabels("carbon-calculator", null) />

    <vs-module-wrapper
        class="text-left"
    >
        <vs-carbon-calculator
            :labels-map="{
                <#list labels?keys as key>
                    '${key}': '${labels[key]}',
                </#list>
            }"
        >
        </vs-carbon-calculator>
    </vs-module-wrapper>

</#macro>