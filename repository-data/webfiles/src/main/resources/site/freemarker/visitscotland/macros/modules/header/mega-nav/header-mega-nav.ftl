<#include "../../../../../include/imports.ftl">
<#include "../../../../../frontend/components/vs-mega-nav.ftl">
<#include "./header-desktop-nav.ftl">
<#include "./header-mobile-nav.ftl">

<#macro headerMegaNav menu=menu>
    <vs-mega-nav 
        href="<@hst.link fullyQualified=fullyQualifiedURLs siteMapItemRefId='root'/>"
        menu-toggle-alt-text="${label('navigation.static', 'meganav-toggle-btn-alt-text')}"
    >
        <template slot="megaNavTopMenuItems">
            <@headerDesktopNav menu=menu/>
        </template>

        <template slot="megaNavMobileItems">
            <@headerMobileNav menu=menu/>
        </template>
    </vs-mega-nav>
</#macro>