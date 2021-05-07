<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-icentre.ftl">
<#include "../../../../frontend/components/vs-quote.ftl">
<#include "../../../../frontend/components/vs-img.ftl">
<#include "../../global/image-with-caption.ftl">
<#include "../../global/quote.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">

<#macro icentre module themeName>
    <#if module.image.cmsImage??>
        <#assign image>
            <@hst.link hippobean=module.image.cmsImage.original/>
        </#assign>
    <#else>
        <#assign image = module.image.externalImage!'' />
    </#if>

    <vs-module-wrapper class="theme-${themeName}">
        <template slot="vsModuleWrapperHeading">
            ${module.title}
        </template>
        <vs-container>
            <vs-icentre>
                <template slot="icentreImageWithCaption">
                    <@imageWithCaption imageSrc=image imageDetails=module.image variant="fullwidth"/>
                </template>

                <template slot="icentreLinks">
                    ${module.description}
                    <#list module.links as iCentre>
                        <vs-link href="${iCentre.link}">
                            ${iCentre.label}
                        </vs-link>
                        <#if iCentre?counter == (module.links?size - 1)>
                            &nbsp;and&nbsp;
                        <#else>
                            <#if iCentre?has_next>,&nbsp;</#if>
                        </#if>
                    </#list>
                    .
                </template>

                <#if module.quote??>
                    <#if module.quote.image??>
                        <#assign imageQuote>
                            <@hst.link hippobean=module.quote.image.cmsImage.original/>
                        </#assign>
                    <#else>
                        <#assign imageQuote="" />
                    </#if>

                    <#if module.quote.link??>
                        <#assign quoteLink=module.quote.link />
                    <#else>
                        <#assign quoteLink="" />
                    </#if>

                    <#if module.quote.authorName??>
                        <#assign quoteAuthorName=module.quote.authorName />
                    <#else>
                        <#assign quoteAuthorName="" />
                    </#if>

                    <#if module.quote.authorTitle??>
                        <#assign quoteAuthorTitle=module.quote.authorTitle />
                    <#else>
                        <#assign quoteAuthorTitle="" />
                    </#if>

                    <template slot="icentreQuote">
                        <@quote authorImage=imageQuote content=module.quote.quote authorName=quoteAuthorName authorTitle=quoteAuthorTitle link=quoteLink />
                    </template>
                </#if>
            </vs-icentre>
        </vs-container>
    </vs-module-wrapper>

</#macro>
