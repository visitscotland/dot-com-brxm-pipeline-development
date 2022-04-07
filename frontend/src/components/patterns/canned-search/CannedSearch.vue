<template>
    <VsModuleWrapper
        v-show="products.length"
    >
        <template
            slot="vsModuleWrapperHeading"
            v-if="heading"
        >
            {{ heading }}
        </template>
        <template
            slot="vsModuleWrapperIntro"
            v-if="!!this.$slots['vsCannedSearchIntro']"
        >
            <!--
                @slot Holds optional introduction copy
                Expects html
            -->
            <slot name="vsCannedSearchIntro" />
        </template>
        <div
            class="vs-canned-search"
            data-test="vs-canned-search"
        >
            <VsContainer>
                <VsRow>
                    <VsCol
                        cols="10"
                        offset="1"
                        md="8"
                        offset-md="2"
                        lg="6"
                        offset-lg="3"
                        class="vs-canned-search__buttons"
                    >
                        <!-- @slot Holds one or more navigation buttons  -->
                        <slot
                            name="vsCannedSearchButtons"
                        />
                    </VsCol>
                </VsRow>
            </VsContainer>
            <VsCarousel
                :next-text="carouselNextText"
                :prev-text="carouselPreviousText"
                slides-xs="1"
                slides-md="2"
                slides-lg="3"
                v-if="products.length"
            >
                <VsCannedSearchProductCard
                    v-for="(prod, index) in products"
                    :key="index"
                    :slide-index="'' + index"
                    :img-src="prod.images ? prod.images[0].mediaUrl : ''"
                    :img-alt="prod.name"
                    :title="prod.name"
                    :detail-link="{
                        link: prod.productLink.link,
                        label: prod.productLink.label,
                        type: prod.productLink.type.toLowerCase()
                    }"
                    :description="prod.description"
                    :search-type="searchType"
                >
                    <div
                        v-if="searchType === 'tour'"
                        slot="vsCannedSearchTourInfo"
                    >
                        <div>
                            <VsCannedSearchTourRuns
                                v-if="prod.opening && prod.opening.period
                                    && prod.opening.period.startDay"
                                :label="prod.opening.period.label"
                                :start-day="prod.opening.period.startDay"
                                :end-day="prod.opening.period.endDay"
                            />
                            <VsCannedSearchTourDeparts
                                v-if="prod.tourOrigin && prod.tourOrigin.tourOrigin"
                                :label="prod.tourOrigin.label"
                                :origins="prod.tourOrigin.tourOrigin"
                            />
                        </div>
                    </div>
                    <VsCannedSearchSubHeading
                        slot="vsCannedSearchSubHeading"
                        :sub-heading="fetchSubHeading(prod)"
                        :line-limit="searchType === 'tour' ? 1 : 2"
                    />
                    <VsCannedSearchStars
                        v-if="prod.grading"
                        slot="vsCannedSearchStarRating"
                        :min="prod.grading.minStars"
                        :max="prod.grading.maxStars"
                        :gold="prod.grading.gold"
                    />
                    <VsCannedSearchCategories
                        slot="vsCannedSearchCategories"
                        v-if="prod.locations"
                        :categories="prod.locations"
                    />
                    <VsCannedSearchLogos
                        slot="vsCannedSearchLogos"
                        v-if="showLogos"
                        :good-to-go-logo="prod.covidInformation && prod.covidInformation.goodToGo ?
                            prod.covidInformation.goodToGo : null"
                        :safe-travels-logo="prod.covidInformation &&
                            prod.covidInformation.safeTravels ?
                                prod.covidInformation.safeTravels :
                                null"
                        :access-guide="prod.accessGuide || null"
                        :awards="prod.awards"
                    />
                    <VsCannedSearchBadges
                        slot="vsCannedSearchBadges"
                        :badge-one="fetchBadgeOne(prod)"
                        :multi-badge-one="fetchMultiBadgeOne(prod)"
                        :badge-two="prod.offers"
                        :badge-three="fetchBadgeThree(prod)"
                    />
                    <VsCannedSearchSummaryBox
                        slot="vsCannedSearchSummary"
                        :link-href="prod.website.link"
                        :link-type="prod.website.type"
                        :link-label="prod.website.label"
                    >
                        <VsCannedSearchDates
                            v-if="prod.opening && searchType !== 'tour'"
                            slot="vsCannedSearchSummaryTop"
                            :period="prod.opening.period"
                            :label="prod.opening.period.label"
                        />
                        <VsCannedSearchDuration
                            v-if="searchType === 'tour' && prod.tourLength"
                            :slot="'vsCannedSearchSummaryLeft'"
                            :duration-intro="prod.tourLength.label"
                            :duration="prod.tourLength.value"
                        />
                        <VsCannedSearchPrice
                            v-if="prod.price"
                            :slot="searchType === 'tour' ? 'vsCannedSearchSummaryCentre'
                                : 'vsCannedSearchSummaryLeft'"
                            :price-intro="prod.price.priceLabel"
                            :price="prod.price.price"
                            :price-outro="prod.price.priceBasis"
                        />
                        <VsCannedSearchCuisines
                            v-if="prod.cuisines"
                            slot="vsCannedSearchSummaryLeft"
                            :cuisines="prod.cuisines"
                        />
                    </VsCannedSearchSummaryBox>
                </VsCannedSearchProductCard>
                <template slot="vsCarouselOf">
                    <!--
                        @slot Holds the translation for `of` and passes it to the carousel

                        Expects html
                    -->
                    <slot
                        name="vsCannedSearchOf"
                    />
                </template>
            </VsCarousel>
            <VsContainer
                v-if="!!this.$slots['vsCannedSearchCredit']"
                class="vs-canned-search__credit-container"
            >
                <!--
                    @slot Holds credit info for search data from third parties

                    Expects html
                -->
                <slot
                    name="vsCannedSearchCredit"
                />
            </VsContainer>
        </div>
    </VsModuleWrapper>
