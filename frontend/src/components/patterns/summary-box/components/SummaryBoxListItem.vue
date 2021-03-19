<template>
    <li
        class="vs-summary-box-list-item position-relative"
        data-test="vs-summary-box-list-item"
    >
        <slot />
    </li>
</template>

<script>
/**
 * Summary Box List Item
 *
 * @displayName Summary Box List Item
 */
export default {
    name: 'VsSummaryBoxListItem',
    status: 'prototype',
    release: '0.0.1',
    components: {
    },
    props: {
    },
};
</script>

<style lang="scss" scoped>
.vs-summary-box-list-item {
    flex-grow: 1;
    flex-basis: calc(50% - 1rem);
    margin: 0.5rem;
    @include media-breakpoint-up(md) {
        flex-grow: 1;
        flex-basis: calc(50% - 1.5rem);
        margin: 0.75rem;
    }

    &::after {
        border: 1px solid $color-theme-dark;
        content: "";
        display: block;
        padding-bottom: 100%;
    }
}
</style>

<docs>
  ```jsx
    <ul style="width: 200px; list-style-type: none;">
       <VsSummaryBoxListItem>
            <VsSummaryBoxDisplay
                :text=itineraries.sampleItinerary.totalDays
            />
            <VsSummaryBoxLabel
                label="Days"
            />
        </VsSummaryBoxListItem>
    </ul>
  ```
</docs>
