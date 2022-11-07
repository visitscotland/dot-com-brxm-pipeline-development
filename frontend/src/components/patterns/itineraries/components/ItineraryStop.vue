<template>
    <li
        class="vs-itinerary-stop"
        data-test="vs-itinerary-stop"
    >
        <div class="border">
            <div
                class="d-flex justify-content-start align-items-top"
                data-test="vs-itinerary-stop-heading"
            >
                <div class="position-relative">
                    <VsIcon
                        name="map-marker-filled"
                        variant="secondary-teal"
                        size="xl"
                    />
                    <span
                        class="map-marker__count"
                        aria-hidden="true"
                        data-test="vs-itinerary-stop-marker"
                    >{{ stopNumber }}</span>
                </div>

                <VsHeading
                    level="3"
                    class="vs-itinerary-stop__title ml-4 mt-0"
                >
                    {{ stopLabel }}

                    <template slot="sub-heading">
                        {{ stopTitle }}
                    </template>
                </VsHeading>
            </div>

            <!-- @ Default slot for the image component for the stop -->
            <slot />

            <!-- @slot The description content for the stop -->
            <slot name="stop-description" />

            <!-- @slot The tips content for the stop -->
            <slot name="stop-tips" />

            <div class="vs-itinerary-stop__details-container">
                <div
                    class="vs-itinerary-stop__address"
                    v-if="!!this.$slots['stop-address']"
                    data-test="vs-itinerary-stop-address"
                >
                    <!-- @slot The address component for the stop -->
                    <slot name="stop-address" />
                </div>

                <!-- @slot The opening hours components for the stop -->
                <slot name="stop-info" />
            </div>

            <!-- @slot The facilities content for the stop -->
            <slot name="stop-facilities" />
        </div>
        <template v-if="!!this.$slots['stop-buttons']">
            <!-- @slot The button content for the stop -->
            <slot name="stop-buttons" />
        </template>
    </li>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
import VsHeading from '@components/elements/heading/Heading';

/**
 * Itinerary Day list items.
 *
 * @displayName Itinerary Stop
 */

export default {
    name: 'VsItineraryStop',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsHeading,
        VsIcon,
    },
    props: {
        /**
         * Label used for the word 'Stop'
         */
        stopLabel: {
            type: String,
            required: true,
        },
        /**
         * Number of the stop in the component
         */
        stopNumber: {
            type: String,
            required: true,
        },
        /**
        * Title of the stop in the component
        */
        stopTitle: {
            type: String,
            required: true,
        },

    },
};
</script>

<style lang="scss">
.vs-itinerary-stop {
    margin-bottom: 3.125rem;

    &:last-of-type {
        margin-bottom: 0;

        .border {
            padding-bottom: $spacer-8;
        }
    }

    .border {
        position: relative;
        background-color: $color-white;
        border: 1px solid $color-gray-tint-5;
        padding: $spacer-4;
    }

    @include media-breakpoint-up(sm) {
        margin-bottom: 6.35rem;
        &:last-of-type {
            margin-bottom: 0;
        }
    }
    .map-marker__count {
        color: $color-white;
        font-family: $headings-font-family;
        font-size: $font-size-4;
        display: block;
        position: absolute;
        top: 4px;
        left: 0;
        text-align: center;
        width: 100%;
    }

    .vs-icon-list {
        border-top: 1px solid $color-gray-tint-5;
        margin-top: $spacer-4;

        .vs-icon-list__title {
            margin-top: $spacer-4;
        }
    }

    &__address {
        @include media-breakpoint-up(md) {
            border-right: 1px solid $color-gray-tint-5;
            margin-bottom: -#{$spacer-4};
            padding: $spacer-4 $spacer-0;
            margin-right: $spacer-6;
        }
    }

    &__address,
    &__info {
        @include media-breakpoint-up(md) {
            width: 50%;
        }
    }

    &__details-container {
        @include media-breakpoint-up(md) {
            display: flex;
        }
    }

    &__info {
        @include media-breakpoint-up(md) {
            padding: $spacer-6 0;
        }
    }
}
</style>

