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

    <vs-col cols="12">
        <vs-megalink-single-image 
            title="${item.innerTitle}"
            <#if (item.cta.link)??>button-link="${item.cta.link}"</#if>
            <#if item.alignment?? && item.alignment == 'left'>alternate</#if>
        >
            <template v-slot:vs-single-image>
                <@imageWithCaption imageSrc=image imageDetails=item.image mobileOverlap="true" alignment=item.alignment/>
            </template>

            <template v-slot:vs-single-image-content>
                <@hst.html hippohtml=item.innerIntroduction/>
            </template>

            <template v-slot:vs-single-image-links>
                <#list item.links as listItem>
                    <vs-link-list-item
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
                        <@videoModal video=listItem/>
                    </#if>
                </#list>
            </template>
            
            <#if (item.cta.link)??>
                <template v-slot:vs-single-image-button-text>
                    ${item.cta.label}
                </template>
            </#if>            
        </vs-megalink-single-image>
    </vs-col>
</#macro>