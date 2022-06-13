<#include "../../../../../include/imports.ftl">
<#include "../../../../../frontend/components/vs-megalink-multi-image.ftl">
<#include "./multi-image-featured.ftl">
<#include "./multi-image-two-items.ftl">
<#include "./multi-image-three-items.ftl">
<#include "../../video/video-modal.ftl">

<#macro multiImage item showTeaser theme>
    <vs-col
        cols="10"
        class="offset-1"
    >
        <vs-row>
            <#-- if there's at least one featured link -->
            <#if item.featuredLinks?size gt 0>
                <@multiImageFeatured lastFeatured='false' feature=item.featuredLinks[0] theme=theme />
                
                <#if item.featuredLinks[0].youtubeId??>
                    <@videoModal videoId=item.featuredLinks[0].youtubeId />
                </#if>              
            </#if>

            <#list item.links as megalink>
                <#-- 2 and 4 links will be displayed in 2 columns -->
                <#if item.links?size == 2 || item.links?size == 4>
                    <@multiImageTwoItems megalink=megalink showTeaser=showTeaser theme=theme />
                </#if>
                <#-- 3 and 6 links will be displayed in 3 columns -->
                <#if item.links?size == 3 || item.links?size == 6>
                    <@multiImageThreeItems megalink=megalink showTeaser=showTeaser theme=theme />
                </#if>
                <#-- 5 links will be displayed in a combintation of 2 and 3 columns -->
                <#if item.links?size == 5>
                    <#if megalink?index lt 3>
                        <@multiImageThreeItems megalink=megalink showTeaser=showTeaser theme=theme/>
                    <#else>
                        <@multiImageTwoItems megalink=megalink showTeaser=showTeaser theme=theme/>
                    </#if>
                </#if>
                
                <#if megalink.youtubeId??>
                    <@videoModal videoId=megalink.youtubeId />
                </#if>
            </#list>

            <#-- if there's a second featured link -->
            <#if item.featuredLinks?? && item.featuredLinks?size gt 1>
                <@multiImageFeatured  lastFeatured='true' feature=item.featuredLinks[1] theme=theme />
                
                <#if item.featuredLinks[1].youtubeId??>
                    <@videoModal videoId=item.featuredLinks[1].youtubeId />
                </#if>
            </#if>
        </vs-row>
    </vs-col>
</#macro>