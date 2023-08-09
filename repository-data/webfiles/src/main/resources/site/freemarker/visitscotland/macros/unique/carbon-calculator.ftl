<#macro carbonCalculator >
    lable ${ResourceBundle.getResourceBundle("carbon-calculator", "next", locale, false)}
    <#if ResourceBundle.getAllLabels("carbon-calculator", label)?keys>
fdsal
    </#if>
    <#assign labels =  ResourceBundle.getAllLabels("forms", null) />
    forms

                <#list labels?keys as key>
                    x
                    "${key}" : "${labels.get(key)}",
                </#list>
<#--    <VsCarbonForm-->
<#--            dataUrl="fixtures/carbon-calculator/example-form.json"-->
<#--            messagingUrl="fixtures/carbon-calculator/messaging.json"-->
<#--            countryListUrl="fixtures/carbon-calculator/countries.json"-->
<#--            labelsMap={-->
<#--                <#list labels?keys as key>-->
<#--                    "${key}" : "${labels.get(key)}",-->
<#--                </#list>-->
<#--            }-->
<#--    >-->
<#--    </VsCarbonForm>-->
</#macro>