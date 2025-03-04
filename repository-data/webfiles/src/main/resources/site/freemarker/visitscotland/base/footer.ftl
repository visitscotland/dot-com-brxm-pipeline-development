<#compress>
<#include "../../include/imports.ftl">
<#include "../../frontend/components/vs-footer.ftl">
<#include "../../frontend/components/vs-footer-nav-list.ftl">
<#include "../../frontend/components/vs-container.ftl">
<#include "../../frontend/components/vs-row.ftl">
<#include "../../frontend/components/vs-col.ftl">

<#include "../macros/modules/footer/footer-accordion-item.ftl">
<#include "../macros/modules/footer/footer-copyright.ftl">
<#include "../macros/modules/footer/footer-social-menu.ftl">

<#-- @ftlvariable name="menu" type="com.visitscotland.brxm.components.navigation.RootMenuItem" -->
<#-- @ftlvariable name="item" type="com.visitscotland.brxm.components.navigation.MenuItem" -->
</#compress>
<vs-footer class="has-edit-button">
    <#if menu??>
        <template v-slot:accordion-items>
            <@footerAccordionItem footerMenuItems=menu.siteMenuItems />
        </template>

        <template v-slot:social-menu>
            <@footerSocialMenu />
        </template>

        <@hst.cmseditmenu menu=menu/>

        <@hst.include ref="utility"/>
    </#if>

    <@footerCopyright />
</vs-footer>