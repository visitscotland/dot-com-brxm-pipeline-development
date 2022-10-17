<template>
    <VsStretchedLinkCard
        :link="linkUrl"
        :type="linkType"
        class="vs-megalink-multi-image p-2"
        :class="multiImageClasses"
        :img-src="imgSrc"
        :img-alt="imgAlt"
        :data-test="featured ? 'megalink-multi-image-featured' : 'megalink-multi-image-card'"
        :theme="theme"
        :video-id="videoId"
        :video-btn-text="videoBtnText"
        :error-message="errorMessage"
    >
        <VsStretchedLinkPanels
            v-if="days && transport"
            :days="days"
            :transport="transport"
            :transport-name="transportName"
            slot="stretchedCardPanels"
            :days-label="daysLabel"
            data-test="vs-itinerary-panels"
        />

        <span
            slot="stretchedCardHeader"
            class="vs-megalink-multi-image__title"
            data-test="megalink-multi-image__title"
        ><!-- @slot Slot to contain heading --><slot name="vsMultiImageHeading" /></span>

        <VsRichTextWrapper
            slot="stretchedCardContent"
            class="vs-megalink-multi-image__content"
            data-test="megalink-multi-image__content"
        >
            <!-- @slot Slot to contain content -->
            <slot name="vsMultiImageContent" />
        </VsRichTextWrapper>
    </VsStretchedLinkCard>
</template>

<script>
import VsStretchedLinkCard from '@components/patterns/stretched-link-card/StretchedLinkCard';
import VsStretchedLinkPanels from '@components/patterns/stretched-link-card/components/StretchedLinkPanels';
import VsRichTextWrapper from '@components/elements/rich-text-wrapper/RichTextWrapper';

/**
* Megalink cards to be used in the megalinks component
* There is a standard and featured variant.
 *
 * @displayName Megalinks Multi-Image
*/

export default {
    name: 'VsMegalinkMultiImage',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsStretchedLinkCard,
        VsRichTextWrapper,
        VsStretchedLinkPanels,
    },
    props: {
        /**
        * If the component should be the featured variant
        */
        featured: {
            required: false,
            type: Boolean,
            default: false,
        },
        /**
        * If the featured component is the last one
        */
        lastFeatured: {
            required: false,
            type: Boolean,
            default: false,
        },
        /**
        * The image to use in the component
        */
        imgSrc: {
            required: true,
            type: String,
        },
        /**
        * The image alt text to use in the component
        */
        imgAlt: {
            required: false,
            type: String,
            default: '',
        },
        /**
        * The type of link. This will set the icon.
        * `external, internal, download, video`
        */
        linkType: {
            type: String,
            required: true,
            validator: (value) => value.match(/(default|external|internal|download|video)/),
        },
        /**
        * The link destination
        */
        linkUrl: {
            type: String,
            required: true,
        },
        /**
        * The component color theme
        */
        theme: {
            type: String,
            default: 'light',
            validator: (value) => value.match(/(light|dark)/),
        },
        /**
        * Optional prop for number of days
        */
        days: {
            type: String,
            default: '',
        },
        /**
        * Label for days - too allow translation in CMS
        */
        daysLabel: {
            type: String,
            default: 'days',
        },
        /**
        * Optional prop for transport type (will show a the transport icon if used)
        */
        transport: {
            type: String,
            default: '',
        },
        /**
        * Display-friendly transport name
        * to allow for translation
        */
        transportName: {
            type: String,
            default: '',
        },
        /**
         * An optional YouTube video ID
         */
        videoId: {
            type: String,
            default: '',
        },
        /**
         * A label to add to the youtube play button if one is present.
         * Only appears in certain page layouts.
         */
        videoBtnText: {
            type: String,
            default: 'Play Video',
        },
        /**
         * Message to show when there's an error with a third party
        */
        errorMessage: {
            type: String,
            required: true,
        },
    },
    computed: {
        multiImageClasses() {
            return [
                `vs-megalink-multi-image--${this.theme}`,
                {
                    'vs-megalink-multi-image--featured': this.featured,
                    'vs-megalink-multi-image--featured-last': this.lastFeatured,
                },
            ];
        },
    },
};
</script>

