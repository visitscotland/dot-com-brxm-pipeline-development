<#compress>
<#include "../../include/imports.ftl">
<#include "../../frontend/components/vs-footer-utility-list.ftl">
<#include "../../frontend/components/vs-footer-nav-list-item.ftl">

<#-- @ftlvariable name="menu" type="com.visitscotland.brxm.components.navigation.RootMenuItem" -->
<#-- @ftlvariable name="item" type="com.visitscotland.brxm.components.navigation.MenuItem" -->

</#compress>
<#if menu??>
    <div class="has-edit-button">
        <#list menu.siteMenuItems as item>
            <vs-footer-utility-list>
                <#list item.childMenuItems as childItem>
                    <#if childItem.title?has_content>
                        <vs-footer-nav-list-item
                            href="${getUrl(childItem)}"
                            link-text="${childItem.title}"
                            type="<#if childItem.externalLink??>external<#else>none</#if>"
                        ></vs-footer-nav-list-item>
                    </#if>
                </#list>
            </vs-footer-utility-list>
        </#list>

        <@hst.cmseditmenu menu=menu/>
    </div>
</#if>


