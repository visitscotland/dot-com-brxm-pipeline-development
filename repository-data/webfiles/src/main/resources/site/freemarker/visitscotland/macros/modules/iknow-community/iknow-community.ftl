<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-image-with-caption.ftl">
<#include "../../global/image-with-caption.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.IKnowCommunityModule" -->

<#macro iknowCommunity module>
    <vs-container slot="upper" class="py-lg-4" >
            <vs-heading>${module.title}</vs-heading>
            <@hst.html formattedText=module.copy/> <br />
            <vs-link href="${module.link.link}">${module.link.label}</vs-link> <br />
            <#list module.tags as tag>
                <a href="${tag.link}">${tag.label}</a>
            </#list>
    </vs-container>
</#macro>