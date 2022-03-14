<template>
    <section
        class="vs-megalinks"
        :class="megalinksClasses"
        data-test="megalinks"
    >
        <VsContainer fluid="lg">
            <VsRow>
                <VsCol
                    cols="12"
                    md="8"
                    offset-md="2"
                >
                    <div
                        class="vs-megalinks__intro"
                        v-if="title"
                        data-test="vs-megalinks__intro"
                    >
                        <VsHeading
                            level="2"
                            class="vs-megalinks__heading"
                            data-test="vs-megalinks__heading"
                        >
                            {{ title }}
                        </VsHeading>

                        <div class="vs-megalinks__intro-content lead">
                            <!-- @slot Slot to contain optional intro content -->
                            <slot name="vsMegalinksIntro" />
                        </div>
                    </div>
                </VsCol>
            </VsRow>
            <div class="row vs-megalinks__links-wrapper">
                <!-- @slot Default slot to contain link blocks -->
                <slot />
            </div>
            <VsRow v-if="buttonLink && variant !== 'single-image'">
                <VsCol cols="12">
                    <div
                        class="vs-megalinks__button"
                        data-test="vs-megalinks__button"
                    >
                        <VsButton :href="buttonLink">
                            <!-- @slot Slot to contain button text -->
                            <slot name="vsMegalinksButton" />
                        </VsButton>
                    </div>
                </VsCol>
            </VsRow>
        </VsContainer>
    </section>
</template>

<script>
import VsHeading from '@components/elements/heading/Heading';
import VsButton from '@components/elements/button/Button';
import {
    VsContainer,
    VsRow,
    VsCol,
} from '@components/elements/layout';

/**
 * Megalinks wrapper used with Megalinks components.
 *
 * @displayName Megalinks
 */
export default {
    name: 'VsMegalinks',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsContainer,
        VsRow,
        VsCol,
        VsHeading,
        VsButton,
    },
    props: {
        /**
        * Title for the megalinks component
        */
        title: {
            type: String,
            required: false,
            default: null,
        },
        /**
        * Component has an optional main link at the bottom
        * this is the href for that
        */
        buttonLink: {
            type: String,
            required: false,
            default: null,
        },
        /**
        * The component variant
        */
        variant: {
            type: String,
            required: false,
            default: null,
        },
        /**
        * The component color theme
        */
        theme: {
            type: String,
            default: 'light',
            validator: (value) => value.match(/(light|dark)/),
        },
    },
    computed: {
        megalinksClasses() {
            return [
                this.variant ? `vs-megalinks--${this.variant}` : '',
                `vs-megalinks--${this.theme}`,
            ];
        },
    },
};
</script>

