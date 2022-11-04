<template>
    <section
        data-test="vs-main-map-wrapper-panel"
        class="vs-main-map-wrapper-panel"
        :class="currentStage === 2 ? 'vs-main-map-wrapper-panel--small-padding' : ''"
    >
        <div
            class="vs-main-map-wrapper-panel__header-section"
            :class="headerClasses"
        >
            <div
                class="vs-main-map-wrapper-panel__back"
                v-if="currentStage > 0"
            >
                <VsButton
                    icon-only
                    icon="internal-link"
                    icon-orientation="down"
                    size="md"
                    variant="secondary"
                    @click.native="stageBack"
                    data-test="vs-main-map-wrapper-panel--btn-back"
                >
                    <span class="sr-only">
                        <!-- @slot Text for panel back button  -->
                        <slot name="backBtnText" />
                    </span>
                </VsButton>
            </div>

            <VsHeading
                :level="headingLevel"
                override-style-level="4"
                class="vs-main-map-wrapper-panel__heading text-center mt-0"
                :class="currentStage === 2 ? 'd-none d-lg-block' : ''"
                v-if="currentHeading !== ''"
                data-test="vs-main-map-wrapper-panel__heading"
            >
                {{ currentHeading }}
            </VsHeading>

            <div
                class="vs-main-map-wrapper-panel__close d-lg-none"
            >
                <VsButton
                    icon-only
                    icon="close"
                    size="md"
                    variant="secondary"
                    @click.native="closePanel"
                    data-test="vs-main-map-wrapper-panel--btn-close"
                >
                    <span class="sr-only">
                        <!-- @slot Text for panel close button  -->
                        <slot name="closeSidePanelText" />
                    </span>
                </VsButton>
            </div>

            <div
                class="vs-main-map-wrapper-panel__reset"
                :class="currentStage < 2 ? 'd-lg-none' : ''"
            >
                <VsButton
                    icon-only
                    icon="close"
                    size="md"
                    variant="secondary"
                    @click.native="resetPanel"
                    data-test="vs-main-map-wrapper-panel--btn-reset"
                >
                    <span class="sr-only">
                        <!-- @slot Text for panel reset button  -->
                        <slot name="resetSidePanelText" />
                    </span>
                </VsButton>
            </div>
        </div>

        <template v-if="currentStage === 0">
            <div
                v-for="filter in filters"
                :key="filter.id"
            >
                <VsMainMapWrapperCategory
                    :category-name="filter.label"
                    :type="filter.id"
                />
            </div>
        </template>
        <template v-if="currentStage === 1">
            <div
                v-for="place in placesData"
                :key="place.id"
            >
                <VsMainMapWrapperListItem
                    v-if="typeof place.properties !== 'undefined'
                        && place.properties.category.id === selectedCategory"
                    :item-data="place.properties"
                    @show-item-detail="showDetail(place.properties.id)"
                >
                    {{ place.properties.title }}
                </VsMainMapWrapperListItem>
            </div>
        </template>
        <template v-if="currentStage === 2">
            <VsMainMapWrapperDetail
                :content-data="currentPlaceData[0]"
            />

            <VsMainMapWrapperButtons
                :content-data="currentPlaceData[0]"
            />
        </template>
    </section>
</template>

<script>
import VsButton from '@components/elements/button/Button/';
import VsHeading from '@components/elements/heading/Heading';
import VsMainMapWrapperCategory from './MainMapWrapperCategory';
import VsMainMapWrapperListItem from './MainMapWrapperListItem';
import VsMainMapWrapperDetail from './MainMapWrapperDetail';
import VsMainMapWrapperButtons from './MainMapWrapperButtons';

/**
 * Renders a side panel for the map wrapper component
 *
 * @displayName Main Map Wrapper Panel
 */

