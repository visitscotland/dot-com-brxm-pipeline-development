<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-marketo-form.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">

<#-- Reference: https://developers.marketo.com/javascript-api/forms/ -->
<@hst.headContribution category="htmlBodyEndScripts">
    <script src="//e.visitscotland.com/js/forms2/js/forms2.min.js"></script>
</@hst.headContribution>

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.MarketoFormModule" -->
<#macro marketo form>
    <vs-container class="mb-10">
        <vs-row>
            <vs-col
                cols="12"
                md="10"
                lg="7"
                xl="7"
                class="col-xxl-6"
            >
                <#assign language = locale?keep_before("-")>  
                
                <#if property("gtm.is-production") = 'false'>
                    <#assign munchkinId = "${label('forms', 'form.munchkin-sandbox')}">
                    <#assign marketoInstance = "${label('forms', 'form.marketo-instance-sandbox')}">
                <#else>
                    <#assign munchkinId = "${label('forms', 'form.munchkin-prod')}">
                    <#assign marketoInstance = "${label('forms', 'form.marketo-instance-prod')}">
                </#if>

                <vs-marketo-form
                    data-url="${form.jsonUrl}"
                    messaging-url="${label('forms', 'form.messaging-url')}"
                    country-list-url="${label('forms', 'form.country-url')}"
                    recaptcha-key="${label('forms', 'form.recaptcha-key')}"
                    marketo-instance="${marketoInstance}"
                    munchkin-id="${munchkinId}"
                    language="${language}"
                    :is-prod="${property('gtm.is-production')}"
                >
                    <template slot="submitError">
                        ${label('forms', 'form.error')}
                    </template>

                    <template slot="submitting">
                        ${label('forms', 'form.submitting')}
                    </template>
                </vs-marketo-form>
            </vs-col>
        </vs-row>
    </vs-container>
</#macro>