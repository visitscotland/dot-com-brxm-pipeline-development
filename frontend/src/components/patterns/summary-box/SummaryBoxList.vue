<template>
    <div
        class="vs-summary-box__wrapper"
        data-test="vs-summary-box-list"
    >
        <ul class="vs-summary-box__list list-unstyled d-flex flex-wrap p-2 p-sm-3">
            <slot />
        </ul>
    </div>
</template>

<script>
/**
 * Summary Box List - Wraps a number of VsSummaryBoxListItem components
 *
 * @displayName Summary Box List
 */
export default {
    name: 'VsSummaryBoxList',
    status: 'prototype',
    release: '0.0.1',
    components: {
    },
    props: {
    },
};
</script>

<style lang="scss">
.vs-summary-box__list {
    background-color: $color-yellow;
    width: 350px;
    @include media-breakpoint-up(lg) {
        width: 400px;
    }
}
.vs-summary-box__wrapper {
    display: flex;
    justify-content: center;
    @include media-breakpoint-up(lg) {
        position: absolute;
        top: 0;
        width: calc(100% - 2rem);
        z-index: 1000;
    }
}
</style>
<docs>

  ```jsx
  <div class="position-relative" style="height: 400px;">
    <VsSummaryBoxList>
       <VsSummaryBoxListItem>
            <VsSummaryBoxDisplay
                :text=itineraries.sampleItinerary.totalDays
            />
            <VsSummaryBoxLabel
                label="Days"
            />
        </VsSummaryBoxListItem>
        <VsSummaryBoxListItem>
            <VsSummaryBoxDistanceDisplay
                :miles=itineraries.sampleItinerary.totalMiles
                :kilometres=itineraries.sampleItinerary.totalKM
                miles-label="miles"
                kilometres-label="kilometres"
            />
            <VsSummaryBoxDistanceLabel
                distance-label="Distance"
                kilometres-abbr="km"
                kilometres-label="kilometres"
                miles-abbr="mi"
                miles-label="miles"
            />
        </VsSummaryBoxListItem>
        <VsSummaryBoxListItem>
            <VsSummaryBoxIconWithLabel
                :icon=itineraries.sampleItinerary.transport.key
                :label=itineraries.sampleItinerary.transport.value
            />
            <VsSummaryBoxLabel
                label="Transport"
            />
        </VsSummaryBoxListItem>
        <VsSummaryBoxListItem>
            <VsSummaryBoxIconWithLabel
                :icon=itineraries.sampleItinerary.theme.key
                :label=itineraries.sampleItinerary.theme.value
            />
            <VsSummaryBoxLabel
                label="Main theme"
            />
        </VsSummaryBoxListItem>
    </VsSummaryBoxList>
    </div>
  ```
</docs>
