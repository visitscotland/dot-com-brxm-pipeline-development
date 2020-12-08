

<#include "../../../../include/imports.ftl">
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


<#macro pageIntro content heroImage heroCoordinates hero theme areas days firstStop lastStop>

<#if theme="theme1">
    <#assign themeName = "dark">
<#else>
    <#assign themeName = "light">
</#if>

<vs-page-intro background="${themeName}">
<vs-hero
        slot="hero"
        alt-text="${heroImage.altText!''}"
        credit="${heroImage.credit!''}"
        caption="${heroImage.description!''}"
        image-src="${hero}"
        latitude="${(heroCoordinates.latitude)!''}"
        longitude="${(heroCoordinates.longitude)!''}"
>
    <vs-img
            src="${hero}"
            alt="${heroImage.altText!''}"
    > </vs-img>
</vs-hero>
<vs-container slot="upper" class="py-lg-4">
			<vs-row class="justify-content-md-between">
				<vs-col cols="12" lg="8" offset-lg="1">
					<@hst.include ref="breadcrumb"/>
				</vs-col>
			</vs-row>

			<vs-row>
				<vs-col cols="10" lg="8" offset-lg="1">
					<vs-heading level="1">${document.title}</vs-heading>
				</vs-col>
				<vs-col cols="2">
					<div class="d-flex justify-content-center justify-content-sm-end">
						<vs-social-share></vs-social-share>
					</div>
				</vs-col>
			</vs-row>
            <vs-row>
                <#if days?has_content>
                <vs-col cols="12" md="6" lg="5" xl="6" offset-lg="1">
                <#else>
                <vs-col cols="12" md="10" lg="10" xl="10" offset-lg="1">
                </#if>
                    <vs-rich-text-wrapper variant="lead">
                        <@hst.html hippohtml=document.introduction/>
                    </vs-rich-text-wrapper>

                    <#if firstStop?has_content && lastStop?has_content>
                    <dl class="list-inline">
                        <dt class="list-inline-item">${label("itinerary", "start-finish")}</dt>
                        <dd class="list-inline-item">${firstStop} / ${lastStop}</dd>
                    </dl>
                    </#if>
                </vs-col>
                <#if days?has_content>
                    <vs-col cols="12" md="6" lg="5" xl="4">
                        <vs-summary-box-list>
                            <vs-summary-box-list-item>
                                <vs-summary-box-display text="${document.days?size}"/></vs-summary-box-display>
                                <vs-summary-box-label label="${label('itinerary', 'days')}"></vs-summary-box-label>
                            </vs-summary-box-list-item>
                            <vs-summary-box-list-item>
                                <vs-summary-box-distance-display
                                    miles="${distance}"
                                    kilometres="${(distance*1.6)}"
                                    miles-label="${label("itinerary", "miles")}"
                                    kilometres-label="${label("itinerary", "kilometres")}">
                                </vs-summary-box-distance-display>
                                <vs-summary-box-distance-label
                                    distance-label="${label("itinerary", "distance")}"
                                    kilometres-abbr="${label("itinerary", "kilometres-abbreviation")}"
                                    kilometres-label="${label("itinerary", "kilometres")}"
                                    miles-abbr="${label("itinerary", "miles-abbreviation")}"
                                    miles-label="${label("itinerary", "miles")}">
                                </vs-summary-box-distance-label>
                            </vs-summary-box-list-item>
                                <vs-summary-box-list-item>
                                <vs-summary-box-icon-with-label
                                    icon="${mainTransport}"
                                    label="${label("transports", "${mainTransport}")}">
                                </vs-summary-box-icon-with-label>
                                <vs-summary-box-label 
                                    label="${label("itinerary", "transport")}">
                                </vs-summary-box-label>
                            </vs-summary-box-list-item>
                            <vs-summary-box-list-item>
                                <vs-summary-box-icon-with-label
                                    icon="${document.theme}"
                                    label="${label("themes", "${document.theme}")}">
                                </vs-summary-box-icon-with-label>
                                <vs-summary-box-label label="${label("itinerary", "theme")}"></vs-summary-box-label>
                            </vs-summary-box-list-item>
                        </vs-summary-box-list>
                    </vs-col>
                </#if>
            </vs-row>
			<#--  <vs-row>
				<vs-col cols="12" md="10" lg="10" xl="10" offset-lg="1">
					<vs-rich-text-wrapper variant="lead">
						<@hst.html hippohtml=document.introduction/>
					</vs-rich-text-wrapper>
				</vs-col>
			</vs-row>  -->
		</vs-container>

        <#if areas?has_content>
        TEST
            <vs-container slot="lower">
                <vs-row>
                    <vs-col cols="12" lg="11" offset-lg="1">
                        <vs-description-list class="mb-6">
                            <vs-description-list-item title>
                                ${label("itinerary", "highlights")}
                            </vs-description-list-item>
                            <#-- TODO: each ${document.highlight} should render a new dd element -->
                            <vs-description-list-item>
                                <div style="white-space: pre-wrap">${document.highlights}</div>
                            </vs-description-list-item>
                        </vs-description-list>
                        <vs-description-list class="mb-8">
                            <vs-description-list-item title>
                                ${label("itinerary", "areas-covered")}
                            </vs-description-list-item>
                            <#list areas as area>
                                <vs-description-list-item>
                                    ${label("areas", "${area}")}${"\n"}
                                </vs-description-list-item>
                            </#list>
                        </vs-description-list>
                    </vs-col>
                </vs-row>
            </vs-container>
        </#if>
</vs-page-intro>
</#macro>