<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-itinerary-stop.ftl">
<#include "../../../../frontend/components/vs-itinerary-tips.ftl">
<#include "../../../../frontend/components/vs-itinerary-border-overlap-wrapper.ftl">
<#include "../../../../frontend/components/vs-description-list.ftl">
<#include "../../../../frontend/components/vs-description-list-item.ftl">
<#include "../../../../frontend/components/vs-link.ftl">
<#include "../../../../frontend/components/vs-svg.ftl">
<#include "../../../../frontend/components/vs-button.ftl">
<#include "../../../../frontend/components/vs-address.ftl">

<#include "../../global/key-facilities.ftl">
<#include "../../global/image-with-caption.ftl">
<#include "../../global/cms-errors.ftl">

<#macro itineraryStop stop lastStop>
<#-- @ftlvariable name="stop" type="com.visitscotland.brxm.hippobeans.Stop" -->
<#-- @ftlvariable name="prod" type="com.visitscotland.brxm.model.ItineraryStopModule" -->

    <#assign prod = itinerary.stops[stop.identifier]>
    <#assign image = "" />

    <#if prod.image??>
        <#if prod.image.cmsImage??>
            <#assign image>
                <@hst.link hippobean=prod.image.cmsImage.original/>
            </#assign>
        <#elseif prod.image.externalImage??>
            <#assign image = prod.image.externalImage />
        </#if>
    </#if>

    <vs-itinerary-stop
            slot="stops"
            stop-number="${prod.index}"
            stop-label="${prod.title}"
            stop-title="${prod.subTitle!''}"
    >
        <div slot="stop-details" class="has-edit-button">
            <@hst.manageContent hippobean=stop />
            <@cmsErrors errors=prod.errorMessages!"" editMode=editMode />
            <#if image?? && image?has_content>
                <@imageWithCaption imageSrc=image imageDetails=prod.image variant="fullwidth"/>
            </#if>

            <#if prod?? && prod.description?? && prod.description?has_content>
                <@hst.html hippohtml=prod.description/>
            </#if>

            <#if prod.ctaLink?? && prod.ctaLink.link?? && prod.ctaLink.link?has_content>
                <vs-link href="${prod.ctaLink.link}">
                    ${prod.ctaLink.label}
                </vs-link>
            </#if>
            <#--TODO show open times the field is:
        </br>
             <#if prod.openLink?? && prod.openLink.link?? && prod.openLink.link?has_content>
                 </br>OPENINGS </br>
                 ${prod.open}</br>
                <vs-link href="${prod.openLink.link}">
                    ${prod.openLink.label}
                </vs-link>
             </#if>
             -->
            <#--TODO price the field is:
        </br>
         <#if prod.price?? && prod.price?has_content>
        </br>PRICE</br>

            ${prod.price}
         </#if>
            -->

            <#if prod??>
                <#if prod.timeToExplore?? && prod.timeToExplore?has_content>
                    <vs-description-list class="my-4 mb-0 justify-content-start" inline>
                        <vs-description-list-item title class="mb-0 mr-0 pr-1 col-auto">${label("itinerary", "stop.time-to-explore")}</vs-description-list-item>
                        <vs-description-list-item class="mb-0 col-auto px-0">${prod.timeToExplore}</vs-description-list-item>
                    </vs-description-list>
                </#if>

                <#if prod.address.line1?? && prod.address.line1?has_content>
                    <vs-address>
                        <#assign addressArr = [
                            prod.address.line1!"",
                            prod.address.line2!"",
                            prod.address.line3!"",
                            prod.address.city!"",
                            prod.address.postCode!""
                        ]/>

                        <#--
                            Filter out empty strings in address

                            It would be tidier to do this within the loop below, but that
                            causes <#sep> to incorrectly assume that the postCode is always
                            a value that needs a comma before it, even if it is an empty
                            string. The ideal solution would be to iterate over

                            addressArr?filter()

                            rather than constructing a whole filtered copy of the array
                            for readability but that is not doable until we reach a future
                            version of freemarker (2.3.29).
                        -->
                        <#assign filterAddressArr = [] />
                        <#list addressArr as addrLine>
                            <#if addrLine != "">
                                <#assign filterAddressArr = filterAddressArr + [ addrLine ] />
                            </#if>
                        </#list>

                        <#list filterAddressArr as addressLine>
                            <span>${addressLine}<#sep>,</span>
                        </#list>
                    </vs-address>
                </#if>

                <#if (prod.tipsTitle?? && prod.tipsTitle?has_content)>
                    <vs-itinerary-tips>
                        <div slot="text">
                            <strong>${prod.tipsTitle}</strong>
                            <@hst.html hippohtml=prod.tipsBody/>
                        </div>
                        <vs-svg slot="svg" path="highland-cow" />
                    </vs-itinerary-tips>
                </#if>

                <#if prod.facilities?? && prod.facilities?size gt 1>
                    <@keyFacilities facilitiesList=prod.facilities />
                </#if>
            </#if>
    </div>
        <#if lastStop == 'true' && prod.coordinates.longitude?? && prod.coordinates.longitude?has_content && prod.coordinates.latitude?? && prod.coordinates.latitude?has_content>
            <#assign nearbyEatsUrl = productSearch(locale, "cate", prod.coordinates.latitude, prod.coordinates.longitude, 5)>
            <#assign nearbyStayUrl = productSearch(locale, "acco", prod.coordinates.latitude, prod.coordinates.longitude, 5)>

            <vs-itinerary-border-overlap-wrapper slot="nearby-links">
                <vs-button
                    class="mb-3"
                    background="white"
                    variant="outline-primary"
                    icon="food"
                    href="${nearbyEatsUrl}"
                >
                    ${label("itinerary", "stop.nearby-eat")}
                </vs-button>
                <vs-button
                    background="white"
                    variant="outline-primary"
                    icon="product-accommodation"
                    href="${nearbyStayUrl}"
                >
                    ${label("itinerary", "stop.nearby-stay")}
                </vs-button>
            </vs-itinerary-border-overlap-wrapper>
        </#if>
    </vs-itinerary-stop>
</#macro>