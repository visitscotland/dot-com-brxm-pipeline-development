<#include "../../../../frontend/components/vs-article-sidebar.ftl">
<#include "../../../macros/modules/modal/modal.ftl">
<#include "../../../macros/modules/video/video.ftl">
<#include "../../global/quote.ftl">

<#macro articleSidebar section alignSidebar>
    <vs-article-sidebar sidebar-align="${alignSidebar}">
        <#if section.video??>
            <#if section.video.image.cmsImage??>
                <#assign media>
                    <@hst.link hippobean=section.video.image.cmsImage.original/>
                </#assign>
            <#else>
                <#assign media = section.video.image.externalImage!'' />
            </#if>

            <template v-slot:vs-article-sidebar-img>
                <@modal
                    modalId="${section.video.youtubeId}"
                    closeBtnText="${label('essentials.global', 'close')}"
                    isVideoModal="true"
                >
                    <vs-row>
                        <vs-col cols="12">
                            <@video video=section.video />
                        </vs-col>
                    </vs-row>

                    <vs-row class="mt-8">
                        <vs-col
                            cols="10"
                            offset="1"
                        >
                            <vs-rich-text-wrapper>
                                <p>${section.video.teaser}</p>
                            </vs-rich-text-wrapper>
                        </vs-col>
                    </vs-row>
                </@modal>
                <#if section.video.label??>
                    <#assign videoTitle = section.video.label />
                <#else>
                    <#assign videoTitle = label('video', 'video.play-btn') />
                </#if>
                <@imageWithCaption
                    imageSrc=media
                    imageDetails=section.video.image
                    isVideo="true"
                    videoId="${section.video.youtubeId}"
                    videoTitle="${videoTitle}"
                    smallPlayButton="true"
                    showToggle="false"
                />
            </template>
        <#elseif section.image??>
            <#if section.image.cmsImage??>
                <#assign media>
                    <@hst.link hippobean=section.image.cmsImage.original/>
                </#assign>
            <#else>
                <#assign media = section.image.externalImage!'' />
            </#if>
            
            <template v-slot:vs-article-sidebar-img>
                <@imageWithCaption imageSrc=media imageDetails=section.image/>
            </template>
        </#if>
        
        <#if section.quote??>
            <template v-slot:vs-article-sidebar-quote>
                <@quote quoteItem=section.quote />
            </template>
        </#if>
    </vs-article-sidebar>
</#macro>