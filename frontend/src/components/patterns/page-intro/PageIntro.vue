<template>
    <div
        class="vs-page-intro position-relative"
        :class="backgroundClass"
        data-test="vs-page-intro"
    >
        <slot name="hero" />
        <div class="vs-page-intro__wrapper--outer">
            <div class="vs-page-intro__wrapper--inner">
                <div
                    class="vs-page-intro__wrapper--inner-top"
                    data-test="vs-page-intro__upper"
                >
                    <slot name="upper" />
                </div>
                <div
                    class="vs-page-intro__wrapper--inner-bottom py-9"
                    v-if="!!this.$slots['lower']"
                    data-test="vs-page-intro__lower"
                >
                    <slot name="lower" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
/**
 * Component is just a wrapper for the page hero and introduction.
 * Note that everything within is just a slot.
 *
 * @displayName Page Intro
 */
export default {
    name: 'VsPageIntro',
    status: 'prototype',
    release: '0.0.1',
    props: {
        background: {
            type: String,
            default: 'light',
            validator: (value) => value.match(/(light|dark)/),
        },
    },
    computed: {
        backgroundClass() {
            return `vs-page-intro--${this.background}`;
        },
    },
};
</script>

<style lang="scss">
.vs-page-intro__wrapper--outer {
    background: $color-white;
    margin-top: -1rem;

    @include media-breakpoint-up(lg) {
        margin: 0;
        background: none;
        padding: 0 1rem;
        width: 100%;
        max-width: 960px;
        margin-right: auto;
        margin-left: auto;
    }

    @include media-breakpoint-up(xl) {
        max-width: 1140px;
    }

    @include media-breakpoint-up(xxl) {
        max-width: 1340px;
    }
}

.vs-page-intro__wrapper--inner-bottom {
    background-color: $color-theme-light;
}

.vs-page-intro .vs-page-intro__wrapper--inner {
    @include media-breakpoint-up(lg) {
        background: $color-white;
        margin: -250px 0 0;
        position: relative;
    }

    @include media-breakpoint-up(xl) {
        margin: -250px 0 0;
    }
}

.vs-page-intro .vs-hero figcaption {
    @include media-breakpoint-up(lg) {
        bottom: 200px;
    }

    // IE11 - force min width of hero caption
    @media screen and (-ms-high-contrast: active), screen and (-ms-high-contrast: none) {
        min-width: 200px;
    }
}

.vs-page-intro--dark {
    background: $color-secondary-gray-shade-4;
}
</style>

<docs>

  ```jsx
    const sampleItinerary = require("../../../assets/fixtures/itineraries/sample-itinerary.json")
    <VsPageIntro background="dark">
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
        srcset="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="
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
              <VsIcon name="share" variant="dark" size="md" />
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
                <VsSummaryBoxListItem>
                    <VsSummaryBoxDisplay :text=itineraries.sampleItinerary.totalDays />
                    <VsSummaryBoxLabel label="Days" />
                    </VsSummaryBoxListItem>
                    <VsSummaryBoxListItem>
                        <VsSummaryBoxDistanceDisplay
                            :miles=itineraries.sampleItinerary.totalMiles
                            :kilometres=itineraries.sampleItinerary.totalKM
                            miles-label="miles"
                            kilometres-label="kilometres"
                        />
                        <VsSummaryBoxDistanceLabel
                            distance-label="Distance"
                            kilometres-abbr="km"
                            kilometres-label="kilometres"
                            miles-abbr="mi"
                            miles-label="miles"
                        />
                    </VsSummaryBoxListItem>
                    <VsSummaryBoxListItem>
                        <VsSummaryBoxIconWithLabel
                            :icon=itineraries.sampleItinerary.transport.key
                            :label=itineraries.sampleItinerary.transport.value
                        />
                        <VsSummaryBoxLabel label="Transport" />
                    </VsSummaryBoxListItem>
                    <VsSummaryBoxListItem>
                        <VsSummaryBoxIconWithLabel
                            :icon=itineraries.sampleItinerary.theme.key
                            :label=itineraries.sampleItinerary.theme.value
                        />
                        <VsSummaryBoxLabel label="Main theme" />
                    </VsSummaryBoxListItem>
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
  ```
</docs>
