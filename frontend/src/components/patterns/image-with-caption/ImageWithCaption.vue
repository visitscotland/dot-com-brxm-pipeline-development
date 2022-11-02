<template>
    <figure
        class="vs-image-with-caption"
        data-test="vs-image-with-caption"
        :class="imageWithCaptionClasses"
    >
        <div
            class="vs-image-with-caption__image-wrapper"
            :class="mobileOverlap ? 'vs-image-with-caption--overlapped' : ''"
        >
            <!-- @slot Default slot for image -->
            <slot>
                <VsImg
                    v-if="imageSrc"
                    :src="imageSrc"
                    :alt="altText"
                    :use-lazy-loading="useLazyLoading"
                />
            </slot>

            <VsToggleButton
                :img-src="imageSrc"
                :toggle-id="uniqueCaptionId"
                @toggleAction="toggleCaption"
            >
                {{ toggleButtonText }}

                <template slot="toggle-icon">
                    <!-- @slot Slot for custom image to be used for toggle icon
                        eg. social media images -->
                    <slot name="toggle-icon" />
                </template>
            </VsToggleButton>
        </div>

        <figcaption class="vs-image-with-caption__captions">
            <div
                class="vs-image-with-caption__video-caption-wrapper container-lg"
                v-if="isVideo"
            >
                <VsVideoCaption
                    :video-btn-text="playButtonText"
                    :with-toggle-btn="true"
                    @toggleAction="toggleCaption"
                    :video-id="videoId"
                    :cookie-link-text="cookieLinkText"
                    :error-message="errorMessage"
                >
                    <!-- @slot Slot for the video title text -->
                    <template slot="video-title">
                        <slot name="video-title" />
                    </template>
                    <!-- @slot Slot for the video duration text -->
                    <template slot="video-duration">
                        <slot name="video-duration" />
                    </template>
                </VsVideoCaption>
            </div>

            <div
                class="vs-image-with-caption__caption-wrapper"
                :class="captionWrapperClasses"
                :id="uniqueCaptionId"
            >
                <!-- @slot Slot for image caption component -->
                <slot name="img-caption" />
            </div>
        </figcaption>
    </figure>
</template>

<script>

import { v4 as uuidv4 } from 'uuid';
import VsImg from '@components/elements/img/Img';
import VsToggleButton from '@components/patterns/toggle-button/ToggleButton';
import VsVideoCaption from '@components/patterns/video-caption/VideoCaption';
import verifyCookiesMixin from '../../../mixins/verifyCookiesMixin';
import requiredCookiesData from '../../../utils/required-cookies-data';

const cookieValues = requiredCookiesData.youtube;

/**
 * Image with toggle to open a caption and image location map
 *
 * @displayName Image With Caption
 */
