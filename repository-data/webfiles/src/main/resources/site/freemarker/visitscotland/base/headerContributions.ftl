<#macro headContributions>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- BEGIN HEAD CONTRIBUTIONS: seo -->
    <@hst.headContributions categoryIncludes="seo" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: seo -->

    <!-- BEGIN HEAD CONTRIBUTIONS: opengraph -->
    <@hst.headContributions categoryIncludes="opengraph" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: opengraph -->

    <!-- BEGIN HEAD CONTRIBUTIONS: htmlHeadPreload -->
    <@hst.headContributions categoryIncludes="htmlHeadPreload" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: htmlHeadPreload -->

    <!-- BEGIN HEAD CONTRIBUTIONS: htmlHeadStyles -->
    <@hst.headContributions categoryIncludes="htmlHeadStyles" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: htmlHeadStyles -->

    <!-- BEGIN HEAD CONTRIBUTIONS: general -->
    <@hst.headContributions categoryExcludes="htmlHeadPreload,htmlHeadStyles,htmlBodyEndScriptsFirst,htmlBodyEndScripts,htmlBodyEndAppInit,seo,opengraph" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: general -->

    <style>
        body {
            visibility: hidden;
        }
    </style>
</#macro>