<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="breadcrumb" type="org.onehippo.forge.breadcrumb.om.Breadcrumb" -->

<#if breadcrumb?? && breadcrumb.items??>
    <@hst.headContribution category="seo">
        <script type="application/ld+json">
        {
            "@context": "https://schema.org",
            "@type": "BreadcrumbList",
            "itemListElement": [{
                "@type": "ListItem",
                "position": 1,
                "item": {
                    "@id": "<@hst.link siteMapItemRefId="root" fullyQualified=true/>",
                    "name": "${label("essentials.global", "home")}"
                }
        <#assign count = 1>
        <#list breadcrumb.items as item>
            <#-- Avoid duplicated Home element (Document based pages skip this element)-->
            <#if item.link?? &&  item.link.hstSiteMapItem.id?? && item.link.hstSiteMapItem.id != "root">
                <@hst.link var="link" link=item.link fullyQualified=true/>
                <#assign count = count + 1>
            <#-- Keep the indentation -->
            },{
                "@type": "ListItem",
                "position": ${count},
                "item": {
                    "@id": "${link}",
                    "name": "${item.title?html}"
                }
            </#if>
        </#list>
            }]
        }
        </script>
    </@hst.headContribution>
</#if>