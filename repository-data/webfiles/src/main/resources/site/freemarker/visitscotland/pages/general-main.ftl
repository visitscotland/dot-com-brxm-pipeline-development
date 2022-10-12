<#ftl output_format="XML">
<#include "../../include/imports.ftl">
<#include "../macros/modules/page-intro/social-share.ftl">
<#include "../macros/modules/page-intro/intro-image.ftl">
<#include "../macros/modules/product-search/psr-module.ftl">
<#include "../macros/modules/signpost/signpost.ftl">
<#include "../macros/shared/module-builder.ftl">

<#include "../macros/modules/search-results/search-results.ftl">
<#include "../../frontend/components/vs-container.ftl">
<#include "../../frontend/components/vs-row.ftl">
<#include "../../frontend/components/vs-col.ftl">
<#include "../../frontend/components/vs-rich-text-wrapper.ftl">
<#include "../../frontend/components/vs-heading.ftl">
<#include "../../frontend/components/vs-html-error.ftl">

<#include "../macros/modules/page-intro/page-intro.ftl">
<#include "../macros/global/otyml.ftl">

<#-- Implicit Request Objects -->
<#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.General" -->

<#-- @ftlvariable name="heroImage" type="com.visitscotland.brxm.model.FlatImage" -->

<#assign topLevelTemplate = (document.theme == "Top-Level") />
<#assign standardTemplate = (document.theme == "Standard") />
<#assign simpleTemplate = (document.theme == "Simple") />

<div class="has-edit-button">
	<@hst.manageContent hippobean=document/>

	<#if topLevelTemplate>
		<@pageIntro content=document heroDetails=heroImage lightBackground=psrWidget?has_content />
		<@productSearchWidget psrWidget "top"/>
	<#elseif standardTemplate>
        <@pageIntro content=document lightBackground=psrWidget?has_content />
		<@introImage mainImage=heroImage />
		<@productSearchWidget psrWidget "top"/>
	<#else>
        <@pageIntro content=document lightBackground=true />
    </#if>

	<#if errorCode??>
		<vs-html-error status-code="${errorCode}"></vs-html-error>
	</#if>

    <#--TODO Control abput colours, change style="background-color:${style}  -->
	<#list pageItems as module>

		<#--TODO Colour should be only added to Megalinks, add this code to macros or create a common macro to control it-->
		<#if standardTemplate || topLevelTemplate >
			<@moduleBuilder module />
		<#else>
			<@moduleBuilder module=module colourScheme=["light", "light", "light"] />
		</#if>

	</#list>

    <#if searchResultsPage??>
        <@searchResults />
    </#if>

    <@socialShare nojs=true/>

	<#if simpleTemplate>
		<@productSearchWidget psrWidget />
	</#if>

	<#if otyml??>
		<@otymlModule otyml editMode />
	</#if>

	<#if newsletterSignpost??>
		<@signpost module=newsletterSignpost imgSrc="assets/images/illustrations/newsletter.svg"/>
	</#if>
</div>
