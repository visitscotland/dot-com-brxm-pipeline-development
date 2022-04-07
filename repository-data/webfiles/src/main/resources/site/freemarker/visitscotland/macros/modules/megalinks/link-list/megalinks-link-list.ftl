<#include "../../../../../include/imports.ftl">
<#include "../../../../../frontend/components/vs-megalink-link-list.ftl">

<#macro linkList item showTeaser theme>
    <vs-row>
        <vs-col
            cols="12"
            lg="10"
            class="offset-lg-1"
        >
            <vs-row>
                <#list item.links as listItem>
                    <#if listItem.image.cmsImage??>
                        <#assign image>
                            <@hst.link hippobean=listItem.image.cmsImage.original/>
                        </#assign>
                    <#else>
                        <#assign image>
                            ${listItem.image.externalImage}
                        </#assign>
                    </#if>

                    <vs-col
                        cols="12"
                        md="6"
                    >
                        <vs-megalink-link-list
                            img-src="${image}"
                            link-type="${listItem.type}"
                            theme="${theme}"
                            link-url="${listItem.link}"
                            <#if listItem.itineraryTransport??>
                                transport="${listItem.itineraryTransport}"
                                transport-name="${label('transports', listItem.itineraryTransport)}"
                            </#if>
                            <#if listItem.itineraryDays??>
                                <#if listItem.itineraryDays = 1>
                                    days-label="${label('itinerary', 'day')}"
                                <#else>
                                    days-label="${label('itinerary', 'days')}"
                                </#if>
                                days="${listItem.itineraryDays}"
                            <#else>
                                days-label="${label('itinerary', 'day')}"
                            </#if>
                        >
                            <template slot="vsLinkListHeading">
                                ${listItem.label}
                            </template>
                            <#if showTeaser == 'true'>
                                <template slot="vsLinkListContent">
                                    <p>${listItem.teaser}</p>
                                </template>
                            </#if>
                        </vs-megalink-link-list>
                    </vs-col>
                </#list>
            </vs-row>
        </vs-col>
    </vs-row>
</#macro>