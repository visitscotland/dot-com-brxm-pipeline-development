<#ftl output_format="XML">
<#compress>
    <#include "../../include/imports.ftl">
    <#include "../macros/global/otyml.ftl">
    <#include "../macros/modules/page-intro/social-share.ftl">
    <#include "../macros/modules/page-intro/page-intro.ftl">
    <#include "../macros/modules/signpost/signpost.ftl">
    <#include "../macros/modules/product-search/psr-module.ftl">
    <#include "../macros/shared/module-builder.ftl">

    <#-- Implicit Request Objects -->
    <#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.Destination" -->
    <#-- @ftlvariable name="pageItems" type="com.visitscotland.brxm.hippobeans.Megalinks" -->
    <#-- @ftlvariable name="image" type="com.visitscotland.brxm.model.FlatImage" -->

    <#-- @ftlvariable name="heroImage" type="com.visitscotland.brxm.model.FlatImage" -->
    <#-- @ftlvariable name="heroCoordinates" type="com.visitscotland.brxm.model.Coordinates" -->
</#compress>
<div class="has-edit-button">
	<@hst.manageContent hippobean=document/>

    <@pageIntro content=document heroDetails=heroImage lightBackground=true/>

    <@productSearchWidget psrWidget />

	<#list pageItems as item>
        <@moduleBuilder item pageIndex="${item?index + 1}" />
	</#list>

    <@socialShare nojs=true/>

    <@otymlModule otyml editMode />

    <#if newsletterSignpost??>
		<@signpost module=newsletterSignpost />
	</#if>
</div>
