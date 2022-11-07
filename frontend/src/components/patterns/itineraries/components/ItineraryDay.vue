<template>
    <div
        class="vs-itinerary-day"
    >
        <VsContainer>
            <VsRow>
                <VsCol>
                    <VsAccordionItem
                        :open-by-default="show"
                        :control-id="'itinerary-day-' + dayNumber"
                        variant="transparent"
                        item-break-point="lg"
                        class="vs-itinerary-day__list-item"
                    >
                        <template #title>
                            <!-- @slot Put the title here  -->
                            <span
                                class="vs-itinerary-day__header"
                            >
                                <span
                                    class="vs-itinerary-day__title
                                    vs-heading vs-heading--style-level-2 d-inline-block"
                                >
                                    {{ dayLabel }} {{ dayNumber }}
                                </span>

                                <span
                                    class="vs-itinerary-day__sub-heading"
                                >
                                    {{ dayTitle }}
                                </span>
                            </span>
                        </template>

                        <template #icon-open>
                            <!-- @slot Slot for the icon to show when accordion item is open  -->
                            <VsIcon
                                name="chevron"
                                orientation="down"
                                variant="dark"
                                size="xs"
                                :padding="3"
                                class="vs-itinerary-day__toggle-button"
                            />
                        </template>
                        <template #icon-closed>
                            <!-- @slot Slot for the icon to show when accordion item is closed  -->
                            <VsIcon
                                name="chevron"
                                variant="dark"
                                size="xs"
                                :padding="3"
                                class="vs-itinerary-day__toggle-button"
                            />
                        </template>
                        <div
                            :id="'dayPanel_' + dayNumber"
                            class="vs-itinerary-day__panel"
                        >
                            <div class="vs-itinerary-day__intro-content">
                                <slot name="day-transport" />
                                <slot name="day-introduction" />
                            </div>
                            <ul class="list-unstyled">
                                <slot name="stops" />
                            </ul>
                        </div>
                    </VsAccordionItem>
                </VsCol>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import {
    VsContainer,
    VsRow,
    VsCol,
} from '@components/elements/grid';
import VsIcon from '@components/elements/icon/Icon';
import VsAccordionItem from '@components/patterns/accordion/components/AccordionItem';

/**
 * Itinerary Day list items.
 *
 * @displayName Itinerary Day
 */

export default {
    name: 'VsItineraryDay',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsContainer,
        VsRow,
        VsCol,
        VsIcon,
        VsAccordionItem,
    },
    props: {
        /**
         * Logic to collapse certain Day list items on mobile by default
         * (e.g. after Day 1 and 2, collapse the days on mobile)
         */
        defaultShow: {
            type: Boolean,
            default: true,
        },
        /**
         * Label used for the word 'Day'
         */
        dayLabel: {
            type: String,
            required: true,
        },
        /**
         * Number of the day in the component
         */
        dayNumber: {
            type: String,
            required: true,
        },
        /**
         * Title of the day in the component
         */
        dayTitle: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            show: this.defaultShow,
        };
    },
};
</script>

<style lang="scss">
.vs-itinerary-day{
    border-top: 5px solid $color-base-text;

    @include media-breakpoint-down(xs) {
        max-width: initial;
        width: calc(100% + #{$spacer-4});
        margin-left: -#{$spacer-2};
        padding: $spacer-0 $spacer-2;
    }

    &:first-of-type {
        @include media-breakpoint-up(md) {
            border-top: none;
        }
    }

    &__list-item.card {
        width: calc(100% + #{$spacer-4});
        margin-left: -#{$spacer-2};
        padding: 0 $spacer-3 0;

        @include media-breakpoint-up(md) {
            width: calc(100% + #{$spacer-6});
            margin-left: -#{$spacer-3};
            padding: $spacer-4 $spacer-4 0;
        }
    }

    &__header{
        display: block;
        text-align: center;
        margin: $spacer-5 0 $spacer-5;

        .vs-itinerary-day__title  {
            border-bottom: 1px solid $color-base-text;
            color: $color-secondary-teal-shade-3;
            padding: 0 $spacer-6 $spacer-3;
            margin-bottom: $spacer-4;
        }

        .vs-itinerary-day__sub-heading{
            letter-spacing: .15rem;
            font-size: $font-size-8;
            font-family: $headings-font-family-thin;
            font-weight: $font-weight-normal;
            display: block;
        }
    }

    &__toggle-button {
        border: 1px solid $color-base-text;
        border-radius: 50%;
        height: 24px;
        width: 24px;

        .vs-icon {
            height: 100%;
            margin: 0 auto;
            display: block;
        }

        &.vs-icon.vs-icon--size-xs {
            height: 32px;
            width: 32px;
            padding: 8px;
        }
    }

    &__intro-content {
        padding: $spacer-0 $spacer-5;
        margin-bottom: $spacer-9;

        @include media-breakpoint-up(md) {
            padding: $spacer-0;
            margin-bottom: $spacer-0;
        }
    }

    &__panel .list-inline-item:not(:last-child) {
        @include media-breakpoint-down(xs) {
            margin-right: $spacer-1;
        }
    }
}

@include no-js {
    @include media-breakpoint-down(lg) {
        .vs-itinerary-day__list-item {
            .vs-itinerary-day__header:first-child {
                display: none !important;
            }
        }
    }
}
</style>

<docs>
```jsx
        <VsItineraryDay
            v-for="(day, index) in itineraries.sampleItinerary.days"
            :defaultShow="(day.dayCount < 3) ? true : false"
            :key="index"
            :dayNumber="day.dayCount"
            dayLabel="Day"
            slot="list"
            :dayTitle="day.title"
        >
            <VsDescriptionList
                v-if="day.transport.length"
                class="text-center justify-content-center align-items-center"
                slot="day-transport"
            >
                <dt class="list-inline-item">Transport:</dt>
                <dd
                    class="list-inline-item"
                    v-for="(transportType, transportTypeIndex) in day.transport"
                >
                    <VsTooltip
                        :title="transportType.value"
                        href="#"
                        :icon="transportType.key"
                        size="lg"
                        icon-only
                        class="p-0"
                        variant="transparent"
                    >
                        <span class="sr-only">
                            {{transportType.value}}
                        </span>
                    </VsTooltip>
                </dd>
            </VsDescriptionList>

            <div slot="day-introduction" v-html="day.introduction"></div>
        </VsItineraryDay>
```
</docs>