</template>

<script>
import VsCannedSearchProductCard from '@components/patterns/canned-search/components/CannedSearchProductCard';
import VsCannedSearchStars from '@components/patterns/canned-search/components/CannedSearchStars';
import VsCannedSearchLogos from '@components/patterns/canned-search/components/CannedSearchLogos';
import VsCannedSearchCategories from '@components/patterns/canned-search/components/CannedSearchCategories';
import VsCannedSearchPrice from '@components/patterns/canned-search/components/CannedSearchPrice';
import VsCannedSearchDuration from '@components/patterns/canned-search/components/CannedSearchDuration';
import VsCannedSearchSummaryBox from '@components/patterns/canned-search/components/CannedSearchSummaryBox';
import VsCannedSearchDates from '@components/patterns/canned-search/components/CannedSearchDates';
import VsCannedSearchBadges from '@components/patterns/canned-search/components/CannedSearchBadges';
import VsCannedSearchCuisines from '@components/patterns/canned-search/components/CannedSearchCuisines';
import VsCannedSearchSubHeading from '@components/patterns/canned-search/components/CannedSearchSubHeading';
import VsCannedSearchTourRuns from '@components/patterns/canned-search/components/CannedSearchTourRuns';
import VsCannedSearchTourDeparts from '@components/patterns/canned-search/components/CannedSearchTourDeparts';
import VsCarousel from '@components/patterns/carousel/Carousel';
import VsModuleWrapper from '@components/patterns/module-wrapper/ModuleWrapper';
import {
    VsContainer, VsRow, VsCol,
} from '@components/elements/grid';

const axios = require('axios');

/**
* Wrapper for canned search, invokes api call to provided endpoint and
* creates cards for products
*
* @displayName Canned Search
*/