export default {
    name: 'VsMainMapWrapperPanel',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
        VsMainMapWrapperCategory,
        VsHeading,
        VsMainMapWrapperListItem,
        VsMainMapWrapperDetail,
        VsMainMapWrapperButtons,
    },
    props: {
        /**
         * Heading for the categories view
         */
        categoryHeading: {
            type: String,
            default: '',
        },
        /**
         * Heading level for panel header
         */
        headingLevel: {
            type: String,
            default: '2',
        },
        /**
         * Currently selected category
         */
        selectedCategory: {
            type: String,
            default: '',
        },
        /**
         * The current stage
         */
        currentStage: {
            type: Number,
            required: true,
        },
        /**
         * The ID of thecurrently selected item
         */
        selectedItem: {
            type: String,
            default: '',
        },
        /**
         * The ID of the currently hover item
         */
        hovered: {
            type: String,
            default: '',
        },
    },
    inject: [
        'filters',
        'placesData',
    ],
    computed: {
        currentHeading() {
            let headingText = '';

            switch (this.currentStage) {
            case 0:
                headingText = this.categoryHeading;
                break;
            case 1:
                headingText = this.currentFilter.label;
                break;
            case 2:
                headingText = this.currentPlaceData[0].properties.title;
                break;
            default:
                break;
            }

            return headingText;
        },
        headerClasses() {
            if (this.currentStage === 1) {
                return 'vs-main-map-wrapper-panel__header-section--with-spacer';
            }

            if (this.currentStage === 2) {
                return 'vs-main-map-wrapper-panel__header-section--overlapped';
            }

            return '';
        },
        currentFilter() {
            let currentFilter = '';
            this.filters.forEach((filter) => {
                if (filter.id === this.selectedCategory) {
                    currentFilter = filter;
                }
            });

            return currentFilter;
        },
        currentPlaceData() {
            return this.placesData.filter((obj) => {
                if (typeof obj.properties !== 'undefined') {
                    return obj.properties.id === this.selectedItem;
                }

                return false;
            });
        },

    },
    methods: {
        /**
         * Emits 'close-panel'
         */
        closePanel() {
            this.$emit('close-panel');
        },
        /**
         * Moves one stage back
         */
        stageBack() {
            const previousStage = this.currentStage - 1;
            this.setStage(previousStage);
        },
        /**
         * Resets the panel
         */
        resetPanel() {
            this.setStage(0);
        },
        /**
         * Emits the current stage
         */
        setStage(stageNum) {
            this.$emit('set-stage', stageNum);
        },
    },
};
</script>

<style lang="scss">
    .vs-main-map-wrapper-panel {
        position: relative;
        padding: $spacer-11 $spacer-3 $spacer-0;
        border: 1px solid $color-gray;
        height: 100%;
        overflow-y: auto;
        overflow-x: hidden;

        &--small-padding {
            padding-top: $spacer-6;
        }

        &__header-section {
            display: flex;
            min-height: 32px;
            align-items: center;
            margin-bottom: $spacer-5;

            &--overlapped {
                position: absolute;
                width: calc(100% - #{$spacer-6});
            }
        }

        &__close,
        &__back {
            position: absolute;
            top: $spacer-3;
        }

        &__back {
            left: $spacer-3;
        }

        &__close {
            right: $spacer-3;
        }

        &__reset {
            display: none;
        }

        h2.vs-heading,
        h3.vs-heading {
            flex-grow: 1;
            margin: $spacer-11 $spacer-3 $spacer-0;
        }

        .vs-main-wrapper-category:last-of-type {
            &::before {
                display: none;

                @include media-breakpoint-up(lg) {
                    display: block;
                }
            }
        }

        @include media-breakpoint-up(lg) {
            padding: $spacer-8 $spacer-4 $spacer-0;
            border-right: none;

            &__header-section {
                display: flex;
                margin-bottom: $spacer-7;

                &--with-spacer {
                    padding-right: $spacer-8;
                }

                &--overlapped {
                    position: relative;
                    width: 100%;
                }
            }

            &__close {
                display: none;
            }

            &__back {
                left: 0;
                top: 0;
                position: relative;
            }

            &__reset {
                display: block;
            }
        }
    }
</style>
