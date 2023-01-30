<#compress>
    <#include "../../../include/imports.ftl">
    <#include "theme-calculator.ftl">

    <#include "../global/include-once.ftl">

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
    <#if module.getType() == "MultiImageLinksModule" ||  module.getType() == "SingleImageLinksModule" || module.getType()== "ListLinksModule">
        <#assign moduleType = "megalinks">
    <#else>
        <#assign moduleType = module.getType()>
    </#if>

    <div class="has-edit-button vs-module-wrapper__outer--${themeName}">
        <#if module.hippoBean?? >
            <@hst.manageContent hippobean=module.hippoBean />
        </#if>
        <#if moduleType == "megalinks">
            <@includeOnce "../modules/megalinks/megalinks.ftl" />
            <#-- all Megalinks modules except HorizontalListLinksModule -->
            <@megalinks item=module type=module.getType() theme=themeName />
        <#elseif moduleType == "HorizontalListLinksModule">
            <@includeOnce "../modules/horizontal-list/horizontal-list.ftl" />
            <@horizontalList module themeName "vs-megalinks-carousel" />
        <#elseif moduleType == "ICentreModule">
            <@includeOnce "../modules/tourism-information/tourisminformation-icentre.ftl" />
            <@icentre module themeName/>
        <#elseif moduleType == "IKnowModule">
            <@includeOnce "../modules/tourism-information/tourisminformation-iknow.ftl" />
            <@iknow module themeName/>
        <#elseif module.getType()== "ArticleModule">
            <@includeOnce "../modules/article/article.ftl" />
            <@article module/>
        <#elseif module.getType()== "LongCopyModule">
            <@includeOnce "../modules/long-copy/long-copy.ftl" />
            <@longCopy module/>
        <#elseif module.getType()== "StacklaModule">
            <@includeOnce "../modules/stackla/stackla.ftl" />
            <@stackla module/>
        <#elseif module.getType()== "TravelInformationModule">
            <@includeOnce "../modules/travel-information/travel-information.ftl" />
            <@travelInformation module/>
        <#elseif module.getType()== "MapsModule">
            <@includeOnce "../modules/map/map.ftl" />
            <@map module/>
        <#elseif module.getType()== "CannedSearchModule">
            <@includeOnce "../modules/canned-search/canned-search.ftl" />
            <@cannedSearch module themeName/>
        <#elseif module.getType()== "MarketoFormModule">
            <@includeOnce "../modules/marketo/marketo.ftl" />
            <@marketo module/>
        <#elseif module.getType()== "IKnowCommunityModule">
            <@includeOnce "../modules/iknow-community/iknow-community.ftl" />
            <@iknowCommunity module/>
        <#elseif module.getType()== "SimpleDevModule">
            <@includeOnce "../modules/dev-module/dev-module.ftl" />
            <@devModule module/>
        <#else >
            <@includeOnce "../global/preview-warning.ftl" />
            <@previewWarning editMode module module.errorMessages true />
        </#if>
    </div>
</#macro>
