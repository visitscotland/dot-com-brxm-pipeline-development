<#compress>
    <#include "../../../include/imports.ftl">
    <#include "../global/preview-warning.ftl">
    <#include "../modules/megalinks/megalinks.ftl">
    <#include "../modules/article/article.ftl">
    <#include "../modules/long-copy/long-copy.ftl">
    <#include "../modules/marketo/marketo.ftl">
    <#include "../modules/iknow-community/iknow-community.ftl">
    <#include "../modules/travel-information/travel-information.ftl">
    <#include "../modules/tourism-information/tourisminformation-iknow.ftl">
    <#include "../modules/tourism-information/tourisminformation-icentre.ftl">
    <#include "../modules/horizontal-list/horizontal-list.ftl">
    <#include "../modules/stackla/stackla.ftl">
    <#include "../modules/canned-search/canned-search.ftl">
    <#include "../modules/map/map.ftl">
    <#include "../modules/ski-centre/ski-centre.ftl">
<#include "../modules/ski-centre-list/ski-centre-list.ftl">
<#include "theme-calculator.ftl">

    <#-- Implicit Request Objects -->
    <#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.Destination" -->
    <#-- @ftlvariable name="pageItems" type="com.visitscotland.brxm.hippobeans.Megalinks" -->
    <#-- @ftlvariable name="image" type="com.visitscotland.brxm.model.FlatImage" -->
    <#-- @ftlvariable name="heroImage" type="com.visitscotland.brxm.model.FlatImage" -->
    <#-- @ftlvariable name="heroCoordinates" type="com.visitscotland.brxm.model.Coordinates" -->
    <#-- @ftlvariable name="hero" type="com.visitscotland.brxm.hippobeans.Image" -->
    <#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.megalinks.LinksModule" -->
</#compress>
<#macro moduleBuilder module colourScheme=[]>
    <#assign themeName = themeCalculator(module.themeIndex, module, colourScheme)>
    <#if module.getType() == "MultiImageLinksModule" ||  module.getType() == "SingleImageLinksModule" || moduleType == "ListLinksModule">
        <#assign moduleType = "megalinks">
    <#else>
        <#assign moduleType = module.getType()>
    </#if>

    <div class="has-edit-button vs-module-wrapper__outer--${themeName}">
        <#if module.hippoBean?? >
            <@hst.manageContent hippobean=module.hippoBean />
        </#if>
        <#if moduleType == "megalinks">
            <#-- all Megalinks modules except HorizontalListLinksModule -->
            <@megalinks item=module type=module.getType() theme=themeName />
        <#elseif moduleType == "HorizontalListLinksModule">
            <@horizontalList module themeName "vs-megalinks-carousel" />
        <#elseif moduleType == "ICentreModule">
            <@icentre module themeName/>
        <#elseif moduleType == "IKnowModule">
            <@iknow module themeName/>
        <#elseif moduleType == "ArticleModule">
            <@article module/>
        <#elseif moduleType == "LongCopyModule">
            <@longCopy module/>
        <#elseif moduleType == "StacklaModule">
            <@stackla module/>
        <#elseif moduleType == "TravelInformationModule">
            <@travelInformation module/>
        <#elseif moduleType == "MapsModule">
            <@map module/>
        <#elseif moduleType == "CannedSearchModule">
            <@cannedSearch module themeName/>
        <#elseif moduleType == "MarketoFormModule">
            <@marketo module/>
        <#elseif moduleType == "SkiModule">
            <@skiCentre module/>

        <#elseif moduleType == "SkiListModule">
            <@skiCentreList module/>
        <#else >
            <@previewWarning editMode module module.errorMessages true />
        </#if>
    </div>
</#macro>
