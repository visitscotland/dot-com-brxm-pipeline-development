<#include "../../../../../include/imports.ftl">
<#include "../../../../macros/global/cms-errors.ftl">
<#include "../../../../../frontend/components/vs-summary-box-list.ftl">
<#include "../../../../../frontend/components/vs-summary-box-list-item.ftl">
<#include "../../../../../frontend/components/vs-summary-box-display.ftl">
<#include "../../../../../frontend/components/vs-summary-box-label.ftl">
<#include "../../../../../frontend/components/vs-summary-box-distance-display.ftl">
<#include "../../../../../frontend/components/vs-summary-box-distance-label.ftl">
<#include "../../../../../frontend/components/vs-summary-box-icon-with-label.ftl">

<#macro summaryBox days>
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
</#macro>