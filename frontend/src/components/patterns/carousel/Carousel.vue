<template>
    <section
        class="vs-carousel"
        data-test="vs-carousel"
        ref="carousel"
    >
        <VsContainer>
            <VsRow>
                <VsCol
                    cols="10"
                    offset="1"
                    sm="12"
                    offset-sm="0"
                >
                    <div class="slider">
                        <VsRow
                            class="vs-carousel__track"
                            :style="{ 'transform': `translateX(${trackOffset})` }"
                        >
                            <!-- @slot default slot to contain slides -->
                            <slot />
                        </VsRow>
                        <button
                            v-if="!prevDisabled"
                            class="vs-carousel__control vs-carousel__control--prev"
                            @click.prevent="sliderNavigate('prev')"
                            @keypress.prevent="sliderNavigate('prev')"
                        >
                            <VsIcon
                                name="internal-link"
                                size="xs"
                                orientation="down"
                                variant="light"
                            />
                            <div class="vs-carousel__control-label-container">
                                <span
                                    class="vs-carousel__control-label
                                           vs-carousel__control-label--prev"
                                >
                                    {{ prevText }}
                                </span>
                            </div>
                        </button>
                        <button
                            v-if="!nextDisabled"
                            class="vs-carousel__control vs-carousel__control--next"
                            @keypress.prevent="sliderNavigate('next')"
                            @click.prevent="sliderNavigate('next')"
                        >
                            <div class="vs-carousel__control-label-container">
                                <span
                                    class="vs-carousel__control-label
                                           vs-carousel__control-label--next"
                                >
                                    {{ nextText }}
                                </span>
                            </div>
                            <VsIcon
                                name="internal-link"
                                size="xs"
                                variant="light"
                            />
                            <span class="sr-only">{{ nextText }}</span>
                        </button>

                        <ul
                            v-if="totalSlides > slidesPerPage[currentWidth]"
                            class="vs-carousel__navigation"
                        >
                            <li
                                v-for="index in maxPages"
                                :key="index"
                            >
                                <button
                                    class="vs-carousel__navigation-item"
                                    :class="index === currentPage + 1 ?
                                        'vs-carousel__navigation-item--active' : ''"
                                    @click.prevent="sliderNavigate(index - 1)"
                                    @keypress.prevent="sliderNavigate(index - 1)"
                                    tabindex="0"
                                    :data-test="`vs-carousel__nav-${index}`"
                                >
                                    <span class="sr-only">
                                        <!-- @slot 'navigate to page' text to allow translation -->
                                        <slot name="vsCarouselNavigate" /> {{ index }}
                                    </span>
                                </button>
                            </li>
                        </ul>
                    </div>

                    <div
                        v-if="totalSlides > slidesPerPage[currentWidth]"
                        class="vs-carousel__mobile-pagination-wrapper"
                    >
                        <p
                            class="vs-carousel__mobile-pagination"
                            data-test="vs-carousel__mobile-pagination"
                        >
                            {{ currentPage + 1 }}
                            <!-- @slot 'of' text to allow translation -->
                            <slot name="vsCarouselOf" />
                            {{ totalSlides }}
                        </p>
                    </div>
                </VsCol>
            </VsRow>
        </VsContainer>
    </section>
</template>

<script>
import VsIcon from '@components/elements/icon';
import {
    VsContainer,
    VsRow,
    VsCol,
} from '@components/elements/layout';

/**
* Multi purpose carousel component to use
* for carousel and slider content
*
* @displayName Carousel
*/

