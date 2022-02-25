<template>
    <li
        class="vs-summary-box-distance position-relative"
        data-test="vs-summary-box-distance-list-item"
    >
        <div
            class="text-center vs-summary-box-distance__label d-block position-absolute w-100"
            data-test="vs-summary-box-distance-list-item-label"
            v-if="distanceLabel"
        >
            <span class="vs-summary-box-distance__label-inner">{{ distanceLabel }}</span>
        </div>

        <div
            class="vs-summary-box-distance__display position-absolute d-block text-center w-100"
            data-test="vs-summary-box-distance-display"
        >
            <div class="vs-summary-box-distance__display-entry">
                <span class="vs-summary-box-distance__display-value">
                    {{ miles }}
                </span>
                <span class="vs-summary-box-distance__display-label">{{ milesLabel }}</span>
            </div>
            <hr class="vs-summary-box-distance__divider">
            <div class="vs-summary-box-distance__display-entry">
                <span class="vs-summary-box-distance__display-value">
                    {{ kilometres }}
                </span>
                <span class="vs-summary-box-distance__display-label">{{ kilometresLabel }}</span>
            </div>
        </div>
    </li>
</template>

<script>
/**
 * Summary Box Distance List Item
 *
 * @displayName Summary Box Distance List Item
 */
export default {
    name: 'VsSummaryBoxDistanceListItem',
    status: 'prototype',
    release: '0.0.1',
    props: {
        /**
         * The number of miles the summary box covers
         */
        miles: {
            type: String,
            default: '',
        },
        /**
         * The number of kilometres the summary box covers
         */
        kilometres: {
            type: String,
            default: '',
        },
        /**
         * The main label for the box, should generally be a
         * localised version of "Distance"
         */
        distanceLabel: {
            type: String,
            default: 'Distance',
        },
        /**
         * The word for `miles` in the current language
         */
        milesLabel: {
            type: String,
            default: 'miles',
        },
        /**
         * The word for `kilometres` in the current language
         */
        kilometresLabel: {
            type: String,
            default: 'km',
        },
        /**
         * Whether the summary box should default to miles
         * If false it will default to km
         */
        showMiles: {
            type: Boolean,
            default: true,
        },
    },
};
</script>

<style lang="scss" scoped>
.vs-summary-box-distance {
    flex-grow: 1;
    flex-basis: calc(50% - #{$spacer-4});
    margin: $spacer-2;

    @include media-breakpoint-up(md) {
        flex-grow: 1;
        flex-basis: calc(50% - #{$spacer-6});
        margin: $spacer-3;
    }

    &::after {
        border: 1px solid $color-theme-dark;
        content: "";
        display: block;
        padding-bottom: 100%;
    }
}

.vs-summary-box-distance__display {
    top: 35%;
    font-size: $small-font-size;

    .vs-summary-box-distance__display-entry {
        width: 80%;
        text-align: left;
        margin: 0 auto;
        font-size: 0;

        @include media-breakpoint-up(lg) {
            width: 60%;
        }
    }

    .vs-summary-box-distance__display-value {
        font-size: $font-size-lg;
        display: inline-block;
        width: 60%;
        text-align: left;
        vertical-align: middle;
        font-family: $headings-font-family;
        font-weight: $font-weight-bold;
    }

    .vs-summary-box-distance__display-label {
        font-size: $font-size-base;
        display: inline-block;
        width: 40%;
        text-align: right;
        vertical-align: middle;
        text-transform: lowercase;
    }
}

.vs-summary-box-distance__divider {
    width: 85%;
    border-top: 1px solid rgba(0, 0, 0, 0.2);
    margin-top: $spacer-1;
    margin-bottom: $spacer-1;

    @include media-breakpoint-up(lg) {
        margin-top: $spacer-2;
        margin-bottom: $spacer-2;
    }
}

.vs-summary-box-distance__label {
    top: $spacer-3;
    left: 0;
    line-height: $line_height_xs;

    @include media-breakpoint-up(lg) {
        top: $spacer-2;
    }

    .vs-summary-box-distance__label-inner {
        display: block;
        font-size: $small-font-size;
        line-height: $line_height_standard;
        font-weight: $font-weight-bold;

        @include media-breakpoint-up(lg) {
            font-size: $font-size-md
        }
    }
}
</style>

<docs>
  ```jsx
    <div class="position-relative" style="height: 200px;">
        <VsSummaryBoxList>
            <VsSummaryBoxDistanceListItem
                :miles=itineraries.sampleItinerary.totalMiles
                :kilometres=itineraries.sampleItinerary.totalKM
                distance-label="Distance"
                miles-label="Miles"
                kilometres-label="km"
            >
            </VsSummaryBoxDistanceListItem>
            <VsSummaryBoxDistanceListItem
                :miles=itineraries.sampleItinerary.totalMiles
                :kilometres=itineraries.sampleItinerary.totalKM
                distance-label="Distance"
                miles-label="Miles"
                kilometres-label="km"
            >
            </VsSummaryBoxDistanceListItem>
        </VsSummaryBoxList>
    </div>
  ```
</docs>
