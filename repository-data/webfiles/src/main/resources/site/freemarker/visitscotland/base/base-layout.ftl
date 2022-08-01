<!doctype html>
<#include "../../include/imports.ftl">
<#include "../macros/global/gtm.ftl">
<#include "headerContributions.ftl">
<#include "footerContributions.ftl">
<#include "../../frontend/components/vs-tag-manager-wrapper.ftl">

<html data-version="${version}" lang="en">
    <head>
        <#if hstRequest.requestContext.channelManagerPreviewRequest>
            <link rel="stylesheet" href="<@hst.webfile path="/assets/css/cms-request.css"/>" type="text/css"/>
        </#if>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <@headContributions />
    </head>
    <body>
        <@gtm noscript=true />
        <div class="no-js" data-vue-app-init>
            <!-- payload prop to be updated by back end -->
            <vs-tag-manager-wrapper
                :payload="{
                    'page-category-1': 'PageCategoryTest1',
                    'page-category-2': 'PageCategoryTest2',
                    'page-category-3': '',
                    'user-country-setting': 'User Country Test',
                    'meta-data': 'Meta Data Test',
                }"
            ></vs-tag-manager-wrapper>
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