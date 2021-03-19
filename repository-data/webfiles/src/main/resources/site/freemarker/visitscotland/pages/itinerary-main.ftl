<#compress>
<#include "../../include/imports.ftl">

<#--  <#include "../../frontend/stores/vs-store-itineraries-store.ftl">  -->
<#include "../../frontend/components/vs-page-intro.ftl">
<#include "../../frontend/components/vs-icon.ftl">

<#include "../../frontend/components/vs-tooltip.ftl">

<#include "../../frontend/components/vs-itinerary-day.ftl">
<#include "../../frontend/components/vs-itinerary.ftl">

<#include "../macros/modules/itineraries/itinerary-stop.ftl">
<#include "../macros/modules/itineraries/itinerary-map.ftl">
<#include "../macros/modules/page-intro/page-intro.ftl">
<#include "../macros/global/cms-errors.ftl">
<#include "../macros/shared/module-builder.ftl">

<#-- Implicit Request Objects -->
<#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.Itinerary" -->
<#-- @ftlvariable name="firstStopLocation" type="java.lang.String" -->
<#-- @ftlvariable name="lastStopLocation" type="java.lang.String" -->
<#-- @ftlvariable name="heroImage" type="com.visitscotland.brxm.model.FlatImage" -->
<#-- @ftlvariable name="heroCoordinates" type="com.visitscotland.brxm.model.Coordinates" -->

<#-- Template defined objects -->
<#-- @ftlvariable name="day" type="com.visitscotland.brxm.hippobeans.Day" -->
<#-- @ftlvariable name="hero" type="com.visitscotland.brxm.hippobeans.Image" -->

<#assign mainTransport = "">
<#assign dayNumber = 0>
<#assign stopNumber = 0>
<#assign lastStop = 0>

<#if document.transports?has_content >
    <#assign mainTransport = document.transports[0]>
</#if>
</#compress>
<div class="has-edit-button">
    <@hst.manageContent hippobean=document documentTemplateQuery="new-day" rootPath="site" defaultPath="${path}" />
    <@cmsErrors errors=alerts!"" editMode=editMode />
     <@hst.link var="hero" hippobean=document.heroImage.original/>

    <@pageIntro content=document heroImage=heroImage heroCoordinates=heroCoordinates hero=heroImage hero=hero theme="light" areas=document.areas days="document.days" firstStop="firstStopLocation" lastStop="lastStopLocation" />	
    
    <vs-itinerary>
        <@itineraryMap days=document.days />
        <#list document.days as day>
            <#assign dayNumber++>
            <#assign dayTransport = "">
            <vs-itinerary-day 
                slot="list"
                :default-show="${(dayNumber < 3)?c}"
                day-number="${dayNumber}"
                day-label="${label("itinerary", "day")}"
                day-title="${day.title}"
                >
                <#if day.transports?has_content>
                    <#assign dayTransport = day.transports[0]>
                    <vs-description-list class="text-center justify-content-center align-items-center has-edit-button" slot="day-transport">
                         <@hst.manageContent hippobean=day />
                        <#-- 
                            Note - can't use vs-description-list-item
                            here yet as font style and layout are different 
                        -->
                        <dt class="list-inline-item">${label("itinerary", "transport")}:</dt>
                        <#list day.transports as transport>
                            <dd class="list-inline-item">
                                <vs-tooltip title="${label("transports", "${transport}")}">
                                    <vs-icon name="${transport}" variant="dark" size="md"></vs-icon>
                                </vs-tooltip>
                                <span class="sr-only">${label("transports", "${transport}")}</span>
                            </dd>
                        </#list>
                    </vs-description-list>
                </#if>

                <div slot="day-introduction">
                    <@hst.html hippohtml=day.introduction/>
                </div>

                <!-- STOP STARTS HERE -->
                <#assign lastStop = lastStop + day.stops?size>
                <#list day.stops as stop>
                    <#assign stopNumber++>
                    <@itineraryStop stop=stop lastStop=(stopNumber==lastStop)?c/>
                </#list>
                <!-- STOP ENDS HERE -->
            </vs-itinerary-day>
        </#list>
    </vs-itinerary>
    <#if otyml??>
        <@moduleBuilder otyml "theme1" />
    </#if>
</div>