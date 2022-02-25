<template>
    <li
        class="vs-listicle-item border"
        data-test="vs-listicle-item"
    >
        <slot name="hippo-details" />

        <!-- HEADER -->
        <div class="d-flex justify-content-start align-items-top border-bottom border-white ">
            <div class="position-relative">
                <div class="count__bg">
                    <span
                        class="count"
                        aria-hidden="true"
                    >{{ index }}</span>
                </div>
            </div>
            <VsHeading level="3">
                {{ title }}

                <template slot="sub-heading">
                    {{ subTitle }}
                </template>
            </VsHeading>
        </div>

        <!-- BODY -->
        <!-- @slot Contains the image to be shown.  -->
        <slot name="image-slot" />

        <VsRow>
            <VsCol
                cols="12"
                lg="8"
                class="mt-2 mt-sm-9 mb-4 mt-lg-2 pr-lg-9"
            >
                <!-- @slot Contains the description to be shown.  -->
                <slot name="description-slot" />
            </VsCol>
            <VsCol
                cols="12"
                lg="4"
                class="key-facilities-list mt-lg-10"
                :class="[hasKeyFacilitiesSlot ? 'has-facilities' : '']"
            >
                <!-- @slot Contains the facilities list to be shown.  -->
                <slot name="facilities-slot" />
            </VsCol>
        </VsRow>
    </li>
</template>

<script>
import VsHeading from '@components/elements/heading/Heading';
import { VsRow, VsCol } from '@components/elements/layout';

/**
 * TODO: Document usage
 *
 * @displayName Listicle Item
 */
export default {
    name: 'VsListicleItem',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsHeading,
        VsRow,
        VsCol,
    },
    props: {
        /**
         * The index value for the listicle item to be shown on the header
         */
        index: {
            type: String,
            default: '',
        },
        /**
         * The listicle item title
         */
        title: {
            type: String,
            default: '',
        },
        /**
         * The listicle item subtitle
         */
        subTitle: {
            type: String,
            default: '',
        },
    },
    computed: {
        hasKeyFacilitiesSlot() {
            return !!this.$slots['facilities-slot'];
        },
    },
};
</script>

