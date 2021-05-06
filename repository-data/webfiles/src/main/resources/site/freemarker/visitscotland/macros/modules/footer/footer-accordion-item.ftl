<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-footer-accordion-item.ftl">
<#include "../../../../frontend/components/vs-footer-nav-list-item.ftl">
<#include "../../../../frontend/components/vs-icon.ftl">
<#include "../../../../frontend/components/vs-list.ftl">

<#macro footerAccordionItem footerMenuItems>
    <#list footerMenuItems as menuItem>
        <vs-col cols="12" md="4" lg="3">
            <#if menuItem.title?has_content>
                <#assign title>${menuItem.title?html}</#assign>
            </#if>
            <vs-footer-accordion-item
                :open-by-default="false" 
                title="${title}"
                variant="dark" 
                control-id="footer_accordion_item_${menuItem?index}" 
                class="<#if menuItem?is_first>border-left-0 pl-md-0</#if> <#if menuItem?is_last>border-bottom-0</#if>"
            >
                <span slot="icon-open">
                    <vs-icon name="chevron" variant="light" size="xs"></vs-icon>
                </span>

                <span slot="icon-closed">
                    <vs-icon name="chevron" orientation="right" variant="light" size="xs"></vs-icon>
                </span>

                <vs-list
                    unstyled
                    role="menu"
                >
                    <#list menuItem.childMenuItems as childItem>
                        <#if childItem.title?has_content>
                            <vs-footer-nav-list-item
                                href="${getUrl(childItem)}"
                                link-text="${childItem.title}"
                                type="<#if childItem.externalLink??>external<#else>default</#if>"
                            ></vs-footer-nav-list-item>
                        </#if>
                    </#list>
                </vs-list>
            </vs-footer-accordion-item>
         </vs-col>
    </#list>
</#macro>