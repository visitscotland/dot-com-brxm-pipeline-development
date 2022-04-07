<#include "../../../../../frontend/components/vs-mega-nav-featured-item.ftl">
<#include "../../../../../include/imports.ftl">

<#macro headerFeaturedItem item index accordion>
    <#assign imageSrc=""/>
    <#if item.image.cmsImage??>
        <#assign imageSrc>
            <@hst.link hippobean=item.image.cmsImage.original/>
        </#assign>
    <#else>
        <#assign imageSrc = item.image.externalImage!'' />
    </#if>
    <#if accordion=true>
        <template>
    <#elseif index = 0>
        <template slot="navFeaturedItem">
    <#else>
        <template slot="navFeaturedItemLeft">
    </#if>
        <vs-mega-nav-featured-item
            link="${item.link}"
            img-url="${imageSrc}"
            alt="${item.image.altText}"
            align=""
        >
            <template slot="vsFeaturedItemHeader">
                ${item.label}
            </template>

            <template slot="vsFeaturedItemContent">
                ${item.teaser}
            </template>

            <template slot="vsFeaturedItemLink">
                ${item.cta}
            </template>

        </vs-mega-nav-featured-item>
    </template>
</#macro>