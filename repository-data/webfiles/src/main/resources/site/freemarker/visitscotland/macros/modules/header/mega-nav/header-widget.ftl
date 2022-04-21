<#include "../../../../../include/imports.ftl">
<#include "header-featured-item.ftl">
<#include "../../../../../frontend/components/vs-mega-nav-featured-event.ftl">

<#macro headerWidget menu accordion=false>
    <#switch menu.type>
        <#case "FeaturedItem">
            <#list menu.links as link>
                <@headerFeaturedItem link link?index accordion />
            </#list>
            <#break>
        <#case "FeaturedEvent">
            <#if accordion=true>
                <template>
            <#else>
                <template slot="navFeaturedEvent">
            </#if>
                <vs-mega-nav-featured-event
                    source-url="${menu.apiUrl}"
                >
                </vs-mega-nav-featured-event>
            </template>
            <#break>
        <#default>
    </#switch>
</#macro>