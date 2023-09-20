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
        <template v-slot:nav-featured-item>
    <#else>
        <template v-slot:nav-featured-item-left>
    </#if>
        <vs-mega-nav-featured-item
            link="${item.link}"
            img-url="${imageSrc}"
            alt="${item.image.altText}"
        >
            <template v-slot:vs-featured-item-header>
                ${item.label}
            </template>

            <template v-slot:vs-featured-item-content>
                ${item.teaser}
            </template>

            <template v-slot:vs-featured-item-link>
                ${item.cta}
            </template>

        </vs-mega-nav-featured-item>
    </template>
</#macro>