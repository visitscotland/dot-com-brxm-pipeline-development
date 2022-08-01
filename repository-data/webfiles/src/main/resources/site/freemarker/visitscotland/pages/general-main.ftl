<#ftl output_format="XML">
<#include "../../include/imports.ftl">
<#include "../macros/modules/page-intro/social-share.ftl">
<#include "../macros/modules/product-search/psr-module.ftl">
<#include "../macros/modules/signpost/signpost.ftl">
<#include "../macros/shared/module-builder.ftl">

<#include "../macros/modules/search-results/search-results.ftl">
<#include "../../frontend/components/vs-container.ftl">
<#include "../../frontend/components/vs-row.ftl">
<#include "../../frontend/components/vs-col.ftl">
<#include "../../frontend/components/vs-rich-text-wrapper.ftl">
<#include "../../frontend/components/vs-heading.ftl">

<#include "../macros/modules/page-intro/page-intro.ftl">
<#include "../macros/global/otyml.ftl">

<#-- Implicit Request Objects -->
<#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.General" -->

<#-- @ftlvariable name="heroImage" type="com.visitscotland.brxm.model.FlatImage" -->

<#assign standardTemplate = (document.theme == "Standard") />

<div class="has-edit-button">
	<@hst.manageContent hippobean=document/>

	<#--TODO current Starndard template will become Top-level and the Starndard Template will be the same with the hero image displayed as an article
	for now both options are in the CMS so the content team can keep working and both will display the same format -->
	<#if standardTemplate || document.theme=="Top-Level">
		<@pageIntro content=document heroDetails=heroImage lightBackground=psrWidget?has_content />
		<@productSearchWidget psrWidget "top"/>
	<#else>
        <@pageIntro content=document lightBackground=true />
    </#if>

    <#--TODO Control abput colours, change style="background-color:${style}  -->
	<#list pageItems as module>

		<#--TODO Colour should be only added to Megalinks, add this code to macros or create a common macro to control it-->
		<#if standardTemplate || document.theme=="Top-Level" >
			<@moduleBuilder module />
		<#else>
			<@moduleBuilder module=module colourScheme=["light", "light", "light"] />
		</#if>

	</#list>

    <#if searchResultsPage??>
        <@searchResults />
    </#if>

    <@socialShare nojs=true/>

	<#if !standardTemplate && !document.theme=="Top-Level">
		<@productSearchWidget psrWidget />
	</#if>

	<#if otyml??>
		<@otymlModule otyml editMode />
	</#if>

	<#if newsletterSignpost??>
		<@signpost module=newsletterSignpost imgSrc="assets/images/illustrations/newsletter.svg"/>
	</#if>
</div>
