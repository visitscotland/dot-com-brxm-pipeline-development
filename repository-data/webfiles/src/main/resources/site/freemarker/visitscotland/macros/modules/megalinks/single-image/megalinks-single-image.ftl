<#include "../../../../../include/imports.ftl">
<#include "../../../../../frontend/components/vs-megalink-single-image.ftl">
<#include "../../../../../frontend/components/vs-link.ftl">
<#include "../../../../../frontend/components/vs-link-list-item.ftl">
<#include "../../../global/image-with-caption.ftl">
<#include "../../video/video-modal.ftl">

<#macro singleImage item theme>
    <#if item.image.cmsImage??>
        <#assign image>
            <@hst.link hippobean=item.image.cmsImage.original/>
        </#assign>
    <#else>
        <#assign image = item.image.externalImage!'' />
    </#if>

    <#if theme == 'light'>
        <#assign linkVariant>primary</#assign>
    <#else>
        <#assign linkVariant>on-dark</#assign>
    </#if>

    <vs-col cols="12">
        <vs-megalink-single-image 
            title="${item.innerTitle}"
            <#if (item.cta.link)??>button-link="${item.cta.link}"</#if>
            <#if item.alignment == 'left'>alternate</#if>
            theme="${theme}"
        >
            <template slot="vsSingleImage">
                <@imageWithCaption imageSrc=image imageDetails=item.image mobileOverlap="true" alignment=item.alignment/>
            </template>

            <template slot="vsSingleImageContent">
                <@hst.html hippohtml=item.innerIntroduction/>
            </template>

            <template slot="vsSingleImageLinks">
                <#list item.links as listItem>
                    <vs-link-list-item
                        variant="${linkVariant}"

                        <#if listItem.youtubeId??>
                            type="video"
                            href="#"
                            video-id="${listItem.youtubeId}"
                            video-descriptor="${label('video', 'video.video-descriptor')}"
                        <#else>
                            href="${listItem.link}"
                            link-type="${listItem.type}"

                            <#if listItem.type != "internal">type="${listItem.type}"</#if>
                        </#if>
                    >
                        ${listItem.label}
                    </vs-link-list-item>

                    <#if listItem.youtubeId??>
                        <@videoModal videoId=listItem.youtubeId videoTitle=listItem.label />
                    </#if>
                </#list>
            </template>
            
            <template slot="vsSingleImageButtonText">
                <#if (item.cta.link)??>${item.cta.label}</#if>
            </template>
            
        </vs-megalink-single-image>
    </vs-col>
</#macro>