<#macro carbonCalculator >
    <#assign labels =  ResourceBundle.getAllLabels("carbon-calculator", null) />
    <VsCarbonForm
            dataUrl="fixtures/carbon-calculator/example-form.json"
            messagingUrl="fixtures/carbon-calculator/messaging.json"
            countryListUrl="fixtures/carbon-calculator/countries.json"
            labelsMap={
                <#list labels?keys as key>
                    "${key}" : "${labels[key]}",
                </#list>
            }
    >
    </VsCarbonForm>
</#macro>