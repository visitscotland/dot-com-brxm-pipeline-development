<#ftl output_format="XML">
<#include "../../include/imports.ftl">
<#include "../macros/global/cms-errors.ftl">
<#include "../../frontend/components/vs-page-intro.ftl">
<#include "../../frontend/components/vs-hero.ftl">
<#include "../../frontend/components/vs-container.ftl">
<#include "../../frontend/components/vs-row.ftl">
<#include "../../frontend/components/vs-col.ftl">
<#include "../../frontend/components/vs-rich-text-wrapper.ftl">
<#include "../../frontend/components/vs-img.ftl">
<#include "../../frontend/components/vs-button.ftl">

<#include "../../frontend/components/vs-heading.ftl">
<#include "../../frontend/components/vs-social-share.ftl">

<#include "../macros/global/cms-errors.ftl">

<#include "../macros/shared/module-builder.ftl">

<#-- Implicit Request Objects -->
<#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.General" -->
<#-- @ftlvariable name="pageItems" type="com.visitscotland.brxm.hippobeans.Megalinks" -->
<#-- @ftlvariable name="image" type="com.visitscotland.brxm.model.FlatImage" -->

<#-- @ftlvariable name="heroImage" type="com.visitscotland.brxm.model.FlatImage" -->
<#-- @ftlvariable name="heroCoordinates" type="com.visitscotland.brxm.model.Coordinates" -->

<#-- @ftlvariable name="hero" type="com.visitscotland.brxm.hippobeans.Image" -->

<#assign standardTemplate = (document.theme == "Standard") />

<div class="has-edit-button">
	<@hst.manageContent hippobean=document documentTemplateQuery="new-module" rootPath="site" defaultPath="${path}" />
    <@cmsErrors errors=alerts!"" editMode=editMode />

    <#if standardTemplate>
	<vs-page-intro>
		<@hst.link var="hero" hippobean=document.heroImage.original/>

			<vs-hero
					slot="hero"
					alt-text="${heroImage.altText!''}"
					credit="${heroImage.credit!''}"
					caption="${heroImage.description!''}"
					image-src="${hero}"
					latitude="${(heroCoordinates.latitude)!''}"
					longitude="${(heroCoordinates.longitude)!''}"
			>
				<vs-img
						src="${hero}"
						alt="${heroImage.altText!''}"
				> </vs-img>
			</vs-hero>
	</#if>
		<vs-container slot="upper" class="py-lg-4">
			<vs-row class="justify-content-md-between">
				<vs-col cols="12" lg="8" offset-lg="1">
					<@hst.include ref="breadcrumb"/>
				</vs-col>
			</vs-row>

			<vs-row>
				<vs-col cols="10" lg="8" offset-lg="1">
					<vs-heading level="1">${document.title}</vs-heading>
				</vs-col>
				<vs-col cols="2">
					<div class="d-flex justify-content-center justify-content-sm-end">
						<vs-social-share></vs-social-share>
					</div>
				</vs-col>
			</vs-row>
			<vs-row>
				<vs-col cols="12" md="10" lg="10" xl="10" offset-lg="1">
					<vs-rich-text-wrapper variant="lead">
						<@hst.html hippohtml=document.introduction/>
						<ul>
						<#list pageItems as module>
							<#if module.anchor?has_content >
								<li><a href="#${module.anchor}">${module.title}</a></li>
							</#if>
						</#list>
						</ul>
					</vs-rich-text-wrapper>
				</vs-col>
			</vs-row>
		</vs-container>
	<#if standardTemplate>
	</vs-page-intro>
	</#if>

  <#--TODO Control abput colours, change style="background-color:${style}  -->
	<#list pageItems as module>

		<#--TODO Colour should be only added to Megalinks, add this code to macros or create a common macro to control it-->
		<#if module.theme?? && module.theme == "style3" && standardTemplate >
			<#assign theme = "#292929" />
		<#else>
			<#assign theme = "#FFFFFF" />
		</#if>

		<@moduleBuilder module=module theme=theme />

	</#list>
</div>
