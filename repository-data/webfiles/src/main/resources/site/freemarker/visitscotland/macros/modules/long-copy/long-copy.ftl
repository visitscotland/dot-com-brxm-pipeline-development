<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-rich-text-wrapper.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.LongCopyModule" -->

<#macro longCopy module>
    <vs-container class="mb-10">
        <vs-row>
            <vs-col cols="7">
                <vs-rich-text-wrapper>
                    <@hst.html hippohtml=module.copy/>
                </vs-rich-text-wrapper>
            <vs-col>
        </vs-row>
    </vs-container>
</#macro>