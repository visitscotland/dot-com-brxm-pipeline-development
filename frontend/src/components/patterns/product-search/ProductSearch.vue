<template>
    <div class="vs-product-search pt-9">
        <VsContainer>
            <VsRow>
                <VsCol
                    class="vs-product-search__col vs-product-search__col--left"
                    xl="7"
                >
                    <VsHeading
                        level="2"
                        alternative
                        class="vs-product-search__heading mt-0"
                    >
                        <!-- @slot Text for the module heading -->
                        <slot name="vsModuleHeading" />
                    </VsHeading>

                    <div class="vs-product-search__intro">
                        <!-- @slot Text for the module intro -->
                        <slot name="vsModuleIntro" />
                    </div>
                </VsCol>
                <VsCol
                    class="vs-product-search__col vs-product-search__col--right"
                    xl="5"
                >
                    <VsProductSearchEmbed
                        :config="configArr"
                    />
                </VsCol>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import {
    VsContainer,
    VsRow,
    VsCol,
} from '@components/elements/grid';
import VsHeading from '@components/elements/heading/Heading';
import VsProductSearchEmbed from './components/ProductSearchEmbed';

/**
 * A module housing the PSR widget, a title and optional text
 *
 * @displayName Product Search
 */

export default {
    name: 'VsProductSearch',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsContainer,
        VsRow,
        VsCol,
        VsProductSearchEmbed,
        VsHeading,
    },
    props: {
        /**
         * Config for prefilled fields and language
         */
        configArr: {
            type: Array,
            default() {
                return [];
            },
        },
    },
};
</script>

<style lang="scss">
    .vs-product-search {
        width: 100%;
        background: $color-gray-shade-5;
        padding: 0 6px $spacer-9;
        font-family: $font-family-sans-serif;

        &__heading,
        &__intro {
            color: $color-white;
        }

        &__heading.vs-heading {
            font-size: $font-size-9;
            font-weight: $font-weight-light;
            letter-spacing: normal;
        }

        &__col--left {
            background: $color-gray-shade-5;
            font-family: $font-family-sans-serif;
            margin-bottom: $spacer-10;
        }

        &__col--right {
            display: flex;
            flex-direction: column;
            justify-content: center;
            background: $color-yellow;
            padding: $spacer-6;
        }

        // id needed for specificity
        #inline-search-container {
            font-family: $font-family-base;

            input[type='text'],
            input[type='search'],
            input[type='email'] {
                height: 50px;
            }

            .Select--single > .Select-control .Select-value {
                position: relative;
            }

            .c-search__label,
            .form-control,
            .DateInput_input,
            .Select.form-control {
                font-family: $font-family-base;
                font-size: $font_size_base;
                margin-top: $spacer-1;
            }

            .form-control,
            .DateInput_input,
            .Select.form-control,
            .Select-multi-value-wrapper,
            .Select-placeholder {
                min-height: 50px;
                font-style: normal;
                font-weight: $font-weight-normal;
                color: $color-gray-shade-7;
                font-size: $font_size_base;
                display: flex;
                flex-wrap: wrap;
                align-items: center;
                padding-top: 0;
                padding-bottom: 0;

                &:focus {
                    border: $color-pink 4px solid;
                }
            }

            .c-search__label {
                font-weight: $font-weight-semi-bold;

                &::first-letter {
                    text-transform: uppercase;
                }
            }

            .is-focused {
                border: $color-pink 4px solid;
            }

            .DateRangePickerInput,
            .form-flex-row {
                height: 50px;
                overflow: hidden;

                .form-control {
                    margin-top: 0;
                }
            }

            .c-search__date-anytime.form-control,
            .c-search-rooms-guests__trigger.form-control {
                display: flex;
                align-items: center;
            }

            .c-search__in,
            .c-search__for {
                font-size: $font-size-4;
            }

            .c-search__date-reset,
            .c-search__rooms-guests-picker__label {
                color: $color-gray-shade-5;
            }

            .c-search__rooms-guests-picker__button {
                position: relative;

                &:focus {
                   border: 1px solid $color-pink;
                   background-color: $color-white;
                   color: $color-pink;
                   outline: none;

                    &::before {
                        content: '';
                        border: 3px solid  $color-secondary-teal;
                        position: absolute;
                        top: -4px;
                        left: -4px;
                        width: calc(100% + 8px);
                        height: calc(100% + 8px);

                    }
                }
            }

            .c-search__date-reset:focus {
                outline: 3px solid $color-secondary-teal;
            }

            .c-search__date-summary .DateRangePickerInput div:nth-child(3) {
                flex-grow: 0;
            }

            .DateRangePicker_picker {
                .CalendarMonth_caption {
                    color: $color-gray-shade-5;
                    font-weight: $font-weight-bold;
                    line-height: 1;
                    font-size: $font-size-6;
                }

                .DayPicker_weekHeader_ul {
                    color: $color-gray-shade-7;

                    small {
                        font-size: $font-size-5;
                    }
                }

                .CalendarDay {
                    border: none;
                    color: $color-gray-shade-7;
                    font-size: $font-size-5;

                    &:focus {
                        @extend %outline-link-focus;
                    }

                    &:hover {
                        @media (hover: hover) {
                            background-color: $color-secondary-teal-tint-5;
                        }
                    }

                    &__blocked_out_of_range {
                        color: $color-gray-tint-2;
                    }

                    &__selected {
                        background-color: $color-secondary-teal-shade-2;
                        color: $color-white;

                    }

                    &__selected_span {
                        background-color: $color-secondary-teal-tint-5;
                    }
                }
            }

            .button-row {
                & > a {
                    &:focus {
                        outline: 3px solid $color-secondary-teal;
                    }
                }
            }
        }

        #inline-search-container .form-control::-webkit-input-placeholder {
            color: $color-theme-secondary-light;
        }

        @include media-breakpoint-up(sm) {
            padding-left: $spacer-3;
            padding-right: $spacer-3;
        }

        @include media-breakpoint-up(xl) {
            padding: 0;
            background: transparent;

            &__heading,
            &__intro {
                color: $color-white;
                margin-left: $spacer-10;
                max-width: 70%;
            }

            &__heading.vs-heading {
                font-size: $font-size-10;
            }

            &__col {
                min-height: 458px;
            }

            &__col--left {
                padding: $spacer-12;
                margin-bottom: 0;
            }

            &__col--right.col-lg-4 {
                padding: $spacer-8;
            }
        }
    }
</style>

<docs>
```jsx
    <VsContainer>
        <VsRow>
            <VsProductSearch
                :configArr="[
                    {'subSearchType': 'acco'},
                    {'locplace': '4161'},
                    {'lang':'en'},
                    {'domain':'http://172.28.81.65:8089'},
                ]"
            >
                <template slot="vsModuleHeading">
                    Find places to stay & things to do
                </template>

                <template slot="vsModuleIntro">
                    Search through a fantastic range of things to do, places to stay,
                    local events and tours
                </template>
            </VsProductSearch>
        </VsRow>
    </VsContainer>
```
</docs>
