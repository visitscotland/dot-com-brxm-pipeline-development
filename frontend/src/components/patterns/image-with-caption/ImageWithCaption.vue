<template>
    <figure
        class="vs-image-with-caption position-relative"
        :class="{ 'vs-image-with-caption--closed-default': closedDefaultCaption }"
    >
        <div
            class="vs-image-with-caption__image-wrapper"
            :class="mobileOverlap ? 'vs-image-with-caption__image-wrapper--overlapped' : ''"
        >
            <slot>
                <VsImg
                    v-if="imageSrc"
                    class="lazyload"
                    :src="imageSrc"
                    :data-srcset="imageSrc"
                    :alt="altText"
                    data-sizes="auto"
                />
            </slot>

            <VsButton
                variant="outline-transparent"
                class="vs-image-with-caption__toggle-caption-btn position-absolute"
                aria-label="Expand caption"
                :animate="false"
                :aria-controls="'image_' + imageSrc"
                :aria-expanded="showCaption ? 'true' : 'false'"
                @click.native="toggleCaption"
            >
                <span
                    class="sr-only"
                >
                    {{ captionButtonText }}
                </span>

                <VsIcon
                    v-if="showCaption"
                    name="close-circle"
                    variant="light"
                    size="md"
                />
                <slot
                    v-else
                    name="toggle-icon"
                >
                    <VsSvg
                        path="info-toggle"
                        height="24"
                        width="24"
                    />
                </slot>
            </VsButton>
        </div>

        <div
            :class="[
                { 'd-block': showCaption },
                `vs-image-with-caption__caption-wrapper--${textAlign}`
            ]"
            class="vs-image-with-caption__caption-wrapper"
            :id="'image_' + imageSrc"
        >
            <figcaption
                ref="figcaption"
                :class="[
                    isLargeCaption
                        ? 'vs-image-with-caption__large-caption'
                        : 'vs-image-with-caption__fullwidth-caption',
                    closedDefaultCaption ? 'default-closed' : '',
                ]"
                class="d-flex d-sm-block"
            >
                <VsRow class="justify-content-center justify-content-sm-start">
                    <VsCol
                        class="order-2 order-sm-1"
                        :class="[!showMap ? 'align-self-center' : '']"
                    >
                        <div :class="isLargeCaption ? 'p-4' : 'px-4 py-3 pr-8'">
                            <p class="vs-image-with-caption__image-caption">
                                <slot name="caption" />
                            </p>

                            <p class="vs-image-with-caption__image-credit">
                                <slot name="credit" />
                            </p>

                            <slot name="social-link" />
                        </div>
                    </VsCol>
                    <VsCol
                        class="col-12 col-sm-auto order-1
                        order-sm-2 pl-sm-0 align-self-end
                        align-self-sm-start"
                        v-if="showMap && variant !== isLargeCaption"
                    >
                        <div class="map-wrapper pt-3 pt-sm-2 pb-sm-2 pr-sm-4 mx-auto">
                            <VsImageLocationMap
                                :latitude="latitude"
                                :longitude="longitude"
                                :map-outline-color="tokens.color_white"
                                :map-marker-color="tokens.color_secondary_teal_tint_3"
                            />
                        </div>
                    </VsCol>
                </VsRow>
            </figcaption>
        </div>
    </figure>
</template>

<script>