<style lang="scss">
    .vs-megalink-multi-image.card {
        border: none;
        background: transparent;
        position: relative;
        margin-bottom: $spacer-8;
        transition: box-shadow $duration-slowly;

        &:hover {
            box-shadow: $shadow_popover;

            .vs-megalink-multi-image__title {
                text-decoration: underline;
            }
        }

        .card-body {
            padding: $spacer-4 0 $spacer-2;
            width: 100%;
        }

        .vs-megalink-multi-image__img {
            max-width: 100%;
        }

        .vs-megalink-multi-image__title {
            font-size: $font-size-2;
            line-height: $line-height-s;
            letter-spacing: $letter-spacing-xl;
        }

        .card-title {
            margin-bottom: 0;
            margin-top: 0;
        }

        .vs-link__icon {
            height: 12px;
            width: 12px;
        }

        .vs-megalink-multi-image__content {
            margin-top: $spacer-2;
            line-height: $line-height-s;

            p:last-of-type {
                margin-bottom: 0;
            }
        }
    };

    .vs-megalink-multi-image--dark.card {
        .vs-stretched-link-card__title {
            color: $color-white;

            .stretched-link {
                color: $color-white;
            }
        }

        &:hover {
            box-shadow: $shadow_popover_dark;

            &:not(.vs-megalink-multi-image--featured) {
                background-color: $color-secondary-gray-shade-5;
            }
        }
    }

    @include media-breakpoint-up(xl) {
        .vs-megalinks--multi-image .vs-megalinks__links-wrapper .row {
            width: calc(100% + #{$spacer-10} + #{$spacer-3});
            margin-left: -#{$spacer-8};

            [class*=col-] {
                padding-left: $spacer-8;
                padding-right: $spacer-8;
            }
        }

        .vs-megalink-multi-image.card {
            margin-bottom: $spacer-11;

            .vs-megalink-multi-image__title {
                font-size: $font-size-3;
                line-height: $line-height-s;
            }

            .card-body {
                padding-bottom: $spacer-5;
            }
        }

        .vs-megalink-multi-image--featured.card {
            display: flex;
            flex-direction: row;
            justify-content: flex-start;
            width: 100%;
            transform: rotate(0deg);

            .vs-megalink-multi-image__title {
                font-size: $font-size-5;
                letter-spacing: $letter-spacing-xxl;
            }

            .vs-link__icon {
                height: 16px;
                width: 16px;
            }

            .vs-stretched-link-card__img-container {
                width: 75%
            }

            .megalink-multi-image__content {
                font-size: $font-size-5;
                margin-top: $spacer-8;
                line-height: $line-height-m;
            }

            .card-body {
                position: absolute !important;
                background-color: $color-white;
                width: 40%;
                right: 0;
                top: $spacer-10;
                padding: $spacer-8;
                transition: box-shadow $duration-slowly;
            }

            .vs-stretched-link-card__video-button {
                position: fixed;
            }

            .stretched-link:after {
                position: fixed;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
            }

            .vs-stretched-link-panels {
                left: $spacer-5;
                top: $spacer-5;
            }

            .vs-stretched-link-panels__panel:first-of-type {
                margin-left: 0;
            }

            &.vs-megalink-multi-image--featured-last {
                flex-direction: row-reverse;

                .vs-stretched-link-panels {
                    left: auto;
                    right: $spacer-5;
                }

                .card-body {
                    left: $spacer-0;;
                    right: auto;
                }

                .vs-stretched-link-card__video-button {
                    left: auto;
                    right: $spacer-2;
                }
            }

            &:hover {
                box-shadow: none !important;

                .card-body {
                    box-shadow: $shadow_popover;
                }
            }

            &.vs-megalink-multi-image--dark {
                .card-body {
                    background-color: $color-secondary-gray-shade-5;
                }

                &:hover {
                    .card-body {
                        box-shadow: $shadow_popover_dark;
                    }
                }
            }
        }
    }
</style>

<docs>
    ```js
    <VsMegalinks variant="multi-image">
        <VsContainer>
            <VsRow>
                <VsCol
                    cols="12"
                    lg="6"
                    xl="12"
                >
                    <VsMegalinkMultiImage
                        featured
                        imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                        imgAlt="This is the alt text"
                        linkType="internal"
                        linkUrl="www.visitscotland.com"
                        days="2"
                        daysLabel="days"
                        transport="bus"
                        transportName="bus"
                    >
                        <template slot="vsMultiImageHeading">
                            The Edinburgh International Festival and summer festival</template>
                        <template slot="vsMultiImageContent">
                            <p>Right across the country, you’ll find amazing places
                            to eat and drink from local markets to renowned
                            restaurants.</p>
                        </template>
                    </VsMegalinkMultiImage>
                </VsCol>
                <VsCol
                    cols="12"
                    md="6"
                    xl="4"
                >
                    <VsMegalinkMultiImage
                        imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                        imgAlt="This is the alt text 1"
                        linkType="external"
                        linkUrl="www.visitscotland.com"
                    >
                        <template slot="vsMultiImageHeading">
                            Count 7,000 shining stars in the iconic
                            galloway forest</template>
                        <template slot="vsMultiImageContent">
                            <p>Right across the country, you’ll find amazing
                            places to eat and drink from local markets to renowned
                            restaurants. Here are some recomm…</p>
                        </template>
                    </VsMegalinkMultiImage>
                </VsCol>
                <VsCol
                    cols="12"
                    md="6"
                    xl="4"
                >
                    <VsMegalinkMultiImage
                        imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                        imgAlt="This is the alt text 2"
                        linkType="external"
                        linkUrl="www.visitscotland.com"
                    >
                        <template slot="vsMultiImageHeading">
                            Count 7,000 shining stars in the iconic galloway forest</template>
                        <template slot="vsMultiImageContent">
                            <p>Right across the country, you’ll find amazing places
                            to eat and drink
                            from local markets to renowned restaurants.
                            Here are some recomm…</p>
                            <p>Right across the country, you’ll find amazing places
                            to eat and drink
                            from local markets to renowned restaurants.
                            Here are some recomm…</p>
                            <p>Right across the country, you’ll find amazing places
                            to eat and drink
                            from local markets to renowned restaurants.
                            Here are some recomm…</p>
                        </template>
                    </VsMegalinkMultiImage>
                </VsCol>
                <VsCol
                    cols="12"
                    md="6"
                    xl="4"
                >
                    <VsMegalinkMultiImage
                        imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                        linkType="download"
                        linkUrl="www.visitscotland.com"
                    >
                        <template slot="vsMultiImageHeading">
                            Soar through the air on a boat of Falkirk Wheel (PDF 3MB)</template>
                        <template slot="vsMultiImageContent">
                            <p>Right across the country, you’ll find amazing
                            places to eat and drink from local markets to renowned
                            restaurants. Here are some recomm…</p>
                        </template>
                    </VsMegalinkMultiImage>
                </VsCol>
                <VsCol
                    cols="12"
                    lg="6"
                    xl="12"
                >
                    <vs-megalink-multi-image
                        featured
                        lastFeatured
                        imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                        imgAlt="This is the alt text"
                        linkType="internal"
                        linkUrl="www.visitscotland.com"
                        days="6"
                        daysLabel="days"
                        transport="bus"
                        transportName="bus"
                    >
                        <template slot="vsMultiImageHeading">
                            The Edinburgh International Festival and summer festival</template>
                        <template slot="vsMultiImageContent">
                            <p>Right across the country, you’ll find amazing places
                            to eat and drink from local markets to renowned
                            restaurants.</p>
                        </template>
                    </vs-megalink-multi-image>
                </VsCol>
            </VsRow>
        </VsContainer>
    </VsMegalinks>
    ```
</docs>
