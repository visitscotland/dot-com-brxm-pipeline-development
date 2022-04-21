<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../../../frontend/components/vs-embed-wrapper.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.StacklaModule" -->

<#macro stackla module>
    <@hst.headContribution category="htmlBodyEnd">
        <script type="text/javascript">
            (function (d, id) {
                var t, el = d.scripts[d.scripts.length - 1].previousElementSibling;
                if (el) el.dataset.initTimestamp = (new Date()).getTime();
                if (d.getElementById(id)) return;
                t = d.createElement('script');
                t.src = '//assetscdn.stackla.com/media/js/widget/fluid-embed.js';
                t.id = id;
                (d.getElementsByTagName('head')[0] || d.getElementsByTagName('body')[0]).appendChild(t);
            }(document, 'stackla-widget-js'));
        </script>
    </@hst.headContribution>

    <@previewWarning editMode module module.errorMessages />

    <vs-module-wrapper theme="<#if themeName?has_content>${themeName}<#else>light</#if>">
        <template slot="vsModuleWrapperHeading">
            ${module.title}
        </template>
        <vs-embed-wrapper>
            <template slot="embedIntroCopy">
                <@hst.html hippohtml=module.copy/>
            </template>
            <template slot="embedIntroCopyNoJs">
                ${module.noJsMessage}
            </template>
            <template slot="embedIntroCopyNoCookies">
                ${module.noCookiesMessage}
            </template>
            <template slot="embedWidget">
                <div class="stackla-widget" data-ct="" data-hash="${module.dataHash}"
                    data-id="${module.dataId}" data-title="social_vs.org_IGfeed" data-ttl="60"
                    style="width: 100%; overflow: hidden;"></div>
            </template>
        </vs-embed-wrapper>
    </vs-module-wrapper>
</#macro>