<style lang="scss">
    .vs-megalinks {
        padding: $spacer-9 0 $spacer-9;

        // make panels in a row equal height
        .row {
            display: flex;
            min-width: 100%;

            // IE11 fix
            @media screen and (-ms-high-contrast: active), screen and (-ms-high-contrast: none) {
                width: 100%;
            }
        }
        @supports not (-ms-high-contrast: none) {
            .row > [class*='col-'] {
                display: flex;
            }
        }

        .vs-megalinks__intro {
            width: 100%;
            text-align: center;
            margin-bottom: $spacer-8;

            .vs-heading__sub-heading {
                margin: 0;
            }

            p:first-of-type {
                margin-top: $spacer-6;
            }

            p:last-of-type {
                margin-bottom: 0;
            }
        }

        .vs-megalinks__heading {
            margin-bottom: 0 !important;

            .heading__sub-heading {
                display: none;
            }
        }

        .vs-megalinks__intro-content,
        .vs-megalinks__intro-content .vs-rich-text-wrapper {
            margin-top: $spacer-6;
            font-size: $lead-font-size;
            line-height: $line-height-s;
        }

        .vs-megalinks__button {
            width: 100%;
            text-align: center;
        }

        &--multi-image {
            padding-bottom: 0;

            .vs-megalinks__button {
                margin-bottom: $spacer-9;
            }
        }

        &--link-list {
            .vs-megalinks__button {
                margin-top: $spacer-8;
            }
        }

        @include media-breakpoint-up(lg) {
            padding: $spacer-12 0 $spacer-12;

            .vs-megalinks__intro {
                text-align: center;
                margin-bottom: $spacer-9;
            }

            .vs-megalinks__intro-content,
            .vs-megalinks__intro-content .vs-rich-text-wrapper {
                font-size: $font-size-lg;
                line-height: $line-height-m;
            }

            &--multi-image {
                padding-bottom: $spacer-8;

                .vs-megalinks__button {
                    margin-bottom: $spacer-10;
                }
            }

             &--single-image {
                padding-bottom: 0;
            }
        }

        &--light {
            & + .vs-megalinks--light {
                padding-top: 0;
            }
        }

        &--dark {
            background: $color-secondary-gray-shade-4;

            & + .vs-megalinks--dark {
                padding-top: 0;
            }

            .vs-megalinks__heading {
                color: $color-yellow;
            }

            p {
                color: $color-white;
            }

            .vs-megalink-multi-image.card,
            .vs-megalink-link-list .vs-megalink-link-list__wrapper.card {
                &:hover {
                    box-shadow: 10px 10px 15px #000000;
                }

                .stretched-link:focus {
                    outline: 2px $color-yellow solid;
                }
            }
        }
    }

     // styles needed to control spacing with CMS containers
    .has-edit-button.theme-light + .has-edit-button.theme-light .vs-megalinks {
        padding-top: 0;
    }

    .has-edit-button.theme-dark + .has-edit-button.theme-dark .vs-megalinks {
        padding-top: 0;
    }
</style>

