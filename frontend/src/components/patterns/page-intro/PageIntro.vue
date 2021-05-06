<template>
    <div
        class="vs-page-intro"
        :class="introClasses"
        data-test="vs-page-intro"
    >
        <!-- @slot Slot for hero component  -->
        <slot name="vsIntroHero" />

        <section class="vs-page-intro__wrapper">
            <VsContainer>
                <VsRow>
                    <VsCol
                        cols="12"
                        :lg="heroIntro ? '8' : ''"
                        :offset-lg="heroIntro ? '1' : ''"
                    >
                        <div class="vs-page-intro__breadcrumb">
                            <!-- @slot Breadcrumb code  -->
                            <slot name="vsIntroBreadcrumb" />
                        </div>
                    </VsCol>
                </VsRow>

                <div class="vs-page-intro__share">
                    <VsSocialShare />
                </div>

                <VsRow>
                    <VsCol
                        cols="12"
                        md="12"
                        :lg="heroIntro ? '7' : '12'"
                        :xl="heroIntro ? '8' : '12'"
                        :offset-lg="heroIntro ? '1' : '0'"
                    >
                        <VsHeading level="1">
                            <!-- @slot Intro section heading -->
                            <slot name="vsIntroHeading" />
                        </VsHeading>
                    </VsCol>
                </VsRow>
                <VsRow>
                    <VsCol
                        cols="12"
                        v-bind="contentColProps"
                    >
                        <VsRichTextWrapper variant="lead">
                            <!-- @slot Intro section content -->
                            <slot name="vsIntroContent" />
                        </VsRichTextWrapper>
                        <dl
                            class="list-inline"
                            v-if="!!this.$slots['vsIntroStartFinish']"
                        >
                            <!-- @slot Intro section start / finish text
                            (used for itineraries) -->
                            <slot name="vsIntroStartFinish" />
                        </dl>
                    </VsCol>

                    <!-- @slot Intro section summary box (used for itineraries) -->
                    <slot name="VsIntroSummaryBox" />
                </VsRow>
            </VsContainer>
            <div
                class="vs-page-intro__lower py-9"
                v-if="!!this.$slots['VsIntroLower']"
                data-test="vs-page-intro__lower"
            >
                <!-- @slot Lower intro section content -->
                <slot name="VsIntroLower" />
            </div>
        </section>
    </div>
</template>

<script>
import VsHeading from '@components/elements/heading/Heading';
import VsSocialShare from '@components/patterns/social-share/SocialShare';
import VsRichTextWrapper from '@components/elements/rich-text-wrapper/RichTextWrapper';

import {
    VsContainer,
    VsRow,
    VsCol,
} from '@components/elements/layout';

/**
* Component for the page hero and introduction.
* Designed to be flexible so multiple designs of page intro
* can be accommodated.
*
* @displayName Page Intro
*/

export default {
    name: 'VsPageIntro',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsHeading,
        VsSocialShare,
        VsRichTextWrapper,
        VsContainer,
        VsRow,
        VsCol,
    },
    props: {
        /**
        * Background theme
        */
        background: {
            type: String,
            default: 'light',
            validator: (value) => value.match(/(light|dark)/),
        },
        /**
        * Option for an intro to be used on simple page type
        */
        heroIntro: {
            type: Boolean,
            default: false,
        },
        /**
        * Identifies the hero as an itinerary
        */
        isItinerary: {
            type: Boolean,
            default: false,
        },
    },
    computed: {
        introClasses() {
            return [
                `vs-page-intro--${this.background}`,
                this.heroIntro ? 'vs-page-intro--hero' : '',
            ];
        },
        contentColProps() {
            const props = {

            };

            if (this.isItinerary) {
                props.md = '6';
                props.lg = '5';
                props.xl = '6';
                props['offset-lg'] = '1';
            } else if (this.heroIntro) {
                props.md = '12';
                props.lg = '7';
                props.xl = '8';
                props['offset-lg'] = '1';
            } else {
                props.md = '8';
            }

            return props;
        },
    },
};
</script>

<style lang="scss">
.vs-page-intro {
    &__wrapper {
        position: relative;
        background: $color-white;

        @include media-breakpoint-up(lg) {
            background: transparent;
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

        .row {
            background: $color-white;
        }
    }

    &__share {
        position: absolute;
        top: $spacer-4;
        right: $spacer-4;

        @include media-breakpoint-up(lg) {
            top: $spacer-9;
            right: $spacer-9;
        }
    }

     &__breadcrumb {
        @include media-breakpoint-up(lg) {
            margin-top: $spacer-6;
        }
    }

    &__lower,
    &__lower .row {
        background-color: $color-theme-light;
    }

    .vs-hero {
        margin-bottom: $spacer-0;

        figcaption {
            @include media-breakpoint-up(lg) {
                bottom: 200px;
            }

            // IE11 - force min width of hero caption
            @media screen and (-ms-high-contrast: active), screen and (-ms-high-contrast: none) {
                min-width: 200px;
            }
        }
    }

    &--dark {
        background: $color-secondary-gray-shade-4;
    }

    &--hero {
        .vs-page-intro__wrapper {
            @include media-breakpoint-up(lg) {
                padding: 0 $spacer-4;
                margin-top: -240px;
            }
        }

        .vs-page-intro__share {
            @include media-breakpoint-up(lg) {
                top: $spacer-12;
            }
        }
    }
}
</style>

<docs>

  ```jsx
    const sampleItinerary = require("../../../assets/fixtures/itineraries/sample-itinerary.json")
    <VsPageIntro background="dark" :heroIntro="true" :isItinerary="true" class="mb-8">
      <VsHero
        slot="vsIntroHero"
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
        <template slot="vsIntroBreadcrumb">
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
        </template>
        <template slot="vsIntroHeading">
            {{itineraries.sampleItinerary.h1Heading}}
        </template>

        <template slot="vsIntroContent">
            <div v-html="itineraries.sampleItinerary.introduction" />
        </template>

        <template slot="vsIntroStartFinish">
            <dl class="list-inline">
                <dt class="list-inline-item">Start / Finish</dt>
                <dd class="list-inline-item">Edinburgh / Inverness</dd>
            </dl>
        </template>

        <template slot="VsIntroSummaryBox">
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
        </template>

        <VsContainer slot="VsIntroLower">
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
                                v-for="(areaCovered, index) in
                                    itineraries.sampleItinerary.areasCovered"
                                key="index"
                            >
                            {{areaCovered}}
                        </VsDescriptionListItem>
                    </VsDescriptionList>
                </VsCol>
            </VsRow>
      </VsContainer>
    </VsPageIntro>

    <VsPageIntro background="dark" :heroIntro="true" :isItinerary="false" class="mb-8">
      <VsHero
        slot="vsIntroHero"
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
        <template slot="vsIntroBreadcrumb">
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
        </template>
        <template slot="vsIntroHeading">
            {{itineraries.sampleItinerary.h1Heading}}
        </template>

        <template slot="vsIntroContent">
            <div v-html="itineraries.sampleItinerary.introduction" />
        </template>
    </VsPageIntro>

    <VsPageIntro background="light" :heroIntro="false" :isItinerary="false">
        <template slot="vsIntroBreadcrumb">
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
        </template>
        <template slot="vsIntroHeading">
            {{itineraries.sampleItinerary.h1Heading}}
        </template>

        <template slot="vsIntroContent">
            <div v-html="itineraries.sampleItinerary.introduction" />
        </template>
    </VsPageIntro>
  ```
</docs>
