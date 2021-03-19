<#include "../../../../../include/imports.ftl">
<#include "../../../../../frontend/components/vs-megalink-single-image.ftl">
<#include "../../../../../frontend/components/vs-link.ftl">
<#include "../../../../../frontend/components/vs-link-list-item.ftl">

<#macro singleImage item theme>
    <#if item.image.cmsImage??>
        <#assign image>
            <@hst.link hippobean=item.image.cmsImage.original/>
        </#assign>
    <#else>
        <#assign image = item.image.externalImage!'' />
    </#if>

    <vs-col cols="12">
        <vs-megalink-single-image 
            title="${item.innerTitle}"
            img-src="${image}"
            <#if item.cta.link??>button-link="${item.cta.link}"</#if>
            <#if item.alignment == 'left'>alternate</#if>
            theme="${theme}"
        >
            <template slot="vsSingleImageCaption">
                ${item.image.description}
            </template>

            <template slot="vsSingleImageCredit">
                ${item.image.credit}
            </template>

            <template slot="vsSingleImageContent">
                <@hst.html hippohtml=item.innerIntroduction/>
            </template>

            <template slot="vsSingleImageLinks">
                <#list item.links as listItem>
                    <vs-link-list-item
                        href="${listItem.link}"
                        <#if listItem.type != "internal">type="${listItem.type}"</#if>
                        variant="${theme}"
                    >
                        ${listItem.label}
                    </vs-link-list-item>
                </#list>
            </template>
            
            <template slot="vsSingleImageButtonText">
                ${item.cta.label}
            </template>
            
        </vs-megalink-single-image>
    </vs-col>
</#macro>