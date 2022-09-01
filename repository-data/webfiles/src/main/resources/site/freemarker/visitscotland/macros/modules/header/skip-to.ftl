<#include "../../../../frontend/components/vs-skip-to.ftl">

<#macro headerSkipTo>
    <vs-skip-to>
        <template slot="skipToText">
            ${label('skip-to', 'skip-to.label')}
        </template>
        <template slot="mainMenuText">
            ${label('skip-to', 'skip-to.main-menu')}
        </template>
        <template slot="mainContentText">
            ${label('skip-to', 'skip-to.main-content')}
        </template>
        <template slot="searchText">
            ${label('skip-to', 'skip-to.search')}
        </template>
        <template slot="footerText">
            ${label('skip-to', 'skip-to.footer')}
        </template>
    </vs-skip-to>
</#macro>