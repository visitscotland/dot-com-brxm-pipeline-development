<!doctype html>
<#include "../../include/imports.ftl">
<#include "../macros/global/gtm.ftl">
<#include "../macros/modules/skeleton/skeleton.ftl">
<#include "headerContributions.ftl">
<#include "footerContributions.ftl">

<#include "../../frontend/components/vs-button.ftl">

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
        <div data-vue-app-init>
            <vs-button
                href="#"
            >
                Test button
            </vs-button>
        </div>
        <@footerContributions />
    </body>
</html>