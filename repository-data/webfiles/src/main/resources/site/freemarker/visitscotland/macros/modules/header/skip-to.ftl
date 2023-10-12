<#include "../../../../frontend/components/vs-skip-to.ftl">

<#macro headerSkipTo>
    <vs-skip-to
        skip-to-text="${label('skip-to', 'skip-to.label')}"
    >
        <template v-slot:main-menu-text>
            ${label('skip-to', 'skip-to.main-menu')}
        </template>
        <template v-slot:main-content-text>
            ${label('skip-to', 'skip-to.main-content')}
        </template>
        <template v-slot:search-text>
            ${label('skip-to', 'skip-to.search')}
        </template>
        <template v-slot:footer-text>
            ${label('skip-to', 'skip-to.footer')}
        </template>
    </vs-skip-to>
</#macro>