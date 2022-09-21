<#include "../../../../frontend/components/vs-skip-to.ftl">

<#macro headerSkipTo>
    <vs-skip-to
        skip-to-text="${label('skip-to', 'skip-to.label')}"
    >
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