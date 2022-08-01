<#include "../macros/global/gtm.ftl">

<#macro headContributions>
    <@gtm />
    <#if !integration>
        <!-- BEGIN HEAD CONTRIBUTIONS: seo -->
        <@hst.headContributions categoryIncludes="seo" xhtml=true/>
        <!-- END HEAD CONTRIBUTIONS: seo -->

        <!-- BEGIN HEAD CONTRIBUTIONS: opengraph -->
        <@hst.headContributions categoryIncludes="opengraph" xhtml=true/>
        <!-- END HEAD CONTRIBUTIONS: opengraph -->
    </#if>

    <!-- BEGIN HEAD CONTRIBUTIONS: htmlHeadPreload -->
    <@hst.headContributions categoryIncludes="htmlHeadPreload" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: htmlHeadPreload -->

    <!-- BEGIN HEAD CONTRIBUTIONS: htmlHeadStyles -->
    <@hst.headContributions categoryIncludes="htmlHeadStyles" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: htmlHeadStyles -->

    <!-- BEGIN HEAD CONTRIBUTIONS: general -->
    <@hst.headContributions categoryExcludes="htmlHeadPreload,htmlHeadStyles,htmlBodyEndScriptsFirst,htmlBodyEndScripts,htmlBodyEndAppInit,seo,opengraph" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: general -->

    <link rel="icon" type="image/png" href="<@hst.webfile path="/assets/images/32x32.png"/>" sizes="32x32">

    <style>
        body {
            visibility: hidden;
        }
    </style>
</#macro>