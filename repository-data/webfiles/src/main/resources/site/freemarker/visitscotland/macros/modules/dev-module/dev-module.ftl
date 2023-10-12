<#include "../../../../include/imports.ftl">
<#include "../../global/preview-warning.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.Module" -->

<#macro devModule module>
    <@previewWarning editMode module module.errorMessages />
    <#if module.hippoBean.freemarkerId??>
        <#switch module.hippoBean.freemarkerId>
            <#case "Carbon Calculator">
                <#include "../../unique/carbon-calculator.ftl">
                <@carbonCalculator />
            <#break >
            <#default>
                <@previewWarning editMode=editMode module=module message="The Freemarker module ${module.hippoBean.freemarkerId} has not been recognized" />
        </#switch>
    <#else>
        <#list module.hippoBean.headContributions as asset>
            <@hst.headContribution category="devModuleHead">
                <link rel="stylesheet" href="${asset}" type="text/css"/>
            </@hst.headContribution>
        </#list>
        ${module.hippoBean.html}
        <#list module.hippoBean.footerContributions as asset>
            <@hst.headContribution category="devModuleFooter">
                <script src="${asset}"></script>
            </@hst.headContribution>
        </#list>
    </#if>
</#macro>