export default {
    name: 'VsImageWithCaption',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsImg,
        VsToggleButton,
        VsVideoCaption,
    },
    mixins: [
        verifyCookiesMixin,
    ],
    props: {
        /**
         * The image alt text for screen readers
         */
        altText: {
            type: String,
            default: '',
        },

        /**
         * Chooses to show caption open by default or not: used when images are smaller than 300px
         */
        closedDefaultCaption: {
            type: Boolean,
            default: false,
        },

        /**
         * The source URL for the image
         */
        imageSrc: {
            type: String,
            default: '',
        },

        /**
         * Option for a large Hero image at top of a page
         */
        isHeroImage: {
            type: Boolean,
            default: false,
        },

        /**
         * Option for a video to open in a modal
         */
        isVideo: {
            type: Boolean,
            default: false,
        },

        /**
         * Option if the mobile view is overlapped at the bottom
        */
        mobileOverlap: {
            type: Boolean,
            default: false,
        },

        /**
         * The screenreader text for the toggle button
         */
        toggleButtonText: {
            type: String,
            default: '',
        },

        /**
         * The text for the video play button
         */
        playButtonText: {
            type: String,
            default: '',
        },

        /**
         * The YouTube video ID
         */
        videoId: {
            type: String,
            default: '',
        },
        /**
        * A message explaining why the component has been disabled with disabled cookies, is
        * provided for descendent components to inject
        */
        noCookiesMessage: {
            type: String,
            default: '',
        },
        /**
        * Text used for the link which opens the cookie preference centre, is
        * provided for descendent components to inject
        */
        cookieLinkText: {
            type: String,
            default: '',
        },
        /**
        * A message explaining why the component has been disabled when js is disabled,
        * is provided for descendent components to inject
        */
        noJsMessage: {
            type: String,
            default: '',
        },
        /*
         * If true switches on lazy loading for the image
        */
        useLazyLoading: {
            type: Boolean,
            default: true,
        },
        /**
         * Message to show when there's an error with a third party
        */
        errorMessage: {
            type: String,
            default: '',
        },
    },
    data() {
        return {
            showCaption: false,
            requiredCookies: cookieValues,
            uniqueCaptionId: '',
        };
    },
    computed: {
        imageWithCaptionClasses() {
            return {
                'vs-image-with-caption--closed-default': this.closedDefaultCaption,
                'vs-image-with-caption--hero': this.isHeroImage,
                'vs-image-with-caption--show-caption': !this.requiredCookiesExist && this.setCookieStatus === true,
                'vs-image-with-caption--video': this.isVideo,
            };
        },
        captionWrapperClasses() {
            return {
                'd-block': this.showCaption && !this.isHeroImage,
                'd-flex': (this.showCaption && this.isHeroImage)
                    || (this.showCaption && this.isVideo),
                container: this.isHeroImage,
            };
        },
    },
    created() {
        this.generateCaptionId();
    },
    methods: {
        toggleCaption() {
            this.showCaption = !this.showCaption;
        },
        generateCaptionId() {
            const randomUUID = uuidv4();
            this.uniqueCaptionId = `vs-caption-${randomUUID}`;
        },
    },
    provide() {
        return {
            noJsMessage: this.noJsMessage,
            noCookiesMessage: this.noCookiesMessage,
            cookieLinkText: this.cookieLinkText,
            errorMessage: this.errorMessage,
        };
    },
};
</script>