// eslint-disable-next-line no-unused-vars
import { lazysizes } from 'lazysizes';
import VsSvg from '@components/elements/svg/Svg';
import VsImg from '@components/elements/img/Img';
import VsIcon from '@components/elements/icon/Icon';
import VsButton from '@components/elements/button/Button';
import { VsRow, VsCol } from '@components/elements/layout';
import VsImageLocationMap from '@components/patterns/image-location-map/ImageLocationMap';
import designTokens from '@/assets/tokens/tokens.json';

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
        VsRow,
        VsCol,
        VsImageLocationMap,
        VsButton,
        VsSvg,
        VsImg,
        VsIcon,
    },
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
         * The image latitude
         */
        latitude: {
            type: String,
            default: '',
        },

        /**
         * The image longitude
         */
        longitude: {
            type: String,
            default: '',
        },

        /**
         * The screenreader text for the toggle button
         */
        toggleButtonText: {
            type: String,
            default: 'Toggle Caption',
        },

        /**
         * Option to choose which variant to show
         * `fullwidth, large`
         */
        variant: {
            type: String,
            default: 'fullwidth',
            validator: (value) => value.match(/(fullwidth|large)/),
        },

        /**
         * Option to choose text alignment
         * `left, right`
         */
        textAlign: {
            type: String,
            default: 'left',
            validator: (value) => value.match(/(left|right)/),
        },

        /**
         * Option if the mobile view is overlapped at the bottom
        */
        mobileOverlap: {
            type: Boolean,
            default: false,
        },
        /**
        * Text for mobile caption toggle button
        */
    },
    data() {
        return {
            showCaption: false,
            tokens: designTokens,
        };
    },
    computed: {
        showMap() {
            return !!(this.longitude && this.latitude);
        },
        isLargeCaption() {
            return this.variant === 'large';
        },
        captionButtonText() {
            return this.showCaption ? 'Close image caption' : 'Open image caption';
        },
    },
    methods: {
        toggleCaption() {
            this.showCaption = !this.showCaption;
        },
    },
};
</script>

<style lang="scss">

.vs-image-with-caption__image-wrapper {
    img {
        width: 100%;
        height: auto;
    }

    .vs-image-with-caption__toggle-caption-btn {
        bottom: $spacer-2;
        padding: 0;
        right: $spacer-2;
        line-height: $line_height_xs;
        z-index: 3;
        display: block;

        @include media-breakpoint-up(sm) {
            display: none;

            .vs-image-with-caption--closed-default & {
                display: block;
            }
        }
    }

    &--overlapped {
        .vs-image-with-caption__toggle-caption-btn {
            bottom: $spacer-9;
            right: $spacer-4;
        }
    }
}

.vs-image-with-caption__caption-wrapper {
    display: none;

    @include media-breakpoint-up(sm) {
        display: block;

        .vs-image-with-caption--closed-default & {
            display: none;
        }
    }

    @include media-breakpoint-down(lg) {
        max-width: 100%;
        padding: 0;
    }

    figcaption {
        background-color: $color-gray-shade-6;
        color: $color-white;

        .vs-image-with-caption__image-caption,
        .vs-image-with-caption__image-credit {
            font-size: $small-font-size;
            line-height: $line-height-standard;
        }

        .vs-image-with-caption__image-caption {
            font-weight: $font-weight-semi-bold;
        }

        .vs-image-with-caption__image-credit {
            font-weight: $font-weight-light;
            margin-bottom: $spacer-0;
        }

        &.vs-image-with-caption__large-caption,
        &.vs-image-with-caption__fullwidth-caption {
            position: absolute;
            top: 0;
            right: 0;
            width: 100%;
            height: 100%;
            z-index: 2;
            text-align: center;

            > .row {
                margin: 0 auto;
            }

            .vs-image-with-caption__image-caption {
                margin-bottom: $spacer-2;
            }

            .map-wrapper {
                max-width: 60px;
            }
        }

        &.vs-image-with-caption__large-caption {
            @include media-breakpoint-up(sm) {
                bottom: -48px;
                top: auto;
                width: 310px;
                height: auto;
                min-height: 96px;
                text-align: left;

                > .row {
                    margin: 0 -16px;
                }

                .vs-image-with-caption__image-caption {
                    margin-bottom: $spacer-8;
                }

                .map-wrapper {
                    max-width: 74px;
                }
            }
        }

        &.vs-image-with-caption__fullwidth-caption:not(.default-closed) {
            @include media-breakpoint-up(sm) {
                position: relative;
                width: 100%;
                height: auto;
                min-height: 64px;
                text-align: left;

                > .row {
                    margin: 0 -16px;
                }
            }
        }
    }

    @include media-breakpoint-up(md) {
        &--right {
            figcaption.vs-image-with-caption__fullwidth-caption p,
            figcaption.vs-image-with-caption__large-caption p {
                text-align: right;
            }
        }
    }
}

