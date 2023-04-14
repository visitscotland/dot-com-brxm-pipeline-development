<#compress>
    <#include "../../../include/imports.ftl">
    <#include "../headerContributions.ftl">
    <#include "../base-footer.ftl">
    <#include "../base-top-menu.ftl">
    <#include "../utility-footer.ftl">

    <#assign integration=true>

    <#if legacy?? && (legacy != "false")>
        <link rel="stylesheet" href="<@hst.link fullyQualified=fullyQualifiedURLs path='/webfiles/static/third-party/embed/processed-styles/legacy.css' />" />
        <link rel="stylesheet" href="<@hst.link fullyQualified=fullyQualifiedURLs path='/webfiles/static/third-party/embed/processed-styles/cookie-banner.css' />" />
    </#if>

    <@headContributions />
    <style>
        .imported-footer-hide {
            display: none;
        }
    </style>
    <!-- end include -->
</#compress>