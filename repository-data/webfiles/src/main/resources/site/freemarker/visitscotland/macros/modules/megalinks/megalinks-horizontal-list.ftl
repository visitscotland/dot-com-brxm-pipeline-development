<#include "../../../../include/imports.ftl">

<#macro horizontalList item>
    <ol>
        <vs-row>
            <<<----- Prev
            <#list item.links as megalink>
                <#if megalink.image.cmsImage??>
                    <#assign image>
                        <@hst.link hippobean=megalink.image.cmsImage.original/>
                    </#assign>
                <#else>
                    <#assign image = megalink.image.externalImage!'' />
                </#if>
                <vs-col cols="12" lg="3">
                    <vs-link href="${megalink.link}">
                        <vs-row>
                            <vs-col >
                                <vs-img alt="${(megalink.image.altText)!'${label("essentials.global", "default.alt-text")}'}"
                                        src="${image}">
                                </vs-img>

                                <#if megalink.itineraryTransport??>
                                    <vs-heading level="7">Transport: ${megalink.itineraryTransport}  <vs-icon name="${megalink.itineraryTransport}" variant="dark" size="lg"></vs-icon>  </vs-heading>
                                    <vs-heading level="7">Days: ${megalink.itineraryDays} days</vs-heading>
                                </#if>
                                <vs-heading level="3">${megalink.label}</vs-heading>

                            </vs-col>
                            </br></br>
                        </vs-row>
                    </vs-link>
                    <vs-heading level="5">${megalink.category}</vs-heading>
                </vs-col>
            </#list>
            Next ----->>>
        </vs-row>
    </ol>

    </vs-row>


</#macro>