<docs>
```jsx
    <VsContainer>
        <VsRow>
            <VsCol cols="12" lg="6">
                <ul style="list-style-type: none; padding: 0;">
                    <VsItineraryStop
                        v-for="(stop, index) in itineraries.sampleItinerary.days[0].stops"
                        :key="index"
                        :stopNumber="stop.stopCount"
                        stopLabel="Stop"
                        :stopTitle="stop.title"
                    >
                        <VsImageWithCaption
                            :altText="stop.image.altText"
                            :image-src="stop.image.imageSrc"
                        >
                            <VsCaption
                                slot="img-caption"
                            >
                                <span slot="caption">
                                    {{ stop.image.caption }}
                                </span>

                                <span slot="credit">
                                    &copy; {{ stop.image.credit }}
                                </span>
                            </VsCaption>
                        </VsImageWithCaption>
                        <div v-html="stop.description" slot="stop-description"></div>
                        <VsLink href="stop.href">
                            Find out more
                        </VsLink>
                        <VsDescriptionList class="my-4 mb-0 justify-content-start" inline>
                            <VsDescriptionListItem title class="mb-0 mr-0 pr-1 col-auto">
                                Time to explore
                            </VsDescriptionListItem>
                            <VsDescriptionListItem class="mb-0 col-auto px-0">
                                {{stop.timeToExplore}}
                            </VsDescriptionListItem>
                        </VsDescriptionList>
                        <VsItineraryTips
                            v-if="stop.tips.tipsTitle.length > 0 && stop.tips.tipsBody.length > 0"
                            slot="stop-tips"
                        >
                            <div slot="text">
                                <strong>{{stop.tips.tipsTitle}}</strong>
                                <div v-html="stop.tips.tipsBody"></div>
                            </div>
                            <VsSvg slot="svg" path="highland-cow" />
                        </VsItineraryTips>
                        <template slot="stop-address">
                            <VsAddress v-if="stop.address && stop.address.line1">
                                <span v-if="stop.address.line1">{{ stop.address.line1 }},<br></span>
                                <span v-if="stop.address.line2">{{ stop.address.line2 }},<br></span>
                                <span v-if="stop.address.line3">{{ stop.address.line3 }},<br></span>
                                <span v-if="stop.address.city">{{ stop.address.city }},<br></span>
                                <span
                                    v-if="stop.address.postcode"
                                >
                                    {{ stop.address.postcode }}
                                </span>
                            </VsAddress>
                        </template>

                        <VsItineraryStopInfo
                            :openingHours="itineraries.sampleItinerary.openingHours"
                            openingTimesLink="https://www.visitscotland.com"
                            closedText="Closed"
                            closingSoonText="Closing soon"
                            openText="Open"
                            hoursLinkUrl="https://www.visitscotland.com"
                            usualText="Usually"
                            provisionalText="Provisionally"
                            slot="stop-info"
                            temporarilyClosedText="Temporarily closed"
                            toText="to"
                            andText="and"
                        >
                            <template slot="stop-to">
                                to
                            </template>

                            <template slot="stop-link-text">
                                Check opening times
                            </template>

                            <template slot="stop-charge-text">
                                Admission charge
                            </template>
                        </VsItineraryStopInfo>

                        <VsIconList
                            v-if="stop.facilities.length"
                            title="Key facilities"
                            slot="stop-facilities"
                        >
                            <VsIconListItem
                                v-for="(facility, facilitiesIndex) in stop.facilities"
                                :key="facilitiesIndex"
                                :label="facility.value"
                                :icon="facility.key"
                                />
                        </VsIconList>
                        <!-- mimic only showing these links on the last stop of the day -->
                        <VsItineraryBorderOverlapWrapper
                            slot="stop-buttons"
                            v-if="index == itineraries.sampleItinerary.days[0].stops.length - 1"
                        >
                            <VsButton
                                class="mb-3"
                                button-size="md"
                                variant="secondary"
                                href="#"
                                icon="food"
                            >
                                Nearby places to eat
                            </VsButton>
                            <VsButton
                                variant="secondary"
                                href="#"
                                icon="product-accommodation"
                            >
                                Nearby places to stay
                            </VsButton>
                        </VsItineraryBorderOverlapWrapper>
                    </VsItineraryStop>
                </ul>
            </VsCol>
        </VsRow>
    </VsContainer>
```
</docs>
