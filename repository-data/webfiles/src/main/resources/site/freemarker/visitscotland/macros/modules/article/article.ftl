<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-article.ftl">
<#include "../../../../frontend/components/vs-video-caption.ftl">

<#include "../../../macros/modules/video/video.ftl">

<#include "../../global/image-with-caption.ftl">
<#include "../../global/preview-warning.ftl">

<#include "article-section.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.ArticleModule" -->
<#-- @ftlvariable name="section" type="com.visitscotland.brxm.model.ArticleModuleSection" -->
<#-- @ftlvariable name="editMode" type="java.lang.Boolean"-->

<#macro article module>
    <@hst.manageContent hippobean=module.hippoBean />
    <@previewWarning editMode module module.errorMessages />

    <#if module.image??>
        <#if module.image.cmsImage??>
            <#assign image>
                <@hst.link hippobean=module.image.cmsImage.original/>
            </#assign>
        <#else>
            <#assign image = module.image.externalImage!'' />
        </#if>
    <#else>
        <#assign image = "" />
    </#if>

    <vs-article
        title="${module.title}"
        anchor-link="<#if module.anchor?has_content>${module.anchor}</#if>"
    >
        <#if module.video?? >
            <template slot="vsArticleImg">
                <@video video=module.video />
                <vs-video-caption
                    video-id="${module.video.youtubeId}"
                >
                    <template slot="video-title">
                        ${module.video.cta}
                    </template>?
                </vs-video-caption>
            </template>
        <#elseif image?? && image?has_content>
            <template slot="vsArticleImg">
                <@imageWithCaption imageSrc=image imageDetails=module.image />
            </template>
        </#if>

        <template slot="vsArticleIntro">
            <@hst.html hippohtml=module.introduction/>
        </template>

        <#assign i = 0/>
        <#assign alignSidebar = ""/>

        <#list module.sections as section>
            <#if section.video??>
            <#-- TODO video in section -->
            </#if>
            <#if section.quote?? || section.image??>
                <#assign i++ />
                <#if i % 2 != 0 >
                    <#assign alignSidebar = "left" />
                <#else>
                    <#assign alignSidebar = "right" />
                </#if>
            <#else>
                <#assign alignSidebar = "right" />
            </#if>

            <@articleSection section=section alignSidebar=alignSidebar />
         </#list>        
    </vs-article>
</#macro>