<template>
    <section class="vs-itinerary position-sticky">
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
            margin-left: 6.25rem;
            position: -webkit-sticky;
            position: sticky;
            width: 45vw;
        }

         @media screen and (-ms-high-contrast: active), screen and (-ms-high-contrast: none) {
            position: relative;
        }
    }

    .vs-itinerary__map-toggle-button {
        padding-left: $spacer-4;
        padding-right: $spacer-4;

        svg {
            margin-right: $spacer-3;
        }
    }

    // layout styles for safari
    @media not all and (min-resolution:.001dpcm) {
        @supports (-webkit-appearance:none) {
            @include media-breakpoint-up(lg) {
                .vs-itinerary__map-container {
                    height: 0;
                    position: relative;
                }

                .vs-itinerary__accordion-container {
                    max-width: 50%;
                }
            }
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
    <VsPageIntro>
      <VsHero
        slot="hero"
        :altText="itineraries.sampleItinerary.image.altText"
        :credit="itineraries.sampleItinerary.image.credit"
        :caption="itineraries.sampleItinerary.image.caption"
        :image-src="itineraries.sampleItinerary.image.imageSrc"
        :latitude="itineraries.sampleItinerary.image.latitude"
        :longitude="itineraries.sampleItinerary.image.longitude"
      >
      <img
        class="lazyload"
        :src="itineraries.sampleItinerary.image.imageSrc"
        :srcset=keysList.keysList[0].imageDataURI
        :data-srcset="itineraries.sampleItinerary.image.imageSrc"
        :alt="itineraries.sampleItinerary.image.altText"
        data-sizes="auto"
        />
      </VsHero>
      <VsContainer slot="upper" class="py-lg-4">
        <VsRow class="justify-content-md-between">
          <VsCol cols="12" lg="8" offset-lg="1">
            <VsBreadcrumb>
              <VsBreadcrumbItem
                v-for="(item, index) in breadcrumb.breadcrumb"
                :key="index"
                :href="item.href"
                :active="item.active"
                :text="item.name"
                >
              </VsBreadcrumbItem>
            </VsBreadcrumb>
          </VsCol>
        </VsRow>
        <VsRow>
          <VsCol cols="10" lg="8" offset-lg="1">
            <VsHeading level="1">
              {{itineraries.sampleItinerary.h1Heading}}
            </VsHeading>
          </VsCol>
          <VsCol cols="2">
            <div class="d-flex justify-content-center justify-content-sm-end">
              <!-- TODO - Below icon is FPO. Replace with icon
              with text component and a share component -->
              <VsSocialShare />
            </div>
          </VsCol>
        </VsRow>
        <VsRow>
          <VsCol cols="12" md="6" lg="5" xl="6" offset-lg="1">
            <VsRichTextWrapper
                variant="lead"
                v-html="itineraries.sampleItinerary.introduction">
            </VsRichTextWrapper>
            <dl class="list-inline">
              <dt class="list-inline-item">Start / Finish</dt>
              <dd class="list-inline-item">
                {{itineraries.sampleItinerary.start}}/{{itineraries.sampleItinerary.finish}}
            </dd>
            </dl>
          </VsCol>
          <VsCol cols="12" md="6" lg="5" xl="4">
            <VsSummaryBoxList>
                <VsSummaryBoxListItem
                    :text=itineraries.sampleItinerary.totalDays
                    label="Days"
                />
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
                <VsSummaryBoxListItem
                    :icon=itineraries.sampleItinerary.transport.key
                    :iconLabel=itineraries.sampleItinerary.transport.value
                    label="Transport"
                />
                <VsSummaryBoxListItem
                    :icon=itineraries.sampleItinerary.theme.key
                    :iconLabel=itineraries.sampleItinerary.theme.value
                    label="Main theme"
                />
            </VsSummaryBoxList>
          </VsCol>
        </VsRow>
      </VsContainer>
      <VsContainer slot="lower">
         <VsRow>
          <VsCol cols="12" lg="11" offset-lg="1">
            <VsDescriptionList class="mb-6">
                <VsDescriptionListItem title>Highlights</VsDescriptionListItem>
                <VsDescriptionListItem
                    v-for="(highlight, index) in itineraries.sampleItinerary.highlights"
                >
                    {{highlight}}
                </VsDescriptionListItem>
            </VsDescriptionList>
            <VsDescriptionList class="mb-8">
                <VsDescriptionListItem title>Areas Covered</VsDescriptionListItem>
                    <VsDescriptionListItem
                        v-for="(areaCovered, index) in itineraries.sampleItinerary.areasCovered"
                        key="index"
                    >
                    {{areaCovered}}
                </VsDescriptionListItem>
            </VsDescriptionList>
          </VsCol>
        </VsRow>
      </VsContainer>
    </VsPageIntro>
<VsItinerary>
  <!-- TODO: move mapbox prod and dev keys to an environment variable -->
    <VsItineraryMap
        slot="map"
        :access-token=keysList.keysList[0].mapToken
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
        >
    </VsItineraryMap>
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
                <VsTooltip :title="transportType.value">
                    <vs-icon :name="transportType.key" variant="dark" size="md" />
                </VsTooltip>
                <span class="sr-only">{{transportType.value}}</span>
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
              <div slot="stop-details">

                <VsImageWithCaption
                    :altText="stop.image.altText"
                    :image-src="stop.image.imageSrc"
                    variant="fullwidth"
                >
                    <VsImg
                        class="lazyload"
                        :src="stop.image.imageSrc"
                        :srcset=keysList.keysList[0].imageDataURI
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
                    <VsDescriptionListItem title class="mb-0 mr-0 col-auto">
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
                <VsAddress v-if="stop.address && stop.address.line1">
                    <span v-if="stop.address.line1">{{ stop.address.line1 }},</span>
                    <span v-if="stop.address.line2">{{ stop.address.line2 }},</span>
                    <span v-if="stop.address.line3">{{ stop.address.line3 }},</span>
                    <span v-if="stop.address.city">{{ stop.address.city }},</span>
                    <span v-if="stop.address.postcode">{{ stop.address.postcode }}</span>
                </VsAddress>
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
                    v-if="stopIndex == day.stops.length - 1"
                >
                    <VsButton
                        class="mb-3"
                        background="white"
                        button-size="md"
                        variant="outline-primary"
                        href="#"
                        icon="food"
                    >
                        Nearby places to eat
                    </VsButton>
                    <VsButton
                        background="white"
                        button-size="md"
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