<style lang="scss">
.vs-listicle-item {
    margin-bottom: $spacer-9;

    @include media-breakpoint-up(sm) {
        margin-bottom: $spacer-11;
    }

    .count {
        color: $color-white;
        font-family: $headings-font-family;
        font-size: $display1-size;
        line-height: $line-height-xs;
        display: block;
        text-align: center;
        width: 100%;

        &:after {
            content: "";
            border-bottom: 1px solid $color-white;
            display: block;
            margin: $spacer-1 $spacer-6 0;
            margin-top: $spacer-1;
        }
    }

    .count__bg {
        background: $color_secondary_teal;
        height: 67px;
        width: 67px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    &.border {
        padding: $spacer-4;

        @include media-breakpoint-up(md) {
            padding: $spacer-8;
        }

        @include media-breakpoint-up(lg) {
            padding: $spacer-8;
        }

        @include media-breakpoint-up(xl) {
            padding: $spacer-11;
        }

        @include media-breakpoint-up(xxl) {
            padding: $spacer-12;
        }
    }

    h3.vs-heading {
        display: flex;
        flex-direction: column;
        justify-content: center;
        margin: 0 0 $spacer-5 $spacer-3;

        @include media-breakpoint-up(md) {
            margin-bottom: 0;
        }
    }

    .vs-caption-image-map {
        height: $spacer-11;
    }

    .key-facilities-list {
        &.has-facilities {
            border-top: 1px solid $color-gray-tint-5;
            padding-top: $spacer-4;

            @include media-breakpoint-up(sm) {
                border-top: 0;
                padding-top: 0;
            }

            @include media-breakpoint-up(lg) {
                border-left: 1px solid $color-gray-tint-5;
            }

            .vs-icon-list {
                .vs-icon-list__item {
                    width: 80px;
                }

                @include media-breakpoint-up(sm) {
                    border-top: 1px solid $color-gray-tint-5;
                    padding-top: $spacer-4;

                    .vs-icon-list__item {
                        width: 90px;
                    }
                }

                @include media-breakpoint-up(lg) {
                    border-top: 0;
                    padding: 0 $spacer-2;

                    .vs-icon-list__item {
                        width: 80px;
                    }
                }
                @include media-breakpoint-up(xl) {
                    padding: 0 $spacer-4;
                }

                @include media-breakpoint-up(xxl) {
                    padding: 0 $spacer-9;
                }
            }
        }
    }
}
</style>

<docs>
```jsx

    <ul style="list-style-type: none; padding: 0;">
        <VsListicleItem
            v-for="(item, index) in listicles.sampleListicle"
            key="index"
            index="1"
            :title="item.title"
            :subTitle="item.subTitle"
            ctaLink=""
            :ctaLabel="item.ctaLabel"
        >
            <div slot="image-slot">
                <VsImageWithCaption
                    :altText="item.image.altText"
                    :image-src="item.image.imageSrc"
                >
                    <VsCaption
                        slot="img-caption"
                        :latitude="item.image.latitude"
                        :longitude="item.image.longitude"
                        variant="large"
                    >
                        <span slot="caption">
                            {{ item.image.caption }}
                        </span>

                        <span slot="credit">
                            &copy; {{ item.image.credit }}
                        </span>
                    </VsCaption>
                </VsImageWithCaption>
            </div>

            <div slot="description-slot">
                <p data-v-0abaabb3="">The&nbsp;<strong data-v-0abaabb3="">National Museum of Scotland</strong>&nbsp;in&nbsp;<a data-v-0abaabb3="" href="https://en.wikipedia.org/wiki/Edinburgh" title="Edinburgh" target="_blank">Edinburgh</a>, Scotland, was formed in 2006 with the merger of the new&nbsp;<strong data-v-0abaabb3="">Museum of Scotland</strong>, with collections relating to Scottish&nbsp;<a data-v-0abaabb3="" href="https://en.wikipedia.org/wiki/Antiquities" title="Antiquities" target="_blank">antiquities</a>,&nbsp;<a data-v-0abaabb3="" href="https://en.wikipedia.org/wiki/Culture_of_Scotland" title="Culture of Scotland" target="_blank">culture</a>&nbsp;and&nbsp;<a data-v-0abaabb3="" href="https://en.wikipedia.org/wiki/History_of_Scotland" title="History of Scotland" target="_blank">history</a>, and the adjacent&nbsp;<strong data-v-0abaabb3="">Royal Scottish Museum</strong>&nbsp;(so renamed in 1904), with collections covering science and technology,&nbsp;<a data-v-0abaabb3="" href="https://en.wikipedia.org/wiki/Natural_history" title="Natural history" target="_blank">natural history</a>, and world cultures. The two connected buildings stand beside each other on&nbsp;<a data-v-0abaabb3="" href="https://en.wikipedia.org/wiki/Chambers_Street_(Edinburgh)" title="Chambers Street (Edinburgh)" target="_blank">Chambers Street</a>, by the intersection with the&nbsp;<a data-v-0abaabb3="" href="https://en.wikipedia.org/wiki/George_IV_Bridge" title="George IV Bridge" target="_blank">George IV Bridge</a>, in central Edinburgh. The museum is part of&nbsp;<a data-v-0abaabb3="" href="https://en.wikipedia.org/wiki/National_Museums_Scotland" title="National Museums Scotland" target="_blank">National Museums Scotland</a>. Admission is free.</p>
            </div>

            <div slot="facilities-slot">
                <VsIconList title="Key Facilities">
                    <VsIconListItem
                        icon="facility-petswelcom"
                        label="Pets Welcome">
                    </VsIconListItem>
                    <VsIconListItem
                        icon="facility-dsblaccess"
                        label="Wheelchair Access">
                    </VsIconListItem>
                    <VsIconListItem
                        icon="facility-audioloop"
                        label="Hearing Loop">
                    </VsIconListItem>
                    <VsIconListItem
                        icon="facility-wifi"
                        label="WiFi">
                    </VsIconListItem>
                    <VsIconListItem
                        icon="publictlt"
                        label="Public Toilets">
                    </VsIconListItem>
                    <VsIconListItem
                        icon="giftshop"
                        label="Gift Shop">
                    </VsIconListItem>
                    <VsIconListItem
                        icon="facility-accessparkdrop"
                        label="Accessible Parking or Drop-off Point">
                    </VsIconListItem>
                </VsIconList>
            </div>
        </VsListicleItem>
    </ul>
```
</docs>
