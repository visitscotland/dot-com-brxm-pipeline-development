<template>
    <div
        class="vs-page-intro"
        :class="introClasses"
        data-test="vs-page-intro"
    >
        <!-- @slot Slot for hero ImageWithCaption component  -->
        <slot name="vsIntroHero" />

        <section class="vs-page-intro__wrapper">
            <VsContainer>
                <VsRow>
                    <VsCol
                        cols="10"
                        :lg="heroIntro ? '8' : ''"
                        :offset-lg="heroIntro ? '1' : ''"
                    >
                        <div class="vs-page-intro__breadcrumb">
                            <!-- @slot Slot to display breadcrumb items  -->
                            <slot name="vsIntroBreadcrumb" />
                        </div>
                    </VsCol>
                </VsRow>

                <div class="vs-page-intro__share">
                    <!-- @slot Slot to display SocialShare button  -->
                    <slot name="vsShareButton" />
                </div>

                <VsRow>
                    <VsCol
                        cols="12"
                        :md="heroIntro ? '12' : '10'"
                        :lg="heroIntro ? '7' : '10'"
                        :xl="heroIntro ? '8' : '10'"
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
import VsRichTextWrapper from '@components/elements/rich-text-wrapper/RichTextWrapper';

import {
    VsContainer,
    VsRow,
    VsCol,
} from '@components/elements/grid';

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

        > .container {
            background: $color-white;
            padding-bottom: $spacer-8;

            @include media-breakpoint-up(lg) {
                padding-bottom: $spacer-9;
            }
        }
    }

    &__share {
        position: absolute;
        top: $spacer-4;
        right: $spacer-1;

        @include media-breakpoint-up(sm) {
            right: $spacer-4;
        }

        @include media-breakpoint-up(lg) {
            top: 7.2rem;
        }
    }

    &--hero {
        .vs-page-intro{
            &__share {
                @include media-breakpoint-up(lg) {
                    top: $spacer-10;
                }
            }

            &__wrapper {
                @include media-breakpoint-up(lg) {
                    margin-top: -240px;
                }
            }
        }

        .vs-hero {
            margin-bottom: $spacer-0;

            figcaption {
                @include media-breakpoint-up(lg) {
                    bottom: 200px;
                }

                // IE11 - force min width of hero caption
                @media screen and (-ms-high-contrast: active),
                screen and (-ms-high-contrast: none) {
                    min-width: 200px;
                }
            }
        }
    }

     &__breadcrumb {
        @include media-breakpoint-up(lg) {
            margin-top: $spacer-8;
        }
    }

    &__lower,
    &__lower .row {
        background-color: $color-theme-light;
    }

    &--dark {
        background: $color-secondary-gray-shade-4;
    }
}
</style>

<docs>

  ```jsx
const sampleItinerary = require("../../../assets/fixtures/itineraries/sample-itinerary.json")

<hr/>
<h3>Page Intro with Hero</h3>
<hr/>
<VsPageIntro background="dark" :heroIntro="true" :isItinerary="false" class="mb-8">
    <VsImageWithCaption
        slot="vsIntroHero"
        isHeroImage
        :altText="itineraries.sampleItinerary.image.altText"
        :image-src="itineraries.sampleItinerary.image.imageSrc"
    >
        <VsCaption
            slot="img-caption"
            :latitude="itineraries.sampleItinerary.image.latitude"
            :longitude="itineraries.sampleItinerary.image.longitude"
            variant="large"
        >
            <span slot="caption">
                {{ itineraries.sampleItinerary.image.caption }}
            </span>

            <span slot="credit">
                {{ itineraries.sampleItinerary.image.credit }}
            </span>
        </VsCaption>
    </VsImageWithCaption>

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

<h3>Page Intro with Hero and Video</h3>
<hr/>
<VsPageIntro background="dark" :heroIntro="true" :isItinerary="false" class="mb-8">
    <VsImageWithCaption
        slot="vsIntroHero"
        isHeroImage
        isVideo
        videoId="c05sg3G4oA4"
        playButtonText="Play the video"
        toggleButtonText="Toggle image caption"
        :altText="itineraries.sampleItinerary.image.altText"
        :image-src="itineraries.sampleItinerary.image.imageSrc"
    >

        <template slot="video-no-js-alert">
            JavaScript needs to be enabled to watch this video.
            You can turn this on in your browser settings.
        </template>
        <template slot="video-title">
            This is the video title
        </template>
        <template slot="video-duration">
            This is the video length
        </template>

        <VsCaption
            slot="img-caption"
            :latitude="itineraries.sampleItinerary.image.latitude"
            :longitude="itineraries.sampleItinerary.image.longitude"
            variant="large"
        >
            <span slot="caption">
                {{ itineraries.sampleItinerary.image.caption }}
            </span>

            <span slot="credit">
                {{ itineraries.sampleItinerary.image.credit }}
            </span>
        </VsCaption>
    </VsImageWithCaption>

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

<hr style="margin-top: 8rem;"/>
<h3>Page Intro without Hero</h3>
<hr/>
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

<hr style="margin-top: 8rem;"/>
<h3>Page Intro for Itinerary</h3>
<hr/>
<VsPageIntro background="dark" :heroIntro="true" :isItinerary="true" class="mb-8">
    <VsImageWithCaption
        slot="vsIntroHero"
        isHeroImage
        :altText="itineraries.sampleItinerary.image.altText"
        :image-src="itineraries.sampleItinerary.image.imageSrc"
    >
        <VsCaption
            slot="img-caption"
            :latitude="itineraries.sampleItinerary.image.latitude"
            :longitude="itineraries.sampleItinerary.image.longitude"
            variant="large"
        >
            <span slot="caption">
                {{ itineraries.sampleItinerary.image.caption }}
            </span>

            <span slot="credit">
                {{ itineraries.sampleItinerary.image.credit }}
            </span>
        </VsCaption>
    </VsImageWithCaption>

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

    <template slot="vsShareButton">
        <VsSocialShare
            page-url="http://www.visitscotland.com"
            page-title="VisitScotland - Scotland's National Tourist Organisation"
            share-btn-text="Share"
            close-alt-text="Close"
            share-popover-title="Share On"
            id="1"
        >
            <VsSocialShareItem
                name="facebook"
                link-text="Facebook"
            />
            <VsSocialShareItem
                name="pinterest"
                link-text="Pinterest"
            />
            <VsSocialShareItem
                name="whatsapp"
                link-text="WhatsApp"
            />
            <VsSocialShareItem
                name="twitter"
                link-text="Twitter"
            />
            <VsSocialShareItem
                name="email"
                link-text="Email"
            />
            <VsSocialShareItem
                name="link"
                link-text="Copy Link"
                link-copied-text="Link copied!"
            />
        </VsSocialShare>
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
                        :key="`itinerary-${index}`"
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

<VsModal
    modalId="c05sg3G4oA4"
    closeBtnText="Close"
    :isVideoModal="true"
>
    <VsRow>
        <VsCol cols="12">
            <VsVideo
                video-id="c05sg3G4oA4"
                class="mb-8"
            />
        </VsCol>
    </VsRow>
</VsModal>
  ```
</docs>