<style lang="scss">
    .vs-image-with-caption {
        position: relative;

        &__image-wrapper {
            position: relative;
            aspect-ratio: 3/2;

            @supports not (aspect-ratio: 3/2) {
                padding-bottom: 66.6%;
            }

            img {
                width: 100%;
                height: 100%;
                object-fit: cover;
                align-self: flex-start;
                flex-shrink: 0; // IE11 fix, prevents image vertical stretching
            }

            .vs-toggle-btn {
                position: absolute;
                bottom: $spacer-2;
                right: $spacer-2;

                @include media-breakpoint-up(sm) {
                    .vs-image-with-caption--closed-default & {
                        display: block;
                    }
                }

                @include media-breakpoint-up(sm) {
                    display: none;
                }
            }
        }

        &__caption-wrapper {
            display: none;
            padding: 0;

            .vs-caption{
                &--large {
                    position: absolute;
                    bottom: -48px;
                    right: 0;
                    z-index: 2;
                }

                @include media-breakpoint-down(xs) {
                    &--large,
                    &--fullwidth {
                        position: absolute;
                        top: 0;
                        width: 100%;
                        height: 100%;
                        display: flex;
                        text-align: center;
                    }
                }
            }

            @include media-breakpoint-up(sm) {
                display: block;

                .vs-image-with-caption--closed-default & {
                    display: none;

                    .vs-caption--fullwidth {
                        display: flex;
                        position: absolute;
                        top: 0;
                        height: 100%;
                        text-align: center;
                    }
                }
            }
        }

        &--fullwidth {
            @include media-breakpoint-up(sm) {
                .vs-image-with-caption__captions {
                    position: relative;
                }
            }
        }

        &--video {
            &.vs-image-with-caption--hero {
                .vs-image-with-caption__image-wrapper {
                    .vs-toggle-btn {
                        display: none;
                    }
                }
            }

            .vs-image-with-caption {
                &__caption-wrapper {
                    display: none;
                    justify-content: flex-end;
                }
            }

            .vs-image-with-caption__video-caption-wrapper {
                margin-top: -50px;
            }

            .vs-image-with-caption__captions {
                position: relative;
                top: auto;
                left: auto;
                background: $color-gray-shade-6;
            }

            .vs-caption {
                &__image-caption {
                    margin-bottom: $spacer-2;
                }

                &__caption-info {
                    padding-left: $spacer-5;
                }

                .row {
                    margin: 0;
                }

                .col {
                    padding: 0;
                }
            }

            &.vs-image-with-caption--hero {
                .vs-image-with-caption__video-caption-wrapper {
                    padding: 0;
                }

                .vs-image-with-caption__caption-wrapper {
                    display: none;

                    .vs-caption {
                        position: relative;
                        bottom: auto;
                        right: auto;
                    }
                }

                &.vs-image-with-caption--show-caption {
                    .vs-image-with-caption__caption-wrapper {
                        display: flex;
                        margin-top: $spacer-2;
                    }
                }
            }

            @include media-breakpoint-up(sm) {
                .vs-image-with-caption__video-caption-wrapper {
                    .vs-video-caption {
                        width: 100%;
                    }
                }
            }

            @include media-breakpoint-up(lg) {
                .vs-toggle-btn {
                    display: none;
                }

                .vs-image-with-caption__video-caption-wrapper {
                    display: flex;
                    justify-content: flex-end;
                    padding: 0;

                    .vs-video-caption {
                        width: 400px;

                        .vs-toggle-btn {
                            display: block;
                        }
                    }
                }

                .vs-image-with-caption__caption-wrapper {
                    justify-content: flex-end;
                    padding: 0;
                }

                .vs-caption {
                    position: absolute;
                    bottom: auto;
                    width: 400px;
                }

                .vs-toggle-btn {
                    right: 0;
                }

                &.vs-image-with-caption--hero {
                     .vs-image-with-caption__captions {
                        position: absolute;
                        bottom: 210px;
                        width: 100%;
                        right: 0;
                        z-index: 3;
                        background: transparent;
                    }
                }
            }
        }

        &--overlapped {
            .vs-toggle-btn {
                bottom: $spacer-9;
                right: $spacer-4;
            }
        }

        &--hero{
            margin-bottom: 0;
            background: $color-gray-shade-6;

            .vs-image-with-caption{
                &__image-wrapper {
                    max-height: 100vh;
                    overflow: hidden;
                    aspect-ratio: auto;

                    @supports not (aspect-ratio: 3/2) {
                        padding-bottom: $spacer-0;
                    }

                    .vs-toggle-btn {
                        @include media-breakpoint-between(sm, md) {
                            display: block;
                        }
                    }

                    @include media-breakpoint-up(lg) {
                        height: 100vh;
                    }
                }

                &__caption-wrapper {
                    display: none;

                    @include media-breakpoint-up(sm) {
                        position: relative;
                    }

                    @include media-breakpoint-up(lg) {
                        display: block;
                    }

                    .vs-caption--large{
                        position: relative;
                        top: 0;

                        @include media-breakpoint-down(xs) {
                            text-align: left;
                            display: block;
                            position: absolute;

                            .order-2 {
                                order: 1;
                            }

                            .order-1 {
                                order: 2;
                                flex: 0 0 auto;
                                width: auto;
                                max-width: 100%;
                                align-self: auto!important;
                            }

                            .vs-caption{
                                &__map-wrapper {
                                    padding-top: 0!important;
                                }

                                &__image-caption {
                                    margin-bottom: $spacer-5;
                                }

                                &__caption-info{
                                    padding: $spacer-3 $spacer-2;
                                }
                            }
                        }

                        @include media-breakpoint-between(sm, md) {
                            width: 100%;
                            height: auto;
                            text-align: left;
                        }

                        @include media-breakpoint-up(lg) {
                            bottom: 200px;
                            top: auto;
                            position: absolute;
                        }
                    }
                }
            }
        }
    }

    @include no-js {
        .vs-image-with-caption {
            &__image-wrapper {
                .vs-toggle-btn {
                    display: none;
                }
            }

            &--video {
                .vs-image-with-caption__video-caption-wrapper {
                    margin-top: 0;

                    .vs-video-caption {
                        .vs-toggle-btn {
                            display: none;
                        }
                    }
                }

                .vs-image-with-caption__caption-wrapper {
                    display: flex;
                }
            }

            &__caption-wrapper {
                display: block;
            }

            @include media-breakpoint-up(lg) {
                &--video {
                    .vs-image-with-caption__video-caption-wrapper {
                        margin-bottom: $spacer-2;
                    }
                }
            }
        }

        .vs-image-with-caption--closed-default{
            .vs-image-with-caption{
                &__image-wrapper {
                    .vs-toggle-btn {
                        display: none;
                    }
                }

                &__caption-wrapper{
                    .vs-caption--fullwidth{
                        position: relative;
                        display: block;
                        text-align: left;
                    }
                }
            }
        }
    }
