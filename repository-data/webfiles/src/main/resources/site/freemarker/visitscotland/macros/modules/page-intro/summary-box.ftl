<#include "../../../../include/imports.ftl">
<#include "../../../macros/global/cms-errors.ftl">
<#include "../../../../frontend/components/vs-summary-box-list.ftl">
<#include "../../../../frontend/components/vs-summary-box-list-item.ftl">
<#include "../../../../frontend/components/vs-summary-box-distance-list-item.ftl">

<#macro summaryBox itinerary>
<vs-col cols="12" md="6" lg="5" xl="4">
    <vs-summary-box-list>
        <#if itinerary.days?size = 1>
            <#assign daysLabel = label('itinerary', 'day')>
        <#else>
            <#assign daysLabel = label('itinerary', 'days')>
        </#if>
        <vs-summary-box-list-item
            text="${itinerary.days?size}"
            label="${daysLabel}"
        />
        </vs-summary-box-list-item>
        <vs-summary-box-distance-list-item
            miles="${itinerary.distance}"
            kilometres="${(itinerary.distance*1.6)}"
            miles-label="${label("itinerary", "miles")}"
            kilometres-label="${label("itinerary", "kilometres-abbreviation")}"
            distance-label="${label("itinerary", "distance")}"
        >
        </vs-summary-box-distance-list-item>
        <vs-summary-box-list-item
            icon="${mainTransport}"
            icon-label="${label("transports", "${mainTransport}")}"
            label="${label("itinerary", "transport")}"
        >
        </vs-summary-box-list-item>
        <vs-summary-box-list-item
            icon="${itinerary.document.theme}"
            icon-label="${label("themes", "${itinerary.document.theme}")}"
            label="${label("itinerary", "theme")}"
        >
        </vs-summary-box-list-item>
    </vs-summary-box-list>
</vs-col>
</#macro>