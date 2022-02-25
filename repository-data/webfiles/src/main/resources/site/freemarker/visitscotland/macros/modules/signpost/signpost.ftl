<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-button.ftl">
<#include "../../../../frontend/components/vs-img.ftl">
<#include "../../../../frontend/components/vs-rich-text-wrapper.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.SignpostModule" -->
<#macro signpost module imgSrc>
    <vs-module-wrapper theme="light">
        <template slot="vsModuleWrapperHeading">
            ${module.title}
        </template>

        <vs-container class="text-center text-sm-left mt-2">
            <vs-row>
                <vs-col
                    cols="12"
                    md="10"
                    offset-md="1"
                    lg="6"
                    offset-lg="0"
                    xl="5"
                    offset-xl="1"
                    class="text-center text-lg-left"
                >   
                    <vs-rich-text-wrapper variant="lead" class="mb-9 mb-lg-10">
                        <p>
                            <@hst.html hippohtml=module.copy/>
                        </p>
                    </vs-rich-text-wrapper>
                    <vs-button href="${module.cta.link}">
                        ${module.cta.label}
                    </vs-button>
                </vs-col>
                <vs-col
                    cols="12"
                    lg="5"
                    offset-lg="1"
                    xl="4"
                    offset-xl="1"
                    class="text-center text-lg-left"
                >
                    <vs-img 
                        src="<@hst.webfile path='${imgSrc}'/>" 
                        class="mt-9 mt-lg-0">
                    </vs-img>
                </vs-col>
            </vs-row>
        </vs-container>
    </vs-module-wrapper>
</#macro>