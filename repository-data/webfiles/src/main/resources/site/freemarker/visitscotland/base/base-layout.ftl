<!doctype html>
<#include "../../include/imports.ftl">
<#include "../macros/global/gtm.ftl">
<#include "../macros/modules/skeleton/skeleton.ftl">
<#include "headerContributions.ftl">
<#include "footerContributions.ftl">

<html data-version="${version}" lang="${locale}">
    <head>
        <#if hstRequest.requestContext.channelManagerPreviewRequest>
            <link rel="stylesheet" href="<@hst.webfile path='/assets/css/cms-request.css'/>" type="text/css"/>
        </#if>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <@headContributions />
    </head>
    <body>
        <@gtm noscript=true />
        <@skeleton />
        <div data-vue-hydration-init><div data-vue-app-init>
            <@hst.include ref="top"/>

            <@hst.include ref="menu"/>

            <main id="main">
                <@hst.include ref="main"/>
            </main>

            <@hst.include ref="footer"/>
        </div></div>
        <@footerContributions />
    </body>
</html>