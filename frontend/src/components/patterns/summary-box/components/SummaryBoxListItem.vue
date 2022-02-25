<template>
    <li
        class="vs-summary-box-item position-relative"
        data-test="vs-summary-box-list-item"
    >
        <div
            class="text-center vs-summary-box-item__label d-block position-absolute w-100"
            data-test="vs-summary-box-list-item-label"
            v-if="label"
        >
            <span class="vs-summary-box-item__label-inner">{{ label }}</span>
        </div>

        <div
            class="vs-summary-box-item__display position-absolute d-block text-center w-100"
            data-test="vs-summary-box-list-item-display"
            v-if="text"
        >
            <span>{{ text }}</span>
        </div>

        <div
            class="vs-summary-box-item__icon d-flex align-items-center
            position-absolute justify-content-center w-100"
            data-test="vs-summary-box-list-item-icon-with-label"
            v-if="icon"
        >
            <div class="icon-wrapper text-right">
                <VsIcon
                    slot="icon"
                    :name="icon"
                    variant="dark"
                    size="xl"
                />
            </div>
            <div class="text-wrapper d-block w-auto pt-4">
                <span>{{ iconLabel }}</span>
            </div>
        </div>
    </li>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';

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
        VsIcon,
    },
    props: {
        /**
         * The highlighted, large text in the centre of the list item
         */
        text: {
            type: String,
            default: '',
        },
        /**
         * Smaller text at the bottom of the list item
         */
        label: {
            type: String,
            default: '',
        },
        /**
         * An icon that appears in the centre of the list item, should
         * be used instead of text rather than as well as
         */
        icon: {
            type: String,
            default: '',
        },
        /**
         * A text label that appears next to an icon, if an icon is set
         */
        iconLabel: {
            type: String,
            default: '',
        },
    },
};
</script>

<style lang="scss" scoped>
.vs-summary-box-item {
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

.vs-summary-box-item__label {
    top: $spacer-3;
    left: 0;
    line-height: $line_height_xs;

    @include media-breakpoint-up(lg) {
        top: $spacer-2;
    }

    .vs-summary-box-item__label-inner {
        display: block;
        font-size: $small-font-size;
        line-height: $line_height_standard;
        font-weight: $font-weight-bold;

        @include media-breakpoint-up(lg) {
            font-size: $font-size-md
        }
    }
}

.vs-summary-box-item__display {
    font-family: $headings-font-family;
    font-size: $h1-font-size-md;
    bottom: 35%;

    .divider {
        font-family: $headings-font-family-thin;
    }

    @include media-breakpoint-up(lg) {
        font-size: $font-size-xl;
    }
}

.vs-summary-box-item__icon {
    top: 35%;
    flex-direction: column;
    text-align: center;

    .icon-wrapper {
        max-width: 40%;
        width: auto;
    }

    .text-wrapper {
        font-size: $small-font-size;
        line-height: $line_height_xs;
        max-width: 90%;
        word-wrap: break-word;
        flex-basis: 0;

        @include media-breakpoint-up(lg) {
            font-size: $spacer-4;
            line-height: $line_height_standard;
        }
    }
}
</style>

<docs>
  ```jsx
    <div class="position-relative" style="height: 200px;">
        <VsSummaryBoxList>
            <VsSummaryBoxListItem
                :text=itineraries.sampleItinerary.totalDays
                label="Days"
            >
            </VsSummaryBoxListItem>
            <VsSummaryBoxListItem
                :icon=itineraries.sampleItinerary.transport.key
                :iconLabel=itineraries.sampleItinerary.transport.value
                label="Transport"
            >
            </VsSummaryBoxListItem>
        </VsSummaryBoxList>
    </div>
  ```
</docs>