export default {
    name: 'VsCannedSearch',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsCannedSearchProductCard,
        VsCannedSearchStars,
        VsCannedSearchLogos,
        VsCannedSearchCategories,
        VsCannedSearchPrice,
        VsCannedSearchDuration,
        VsCannedSearchSummaryBox,
        VsCannedSearchDates,
        VsCannedSearchBadges,
        VsCannedSearchCuisines,
        VsCannedSearchSubHeading,
        VsCannedSearchTourRuns,
        VsCannedSearchTourDeparts,
        VsCarousel,
        VsContainer,
        VsRow,
        VsCol,
        VsModuleWrapper,
    },
    props: {
        /**
        * The url that products should be retrieved from for display
        */
        apiUrl: {
            type: String,
            default: '',
        },
        /**
        * The type of product that is being search for, determines how product
        * card addresses are displayed.
        *
        * Any arbitrary search type could be added in the future and should just
        * work without any specific handling, but at time of development this
        * could be:
        *
        * `even` - events
        * `acco` - accomodation
        * `cate` - food & drink
        * `acti` - things to do
        * `tour` - tours
        */
        searchType: {
            type: String,
            default: '',
        },
        /**
        * Optional header that appears above the canned search carousel, rendered as an h2
        */
        heading: {
            type: String,
            default: '',
        },
        /**
        * Accessible text for next carousel control, passed to vs-carousel
        */
        carouselNextText: {
            type: String,
            default: 'Next slide',
        },
        /**
        * Accessible text for next carousel control, passed to vs-carousel
        */
        carouselPreviousText: {
            type: String,
            default: 'Previous slide',
        },
    },
    data() {
        return {
            products: [],
        };
    },
    computed: {
        showLogos() {
            for (let x = 0; x < this.products.length; x++) {
                const product = this.products[x];

                if (
                    (product.awards && product.awards.length)
                    || product.accessGuide
                    || (product.covidInformation && product.covidInformation.goodToGo)
                    || (product.covidInformation && product.covidInformation.safeTravels)
                ) {
                    return true;
                }
            }

            return false;
        },
    },
    mounted() {
        if (this.apiUrl) {
            this.retrieveProducts();
        }
    },
    methods: {
        /**
         * Invoked when mounted, retrieves products from the dms and sets them
         * to the component
         */
        retrieveProducts() {
            axios.get(this.apiUrl)
                .then((response) => {
                    this.products = response.data.data.products;
                })
                .catch(() => {
                    this.products = [];
                });
        },
        /**
         * Returns an appropriate subheading for the product card based on the search type,
         * tours display a comma separated list of categories, non-tours display a formatted
         * address
         */
        fetchSubHeading(product) {
            if (this.searchType === 'tour') {
                return this.fetchCategoryStrings(product);
            }

            return this.fetchAddress(product);
        },
        /**
         * Returns a comma separated list of all the categories on the product
         */
        fetchCategoryStrings(product) {
            if (product.category && product.category.length) {
                return product.category.map((item) => item.name).join(', ');
            }

            return '';
        },
        /**
         * Returns the address string for each card, dependent on whether the event is
         * online and what type of product the search is looking for
         *
         * Defaults to `city, country` if no exception present
         */
        fetchAddress(product) {
            if (product.onlineEvent) {
                return product.onlineEvent;
            }

            if (!product.address) {
                return '';
            }

            if (this.searchType === 'even') {
                return `${product.eventVenue}, ${product.address.city}`;
            }

            return `${product.address.city}, ${product.address.county}`;
        },
        /**
         * Returns the elements to display in the first badge, usually a category
         */
        fetchBadgeOne(product) {
            if (this.searchType !== 'tour') {
                if (product.category && product.category.length) {
                    if (product.category[0]) {
                        return product.category[0].name;
                    }

                    return null;
                }
            }

            return null;
        },
        /**
         * Returns the elements to display in the first badge section if there are multiple
         * of them, usually occurs for tours and lists modes of transport
         */
        fetchMultiBadgeOne(product) {
            if (this.searchType === 'tour') {
                if (product.tourVehicles && product.tourVehicles.length) {
                    return product.tourVehicles.map((item) => item.name);
                }
            }

            return null;
        },
        /**
         * Returns the elements to display in the first badge if covid opening information is
         * provided return that, otherwise if a nowOn status for an event is provided return that
         */
        fetchBadgeThree(product) {
            if (product.covidInformation && product.covidInformation.weAreOpen) {
                return product.covidInformation.weAreOpen;
            }

            if (product.opening && product.opening.nowOn) {
                return product.opening.nowOn;
            }

            return '';
        },
    },
};

