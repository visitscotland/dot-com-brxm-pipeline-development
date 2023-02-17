<#include "../../../../../include/imports.ftl">
<#include "../../../../../frontend/components/vs-accordion.ftl">
<#include "../../../../../frontend/components/vs-mega-nav-accordion-item.ftl">
<#include "../../../../../frontend/components/vs-mega-nav-list.ftl">
<#include "../../../../../frontend/components/vs-mega-nav-list-item.ftl">
<#include "header-widget.ftl">

<#macro headerAccordionNav menu=menu>
    <vs-accordion>
        <#list menu.siteMenuItems as item>
            <#if item.title?has_content>
                <#if !(item.cta??)>
                    <@log "The Top Navigation element "+ item.title +
                        " has not defined a link  " />
                </#if>
                <vs-mega-nav-accordion-item
                    title="${item.title}"
                    level="1"
                    control-id="${item?index}"
                    cta-link="${getUrl(item)}"
                    cta-text="${item.cta!''}"
                    @click.native='$root.$emit("navAccordionClick", "${item.title}")'
                >
                    <#list item.childMenuItems as childItem>
                        <#if childItem.title??>
                            <vs-mega-nav-accordion-item
                                title="${childItem.title}"
                                level="2"
                                control-id="${childItem?index}"
                                @click.native='$root.$emit("navAccordionClick", "${item.title}")'
                            >
                                <vs-mega-nav-list>
                                    <#list childItem.childMenuItems as thirdChildItem>
                                        <#if thirdChildItem.title??>
                                            <vs-mega-nav-list-item
                                                slot="navListItems"
                                                href="${getUrl(thirdChildItem)}"                                                
                                            >   
                                                ${thirdChildItem.title}
                                            </vs-mega-nav-list-item>
                                        </#if>
                                    </#list>

                                    <#if childItem.cta?? && childItem.hstLink??>
                                        <vs-mega-nav-list-item
                                            href="${getUrl(childItem)}"
                                            subheading-link
                                            slot="navHeadingCtaLink"
                                        >
                                            ${childItem.cta}
                                        </vs-mega-nav-list-item>
                                    </#if>
                                </vs-mega-nav-list>
                            </vs-mega-nav-accordion-item>
                        </#if>
                    </#list>  
                    <#if item.widget?? >
                        <@headerWidget item.widget true />
                    </#if>
                </vs-mega-nav-accordion-item>
            </#if>
        </#list>
    </vs-accordion>
</#macro>