@include no-js {
    .vs-image-with-caption__image-wrapper {
        .vs-image-with-caption__toggle-caption-btn {
            display: none;
        }
    }

    .vs-image-with-caption__caption-wrapper {
        display: block;

        .vs-image-with-caption__large-caption,
        .vs-image-with-caption__fullwidth-caption {
            @include media-breakpoint-down(xs) {
                position: relative;
            }
        }
    }

    .vs-image-with-caption--closed-default {
        .vs-image-with-caption__image-wrapper {
            .vs-image-with-caption__toggle-caption-btn {
                display: none;
            }
        }

        .vs-image-with-caption__caption-wrapper {
            .vs-image-with-caption__large-caption,
            .vs-image-with-caption__fullwidth-caption {
                position: relative;
            }
        }
    }
}
</style>

<docs>

  ```jsx

    <h3>Large Caption Style</h3>
    <VsImageWithCaption
        v-for="(item, index) in imageWithCaption.imageExamples.large"
        :altText="item.altText"
        :image-src="item.imageSrc"
        :key="`large-${index}`"
        :latitude="item.latitude"
        :longitude="item.longitude"
        variant="large"
        style="max-width:700px"
        class="mb-11"
    >
        <VsImg
            class="lazyload"
            :src="item.imageSrc"
            :data-srcset="item.imageSrc"
            :alt="item.altText"
            data-sizes="auto">
        </VsImg>

        <span slot="caption" v-if="item.caption">
            {{ item.caption }}
        </span>

        <span slot="credit" v-if="item.credit">
            &copy; {{ item.credit }}
        </span>
    </VsImageWithCaption>

    <h3 style="margin-top: 7rem;">Fullwidth Caption Style</h3>
    <VsImageWithCaption
        v-for="(item, index) in imageWithCaption.imageExamples.fullwidth"
        :altText="item.altText"
        :closedDefaultCaption="item.isSmall"
        :image-src="item.imageSrc"
        :key="`fullwidth1-${index}`"
        variant="fullwidth"
        style="max-width:700px"
    >
        <VsImg
            class="lazyload"
            :src="item.imageSrc"
            :data-srcset="item.imageSrc"
            :alt="item.altText"
            data-sizes="auto">
        </VsImg>

        <span slot="caption" v-if="item.caption">
            {{ item.caption }}
        </span>

        <span slot="credit" v-if="item.credit">
            &copy; {{ item.credit }}
        </span>
    </VsImageWithCaption>

    <VsImageWithCaption
        v-for="(item, index) in imageWithCaption.imageExamples.small"
        :altText="item.altText"
        :closedDefaultCaption="item.isSmall"
        :image-src="item.imageSrc"
        :key="`fullwidth2-${index}`"
        variant="fullwidth"
        style="max-width:300px"
    >
        <VsImg
            class="lazyload"
            :src="item.imageSrc"
            :data-srcset="item.imageSrc"
            :alt="item.altText"
            data-sizes="auto">
        </VsImg>

        <span slot="caption" v-if="item.caption">
            {{ item.caption }}
        </span>

        <span slot="credit" v-if="item.credit">
            &copy; {{ item.credit }}
        </span>
    </VsImageWithCaption>

    <h3 style="margin-top: 5rem;">Social images</h3>
    <VsImageWithCaption
        v-for="(item, index) in imageWithCaption.imageExamples.social"
        :altText="item.altText"
        :image-src="item.imageSrc"
        :key="`social-${index}`"
        :latitude="item.latitude"
        :longitude="item.longitude"
        :variant="item.variant"
        style="max-width:700px"
    >
        <VsImg
            class="lazyload"
            :src="item.imageSrc"
            :data-srcset="item.imageSrc"
            :alt="item.altText"
            data-sizes="auto">
        </VsImg>

        <VsSvg slot="toggle-icon" path="instagram-bg" height="24" width="24" />

        <span slot="caption" v-if="item.caption">
            {{ item.caption }}
        </span>

        <VsSocialCreditLink
            slot="social-link"
            :credit="item.credit"
            :socialPostUrl="item.socialPostUrl"
            :source="item.source"
        >
        </VsSocialCreditLink>
    </VsImageWithCaption>

  ```
</docs>
