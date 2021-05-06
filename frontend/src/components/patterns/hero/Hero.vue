<template>
    <figure class="vs-hero d-flex flex-column">
        <div
            class="vs-hero__image-wrapper"
            role="document"
        >
            <slot />

            <VsButton
                variant="outline-transparent"
                class="d-lg-none position-absolute vs-hero__toggle-caption"
                v-if="showToggle"
                :animate="false"
                :aria-expanded="showCaption ? 'true' : 'false'"
                :aria-controls="'image_' + imageSrc"
                @click.native="toggleCaption"
            >
                <VsSvg
                    path="info-toggle"
                    height="24"
                    width="24"
                />
                <span class="sr-only">{{ toggleButtonText }}</span>
            </VsButton>
        </div>

        <VsContainer
            class="position-relative vs-hero__caption-wrapper"
            :class="[showCaption ? 'd-flex' : 'd-none d-lg-flex']"
            :id="'image_' + imageSrc"
        >
            <figcaption
                ref="figcaption"
                v-if="showCaptionData || showMap"
            >
                <VsRow>
                    <VsCol>
                        <div
                            class="p-4"
                            v-if="showCaptionData"
                        >
                            <p
                                class="vs-hero__image-caption"
                                v-if="caption"
                            >
                                {{ caption }}
                            </p>
                            <p
                                class="vs-hero__image-credit m-0"
                                v-if="credit"
                            >
                                &copy; {{ credit }}
                            </p>
                        </div>
                    </VsCol>
                    <VsCol
                        cols="auto"
                        class="pl-0"
                        v-if="showMap"
                    >
                        <div class="map__wrapper">
                            <VsImageLocationMap
                                :latitude="latitude"
                                :longitude="longitude"
                                map-outline-color="#FFFFFF"
                                map-marker-color="#7CC9CC"
                            />
                        </div>
                    </VsCol>
                </VsRow>
            </figcaption>
        </VsContainer>
    </figure>
</template>

<script>
import VsSvg from '@components/elements/svg/Svg';
import VsButton from '@components/elements/button/Button';
import {
    VsContainer, VsRow, VsCol,
} from '@components/elements/layout';
import VsImageLocationMap from '@components/patterns/image-location-map/ImageLocationMap';

/**
 * Hero image element
 *
 * @displayName Hero
 */

export default {
    name: 'VsHero',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsContainer,
        VsRow,
        VsCol,
        VsImageLocationMap,
        VsButton,
        VsSvg,
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
         * The image credit
         */
        credit: {
            type: String,
            default: '',
        },

        /**
         * The caption for the hero's image
         */
        caption: {
            type: String,
            default: '',
        },

        /**
         * The source URL for the hero's image
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
    },
    data() {
        return {
            showCaption: false,
        };
    },
    computed: {
        showCaptionData() {
            return this.caption.length || this.credit.length;
        },
        showToggle() {
            // only show the image caption toggle button if there's a map or caption data
            return this.showCaptionData || this.showMap;
        },
        showMap() {
            // only show the map if longitude and latitude are both set
            return this.longitude.length && this.latitude.length;
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
.vs-hero {
    position: relative;
    background-color: $color-theme-dark;

    .map__wrapper {
        max-width: 80px;
        width: 80px;
    }

    .vs-hero__caption-wrapper {
        @include media-breakpoint-down(lg) {
            padding: 0;
        }
    }

    .vs-hero__toggle-caption {
        bottom: 0.5rem;
        padding: 0;
        right: 0.5rem;
        border-radius: 50%;
    }

    img {
        width: 100%;
        height: auto;
    }

    .vs-hero__image-wrapper {
        position: relative;
        max-height: 100vh;
        overflow: hidden;

        @include media-breakpoint-up(xl) {
            display: flex;
            flex-direction: column;
            justify-content: top; /* Centering y-axis */
            align-items: center; /* Centering x-axis */
        }
    }

    .vs-hero__overlay-text {
        font-family: $headings-font-family;
        font-size: $display1-size;
        left: 50%;
        max-width: 100%;
        position: absolute;
        text-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
        top: 50%;
        transform: translate(-50%, -50%);

        @include media-breakpoint-up(sm) {
            font-size: $display1-size * 1.5;
        }

        @include media-breakpoint-up(md) {
            font-size: $display1-size * 1.75;
        }

        @include media-breakpoint-up(lg) {
            font-size: $display1-size * 2;
        }
    }

    figcaption {
        background-color: $color-theme-dark;
        color: $color-white;
        width: 100%;

        @include media-breakpoint-up(lg) {
            bottom: 0;
            max-width: 400px;
            position: absolute;
            right: 1rem;
            width: 14rem;
            z-index: 2;
        }
    }

    .vs-image-location-map {
        height: $spacer-11;
    }

    .vs-hero__image-caption {
        font-size: 0.875rem;
        font-weight: $font-weight-semi-bold;
        line-height: 1rem;
    }

    .vs-hero__image-credit {
        font-size: 0.875rem;
        font-weight: $font-weight-light;
        line-height: 1rem;
    }
}

</style>

<docs>
  ```jsx
    <VsHero
      v-for="(item, index) in hero.imageExamples"
      :altText="item.altText"
      :credit="item.credit"
      :caption="item.caption"
      :image-src="item.imageSrc"
      :key="index"
      :latitude="item.latitude"
      :longitude="item.longitude"
    >
    <VsImg
        class="lazyload"
        :src="item.imageSrc"
        srcset="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="
        :data-srcset="item.imageSrc"
        :alt="item.altText"
        data-sizes="auto">
    </VsImg>
      <span class="vs-hero__overlay-text text-light">Scotland</span>
    </VsHero>
  ```
</docs>
