
<#include "../../../../include/imports.ftl">

<#include "./summary-box/summary-box.ftl">
<#include "../../../macros/global/cms-errors.ftl">
<#include "../../../../frontend/components/vs-hero.ftl">
<#include "../../../../frontend/components/vs-page-intro.ftl">
<#include "../../../../frontend/components/vs-hero.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-rich-text-wrapper.ftl">
<#include "../../../../frontend/components/vs-img.ftl">
<#include "../../../../frontend/components/vs-button.ftl">
<#include "../../../../frontend/components/vs-link.ftl">
<#include "../../../../frontend/components/vs-heading.ftl">
<#include "../../../../frontend/components/vs-social-share.ftl">
<#include "../../../../frontend/components/vs-description-list.ftl">
<#include "../../../../frontend/components/vs-description-list-item.ftl">
<#include "../../../../frontend/components/vs-image-with-caption.ftl">
<#include "../../../../frontend/components/vs-image-location-map.ftl">
<#include "../../shared/theme-calculator.ftl">


<#-- @ftlvariable name="content" type="com.visitscotland.brxm.hippobeans.Page" -->
<#-- @ftlvariable name="heroDetails" type="com.visitscotland.brxm.model.FlatImage" -->
<#-- @ftlvariable name="itinerary" type="com.visitscotland.brxm.model.ItineraryPage" -->

<#macro pageIntro content heroDetails="" itinerary="">

    <#assign themeName = themeCalculator(introTheme)>

    <#if content.heroImage??>
        <@hst.link var="hero" hippobean=content.heroImage.original/>
    </#if>
    
<div class="has-edit-button">
    <vs-page-intro background="${themeName}" <#if heroDetails?has_content>hero-intro</#if> <#if itinerary?has_content>is-itinerary</#if>>
        <#if heroDetails?has_content>
            <@hst.link var="heroSrc" hippobean=heroImage.cmsImage.original/>

            <vs-hero
                slot="vsIntroHero"
                alt-text="${heroDetails.altText!''}"
                credit="${heroDetails.credit!''}"
                caption="${heroDetails.description!''}"
                image-src="${heroSrc}"
                latitude="${(heroImage.coordinates.latitude)!''}"
                longitude="${(heroImage.coordinates.longitude)!''}"
            >
                <vs-img
                    src="${heroSrc}"
                    alt="${heroDetails.altText!''}"
                > </vs-img>
            </vs-hero>
        </#if>

        <template slot="vsIntroBreadcrumb">
            <@hst.include ref="breadcrumb"/>
        </template>

        <template slot="vsIntroHeading">
            ${content.title}
        </template>

        <template slot="vsIntroContent">
            <@hst.html hippohtml=content.introduction/>
        </template>

        <#if itinerary?has_content>
            <#if itinerary.firstStopLocation?has_content && itinerary.lastStopLocation?has_content>
                <template slot="vsIntroStartFinish">
                    <dt class="list-inline-item">${label("itinerary", "start-finish")}</dt>
                    <dd class="list-inline-item">${itinerary.firstStopLocation} / ${itinerary.lastStopLocation}</dd>
                </template>
            </#if>
        
            <template slot="VsIntroSummaryBox">
                <@summaryBox itinerary />
            </template>

            <#if itinerary.document.areas?has_content>
                <vs-container slot="VsIntroLower">
                    <vs-row>
                        <vs-col cols="12" lg="11" offset-lg="1">
                            <vs-description-list class="mb-6">
                                <vs-description-list-item title>
                                    ${label("itinerary", "highlights")}
                                </vs-description-list-item>
                                <#list itinerary.document.highlights as highlight>
                                    <vs-description-list-item>
                                        ${highlight}
                                    </vs-description-list-item>
                                </#list>
                            </vs-description-list>
                            <vs-description-list class="mb-8">
                                <vs-description-list-item title>
                                    ${label("itinerary", "areas-covered")}
                                </vs-description-list-item>
                                <#list  itinerary.document.areas as area>
                                    <vs-description-list-item>
                                        ${label("areas", "${area}")}
                                    </vs-description-list-item>
                                </#list>
                            </vs-description-list>
                        </vs-col>
                    </vs-row>
                </vs-container>
            </#if>
        </#if>
    </vs-page-intro>
</div>
</#macro>