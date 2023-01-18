<#include "../../../../include/imports.ftl">
<#include "../../global/preview-warning.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.Module" -->

<#macro devModule module>
    <@previewWarning editMode module module.errorMessages />
    <#list module.hippoBean.headContributions as asset>
        <@hst.headContribution category="devModuleHead">
            <link rel="stylesheet" href="${asset}" type="text/css"/>
        </@hst.headContribution>
       HEAD CONTRIBUTION -> ${asset}
    </#list>
    ${module.hippoBean.html}
    <#list module.hippoBean.footerContributions as asset>
        <@hst.headContribution category="devModuleFooter">
            <script src="${asset}"></script>
        </@hst.headContribution>
    </#list>
</#macro>