</style>

<docs>

  ```jsx
    <BsWrapper style="max-width:700px">
        <h3>Large Caption Style</h3>
        <VsImageWithCaption
            v-for="(item, index) in imageWithCaption.imageExamples.large"
            :altText="item.altText"
            :image-src="item.imageSrc"
            :key="`large-${index}`"
            class="mb-11"
        >
            <VsCaption
                slot="img-caption"
                :latitude="item.latitude"
                :longitude="item.longitude"
                variant="large"
            >
                <span slot="caption" v-if="item.caption">
                    {{ item.caption }}
                </span>

                <span slot="credit" v-if="item.credit">
                    {{ item.credit }}
                </span>
            </VsCaption>
        </VsImageWithCaption>

        <h3 style="margin-top: 7rem;">Fullwidth Caption Style</h3>
        <VsImageWithCaption
            v-for="(item, index) in imageWithCaption.imageExamples.fullwidth"
            :altText="item.altText"
            :closedDefaultCaption="item.isSmall"
            :image-src="item.imageSrc"
            :key="`fullwidth1-${index}`"
        >
            <VsCaption
                slot="img-caption"
                variant="fullwidth"
            >
                <span slot="caption" v-if="item.caption">
                    {{ item.caption }}
                </span>

                <span slot="credit" v-if="item.credit">
                    {{ item.credit }}
                </span>
            </VsCaption>
        </VsImageWithCaption>

        <VsImageWithCaption
            v-for="(item, index) in imageWithCaption.imageExamples.small"
            :altText="item.altText"
            :closedDefaultCaption="item.isSmall"
            :image-src="item.imageSrc"
            :key="`fullwidth2-${index}`"
            style="max-width:300px"
        >
            <VsImg
                :src="item.imageSrc"
                :alt="item.altText"
                data-sizes="auto">
            </VsImg>

            <VsCaption
                slot="img-caption"
                variant="fullwidth"
            >
                <span slot="caption" v-if="item.caption">
                    {{ item.caption }}
                </span>

                <span slot="credit" v-if="item.credit">
                    {{ item.credit }}
                </span>
            </VsCaption>
        </VsImageWithCaption>

        <h3 style="margin-top: 5rem;">Social images</h3>
        <VsImageWithCaption
            v-for="(item, index) in imageWithCaption.imageExamples.social"
            :altText="item.altText"
            :image-src="item.imageSrc"
            :key="`social-${index}`"
        >
            <VsImg
                :src="item.imageSrc"
                :alt="item.altText"
                data-sizes="auto">
            </VsImg>

            <VsIcon
                slot="toggle-icon"
                name="instagram-filled"
                size="md"
            />

            <VsCaption
                slot="img-caption"
                :latitude="item.latitude"
                :longitude="item.longitude"
                :variant="item.variant"
            >
                <span slot="caption" v-if="item.caption">
                    {{ item.caption }}
                </span>

                <VsSocialCreditLink
                    slot="credit"
                    :credit="item.credit"
                    :socialPostUrl="item.socialPostUrl"
                    :source="item.source"
                >
                </VsSocialCreditLink>
            </VsCaption>
        </VsImageWithCaption>
    </BsWrapper>
  ```
</docs>
