<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-image-with-caption.ftl">
<#include "../../global/image-with-caption.ftl">
<#include "article-main.ftl">
<#include "article-side.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.ArticleModule" -->
<#-- @ftlvariable name="section" type="com.visitscotland.brxm.model.ArticleModuleSection" -->

<#macro article module>
    <@hst.manageContent hippobean=module.hippoBean />
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
    <vs-col cols="12" lg="8" offset-lg="2" style="border: 1px solid grey;">
        <#if image?? && image?has_content>
            <@imageWithCaption imageSrc=image imageDetails=module.image variant="fullwidth"/>
        </#if>

        </br>

        <vs-col offset-lg="2" lg="8">
            <div style="text-align: center;">
                <vs-row>
                    <vs-heading level="2">
                        <#if module.anchor?has_content>
                            <span id="${module.anchor}">${module.title}</span>
                        <#else>
                            ${module.title}
                        </#if>
                    </vs-heading>
                </vs-row>
                <vs-row>
                    <@hst.html hippohtml=module.introduction/>
                </vs-row>
            </div>
        </vs-col>



        <#assign i = 0/>
        </br></br>
        <#list module.sections as section>
            <vs-row>
                <#if section.quote?? || section.image??>
                    <#assign i++ />
                    <#if i % 2 != 0 >
                        <vs-row>
                            <vs-col cols="4" >
                                <@articleSide section />
                            </vs-col>

                            <vs-col cols="7">
                                <@articleMain section/>
                            </vs-col>
                        </vs-row>
                    <#else>
                        <vs-row>

                            <vs-col offset-lg="1">
                                <@articleMain section/>
                            </vs-col>
                            <vs-col cols="4">
                                <@articleSide section />
                            </vs-col>

                        </vs-row>
                    </#if>
                <#else>
                    <vs-row>
                        <vs-col offset-lg="1" cols="10">
                        <@articleMain section/>
                        </vs-col>
                    </vs-row>
                </#if>
            </vs-row>
            </br></br>

        </#list>
    </vs-col>
    </br> </br>
</#macro>