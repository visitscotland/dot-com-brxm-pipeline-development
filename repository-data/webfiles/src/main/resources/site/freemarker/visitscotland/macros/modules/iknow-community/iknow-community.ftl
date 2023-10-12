<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-img.ftl">
<#include "../../../../frontend/components/vs-tag.ftl">
<#include "../../../../frontend/components/vs-link.ftl">
<#include "../../../../frontend/components/vs-list.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.IKnowCommunityModule" -->

<#macro iknowCommunity module>
    <#-- THIS MODULE IS NOT CURRENTLY IN USE, CONSIDER REMOVING IT -->
    <@previewWarning editMode module module.errorMessages />
    <vs-module-wrapper theme="<#if themeName?has_content>${themeName}<#else>light</#if>">
        <template v-slot:vs-module-wrapper-heading>
            ${module.title}
        </template>
        <vs-container>
            <vs-row>
                <vs-col
                    cols="12"
                    class="text-center"
                >
                    <vs-img 
                        src="<@hst.webfile path='assets/images/iknow-community-logo.svg'/>" 
                        width="190px"
                        class="mb-9 mb-md-10">
                    </vs-img>
                </vs-col>
            </vs-row>
            <vs-row>
                <vs-col
                    cols="12"
                    sm="10"
                    offset-sm="1"
                    lg="6"
                    offset-lg="0"
                    xl="5"
                    offset-xl="1"
                    class="text-left"
                >
                    <vs-rich-text-wrapper variant="lead" class="mb-4">
                        <@hst.html hippohtml=module.copy/>
                    </vs-rich-text-wrapper>

                    <vs-link
                        class="mb-9 mb-md-10 d-block"
                        href="${module.link.link}"
                        type="<#if module.link.type??>${module.link.type}<#else>default</#if>"
                    >
                        ${module.link.label}
                    </vs-link>

                    <vs-list unstyled inline>
                        <#list module.tags as tag>
                            <li>
                                <vs-tag href="${tag.link}">
                                    ${tag.label}
                                </vs-tag>
                            </li>
                        </#list>
                    </vs-list>
                </vs-col>
                <vs-col
                    cols="10"
                    offset="1"
                    sm="8"
                    offset-sm="2"
                    md="6"
                    offset-md="3"
                    lg="6"
                    offset-lg="0"
                    xl="4"
                    offset-xl="1"
                >
                    <vs-img 
                        class="mt-8 mt-lg-9 mt-xl-4"
                        src="<@hst.webfile path='assets/images/illustrations/iknow-coo-puffin.svg'/>">
                    </vs-img>
                </vs-col>
            </vs-row>
        </vs-container>
    </vs-module-wrapper>
</#macro>