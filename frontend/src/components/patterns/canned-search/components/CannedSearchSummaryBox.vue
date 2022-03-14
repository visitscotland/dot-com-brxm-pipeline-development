<template>
    <div
        class="vs-canned-search-summary-box"
        :disabled="!slideVisible() ? true : false"
    >
        <VsContainer>
            <VsRow
                v-if="!!this.$slots['vsCannedSearchSummaryTop']"
            >
                <VsCol
                    cols="12"
                    class="vs-canned-search-summary-box__summary-item"
                >
                    <!--
                        @slot Holds the content for the wider optional item in the grey
                        summary box  at the bottom of the card, usually a
                        vsCannedSearchDates

                        Expects html
                    -->
                    <slot
                        name="vsCannedSearchSummaryTop"
                    />
                </VsCol>
            </VsRow>
            <VsRow
                class="row--tall"
            >
                <VsCol
                    class="vs-canned-search-summary-box__summary-item"
                    v-if="!!this.$slots['vsCannedSearchSummaryLeft']"
                >
                    <!--
                        @slot Holds the content for the first optional item in the grey
                        summary box  at the bottom of the card, usually a
                        vsCannedSearchPrice

                        Expects html
                    -->
                    <slot
                        name="vsCannedSearchSummaryLeft"
                    />
                </VsCol>
                <VsCol
                    class="vs-canned-search-summary-box__summary-item"
                    v-if="!!this.$slots['vsCannedSearchSummaryCentre']"
                >
                    <!--
                        @slot Holds the content for the second optional item in the grey
                        summary box  at the bottom of the card, usually a duration

                        Expects html
                    -->
                    <slot
                        name="vsCannedSearchSummaryCentre"
                    />
                </VsCol>
                <VsCol
                    class="vs-canned-search-summary-box__summary-item"
                    v-if="linkHref"
                >
                    <VsLink
                        data-test="vs-canned-search-summary-box__link"
                        :href="linkHref"
                        :type="linkType.toLowerCase()"
                        :disabled="!slideVisible()"
                    >
                        {{ linkLabel }}
                    </VsLink>
                </VsCol>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import VsContainer from '@components/elements/layout/Container';
import VsRow from '@components/elements/layout/Row';
import VsCol from '@components/elements/layout/Col';
import VsLink from '@components/elements/link/Link';

/**
* Component that displays a grey summary box for product cards within canned
* search. Contains a series of summary items
*
* @displayName Canned Search Summary Box
*/

export default {
    name: 'VsCannedSearchSummaryBox',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsContainer,
        VsRow,
        VsCol,
        VsLink,
    },
    props: {
        /**
        * The href of a product link, should match the website on the
        * parent product card
        */
        linkHref: {
            type: String,
            default: '',
        },
        /**
        * The label of the product link, usually "View Details"
        */
        linkLabel: {
            type: String,
            default: '',
        },
        /**
        * The link type for the product link, usually "external"
        */
        linkType: {
            type: String,
            default: '',
        },
    },
    inject: ['slideVisible'],
};

</script>

<style lang="scss">
    .vs-canned-search-summary-box {
        padding: $spacer-2;
        background-color: $color-gray-tint-7;
        min-height: 4.5rem;

        .row {
            min-height: $spacer-9;

            &--tall {
                min-height: 4.75rem;
            }

            .vs-canned-search-summary-box__summary-item {
                display: flex;
                justify-content: center;
                align-items: center;
            }
        }
    }

    .vs-canned-search-summary-box__summary-item {
        font-size: $font-size-base;
        line-height: $line-height-s;
        text-align: center;

        &:not(:last-child) {
            border-right: 1px solid $color-gray-tint-1;
        }
    }
</style>

<docs>
```jsx
    const sampleAccom = require("../../../../assets/fixtures/canned-search/sample-accom.json");
    const sampleEvent = require("../../../../assets/fixtures/canned-search/sample-event.json");

    <div class="row">
        <div class="col-12 col-sm-6 col-md-4">
            <VsCannedSearchSummaryBox>
                <VsCannedSearchPrice
                    v-if="sampleAccom.price"
                    slot="vsCannedSearchSummaryLeft"
                    :priceIntro="sampleAccom.price.priceLabel"
                    :price="sampleAccom.price.price"
                    :priceOutro="sampleAccom.price.priceBasis"
                />
            </VsCannedSearchSummaryBox>
        </div>
        <div class="col-12 col-sm-6 col-md-4">
            <VsCannedSearchSummaryBox>
                <VsCannedSearchDates
                    v-if="sampleEvent.opening"
                    slot="vsCannedSearchSummaryTop"
                    :period="sampleEvent.opening.period"
                    :label="sampleEvent.opening.label"
                />
                <VsCannedSearchPrice
                    v-if="sampleEvent.price"
                    slot="vsCannedSearchSummaryLeft"
                    :priceIntro="sampleEvent.price.priceLabel"
                    :price="sampleEvent.price.price"
                    :priceOutro="sampleEvent.price.priceBasis"
                />
            </VsCannedSearchSummaryBox>
        </div>
        <div class="col-12 col-sm-6 col-md-4">
            <VsCannedSearchSummaryBox>
                <VsCannedSearchPrice
                    v-if="sampleAccom.price"
                    slot="vsCannedSearchSummaryLeft"
                    :priceIntro="sampleAccom.price.priceLabel"
                    :price="sampleAccom.price.price"
                    priceOutro="per Adult"
                />
                <VsCannedSearchDuration
                    slot="vsCannedSearchSummaryCentre"
                    durationIntro="Length"
                    duration="2 hours"
                />
            </VsCannedSearchSummaryBox>
        </div>
    </div>
```
</docs>
