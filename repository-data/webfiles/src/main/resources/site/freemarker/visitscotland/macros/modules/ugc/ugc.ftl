<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-embed-wrapper.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.StacklaModule" -->

<#macro ugc module>
    <@hst.headContribution category="htmlBodyEnd">
        <script
            type="text/javascript"
            src="https://apps.storystream.ai/app/js/${module.dataHash}.js"
            class="optanon-category-C0001-C0003-C0004"
        ></script>
    </@hst.headContribution>

    <@previewWarning editMode module module.errorMessages />

    <vs-module-wrapper theme="<#if themeName?has_content>${themeName}<#else>light</#if>">
        <template v-slot:vs-module-wrapper-heading>
            ${module.title}
        </template>
        <vs-embed-wrapper
            no-cookie-text="${module.noCookiesMessage}"
            error-text = "${label('essentials.global', 'third-party-error')}"
            no-js-text="${module.noJsMessage}"
        >
            <template v-slot:embed-intro-copy>
                <@hst.html hippohtml=module.copy/>
            </template>

            <template v-slot:embed-widget>
                <div id="stry-wrapper"></div>
            </template>

            <template v-slot:embed-button-text>
                ${label('essentials.global', 'cookie.link-message')}
            </template>
        </vs-embed-wrapper>
    </vs-module-wrapper>
</#macro>