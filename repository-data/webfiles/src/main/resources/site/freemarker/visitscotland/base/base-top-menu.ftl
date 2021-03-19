<#compress>
<#include "../../include/imports.ftl">

<#include "../../frontend/components/vs-header.ftl">

<#include "../macros/modules/header/mega-nav/header-mega-nav.ftl">
<#include "../macros/modules/header/header-global-menu.ftl">

<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#-- @ftlvariable name="item" type=""com.visitscotland.www.components.navigation.VsMenuItem" -->
<#-- @ftlvariable name="editMode" type="java.lang.Boolean"-->

</#compress>
<#if menu??>
    <div class="has-edit-button">
        <vs-header>
            <template slot="globalMenu">
                <@headerGlobalMenu />
            </template>
            <template slot="megaNav">
                <@headerMegaNav menu=menu/>
            </template>
        </vs-header>

        <@hst.cmseditmenu menu=menu />
    </div>
</#if>