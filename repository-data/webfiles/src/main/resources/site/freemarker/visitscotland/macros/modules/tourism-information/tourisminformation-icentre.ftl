<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-icentre.ftl">
<#include "../../../../frontend/components/vs-quote.ftl">
<#include "../../../../frontend/components/vs-img.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">

<#include "../../global/image-with-caption.ftl">
<#include "../../global/quote.ftl">
<#include "../../global/preview-warning.ftl">

<#macro icentre module themeName="">
    <@previewWarning editMode module module.errorMessages />

    <#if module.image.cmsImage??>
        <#assign image>
            <@hst.link hippobean=module.image.cmsImage.original/>
        </#assign>
    <#else>
        <#assign image = module.image.externalImage!'' />
    </#if>

    <vs-module-wrapper theme="<#if themeName?has_content>${themeName}<#else>light</#if>">
        <template slot="vsModuleWrapperHeading">
            ${module.title}
        </template>
        <vs-container>
            <vs-icentre>
                <template slot="icentreImageWithCaption">
                    <@imageWithCaption imageSrc=image imageDetails=module.image noAltText="true" />
                </template>

                <template slot="icentreLinks">
                    ${module.description}
                    <#list module.links as iCentre>
                        <vs-link href="${iCentre.link}">${iCentre.label}</vs-link><#t>
                        <#if iCentre?counter == (module.links?size - 1)>
                            &nbsp;and&nbsp;<#t>
                        <#else>
                            <#if iCentre?has_next>,&nbsp;</#if><#t>
                        </#if>
                    </#list>
                    .<#t>
                </template>
                
                <#if module.quote??>
                    <template slot="icentreQuote">
                        <@quote quoteItem=module.quote variant="wide"/>
                    </template>
                </#if>
            </vs-icentre>
        </vs-container>
    </vs-module-wrapper>

</#macro>