</script>

<style lang="scss">
    .vs-canned-search {
        .vs-carousel > .container > .row > div {
            @include media-breakpoint-down(sm) {
                margin-left: 0;
                max-width: 100%;
                flex: 0 0 100%;
            }
        }
    }

    .vs-canned-search__buttons {
        margin-bottom: $spacer-9;

        .vs-button {
            margin: $spacer-0 $spacer-2 $spacer-2;
        }
    }

    .vs-canned-search__credit-container {
        text-align: right;
        font-size: $font-size-2;
    }
</style>

<docs>
```jsx

    <VsCannedSearch
        apiUrl="http://172.28.81.65:8089/data/component/cannedsearch?prodtypes=acco&avail=off&locplace=4751&locprox=10.0&loc=Glasgow&fac_id=accessguide"
        heading="B&Bs, guesthouses and hostels in Loch Lomond and The Trossachs national park"
    >
        <template slot="vsCannedSearchIntro">
            <p>Find your perfect place to stay</p>
        </template>
        <template slot="vsCannedSearchButtons">
            <VsButton
                href="https://www.visitscotland.com"
            >
                View All B&Bs
            </VsButton>
        </template>

        <template slot="vsCannedSearchOf">
            Of
        </template>
    </VsCannedSearch>
    <VsCannedSearch
        apiUrl="http://172.28.81.65:8089/data/component/cannedsearch?prodtypes=even&locplace=&locprox=10.0&loc=Scotland"
        searchType="even"
        heading="An events search example"
    >
        <template slot="vsCannedSearchButtons">
            <VsButton
                href="https://www.visitscotland.com"
            >
                View All Events
            </VsButton>
        </template>

        <template slot="vsCannedSearchCredit">
            These are some credits for a third party search
        </template>

        <template slot="vsCannedSearchOf">
            Of
        </template>
    </VsCannedSearch>
    <VsCannedSearch
        apiUrl="http://172.28.81.65:8089/data/component/cannedsearch?prodtypes=cate&locpoly=821&locprox=10.0&loc=Royal+Mile"
        searchType="cate"
        heading="A food & drink search example"
    >
        <template slot="vsCannedSearchButtons">
            <VsButton
                href="https://www.visitscotland.com"
            >
                View All Food & Drink
            </VsButton>
        </template>

        <template slot="vsCannedSearchOf">
            Of
        </template>
    </VsCannedSearch>
    <VsCannedSearch
        apiUrl="http://172.28.81.65:8089/data/component/cannedsearch?prodtypes=acti%2Cattr%2Creta&locplace=4751&locprox=10.0&loc=Glasgow"
        searchType="acti"
        heading="A things to do example"
    >
        <template slot="vsCannedSearchButtons">
            <VsButton
                href="https://www.visitscotland.com"
            >
                View All
            </VsButton>
        </template>

        <template slot="vsCannedSearchOf">
            Of
        </template>
    </VsCannedSearch>
    <VsCannedSearch
        apiUrl="http://172.28.81.65:8089/data/component/cannedsearchtours?find%5B%5D=attractions%7Caberdeen%7CAberdeen&locale=en-GB"
        searchType="tour"
        heading="A tours example"
    >
        <template slot="vsCannedSearchButtons">
            <VsButton
                href="https://www.visitscotland.com"
            >
                View All
            </VsButton>
        </template>

        <template slot="vsCannedSearchOf">
            Of
        </template>
    </VsCannedSearch>
```
</docs>
