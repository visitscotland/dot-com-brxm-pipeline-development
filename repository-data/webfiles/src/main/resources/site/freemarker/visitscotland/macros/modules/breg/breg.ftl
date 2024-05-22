<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-marketo-form.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">

<@hst.headContribution category="htmlBodyEndScripts">
    <!-- Is this contribution required? -->
    <script src="${label('forms', 'form.script-url')}"></script>
</@hst.headContribution>

<#-- @ftlvariable name="form" type="com.visitscotland.brxm.model.FormModule" -->
<#macro breg form>
    <vs-container class="mb-10">
        <vs-row>
            <vs-col
                cols="12"
                md="10"
                lg="7"
                class="col-xxl-6"
            >
                <div>
                    <b>Recaptcha</b> ${form.config.recaptcha}<br>
                    <b>Action URL</b> ${form.config.submitURL}<br>
                    <!-- This might be the equivalent of the form.script-url added in the headContributions -->
                    <b>Form Configuration URL (JSON)</b> ${form.config.jsonUrl}<br>
                    Hidden fields:
                    <li>activity_code = ${form.config.activityCode}<br></li>
                    <li>activity_description = ${form.config.activityDescription}<br></li>
                    <li>activity_source = ${form.config.activitySource}<br></li>
                    <li>category =
                    <#list form.config.consents as consent>
                        ${consent}<#sep>,
                    </#list><br><br>

                    ${label('forms', 'form.no-js')}<br>
                    ${label('forms', 'form.error')}<br>
                    ${label('forms', 'form.submitting')}<br>
                </div>
            </vs-col>
        </vs-row>
    </vs-container>
</#macro>
