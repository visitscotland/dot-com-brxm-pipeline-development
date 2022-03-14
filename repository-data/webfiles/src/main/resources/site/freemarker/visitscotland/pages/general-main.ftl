<#ftl output_format="XML">
<#include "../../include/imports.ftl">
<#include "../macros/global/cms-errors.ftl">
<#include "../macros/modules/page-intro/social-share.ftl">
<#include "../../frontend/components/vs-container.ftl">
<#include "../../frontend/components/vs-row.ftl">
<#include "../../frontend/components/vs-col.ftl">
<#include "../../frontend/components/vs-rich-text-wrapper.ftl">
<#include "../../frontend/components/vs-heading.ftl">

<#include "../macros/modules/page-intro/page-intro.ftl">
<#include "../macros/global/cms-errors.ftl">
<#include "../macros/global/otyml.ftl">
<#include "../macros/shared/module-builder.ftl">
<#include "../macros/modules/signpost/signpost.ftl">

<#-- Implicit Request Objects -->
<#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.General" -->
<#-- @ftlvariable name="pageItems" type="com.visitscotland.brxm.hippobeans.Megalinks" -->

<#-- @ftlvariable name="heroImage" type="com.visitscotland.brxm.model.FlatImage" -->
<#-- @ftlvariable name="heroCoordinates" type="com.visitscotland.brxm.model.Coordinates" -->

<#assign standardTemplate = (document.theme == "Standard") />

<div class="has-edit-button">
	<@hst.manageContent hippobean=document/>
    <@cmsErrors errors=alerts!"" editMode=editMode />

    <#if standardTemplate>
        <@pageIntro content=document heroDetails=heroImage />
    <#else>
        <@pageIntro content=document simplePage=!standardTemplate/>
    </#if>

    <#--TODO Control abput colours, change style="background-color:${style}  -->
	<#list pageItems as module>

		<#--TODO Colour should be only added to Megalinks, add this code to macros or create a common macro to control it-->
		<#if standardTemplate >
			<@moduleBuilder module />
		<#else>
			<@moduleBuilder module=module colourScheme=["light", "light", "light"] />
		</#if>

	</#list>

    <@socialShare nojs=true/>

	<@otymlModule otyml editMode />

	<#if newsletterSignpost??>
		<@signpost module=newsletterSignpost imgSrc="assets/images/illustrations/newsletter.svg"/>
	</#if>
</div>
