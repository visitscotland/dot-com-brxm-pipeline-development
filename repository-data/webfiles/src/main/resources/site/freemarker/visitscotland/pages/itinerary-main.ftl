<#compress>
<#include "../../include/imports.ftl">
<#include "../../frontend/components/vs-icon.ftl">
<#include "../../frontend/components/vs-tooltip.ftl">
<#include "../../frontend/components/vs-itinerary-day.ftl">
<#include "../../frontend/components/vs-itinerary.ftl">

<#include "../macros/modules/page-intro/social-share.ftl">
<#include "../macros/modules/itineraries/itinerary-stop.ftl">
<#include "../macros/modules/itineraries/itinerary-map.ftl">
<#include "../macros/modules/page-intro/page-intro.ftl">
<#include "../macros/global/otyml.ftl">
<#include "../macros/shared/module-builder.ftl">
<#include "../macros/modules/horizontal-list/horizontal-list.ftl">
<#include "../macros/modules/signpost/signpost.ftl">
<#include "../macros/modules/product-search/psr-module.ftl">

<#-- Implicit Request Objects -->
<#-- @ftlvariable name="document" type="com.visitscotland.brxm.hippobeans.Itinerary" -->
<#-- @ftlvariable name="itinerary" type="com.visitscotland.brxm.model.ItineraryPage" -->
<#-- @ftlvariable name="heroImage" type="com.visitscotland.brxm.model.FlatImage" -->
<#-- @ftlvariable name="heroCoordinates" type="com.visitscotland.brxm.model.Coordinates" -->

<#-- Template defined objects -->
<#-- @ftlvariable name="day" type="com.visitscotland.brxm.hippobeans.Day" -->
<#-- @ftlvariable name="stop" type="com.visitscotland.brxm.hippobeans.Stop" -->


<#assign mainTransport = "">
<#assign dayNumber = 0>
<#assign lastStop = 0>

<#if document.transports?has_content >
    <#assign mainTransport = document.transports[0]>
</#if>
</#compress>
<div class="has-edit-button">
    <@hst.manageContent hippobean=document/>

    <@pageIntro content=document heroDetails=heroImage itinerary=itinerary />

    <vs-itinerary>
        <@itineraryMap itinerary />
        <#list itinerary.days as day>
            <#assign dayNumber++>

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
                            #VS-2985
                        -->
                        <dt class="list-inline-item">${label("itinerary", "transport")}:</dt>
                        <#list day.transports as transport>
                            <dd class="list-inline-item">
                                <vs-tooltip
                                    title="${label('transports', '${transport}')}"
                                    href="#"
                                    class="p-0"
                                    variant="transparent"
                                >
                                    <span class="sr-only">
                                        ${label("transports", "${transport}")}
                                    </span>
                                    <vs-icon name="${transport}" variant="dark" size="md" small-size="xs"></vs-icon>
                                </vs-tooltip>
                            </dd>
                        </#list>
                    </vs-description-list>
                <#else>
                    <#assign dayTransport = "">
                </#if>

                <div slot="day-introduction">
                    <@hst.html hippohtml=day.introduction/>
                </div>

                <!-- STOP STARTS HERE -->
                <#assign lastStop = lastStop + day.stops?size>
                <#list day.stops as stop>
                    <#assign stopModule = itinerary.stops[stop.identifier]>
                    <@itineraryStop stop=stopModule isLastStop=(stopModule.index==lastStop)?c/>
                </#list>
                <!-- STOP ENDS HERE -->
            </vs-itinerary-day>
        </#list>
    </vs-itinerary>

    <@socialShare nojs=true/>

    <@productSearchWidget psrWidget />

    <#if otyml??>
        <@otymlModule otyml editMode />
    </#if>

    <#if newsletterSignpost??>
		<@signpost module=newsletterSignpost imgSrc="assets/images/illustrations/newsletter.svg"/>
	</#if>
</div>