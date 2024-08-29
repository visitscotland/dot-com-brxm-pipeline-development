<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-form.ftl">
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
            
                <#assign language = locale?keep_before("-")>

                <vs-form
                    :is-marketo="false"
                    submit-url="${form.config.submitUrl}"
                    data-url="${form.config.jsonUrl}"
                    recaptcha-key="${form.config.recaptcha}"
                    recaptcha-textarea-label="${label('forms', 'form.recaptcha-textarea-label')}"
                    :is-prod="${property('form.is-production')}"
                    language="${language}"
                    messaging-url="${label('forms', 'form.messaging-url')}"
                    country-list-url="${label('forms', 'form.country-url')}"
                >
                    <template v-slot:hidden-fields>
                        <input
                            type="hidden"
                            name="activity_code"
                            value="${form.config.activityCode}"
                        />
                        <input
                            type="hidden"
                            name="activity_description"
                            value="${form.config.activityDescription}"
                        />
                        <input
                            type="hidden"
                            name="activity_source"
                            value="${form.config.activitySource}"
                        /> 
                        <input
                            type="hidden"
                            name="consents"
                            value="${form.config.consents}"
                        />
                        <#if form.config.legalBasis?has_content>
                        <input
                            type="hidden"
                            name="legalBasis"
                            value="${form.config.legalBasis}"
                        />
                        </#if>
                        <#if form.config.consents?contains('snow,SKIEMAIL_002')>
                        <input
                            type="hidden"
                            name="interest_in_ski"
                            value="true"
                        />
                        </#if>
                    </template>
                </vs-form>
            </vs-col>
        </vs-row>
    </vs-container>
</#macro>
