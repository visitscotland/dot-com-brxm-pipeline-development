<#include "../../../../include/imports.ftl">
<#--  This needs to be commented out for now because the ItineraryMap component is currently excluded from the build  -->
<#include "../../../../frontend/components/vs-map.ftl">

<#-- @ftlvariable name="stopDocument" type="com.visitscotland.brxm.hippobeans.Stop" -->
<#-- @ftlvariable name="stop" type="com.visitscotland.brxm.model.ItineraryStopModule" -->

<#macro itineraryMap itinerary>
    <vs-map
        list-view-text="${label('itinerary', 'list-view')}"
        map-view-text="${label('itinerary', 'map-view')}"
        map-id="vs-itinerary-map"
        :is-visible="true"
        :fit-to-markers="true"
        :places="[
            <#list itinerary.days as day>
                <#list day.stops as stopDocument>
                    <#assign stop = itinerary.stops[stopDocument.identifier]>
                    <#assign image = "" />
                    <#if stop.image??>
                        <#if stop.image.cmsImage??>

                            <#assign image>
                                <@hst.link hippobean=stop.image.cmsImage.original/>
                            </#assign>
                        <#elseif stop.image.externalImage??>
                            <#assign image = stop.image.externalImage />
                        </#if>
                        <#if stop.coordinates?? && stop.coordinates.latitude?? && stop.coordinates.latitude?has_content && stop.coordinates.longitude?? && stop.coordinates.longitude?has_content>
                        {
                            title: '${escapeJSON(stop.title,true)}',
                            latitude: '${stop.coordinates.latitude}',
                            longitude: '${stop.coordinates.longitude}',
                            stopCount: '${stop.index}',
                            imageSrc: '${image}',
                            altText: '${escapeJSON(stop.title, true)}'
                        },
                        </#if>
                    </#if>
                </#list>
            </#list>
        ]"
    >
    </vs-map>
</#macro>