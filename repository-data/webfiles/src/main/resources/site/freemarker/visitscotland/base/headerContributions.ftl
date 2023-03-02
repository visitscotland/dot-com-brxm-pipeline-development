<#include "../macros/global/gtm.ftl">

<#macro headContributions>
    <#compress>
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
        <@hst.headContributions categoryExcludes="htmlHeadPreload,htmlHeadStyles,htmlBodyEndScriptsFirst,htmlBodyEndScripts,htmlBodyEndAppInit,htmlBodyEndScriptsLast,htmlBodyLocalizedScripts,seo,opengraph,devModuleHead,devModuleFooter" xhtml=true/>
        <@hst.headContributions categoryIncludes="devModuleHead" xhtml=true/>
        <!-- END HEAD CONTRIBUTIONS: general -->

        <link rel="icon" href="<@hst.webfile path="/favicon.ico"/>" sizes="any">
        <link rel="icon" href="<@hst.webfile path="/assets/favicons/favicon.svg"/>" type="image/svg+xml">
        <link rel="apple-touch-icon" href="<@hst.webfile path="/assets/favicons/apple-touch-icon-180-180.svg"/>">
        <link rel="manifest" href="<@hst.link path="manifest.webmanifest"/>">

        <style>
            .skeleton-site {
                display: block;
            }

            .hydrate {
                display: none;
            }
        </style>
    </#compress>
</#macro>