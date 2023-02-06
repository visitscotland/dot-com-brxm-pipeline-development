<#compress>
<#include "../../../include/imports.ftl">
<#include "../../../frontend/components/vs-breadcrumb.ftl">
<#include "../../../frontend/components/vs-breadcrumb-item.ftl">

<#include "content-driven-seo.ftl">
<#include "breadcrumb-schema.ftl">
<#include "schema-org.ftl">

<@hst.setBundle basename="navigation.static"/>

<#-- @ftlvariable name="breadcrumb" type="org.onehippo.forge.breadcrumb.om.Breadcrumb" -->
<#-- @ftlvariable name="requestedURI" type="java.lang.String" -->
<#-- @ftlvariable name="isHome" type="java.lang.Boolean" -->
<#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.Page" -->

</#compress>
<#if breadcrumb?? && breadcrumb.items??>
    <vs-breadcrumb>
        <vs-breadcrumb-item
                key="home"
                href="<@hst.link siteMapItemRefId="root"/>"
                <#if isHome>active</#if>
                text="${label('essentials.global', 'home')}"
        >

        </vs-breadcrumb-item>

        <#list breadcrumb.items as item>
        <#-- Avoid duplicated Home element (Document based pages skip this element)-->
            <#if item.link?? &&  item.link.hstSiteMapItem.id?? && item.link.hstSiteMapItem.id != "root">

                <#if item.link??>
                    <@hst.link var="link" link=item.link/>
                <#else>
                <#-- This asignation removes the link from the breadcrumb -->
                    <#assign link = requestedURI>
                </#if>


            <#--1(${link}) != 2(${requestedURI})-->
                <vs-breadcrumb-item
                        key="${item.title?html}"
                        href="${link}"
                        <#if (link?? && link == requestedURI) || item?counter == breadcrumb.items?size>active</#if>
                        text="${item.title?html}"
                >
                </vs-breadcrumb-item>

            </#if>
        </#list>

    </vs-breadcrumb>
</#if>