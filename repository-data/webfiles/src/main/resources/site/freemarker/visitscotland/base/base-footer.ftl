<#compress>
<#include "../../include/imports.ftl">
<#include "../../frontend/components/vs-footer.ftl">
<#include "../../frontend/components/vs-footer-nav-list.ftl">
<#include "../../frontend/components/vs-container.ftl">
<#include "../../frontend/components/vs-row.ftl">
<#include "../../frontend/components/vs-col.ftl">
<#include "../../frontend/components/vs-cookie-checker.ftl">
<#include "../macros/modules/footer/footer-accordion-item.ftl">
<#include "../macros/modules/footer/footer-copyright.ftl">
<#include "../macros/modules/footer/footer-social-menu.ftl">

<#-- @ftlvariable name="menu" type="com.visitscotland.brxm.components.navigation.RootMenuItem" -->
<#-- @ftlvariable name="item" type="com.visitscotland.brxm.components.navigation.MenuItem" -->
</#compress>

<#if menu??>
    <vs-footer class="has-edit-button">
        <template slot="accordion-items">
            <@footerAccordionItem footerMenuItems=menu.siteMenuItems />
        </template>

        <template slot="social-menu">
            <@footerSocialMenu />
        </template>

        <@hst.cmseditmenu menu=menu/>

        <@hst.include ref="utility"/>

        <@footerCopyright />
    </vs-footer>
    <vs-cookie-checker></vs-cookie-checker>
<#elseif !integration??>
    <@log "The footer menu is not available" />
</#if>
