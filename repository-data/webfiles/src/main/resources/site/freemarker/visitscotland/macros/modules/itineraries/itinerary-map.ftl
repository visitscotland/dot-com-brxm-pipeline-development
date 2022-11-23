<#include "../../../../include/imports.ftl">
<#--  This needs to be commented out for now because the ItineraryMap component is currently excluded from the build  -->
<#include "../../../../frontend/components/vs-itinerary-map.ftl">
<#include "../../../../frontend/components/vs-itinerary-map-marker.ftl">

<#-- @ftlvariable name="stopDocument" type="com.visitscotland.brxm.hippobeans.Stop" -->
<#-- @ftlvariable name="stop" type="com.visitscotland.brxm.model.ItineraryStopModule" -->

<#macro itineraryMap itinerary>
    <vs-itinerary-map
        slot="map"
        overview-map-longitude="57.81"
        overview-map-latitude="-4.13"
        overview-map-zoom="5"
        :stops="[
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
        :labels="{
            stopLabel: '${label('itinerary', 'stop.title')}',
            mapControlsFullscreenOpen:'${label('map', 'map.fullscreen')}',
            mapControlsFullscreenClose: '${label('map', 'map.exitfullscreen')}',
            mapControlsCompass: '${label('map', 'map.reset')}',
            mapControlsZoomIn: '${label('map', 'map.zoomin')}',
            mapControlsZoomOut: '${label('map', 'map.zoomout')}'
        }"
    >
    </vs-itinerary-map>

</#macro>