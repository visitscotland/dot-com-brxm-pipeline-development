<#include "../../../include/imports.ftl">
<#include "../../functions/helpers.ftl">
<#include "../../../frontend/components/vs-experiences-calculator.ftl">
<#include "../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../frontend/components/vs-row.ftl">
<#include "../../../frontend/components/vs-col.ftl">


<#macro experiencesCalculator>
    <#assign labels =  ResourceBundle.getAllLabels("experiences-calculator", locale) />

    <vs-module-wrapper class="text-left">   
        <vs-container>
            <vs-row>
                <vs-col 
                    cols="12"
                    lg="10"
                    offset-lg="1"
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
                </vs-col>
            </vs-row>
        </vs-container>
    </vs-module-wrapper>
</#macro>