export default {
    name: 'VsCarousel',
    status: 'protolink-type',
    release: '0.0.1',
    components: {
        VsContainer,
        VsRow,
        VsCol,
        VsIcon,
    },
    props: {
        /**
        * Accessible text for previous
        * carousel control
        */
        prevText: {
            type: String,
            default: 'Previous slide',
        },
        /**
        * Accessible text for next
        * carousel control
        */
        nextText: {
            type: String,
            default: 'Next slide',
        },
        /**
        *  Number of slides to appear
        *  at 'xs' screen size
        */
        slidesXs: {
            type: String,
            default: '1',
        },
        /**
        *  Number of slides to appear
        *  at 'sm' screen size
        */
        slidesSm: {
            type: String,
            default: '2',
        },
        /**
        *  Number of slides to appear
        *  at 'md' screen size
        */
        slidesMd: {
            type: String,
            default: '3',
        },
        /**
        *  Number of slides to appear
        *  at 'lg' screen size
        */
        slidesLg: {
            type: String,
            default: '4',
        },
    },
    data() {
        return {
            totalSlides: null,
            currentPage: 0,
            maxPages: 1,
            nextDisabled: false,
            prevDisabled: true,
            currentWidth: 'lg',
            activeSlides: [],
            remainderOffset: 0,
        };
    },
    computed: {
        slidesPerPage() {
            const slidesPerPage = {

            };
            slidesPerPage.lg = this.slidesLg;
            slidesPerPage.md = this.slidesMd;
            slidesPerPage.sm = this.slidesSm;
            slidesPerPage.xs = this.slidesXs;

            return slidesPerPage;
        },
        trackOffset() {
            // how much the carousel track should be offset by
            // to show the current active slides
            if (this.remainderOffset) {
                return `-${((this.currentPage - 1) * 100) + this.remainderOffset}%`;
            }

            return `-${((this.currentPage) * 100)}%`;
        },
    },
    provide() {
        const slideCols = {
        };

        const visibleSlides = this.activeSlides;

        Object.keys(this.slidesPerPage).forEach((key) => {
            const colSpan = 12 / this.slidesPerPage[key];
            slideCols[key] = colSpan;
        });

        return {
            slideCols,
            visibleSlides,
        };
    },
    mounted() {
        window.addEventListener('resize', () => {
            this.setActivePage();

            this.sliderNavigate(this.currentPage);
        });
        this.defineActiveSlides();
        this.initNavigation();
    },
    methods: {
        defineActiveSlides(remainder) {
            this.calcViewport();
            this.activeSlides.length = 0;
            const allSlides = this.$refs.carousel
                .getElementsByClassName('vs-carousel-slide__card');
            let slideCount = 0;

            // iterate through child slides and evaluate if they're active
            // if they are, add to the activeSlides array
            if (allSlides.length > 0) {
                allSlides.forEach((slide, index) => {
                    const activeSlideStart = this.currentPage
                        * this.slidesPerPage[this.currentWidth];
                    const activeSlideEnd = parseInt(
                        this.currentPage * this.slidesPerPage[this.currentWidth], 10
                    ) + parseInt(this.slidesPerPage[this.currentWidth], 10);

                    // if we're at a final slide that has a remainder
                    // the last slides that fit on a page are active
                    if (remainder && typeof remainder !== 'undefined') {
                        if (index >= this.totalSlides - this.slidesPerPage[this.currentWidth]) {
                            this.activeSlides.push(index);
                        }
                    } else if (index >= activeSlideStart && index < activeSlideEnd) {
                        this.activeSlides.push(index);
                    }

                    slideCount += 1;
                });
            }

            // update total slide number for mobile navigation
            this.totalSlides = slideCount;

            // calculate total number of pages in carousel
            this.maxPages = Math.ceil(slideCount / this.slidesPerPage[this.currentWidth]);
        },
        calcViewport() {
            if (window.matchMedia('(min-width: 1200px)').matches) {
                this.currentWidth = 'lg';
            } else if (window.matchMedia('(min-width: 992px)').matches) {
                this.currentWidth = 'md';
            } else if (window.matchMedia('(min-width: 576px)').matches) {
                this.currentWidth = 'sm';
            } else {
                this.currentWidth = 'xs';
            }
        },
        setActivePage() {
            // method to calculate active page after resize
            // and ensure user sees same slides
            let firstActiveItem;

            // calculate first active item for reference when resizing
            if (this.currentWidth === 'xs' && this.slidesXs === '1') {
                firstActiveItem = this.currentPage + 1;
            } else {
                firstActiveItem = (this.currentPage * this.slidesPerPage[this.currentWidth]) + 1;
            }

            // work out which viewport we're currently on
            this.calcViewport();

            // calculate which page we're on in the new viewport
            // based on the first active item
            let newFirstPage;
            if (this.currentWidth === 'xs' && this.slidesXs === '1') {
                newFirstPage = firstActiveItem;
            } else {
                newFirstPage = Math.ceil(
                    firstActiveItem / this.slidesPerPage[this.currentWidth]
                );
            }

            this.currentPage = newFirstPage - 1;

            // navigate to the correct page to keep the user at
            // the same position they were at before the resize
            this.sliderNavigate(this.currentPage);
        },
        sliderNavigate(direction) {
            if (direction === 'next') {
                this.currentPage += 1;
            } else if (direction === 'prev') {
                this.currentPage -= 1;
            } else {
                this.currentPage = direction;
            }

            this.initNavigation();

            // calculate if the slides don't neatly fit into the pages
            // this will highlight if there's an 'odd' number of slides
            // on the final page of the carousel
            const totalSlideSpaces = this.slidesPerPage[this.currentWidth] * (this.currentPage + 1);
            let finalSlideRemainder = false;

            if (totalSlideSpaces > this.totalSlides) {
                this.remainderOffset = this.checkRemainder(totalSlideSpaces);
                finalSlideRemainder = true;
            } else {
                this.remainderOffset = 0;
            }

            this.defineActiveSlides(finalSlideRemainder);
        },
        initNavigation() {
            // method to enable/disable arrow controls for carousel
            // depending on current position
            if (this.currentPage + 1
                >= this.totalSlides / this.slidesPerPage[this.currentWidth]) {
                this.nextDisabled = true;
            } else {
                this.nextDisabled = false;
            }

            if (this.currentPage > 0) {
                this.prevDisabled = false;
            } else {
                this.prevDisabled = true;
            }
        },
        checkRemainder(totalSpaces) {
            // this method checks if there's an 'odd' number of slides
            // meaning the final navigation shouldn't move a full page width
            const slideRemainder = this.slidesPerPage[this.currentWidth]
                - (totalSpaces - this.totalSlides);
            const singleSlideOffset = 100 / this.slidesPerPage[this.currentWidth];
            const remainderOffset = singleSlideOffset * slideRemainder;

            return remainderOffset;
        },
    },
};
</script>

