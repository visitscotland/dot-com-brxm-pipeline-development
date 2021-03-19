<#macro footerContributions>
    <!-- BEGIN HEAD CONTRIBUTIONS: htmlBodyEndScriptsFirst -->
    <@hst.headContributions categoryIncludes="htmlBodyEndScriptsFirst" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: htmlBodyEndScriptsFirst -->

    <!-- BEGIN HEAD CONTRIBUTIONS: htmlBodyEndScripts -->
    <@hst.headContributions categoryIncludes="htmlBodyEndScripts" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: htmlBodyEndScripts -->

    <!-- BEGIN HEAD CONTRIBUTIONS: htmlBodyEndAppInit -->
    <@hst.headContributions categoryIncludes="htmlBodyEndAppInit" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: htmlBodyEndAppInit -->

    <style>
        body {
            visibility: visible;
        }
    </style>
</#macro>