<docs>
    ```js
    <VsMegalinks
        title="A megalinks multi image component"
        class="vs-megalinks--multi-image"
        buttonLink="http://www.visitscotland.com"
    >
        <template slot="vsMegalinksIntro">
            <p>Sed at mauris a est dictum luctus. Nullam viverra
            pellentesque dolor, id elementum neque viverra quis.
            Morbi lacinia est id risus facilisis porttitor ut ac mi.
            Maecenas bibendum sodales nisi eu luctus.</p>
        </template>
        <VsCol
            cols="12"
            md="12"
            lg="10"
            class="offset-lg-1"
        >
            <VsContainer>
                <VsRow>
                    <VsCol
                        cols="12"
                        md="6"
                        lg="12"
                    >
                        <VsMegalinkMultiImage
                            featured
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            imgAlt="This is the alt text"
                            linkType="internal"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                The Edinburgh International Festival
                            </template>
                            <template slot="vsMultiImageContent">
                                <p>Right across the country, you’ll find amazing
                                places to eat and drink
                                from local markets to renowned restaurants.</p>
                            </template>
                        </VsMegalinkMultiImage>
                    </VsCol>
                    <VsCol
                        cols="12"
                        md="6"
                        lg="4"
                    >
                        <VsMegalinkMultiImage
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            imgAlt="This is the alt text 1"
                            linkType="external"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                Count 7,000 shining stars in the iconic galloway forest
                            </template>
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
                        lg="4"
                    >
                        <VsMegalinkMultiImage
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            imgAlt="This is the alt text 2"
                            linkType="external"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                Count 7,000 shining stars in the iconic galloway forest
                            </template>
                            <template slot="vsMultiImageContent">
                                <p>Right across the country, you’ll find amazing
                                places to eat and drink
                                from local markets to renowned restaurants.
                                Here are some recomm…</p>
                                <p>Right across the country, you’ll find amazing place
                                to eat and drink
                                from local markets to renowned restaurants. Here are
                                some recomm…</p>
                                <p>Right across the country, you’ll find amazing places
                                to eat and drink
                                from local markets to renowned restaurants. Here are
                                some recomm…</p>
                            </template>
                        </VsMegalinkMultiImage>
                    </VsCol>
                    <VsCol
                        cols="12"
                        md="6"
                        lg="4"
                    >
                        <VsMegalinkMultiImage
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            linkType="download"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                Soar through the air on a boat of Falkirk Wheel (PDF 3MB)
                            </template>
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
                    >
                        <VsMegalinkMultiImage
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            linkType="download"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                Soar through the air on a boat of Falkirk Wheel (PDF 3MB)
                            </template>
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
                    >
                        <VsMegalinkMultiImage
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            linkType="download"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                Soar through the air on a boat of Falkirk Wheel (PDF 3MB)
                            </template>
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
                        lg="12"
                    >
                        <VsMegalinkMultiImage
                            featured
                            lastFeatured
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            imgAlt="This is the alt text"
                            linkType="internal"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                The Edinburgh International Festival
                            </template>
                            <template slot="vsMultiImageContent">
                                <p>Right across the country, you’ll find amazing
                                places to eat and drink
                                from local markets to renowned restaurants.</p>
                            </template>
                        </VsMegalinkMultiImage>
                    </VsCol>
                </VsRow>
            </VsContainer>
        </VsCol>
        <template slot="vsMegalinksButton">
            Button Text
        </template>
    </VsMegalinks>

    <VsMegalinks
        title="A megalinks multi image component"
        class="vs-megalinks--multi-image"
        buttonLink="http://www.visitscotland.com"
        theme="dark"
    >
        <template slot="vsMegalinksIntro">
            <p>Sed at mauris a est dictum luctus. Nullam viverra
            pellentesque dolor, id elementum neque viverra quis.
            Morbi lacinia est id risus facilisis porttitor ut ac mi.
            Maecenas bibendum sodales nisi eu luctus.</p>
        </template>
        <VsCol
            cols="12"
            md="12"
            lg="10"
            class="offset-lg-1"
        >
            <VsContainer>
                <VsRow>
                    <VsCol
                        cols="12"
                        md="6"
                        lg="12"
                    >
                        <vs-megalink-multi-image
                            featured
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            imgAlt="This is the alt text"
                            linkType="internal"
                            theme="dark"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                The Edinburgh International Festival and summer festival
                            </template>
                            <template slot="vsMultiImageContent">
                                <p>Right across the country, you’ll find amazing
                                places to eat and drink
                                from local markets to renowned restaurants.</p>
                            </template>
                        </vs-megalink-multi-image>
                    </VsCol>
                    <VsCol
                        cols="12"
                        md="6"
                        lg="4"
                    >
                        <vs-megalink-multi-image
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            imgAlt="This is the alt text 1"
                            linkType="external"
                            theme="dark"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                Count 7,000 shining stars in the iconic galloway forest
                            </template>
                            <template slot="vsMultiImageContent">
                                <p>Right across the country, you’ll find amazing
                                places to eat and drink from local markets to renowned
                                restaurants. Here are some recomm…</p>
                            </template>
                        </vs-megalink-multi-image>
                    </VsCol>
                    <VsCol
                        cols="12"
                        md="6"
                        lg="4"
                    >
                        <vs-megalink-multi-image
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            imgAlt="This is the alt text 1"
                            linkType="external"
                            theme="dark"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                Count 7,000 shining stars in the iconic galloway forest
                            </template>
                            <template slot="vsMultiImageContent">
                                <p>Right across the country, you’ll find amazing
                                places to eat and drink from local markets to renowned
                                restaurants. Here are some recomm…</p>
                            </template>
                        </vs-megalink-multi-image>
                    </VsCol>
                    <VsCol
                        cols="12"
                        md="6"
                        lg="4"
                    >
                        <vs-megalink-multi-image
                            imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                            imgAlt="This is the alt text 1"
                            linkType="external"
                            theme="dark"
                            linkUrl="https://www.visitscotland.com"
                        >
                            <template slot="vsMultiImageHeading">
                                Count 7,000 shining stars in the iconic galloway forest
                            </template>
                            <template slot="vsMultiImageContent">
                                <p>Right across the country, you’ll find amazing
                                places to eat and drink from local markets to renowned
                                restaurants. Here are some recomm…</p>
                            </template>
                        </vs-megalink-multi-image>
                    </VsCol>
                </VsRow>
            </VsContainer>
        </VsCol>
        <template slot="vsMegalinksButton">
            Button Text
        </template>
    </VsMegalinks>

    <VsMegalinks
        title="A megalinks link list component"
        class="vs-megalinks--link-list"
        buttonLink="http://www.visitscotland.com"
        variant="link-list"
    >
        <VsCol
            cols="12"
            md="6"
        >
            <vs-megalink-link-list
                imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                imgAlt="This is the alt text"
                linkType="internal"
                linkUrl="https://www.visitscotland.com"
            >
                <template slot="vsLinkListHeading">
                    The Edinburgh International Festival and summer festival
                </template>
                <template slot="vsLinkListContent">
                    <p>Right across the country, you’ll find amazing places
                    to eat and drink from local markets to renowned
                    restaurants.</p>
                </template>
            </vs-megalink-link-list>
        </VsCol>
        <VsCol
            cols="12"
            md="6"
        >
            <vs-megalink-link-list
                imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                imgAlt="This is the alt text 1"
                linkType="external"
                linkUrl="https://www.visitscotland.com"
            >
                <template slot="vsLinkListHeading">
                    Count 7,000 shining stars in the iconic galloway forest
                </template>
                <template slot="vsLinkListContent">
                    <p>Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…</p>
                </template>
            </vs-megalink-link-list>
        </VsCol>
        <VsCol
            cols="12"
            md="6"
        >
            <vs-megalink-link-list
                imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                imgAlt="This is the alt text 2"
                linkType="external"
                linkUrl="https://www.visitscotland.com"
            >
                <template slot="vsLinkListHeading">
                    Count 7,000 shining stars in the iconic galloway forest
                </template>
                <template slot="vsLinkListContent">
                    <p>Right across the country, you’ll find amazing places
                    to eat and drink
                    from local markets to renowned restaurants.
                    Here are some recomm…</p>
                </template>
            </vs-megalink-link-list>
        </VsCol>
        <VsCol
            cols="12"
            md="6"
        >
            <vs-megalink-link-list
                imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                linkType="download"
                linkUrl="https://www.visitscotland.com"
            >
                <template slot="vsLinkListHeading">
                    Soar through the air on a boat of Falkirk Wheel (PDF 3MB)
                </template>
                <template slot="vsLinkListContent">
                    <p>Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…</p>
                </template>
            </vs-megalink-link-list>
        </VsCol>
        <template slot="vsMegalinksButton">
            Button Text
        </template>
    </VsMegalinks>

    <VsMegalinks
        theme="dark"
        title="A megalinks link list component"
        class="vs-megalinks--link-list"
        buttonLink="http://www.visitscotland.com"
        variant="link-list"
    >
        <VsCol
            cols="12"
            md="6"
        >
            <VsMegalinkLinkList
                imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                imgAlt="This is the alt text"
                linkType="internal"
                theme="dark"
                linkUrl="https://www.visitscotland.com"
            >
                <template slot="vsLinkListHeading">
                    The Edinburgh International Festival and summer festival
                </template>
                <template slot="vsLinkListContent">
                    <p>Right across the country, you’ll find amazing places
                    to eat and drink from local markets to renowned
                    restaurants.</p>
                </template>
            </VsMegalinkLinkList>
        </VsCol>
        <VsCol
            cols="12"
            md="6"
        >
            <VsMegalinkLinkList
                imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                imgAlt="This is the alt text 1"
                linkType="external"
                theme="dark"
                linkUrl="https://www.visitscotland.com"
            >
                <template slot="vsLinkListHeading">
                    Count 7,000 shining stars in the iconic galloway forest
                </template>
                <template slot="vsLinkListContent">
                    <p>Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…</p>
                </template>
            </VsMegalinkLinkList>
        </VsCol>
        <VsCol
            cols="12"
            md="6"
        >
            <VsMegalinkLinkList
                imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                imgAlt="This is the alt text 2"
                linkType="external"
                theme="dark"
                linkUrl="https://www.visitscotland.com"
            >
                <template slot="vsLinkListHeading">
                    Count 7,000 shining stars in the iconic galloway forest
                </template>
                <template slot="vsLinkListContent">
                    <p>Right across the country, you’ll find amazing places
                    to eat and drink
                    from local markets to renowned restaurants.
                    Here are some recomm…</p>
                </template>
            </VsMegalinkLinkList>
        </VsCol>
        <VsCol
            cols="12"
            md="6"
        >
            <VsMegalinkLinkList
                imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                linkType="download"
                theme="dark"
                linkUrl="https://www.visitscotland.com"
            >
                <template slot="vsLinkListHeading">
                    Soar through the air on a boat of Falkirk Wheel (PDF 3MB)
                </template>
                <template slot="vsLinkListContent">
                    <p>Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…</p>
                </template>
            </VsMegalinkLinkList>
        </VsCol>

        <template slot="vsMegalinksButton">
            Button Text
        </template>
    </VsMegalinks>

    <VsMegalinks
        title="A megalinks single image component"
        class="vs-megalinks--single-image"
        buttonLink="http://www.visitscotland.com"
        variant="single-image"
    >
        <VsCol cols="12">
            <vs-megalink-single-image
                title="The Component heading"
                buttonLink="www.visitscotland.com"
                imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
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
                        pharetra eu feugiat vitae, porttitor eget est.
                    </p>
                </template>
                <template slot="vsSingleImageLinks">
                    <li class="vs-megalink-single-image__link-list-item">
                        <VsLink
                            href="www.visitscotland.com"
                        >
                            This is a link here
                        </VsLink>
                    </li>
                    <li class="vs-megalink-single-image__link-list-item">
                        <VsLink
                            href="www.visitscotland.com"
                            type="external"
                        >
                            This is an external link here
                        </VsLink>
                    </li>
                    <li class="vs-megalink-single-image__link-list-item">
                        <VsLink
                            href="www.visitscotland.com"
                            type="download"
                        >
                            This is a download link here
                        </VsLink>
                    </li>
                </template>
                <template slot="vsSingleImageButtonText">
                    This is the button
                </template>
            </vs-megalink-single-image>
        </VsCol>
    </VsMegalinks>
    <VsMegalinks
        title="A second, dark version"
        class="vs-megalinks--single-image"
        buttonLink="http://www.visitscotland.com"
        variant="single-image"
        theme="dark"
    >
        <template slot="vsMegalinksIntro">
            <p>Sed at mauris a est dictum luctus. Nullam viverra
            pellentesque dolor, id elementum neque viverra quis.
            Morbi lacinia est id risus facilisis porttitor ut ac mi.
            Maecenas bibendum sodales nisi eu luctus.</p>
        </template>
        <VsCol cols="12">
            <VsMegalinkSingleImage
                title="This is the second component heading"
                theme="dark"
                buttonLink="www.visitscotland.com"
                imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
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
                        pharetra eu feugiat vitae, porttitor eget est.
                    </p>
                </template>
                <template slot="vsSingleImageLinks">
                    <VsLinkListItem
                        href="www.visitscotland.com"
                        variant="dark"
                    >
                        This is a link here
                    </VsLinkListItem>
                    <VsLinkListItem
                        href="www.visitscotland.com"
                        type="external"
                        variant="dark"
                    >
                        This is an external link here
                    </VsLinkListItem>
                    <VsLinkListItem
                        href="www.visitscotland.com"
                        type="download"
                        variant="dark"
                    >
                        This is a download link here
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
