<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-listicle-item.ftl">
<#include "../../../../frontend/components/vs-link.ftl">

<#include "../../global/key-facilities.ftl">
<#include "../../global/preview-warning.ftl">
<#include "../../global/image-with-caption.ftl">

<#macro listicleItem item isFirstListicle>
<#-- @ftlvariable name="listItem" type="com.visitscotland.brxm.hippobeans.ListicleItem" -->
<#-- @ftlvariable name="item" type="com.visitscotland.brxm.model.ListicleModule" -->
<#-- @ftlvariable name="cta" type="com.visitscotland.brxm.model.FlatLink" -->
	<#assign image = "" />
    <#if item.image?? && item.image.cmsImage??>
        <#assign image>
            <@hst.link hippobean=item.image.cmsImage.original/>
        </#assign>
    <#elseif item.image?? && item.image.externalImage??>
        <#assign image = item.image.externalImage />
    </#if>

    <vs-listicle-item
            index="${item.index}"
            title="${item.title?html}"
            sub-title="${item.subtitle!''}"
    >
        <template v-slot:hippo-details>
            <div class="has-edit-button">
                <@hst.manageContent hippobean=item.hippoBean/>
                <@previewWarning editMode item item.errorMessages />
            </div>
        </template>

        <#--  <#if item.image?? && item.image?has_content>
            <div v-slot:image-slot>
                <@imageWithCaption 
                    imageSrc=image 
                    imageDetails=item.image 
                    variant="large" 
                    noAltText="true"
                    useLazyLoading=isFirstListicle
                />
            </div>
        </#if>  -->

        <template v-slot:description-slot>
            <div>
                <@hst.html hippohtml=item.description />

                <#if item.links?has_content>
                    <#list item.links as cta>
                        <#if cta?has_content>
                            <div class="mb-2">
                                <vs-link
                                    href="${cta.link}"
                                    <#if cta.type != "internal">type="${cta.type}"</#if>
                                >
                                    ${cta.label}<span class="sr-only">: ${item.title}</span>
                                </vs-link>
                            </div>
                        </#if>
                    </#list>
                </#if>
            </div>
        </template>

        <#if item.facilities?? && item.facilities?size gt 1>
            <template v-slot:facilities-slot>
                <div>
                    <@keyFacilities facilitiesList=item.facilities />
                </div>
            </template>
		</#if>
	</vs-listicle-item>
</#macro>
