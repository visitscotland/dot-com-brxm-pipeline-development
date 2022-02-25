<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-link.ftl">
<#include "../../../../frontend/components/vs-img.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">

<#include "../../global/cms-errors.ftl">

<#macro iknow module themeName="">
    <@cmsErrors errors=module.errorMessages!"" editMode=editMode />

    <vs-module-wrapper theme="<#if themeName?has_content>${themeName}<#else>light</#if>">
        <span slot="vsModuleWrapperHeading">
            ${module.title}
        </span>
         <vs-container>
            <vs-row>
                <vs-col
                    cols="10"
                    md="6"
                    offset="1"
                    offset-md="0"
                    order="1"
                    order-md="2"
                    class="pb-md-8 pb-lg-11"
                >
                    <vs-img 
                        src="<@hst.webfile path='assets/images/iknow-scotland-logo.svg'/>" 
                        width="130px"
                        class="mb-8">
                    </vs-img>

                    <vs-rich-text-wrapper variant="lead">
                        <p class="mb-8 mb-md-9">
                            <@hst.html hippohtml=module.description/>
                        </p>

                        <vs-link
                            href="${module.link.link}"
                            type="<#if module.link.type??>${module.link.type}<#else>default</#if>"
                        >
                            ${module.link.label}
                        </vs-link>
                    </vs-rich-text-wrapper>
                </vs-col>
                <vs-col
                    cols="4"
                    md="2"
                    offset="4"
                    offset-md="1"
                    order="2"
                    order-md="1"
                    class="d-flex align-items-end"
                >
                    <vs-img 
                        src="<@hst.webfile path='assets/images/illustrations/highland-cow.svg'/>" 
                        class="w-100 mt-8 mt-md-0">
                    </vs-img>
                </vs-col>
            </vs-row>
        </vs-container>
    </vs-module-wrapper>
</#macro>