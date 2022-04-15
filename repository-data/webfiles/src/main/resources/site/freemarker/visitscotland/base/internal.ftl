<!doctype html>
<#include "../../include/imports.ftl">
<#include "headerContributions.ftl">
<#include "footerContributions.ftl">

<html lang="en">
<head>
    <#compress>
        <internal-css-header>
            <@headContributions />

            <#if version == "legacy">
                <!-- TODO: special headContribution -->
                <link rel="stylesheet" href="<@hst.link fullyQualified=fullyQualifiedURLs path='/webfiles/static/third-party/legacy-1.0.css' />" />
            </#if>
            <!-- end include -->
        </internal-css-header>
    </#compress>
</head>
<body>
<#compress>
    <internal-header>
        <div class="no-js" data-vue-app-init>
            <!-- <a href="https://www.visitscotland.com/user/loginredirect?returnurl=${loginredirectParameters}">(Not You?)</a> -->
            <@hst.include ref="top"/>

            <@hst.include ref="menu"/>

            <main id="main">
                <@hst.include ref="main" />
            </main>
        <!-- end include -->
    </internal-header>
</#compress>
<#compress>
    <internal-footer>
            <@hst.include ref="footer"/>
        </div>
        <!-- end include -->
    </internal-footer>
</#compress>
<#compress>
    <internal-script-footer>
        <@footerContributions />
        <!-- end include -->
    </internal-script-footer>
</#compress>
</body>
</html>
