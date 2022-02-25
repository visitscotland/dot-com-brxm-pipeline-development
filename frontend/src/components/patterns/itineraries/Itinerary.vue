<template>
    <section class="vs-itinerary">
        <div
            class="fixed-bottom"
            v-show="!isDesktop && withinItineraryMain"
        >
            <div class="d-flex justify-content-center pb-2">
                <VsButton
                    class="vs-itinerary__map-toggle-button"
                    @click.native="toggleShowMap()"
                    :icon="showMap ? 'list' : 'map'"
                    icon-variant-override="reverse-white"
                    icon-size-override="md"
                >
                    {{ showMap ? 'List View' : 'Map View' }}
                </VsButton>
            </div>
        </div>
        <div
            class="vs-itinerary__map-container"
            v-if="isDesktop || showMap"
        >
            <slot name="map" />
        </div>
        <VsContainer
            class="vs-itinerary__outer-container"
        >
            <VsRow>
                <VsCol
                    cols="12"
                    class="p-0 vs-itinerary__accordion-container"
                >
                    <VsAccordion break-point="lg">
                        <slot name="list" />
                    </VsAccordion>
                </VsCol>
            </VsRow>
        </VsContainer>
    </section>
</template>

<script>
// import { get } from 'lodash'

import {
    VsContainer,
    VsRow,
    VsCol,
} from '@components/elements/layout';
import VsButton from '@components/elements/button/Button';
import VsAccordion from '@components/patterns/accordion/Accordion';

/**
 * A wrapper component that wraps the itinerary map and list.
 * It controls display of the mobile map toggle on smaller screens.
 *
 * @displayName Itinerary
 */

export default {
    name: 'VsItinerary',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsContainer,
        VsRow,
        VsCol,
        VsAccordion,
        VsButton,
    },
    data() {
        return {
            showMap: false,
            isDesktop: false,
            withinItineraryMain: false,
        };
    },
    mounted() {
        /* Design System wrapper affects page scroll detection, so temporary fix is to
        have a condition checking for design system wrapper. */

        const designSystemWrapper = document.querySelector('.vds-example');

        window.addEventListener('resize', this.resizeWidth);

        if (designSystemWrapper === null) {
            window.addEventListener('scroll', this.onScroll);
        } else {
            designSystemWrapper.addEventListener('scroll', this.onScroll);
        }

        this.resizeWidth();
    },
    destroyed() {
        window.removeEventListener('resize', this.resizeWidth);
    },
    methods: {
        resizeWidth() {
            this.isDesktop = window.innerWidth >= 1200;
            this.showMap = window.innerWidth >= 1200;
        },
        onScroll() {
            const bounding = this.$el.getBoundingClientRect();
            const insideStartOfItineraryMain = bounding.top <= (
                window.innerHeight || document.documentElement.clientHeight
            );
            const outsideEndOfItineraryMain = bounding.bottom <= (
                window.innerHeight || document.documentElement.clientHeight
            );
            this.withinItineraryMain = !!(
                insideStartOfItineraryMain && !outsideEndOfItineraryMain
            );
        },
        toggleShowMap() {
            this.showMap = !this.showMap;
        },
    },
};
</script>

<style lang="scss">
.vs-itinerary {
    background-color: $color-white;

    .vs-itinerary__outer-container {
        @include media-breakpoint-down(sm) {
            max-width: 100%;
        }

        @include media-breakpoint-down(xs) {
            max-width: initial;
            width: calc(100% + #{$spacer-6});
            margin-left: -#{$spacer-3};
        }

        @include media-breakpoint-up(lg) {
            > .row {
                margin-left: 0;
                margin-right: 0;
            }
        }
    }

    .vs-itinerary__map-container {
        height: 100vh;
        position: fixed;
        top: 0;
        width: 100vw;
        z-index: 1020;

        @include media-breakpoint-down(lg) {
            bottom: 0;
            left: 0;
            right: 0;
        }

        @include media-breakpoint-up(lg) {
            float: right;
            position: -webkit-sticky;
            position: sticky;
            width: 45vw;
        }
    }

    .vs-itinerary__accordion-container {
        @include media-breakpoint-up(lg) {
            max-width: calc(100% - 6.25rem);
        }
    }

    .vs-itinerary__map-toggle-button {
        padding-left: $spacer-4;
        padding-right: $spacer-4;

        svg {
            margin-right: $spacer-3;
        }
    }
}
</style>

<docs>
```jsx
  const sampleItinerary = require("../../../assets/fixtures/itineraries/sample-itinerary.json")
  const stops = [];

  sampleItinerary.days.map(day => {
    day.stops.map(stop => {
      return stops.push({
        title: stop.title,
        latitude: stop.latitude,
        longitude: stop.longitude,
        stopCount: stop.stopCount,
        imageSrc: stop.image.imageSrc,
        altText: stop.image.altText
      });
    })
  })

<VsItinerary>
    <VsItineraryMap
        slot="map"
        overview-map-longitude="57.81"
        overview-map-latitude="-4.13"
        overview-map-zoom="5"
        :stops="stops"
        :labels='{
            "stopLabel": "Stop",
            "mapControlsFullscreenOpen": "Show fullscreen",
            "mapControlsFullscreenClose": "Exit fullscreen",
            "mapControlsCompass": "Reset angle",
            "mapControlsZoomIn": "Zoom in",
            "mapControlsZoomOut": "Zoom out"
        }'
    />

    <VsItineraryDay
        slot="list"
        v-for="(day, index) in itineraries.sampleItinerary.days"
            :defaultShow="(day.dayCount < 3) ? true : false"
            :key="index"
            :dayNumber="day.dayCount"
            dayLabel="Day"
            :dayTitle="day.title"
    >
        <VsDescriptionList
            v-if="day.dayMiles && day.dayKM"
            slot="day-distance"
            class="list-inline text-center"
        >
            <VsDescriptionListItem title inline>
                <abbr title="miles">mi</abbr>/<abbr title="kilometres">km</abbr>
            </VsDescriptionListItem>
            <VsDescriptionListItem inline>
                {{day.dayMiles}}/{{day.dayKM}}
            </VsDescriptionListItem>
        </VsDescriptionList>

        <VsDescriptionList
            v-if="day.transport.length"
            class="text-center justify-content-center align-items-center"
            slot="day-transport"
            inline
        >
            <VsDescriptionListItem
                title
                class="col-auto px-0"
            >
                Transport
            </VsDescriptionListItem>
            <VsDescriptionListItem
                class="col-auto px-0"
                v-for="(transportType, transportTypeIndex) in day.transport"
                :key="transportTypeIndex"
            >
                <VsTooltip
                    :title="transportType.value"
                    href="#"
                    :icon="transportType.key"
                    size="lg"
                    icon-only
                    icon-variant-override="dark"
                    class="p-0"
                    variant="transparent"
                >
                    <span class="sr-only">
                        {{transportType.value}}
                    </span>
                </VsTooltip>
            </VsDescriptionListItem>
        </VsDescriptionList>

        <div class="mb-5" slot="day-introduction" v-html="day.introduction"></div>

        <VsItineraryStop
            slot="stops"
            v-for="(stop, stopIndex) in day.stops"
                :key="stopIndex"
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
    </VsItineraryDay>
</VsItinerary>
  ```
</docs>
