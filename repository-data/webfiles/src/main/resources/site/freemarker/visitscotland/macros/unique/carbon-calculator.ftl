<#include "../../../frontend/components/vs-carbon-calculator.ftl">

<#macro carbonCalculator >
    <#assign labels =  ResourceBundle.getAllLabels("carbon-calculator", null) />

    <vs-carbon-calculator
        data-url="fixtures/carbon-calculator/example-form.json"
        messaging-url="fixtures/carbon-calculator/messaging.json"
        countryList-url="fixtures/carbon-calculator/countries.json"
        labels-map="{
            <#list labels?keys as key>
                '${key}': '${labels[key]}',
            </#list>
        }"
    >
    </vs-carbon-calculator>
</#macro>