<template>
    <section
        class="vs-megalink-single-image"
        :class="singleImageClasses"
        data-test="megalink-single-image"
    >
        <!-- @slot Slot for main image -->
        <slot name="vsSingleImage" />

        <VsRow>
            <VsCol
                cols="12"
                sm="10"
                lg="6"
                class="offset-sm-1"
                :class="alternate ? 'offset-lg-6' : 'offset-lg-0'"
            >
                <div
                    class="vs-megalink-single-image__content"
                    data-test="megalink-single-image__content"
                >
                    <VsHeading
                        level="3"
                        v-if="title"
                        class="vs-megalink-single-image__title"
                        data-test="megalink-single-image__title"
                    >
                        {{ title }}
                    </VsHeading>
                    <VsRichTextWrapper variant="lead">
                        <!-- @slot Slot for content -->
                        <slot name="vsSingleImageContent" />
                    </VsRichTextWrapper>

                    <VsLinkList>
                        <!-- @slot Slot for links list -->
                        <slot name="vsSingleImageLinks" />
                    </VsLinkList>

                    <div class="vs-megalink-single-image__button">
                        <VsButton
                            :href="buttonLink"
                            v-if="buttonLink"
                        >
                            <!-- @slot Slot for button text -->
                            <slot name="vsSingleImageButtonText" />
                        </VsButton>
                    </div>
                </div>
            </VsCol>
        </VsRow>
    </section>
</template>

<script>
import VsHeading from '@components/elements/heading/Heading';
import VsRichTextWrapper from '@components/elements/rich-text-wrapper/RichTextWrapper';
import VsButton from '@components/elements/button/Button';
import VsLinkList from '@components/patterns/link-list/LinkList';
import { VsRow, VsCol } from '@components/elements/grid';

/**
 * This component is a variant of the megalinks component with a large image
 * and an overlaid panel containing content.
 *
 * @displayName Megalinks Single Image
 */

export default {
    name: 'VsMegalinkSingleImage',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsHeading,
        VsRichTextWrapper,
        VsButton,
        VsRow,
        VsCol,
        VsLinkList,
    },
    props: {
        /**
        * Alternate layout with image and text switched sides
        */
        alternate: {
            type: Boolean,
            default: false,
        },
        /**
        * Component title
        */
        title: {
            type: String,
            default: null,
        },
        /**
        * Button url
        */
        buttonLink: {
            type: String,
            default: '',
        },
        /**
        * The component theme
        */
        theme: {
            type: String,
            default: 'light',
            validator: (value) => value.match(/(light|dark)/),
        },
    },
    computed: {
        singleImageClasses() {
            return [
                `vs-megalink-single-image--${this.theme}`,
                this.alternate ? 'vs-megalink-single-image--alternate' : '',
            ];
        },
    },
};
</script>

<style lang="scss">
    .vs-megalink-single-image {
        min-width: 100%;

        .vs-megalink-single-image__title {
            margin-bottom: $spacer-8;
            margin-top:0;
        }

        .vs-megalink-single-image__content {
            position: relative;
            z-index: 10;
            background: $color-white;
            margin: $spacer-0 $spacer-3;
            padding: $spacer-8 $spacer-6;
            clear: both;
            width: 100%;

            p {
                margin-bottom: $spacer-4;
            }
        }

        .vs-megalink-single-image__link-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .vs-megalink-single-image__link-list-item {
            margin-top: $spacer-4;
            font-size: $font-size-5;

            &:first-of-type {
                margin-bottom: $spacer-0;
            }
        }

        .vs-image-with-caption {
            overflow: hidden;
            margin: 0 -12px (-$spacer-8);
        }

        .vs-megalink-single-image__button {
            margin-top: $spacer-7;
        }

        &--dark {
            .vs-megalink-single-image__content {
                background: $color-gray-shade-7;
            }

            .vs-megalink-single-image__title,
            p {
                color: $color-white;
            }
        }

        @include media-breakpoint-up(sm) {
            margin: 0;

            .vs-image-with-caption {
                margin-bottom: 0;
            }

            .vs-megalink-single-image__content {
                background: transparent;
            }

            &--dark {
                .vs-megalink-single-image__content {
                    background: $color-secondary-gray-shade-4;
                }
            }
        }

        @include media-breakpoint-up(lg) {
            width: 100%;
            margin: 0 0 $spacer-11;
            display: flex;
            flex-direction: column;

            .vs-megalink-single-image__content {
                padding: $spacer-9 $spacer-9 $spacer-9 $spacer-9;
                margin: 0;
                background: $color-white;
            }

            .vs-image-with-caption {
                width: 66%;
                align-self: flex-end;
                margin: 0 0 -200px;
            }

            &--alternate {
                .vs-image-with-caption {
                    align-self: flex-start;
                }

                .vs-megalink-single-image__content {
                    align-self: flex-end;
                }
            }

            &--dark {
                .vs-megalink-single-image__title {
                    color: $color-white;
                }

                .vs-megalink-single-image__content {
                    background: $color-gray-shade-7;
                }
            }
        }

        @include media-breakpoint-up(xl) {
            .vs-megalink-single-image__content {
                padding: $spacer-9 $spacer-12 $spacer-9 $spacer-9;
            }
        }
    }
