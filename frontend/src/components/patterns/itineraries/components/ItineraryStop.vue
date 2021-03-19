<template>
    <li class="vs-itinerary-stop">
        <div class="border">
            <div class="d-flex justify-content-start align-items-top">
                <div class="position-relative">
                    <VsIcon
                        name="map-marker-filled"
                        variant="secondary-teal"
                        size="xl"
                    />
                    <span
                        class="map-marker__count"
                        aria-hidden="true"
                    >{{ stopNumber }}</span>
                </div>

                <VsHeading
                    level="3"
                    class="vs-itinerary-stop__title ml-4"
                >
                    {{ stopLabel }} {{ stopNumber }}

                    <template slot="sub-heading">
                        {{ stopTitle }}
                    </template>
                </VsHeading>
            </div>
            <slot name="stop-details" />
        </div>
        <slot name="nearby-links" />
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
    data() {
        return {
        };
    },
    methods: {
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
        font-size: 1rem;
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
}
</style>

<docs>
```jsx
    <ul style="list-style-type: none; padding: 0;">
        <VsItineraryStop
            v-for="(stop, index) in itineraries.sampleItinerary.days[0].stops"
            :key="index"
            :stopNumber="stop.stopCount"
            stopLabel="Stop"
            :stopTitle="stop.title"
        >
            <div slot="stop-details">
                <VsImageWithCaption
                    :altText="stop.image.altText"
                    :image-src="stop.image.imageSrc"
                    variant="fullwidth"
                >
                    <VsImg
                        class="lazyload"
                        :src="stop.image.imageSrc"
                        :data-srcset="stop.image.imageSrc"
                        :alt="stop.image.altText"
                        data-sizes="auto">
                    </VsImg>

                    <VsSvg
                        slot="toggle-icon"
                        path="info-toggle"
                        height="24"
                        width="24"
                    />

                    <span slot="caption">
                        {{ stop.image.caption }}
                    </span>

                    <span slot="credit">
                        &copy; {{ stop.image.credit }}
                    </span>
                </VsImageWithCaption>
                <div v-html="stop.description"></div>
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
                <VsItineraryTips v-if="stop.tips.tipsBody.length || stop.tips.tipsTitle.length">
                    <div slot="text">
                    <strong>{{stop.tips.tipsTitle}}</strong>
                    <div v-html="stop.tips.tipsBody"></div>
                    </div>
                    <VsSvg slot="svg" path="highland-cow" />
                </VsItineraryTips>
                <VsIconList v-if="stop.facilities.length" title="Key facilities">
                    <VsIconListItem
                        v-for="(facility, facilitiesIndex) in stop.facilities"
                        :key="facilitiesIndex"
                        :label="facility.value"
                        :icon="facility.key"
                        />
                </VsIconList>
              </div>
            <!-- mimic only showing these links on the last stop of the day -->
            <VsItineraryBorderOverlapWrapper
                slot="nearby-links"
                v-if="index == itineraries.sampleItinerary.days[0].stops.length - 1"
            >
                <VsButton
                    class="mb-3"
                    button-size="md"
                    background="white"
                    variant="outline-primary"
                    href="#"
                    icon="food"
                >
                    Nearby places to eat
                </VsButton>
                <VsButton
                    background="white"
                    variant="outline-primary"
                    href="#"
                    icon="product-accommodation"
                >
                    Nearby places to stay
                </VsButton>
            </VsItineraryBorderOverlapWrapper>
        </VsItineraryStop>
    </ul>
```
</docs>
