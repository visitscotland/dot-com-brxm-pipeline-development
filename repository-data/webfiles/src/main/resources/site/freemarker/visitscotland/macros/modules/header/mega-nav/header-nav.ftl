<#include "../../../../../include/imports.ftl">
<#include "../../../../../frontend/components/vs-mega-nav-top-menu-item.ftl">
<#include "../../../../../frontend/components/vs-mega-nav-list.ftl">
<#include "../../../../../frontend/components/vs-mega-nav-list-item.ftl">
<#include "header-widget.ftl">

<#macro headerDesktopNav menu=menu>
    <#list menu.siteMenuItems as item>

        <#if item.title?has_content>
            <vs-mega-nav-top-menu-item
                    href="${getUrl(item)}"
                    cta-text="<#if item.cta??>${item.cta}<#else></#if>"
            >
                <template v-slot:button-content>
                    ${item.title}
                </template>

                <template v-slot:dropdown-content>
                    <#list item.childMenuItems as childItem>
                        <#if childItem.title??>
                            <vs-mega-nav-list 
                                list-heading="${childItem.title}"
                            >
                                <template v-slot:nav-list-items>
                                    <#list childItem.childMenuItems as thirdChildItem>
                                        <#if thirdChildItem.title??>
                                            <vs-mega-nav-list-item
                                                href="${getUrl(thirdChildItem)}"
                                            >
                                                ${thirdChildItem.title}
                                            </vs-mega-nav-list-item>
                                        </#if>
                                    </#list>
                                </template>
                                <#if childItem.cta?? && childItem.hstLink??>
                                    <vs-mega-nav-list-item
                                        href="${getUrl(childItem)}"
                                        subheading-link
                                        v-slot:nav-heading-cta-link
                                    >
                                        ${childItem.cta}
                                    </vs-mega-nav-list-item>
                                </#if>
                            </vs-mega-nav-list>
                        </#if>
                    </#list>
                </template>

                <#if item.widget?? >
                    <@headerWidget item.widget />
                </#if>
            </vs-mega-nav-top-menu-item>
        </#if>
    </#list>

</#macro>