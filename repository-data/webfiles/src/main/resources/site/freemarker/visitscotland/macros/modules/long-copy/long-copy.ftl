<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-rich-text-wrapper.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../global/preview-warning.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.LongCopyModule" -->

<#macro longCopy module>
    <@previewWarning editMode module module.errorMessages />
    <vs-container class="mb-10">
        <vs-row>
            <vs-col
                cols="12"
                md="10"
                lg="7"
                xl="7"
                class="col-xxl-6"
            >
                <vs-rich-text-wrapper>
                    <@hst.html hippohtml=module.copy/>
                </vs-rich-text-wrapper>
            </vs-col>
        </vs-row>
    </vs-container>
</#macro>