</style>

<docs>
    ```js
    <VsMegalinks>
        <VsCol cols="12">
            <VsMegalinkSingleImage
                title="The Component heading"
                buttonLink="www.visitscotland.com"
            >
                <template slot="vsSingleImage">
                    <VsImageWithCaption
                        mobile-overlap
                        alt-text="Image alt text"
                        image-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                    >
                        <VsCaption
                            slot="img-caption"
                            text-align="right"
                        >
                            <template slot="caption">
                                An image of Scotland
                            </template>

                            <template slot="credit">
                                @2020 Credit here
                            </template>
                        </VsCaption>
                    </VsImageWithCaption>
                </template>

                <template slot="vsSingleImageContent">
                    <p>
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        Integer et eros at est dignissim interdum. Fusce nisl metus,
                        pharetra eu feugiat vitae, porttitor eget est. Vivamus
                        condimentum urna vel ante tempor, a eleifend neque ultricies.
                        Morbi convallis, felis id semper vulputate, nisl est porta quam,
                        luctus vehicula sapien orci quis urna. Suspendisse accumsan leo
                        diam, nec faucibus neque pulvinar vitae. Duis non rutrum felis,
                        ut pretium purus. Nullam hendrerit quam vitae ipsum aliquam
                        fermentum. Fusce gravida eu est in convallis.
                    </p>
                </template>
                <template slot="vsSingleImageLinks">
                    <VsLinkListItem
                        href="www.visitscotland.com"
                        variant="primary"
                    >
                        This is a link here
                    </VsLinkListItem>
                    <VsLinkListItem
                        href="www.visitscotland.com"
                        type="external"
                        variant="primary"
                    >
                        This is an external link here
                    </VsLinkListItem>
                    <VsLinkListItem
                        href="www.visitscotland.com"
                        type="download"
                        variant="primary"
                    >
                        This is a download link here
                    </VsLinkListItem>
                </template>
                <template slot="vsSingleImageButtonText">
                    This is the button
                </template>
            </VsMegalinkSingleImage>
        </VsCol>
        <VsCol cols="12">
            <VsMegalinkSingleImage
                title="This is the second component heading"
                buttonLink="www.visitscotland.com"
                alternate
            >
                <template slot="vsSingleImage">
                    <VsImageWithCaption
                        mobile-overlap
                        alt-text="Image alt text"
                        image-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                    >
                        <VsCaption
                            slot="img-caption"
                            text-align="left"
                        >
                            <template slot="caption">
                                An image of Scotland
                            </template>

                            <template slot="credit">
                                @2020 Credit here
                            </template>
                        </VsCaption>
                    </VsImageWithCaption>
                </template>

                <template slot="vsSingleImageContent">
                    <p>
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        Integer et eros at est dignissim interdum. Fusce nisl metus,
                        pharetra eu feugiat vitae, porttitor eget est. Vivamus
                        condimentum urna vel ante tempor, a eleifend neque ultricies.
                        Morbi convallis, felis id semper vulputate, nisl est porta quam,
                        luctus vehicula sapien orci quis urna. Suspendisse accumsan leo
                        diam, nec faucibus neque pulvinar vitae. Duis non rutrum felis,
                        ut pretium purus. Nullam hendrerit quam vitae ipsum aliquam
                        fermentum. Fusce gravida eu est in convallis.
                    </p>
                </template>
                <template slot="vsSingleImageLinks">
                    <VsLinkListItem
                        href="www.visitscotland.com"
                    >
                        This is a link here
                    </VsLinkListItem>
                    <VsLinkListItem
                        href="www.visitscotland.com"
                        variant="primary"
                    >
                        This is a link here
                    </VsLinkListItem>
                    <VsLinkListItem
                        href="www.visitscotland.com"
                        variant="primary"
                    >
                        This is a link here
                    </VsLinkListItem>
                </template>
                <template slot="vsSingleImageButtonText">
                    This is the button
                </template>
            </VsMegalinkSingleImage>
        </VsCol>
    </VsMegalinks>
    ```
</docs>
