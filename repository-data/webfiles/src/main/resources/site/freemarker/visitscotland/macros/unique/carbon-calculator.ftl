<#include "../../../../include/imports.ftl">
<#include "../../../frontend/components/vs-carbon-calculator.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-row.ftl">

<#macro carbonCalculator >
    <#assign labels =  ResourceBundle.getAllLabels("carbon-calculator", null) />

    <vs-module-wrapper>
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