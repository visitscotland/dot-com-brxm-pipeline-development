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
    <vs-module-wrapper theme="grey">
        <template slot="vsModuleWrapperHeading">
            ${module.title}
        </template>

        <vs-container class="text-center text-sm-left mt-2">
            <vs-row>
                <vs-col
                    cols="12"
                    sm="7"
                    md="6"
                    lg="5"
                    offset-lg="1"
                    class="text-center text-sm-left col-xxl-4"
                >   
                    <vs-rich-text-wrapper variant="lead" class="mb-9 mb-lg-10">
                        <p>
                            <@hst.html hippohtml=module.copy/>
                        </p>
                    </vs-rich-text-wrapper>
                    <vs-button
                        href="${module.cta.link}"
                        class="px-4"
                    >
                        ${module.cta.label}
                    </vs-button>
                </vs-col>
                <vs-col
                    cols="10"
                    offset="1"
                    sm="5"
                    offset-sm="0"
                    md="6"
                    lg="5"
                    xl="5"
                    class="text-center text-lg-left col-xxl-5 offset-xxl-1"
                >
                    <vs-img 
                        src="<@hst.webfile path='${imgSrc}'/>" 
                        class="mt-10 mt-sm-2 w-100">
                    </vs-img>
                </vs-col>
            </vs-row>
        </vs-container>
    </vs-module-wrapper>
</#macro>