<style lang="scss">
    .vs-carousel {
        overflow: hidden;

        &__track {
            flex-wrap: nowrap;
            transition: transform $duration-slowly ease-out;
            margin-bottom: $spacer-9;

            & > [class^="col-"] {
                display: flex;
            }
        }

        &__control {
            position: absolute;
            top: 25%;
            border: none;
            background: $color-theme-primary;
            z-index: 20;
            min-width: 35px;
            height: 35px;
            border-radius: 0;
            display: flex;
            align-items: center;
            justify-content: center;

            &--next {
                right: 0;

                &:focus {
                    right: .3125rem;
                }
            }

            &--prev {
                left: 0;

                &:focus {
                    left: .3125rem;
                }
            }

            &-label {
                color: $color-white;
                white-space: nowrap;
                text-transform: uppercase;
                font-weight: $font-weight-light;

                &--next {
                    padding-right: $spacer-4;
                }

                &--prev {
                    padding-left: $spacer-4;
                }
            }

            .vs-carousel__control-label-container {
                transition: max-width $duration-slowly ease;
                max-width: 0;
                overflow: hidden;
            }

            &:focus {
                box-shadow: $shadow-button-focus;
                outline: none;
                background-color: $color-white;
                border: 1px solid $color-theme-primary;

                .vs-carousel__control-label-container {
                    max-width: 15.5rem;
                }

                .vs-carousel__control-label {
                    color: $color-theme-primary;
                }

                .vs-icon {
                    fill: $color-theme-primary;
                }
            }

            &:hover {
                outline: none;

                .vs-carousel__control-label-container {
                    max-width: 15rem;
                }
            }
        }

        &__navigation {
            display: none;
            justify-content: center;
            list-style: none;

            @include media-breakpoint-up(sm) {
                display: flex;
            }

            li {
                display: flex;
            }
        }

        &__navigation-item {
            display: inline-block;
            width: 10px;
            height: 10px;
            border-radius: 5px;
            background: $color-gray-tint-4;
            transform: translateY(2px);
            margin: 0 2px;
            border: none;
            box-shadow: none;
            cursor: pointer;
            padding: 0;

            &:hover {
                background: $color-pink-tint-5;
            }

            &:focus {
                outline: 1px solid $color-purple;
            }

            &--active {
                width: 14px;
                height: 14px;
                border-radius: 7px;
                background: $color-black;
                transform: none;
                cursor: default;

                &:hover {
                    background: $color-black;
                }
            }

            @media (hover: none) {
                &:hover {
                    background: $color-gray-tint-4;
                }

                &--active {
                    &:hover {
                        background: $color-black;
                    }
                }
            }
        }

        &__mobile-pagination-wrapper {
            display: flex;
            justify-content: center;
            margin-top: $spacer-8;

            @include media-breakpoint-up(sm) {
                display: none;
            }
        }

        &__mobile-pagination {
            background: $color-gray-tint-7;
            padding: $spacer-1 $spacer-2;
            color: $color-black;
            font-size: $font-size-sm;
            font-weight: bold;
            margin: 0;
        }
    }

    @include no-js {
        .vs-carousel {
            &__control {
                display: none;
            }

            &__navigation,
            &__mobile-pagination-wrapper {
                display: none;
            }

            &__track {
                flex-wrap: wrap;

                & > [class^="col-"] {
                    width: 100%;
                    max-width: 100%;
                    flex: 0 0 100%;

                    @include media-breakpoint-up(md) {
                        width: 50%;
                        max-width: 50%;
                        flex: 0 0 50%;
                    }
                }
            }
        }
    }
</style>

<docs>
     ```js
    <VsCarousel
        next-text="next page"
        prev-text="previous page"
        slides-xs="1"
        slides-sm="2"
        slides-md="3"
        slides-lg="4"
    >
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="0"
        >
            <template slot="vsCarouselSlideHeading">
                1 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="1"
        >
            <template slot="vsCarouselSlideHeading">
                2 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="2"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="3"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="4"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="5"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="6"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="7"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="8"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="9"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="10"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="11"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <VsCarouselSlide
            link-url="www.visitscotland.com"
            link-type="external"
            img-src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            img-alt=""
            category="Category"
            days="15"
            transport="bus"
            transport-name="Bus"
            slide-index="12"
        >
            <template slot="vsCarouselSlideHeading">
                3 Count 7,000 shining stars in the iconic
                galloway forest
            </template>
            <template slot="vsCarouselSlideContent">
                <p>
                    Right across the country, you’ll find amazing
                    places to eat and drink from local markets to renowned
                    restaurants. Here are some recomm…
                </p>
            </template>
        </VsCarouselSlide>
        <template slot="vsCarouselOf">
            of
        </template>
    </VsCarousel>
    ```
</docs>
