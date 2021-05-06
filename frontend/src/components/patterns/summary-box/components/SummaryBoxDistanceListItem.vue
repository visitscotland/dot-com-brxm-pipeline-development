<template>
    <li
        class="vs-summary-box-distance position-relative"
        data-test="vs-summary-box-distance-list-item"
    >
        <div
            class="vs-summary-box-distance__display position-absolute d-block text-center w-100"
            data-test="vs-summary-box-distance-display"
        >
            <span
                v-if="showingMiles"
                id="display_miles"
            >{{ miles }}</span>
            <span
                v-else
                id="display_kilometres"
            >{{ kilometres }}</span>
        </div>
        <div
            class="vs-summary-box-distance__label text-center d-block position-absolute w-100"
        >
            <span class="vs-summary-box-distance__label-inner d-block">{{ distanceLabel }}</span>
            <div class="d-flex justify-content-center align-items-center">
                <VsButton
                    @click.native="toggleShowMiles(true)"
                    :class="showingMiles ? 'active' : ''"
                    :aria-expanded="showingMiles ? 'true' : 'false'"
                    variant="transparent"
                    aria-controls="display_miles"
                >
                    <abbr :title="milesLabel">{{ milesAbbr }}</abbr>
                </VsButton>
                <span class="separator">/</span>
                <VsButton
                    @click.native="toggleShowMiles(false)"
                    :class="showingMiles ? '' : 'active'"
                    :aria-expanded="showingMiles ? 'false' : 'true'"
                    variant="transparent"
                    aria-controls="display_kilometres"
                >
                    <abbr :title="kilometresLabel">{{ kilometresAbbr }}</abbr>
                </VsButton>
            </div>
        </div>
    </li>
</template>

<script>
import VsButton from '@components/elements/button/Button';

/**
 * Summary Box Distance List Item
 *
 * @displayName Summary Box Distance List Item
 */
export default {
    name: 'VsSummaryBoxDistanceListItem',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
    },
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
         * The abbreviation for `miles` in the current language
         * e.g. 'mi'
         */
        milesAbbr: {
            type: String,
            default: 'mi',
        },
        /**
         * The word for `kilometres` in the current language
         */
        kilometresLabel: {
            type: String,
            default: 'kilometres',
        },
        /**
         * The abbreviation for `kilometres` in the current language
         * e.g. 'km'
         */
        kilometresAbbr: {
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
    data() {
        return {
            /**
             * Whether the box is currently showing miles or km
             */
            showingMiles: this.showMiles,
        };
    },
    methods: {
        /**
         * Modifies the showingMiles data and toggles between miles and km
         */
        toggleShowMiles(value) {
            this.showingMiles = value;
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
    font-family: $headings-font-family;
    font-size: 1.625rem;
    top: 40%;
    transform: translateY(-50%);

    @include media-breakpoint-up(lg) {
        font-size: $spacer-7;
    }

    span {
        display: block;
        padding: 0 $spacer-1;
        word-break: break-word;
        line-height: $line_height_xs;
    }
}

.vs-summary-box-distance__label {
    bottom: $spacer-3;
    left: 0;
    line-height: $line_height_xs;

    @include media-breakpoint-up(lg) {
        line-height: $line_height_standard;
    }

    .vs-summary-box-distance__label-inner,
    .btn {
        font-size: $small-font-size;

        @include media-breakpoint-up(lg) {
            font-size: $lead-font-size;
        }
    }

    .vs-summary-box-distance__label-inner {
        font-weight: $font-weight-bold;
    }

    .btn {
        padding: 0 2px;
        text-transform: initial !important;
        letter-spacing: initial !important;
        text-decoration: underline;

        &.active {
            font-weight: $font-weight-bold;
            text-decoration: underline;
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
                miles-label="miles"
                miles-abbr="mi"
                kilometres-label="kilometres"
                kilometres-abbr="km"
            >
            </VsSummaryBoxDistanceListItem>
            <VsSummaryBoxDistanceListItem
                :miles=itineraries.sampleItinerary.totalMiles
                :kilometres=itineraries.sampleItinerary.totalKM
                distance-label="Distance"
                miles-label="miles"
                miles-abbr="mi"
                kilometres-label="kilometres"
                kilometres-abbr="km"
            >
            </VsSummaryBoxDistanceListItem>
        </VsSummaryBoxList>
    </div>
  ```
</docs>
