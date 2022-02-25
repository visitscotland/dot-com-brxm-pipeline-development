<!doctype html>
<#include "../../include/imports.ftl">
<#include "headerContributions.ftl">
<#include "footerContributions.ftl">

<html data-version="${version}" lang="en">
    <head>
        <#if hstRequest.requestContext.channelManagerPreviewRequest>
            <link rel="stylesheet" href="<@hst.webfile  path="/assets/css/cms-request.css"/>" type="text/css"/>
        </#if>

        <@headContributions />
    </head>
    <body>
        <div class="no-js" data-vue-app-init>
            <@hst.include ref="top"/>

            <@hst.include ref="menu"/>

            <main id="main">
                <@hst.include ref="main"/>
            </main>

            <@hst.include ref="footer"/>
        </div>

        <@footerContributions />
    </body>
</html>