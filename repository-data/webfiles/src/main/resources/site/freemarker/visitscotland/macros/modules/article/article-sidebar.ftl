<#include "../../../../frontend/components/vs-article-sidebar.ftl">

<#macro articleSidebar section alignSidebar>
    <vs-article-sidebar sidebar-align="${alignSidebar}">
        <#if section.image??>
            <#if section.image.cmsImage??>
                <#assign media>
                    <@hst.link hippobean=section.image.cmsImage.original/>
                </#assign>
            <#else>
                <#assign media = section.image.externalImage!'' />
            </#if>
            
            <template slot="vsArticleSidebarImg">
                <@imageWithCaption imageSrc=media imageDetails=section.image/>
            </template>
        </#if>
        
        <#if section.quote??>
            <template slot="vsArticleSidebarQuote">
                <@quote quoteItem=section.quote />
            </template>
        </#if>
    </vs-article-sidebar>
</#macro>