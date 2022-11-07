<template>
    <div
        class="vs-summary-box"
        data-test="vs-summary-box-list"
    >
        <ul class="vs-summary-box__list list-unstyled d-flex flex-wrap p-2 p-sm-3">
            <!-- Default slot, should load in a set of 4 SummaryBoxListItem and
            SummaryBoxDistanceListItem elements -->
            <slot />
        </ul>
    </div>
</template>

<script>
/**
 * A Summary Box provides a brief summary of factual info about the content on a page. Sub-modules
 * allow for specific types of information like distances.
 *
 * @displayName Summary Box
 */
export default {
    name: 'VsSummaryBoxList',
    status: 'prototype',
    release: '0.0.1',
};
</script>

<style lang="scss">
.vs-summary-box {
    display: flex;
    justify-content: center;

    @include media-breakpoint-up(lg) {
        position: absolute;
        top: 0;
        width: calc(100% - #{$spacer-8});
        z-index: 1000;
    }
}

.vs-summary-box__list {
    background-color: $color-yellow;
    width: 350px;

    @include media-breakpoint-up(lg) {
        width: 400px;
    }
}
</style>
<docs>

  ```jsx
  <div class="position-relative" style="height: 400px;">
    <VsSummaryBoxList>
       <VsSummaryBoxListItem
            :text=itineraries.sampleItinerary.totalDays
            label="Days"
        />
        <VsSummaryBoxDistanceListItem
            :miles=itineraries.sampleItinerary.totalMiles
            :kilometres=itineraries.sampleItinerary.totalKM
            distance-label="Distance"
            miles-label="miles"
            kilometres-label="km"
        >
        </VsSummaryBoxDistanceListItem>
        <VsSummaryBoxListItem
            :icon=itineraries.sampleItinerary.transport.key
            :iconLabel=itineraries.sampleItinerary.transport.value
            label="Transport"
        />
        <VsSummaryBoxListItem
            :icon=itineraries.sampleItinerary.theme.key
            :iconLabel=itineraries.sampleItinerary.theme.value
            label="Main theme"
        />
    </VsSummaryBoxList>
    </div>
  ```
</docs>
