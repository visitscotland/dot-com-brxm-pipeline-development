<template>
    <div
        class="vs-summary-box-display position-absolute d-block text-center w-100"
        data-test="vs-summary-box-display"
    >
        <span>{{ text }}</span>
    </div>
</template>

<script>
/**
 * Summary Box Display
 *
 * @displayName Summary Box Display
 */
export default {
    name: 'VsSummaryBoxDisplay',
    status: 'prototype',
    release: '0.0.1',
    components: {
    },
    props: {
        text: {
            type: String,
            default: '',
        },
    },
};
</script>

<style lang="scss">
.vs-summary-box-display {
    top: 20%;
    font-family: $headings-font-family;
    font-size: 1.625rem;
    .divider {
        font-family: $headings-font-family-thin;
    }
    @include media-breakpoint-up(lg) {
        font-size: 1.75rem;
    }
}
</style>

<docs>

  ```jsx
    <div class="py-2">
        <VsSummaryBoxDisplay
            :text=itineraries.sampleItinerary.totalDays
        />
    </div>
  ```
</docs>
