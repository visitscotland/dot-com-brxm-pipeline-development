<template>
    <div
        class="vs-site-search-form"
        data-test="vs-site-search-form"
        v-if="showSearchForm"
    >
        <VsContainer fluid="lg">
            <VsRow>
                <VsCol cols="12">
                    <BForm
                        role="search"
                        id="cludo-search-form"
                        class="d-flex align-items-start"
                        action
                        method="get"
                        :novalidate="true"
                        @submit="onSubmit"
                        tabindex="-1"
                    >
                        <div class="d-flex flex-column flex-grow-1 position-relative">
                            <label
                                for="search-input"
                                class="vs-site-search-form__label"
                            >
                                <span class="sr-only">
                                    {{ labelText }}
                                </span>
                                <VsIcon
                                    name="search"
                                    size="xxs"
                                    variant="secondary"
                                />
                            </label>

                            <VsInput
                                type="search"
                                aria-label="Search"
                                :placeholder="labelText"
                                autocomplete="off"
                                ref="searchInput"
                                id="search-input"
                                @updated="updateVal"
                                :clear-button-text="clearButtonText"
                                required="true"
                                field-name="site-search"
                            />
                        </div>

                        <VsButton
                            type="submit"
                            class="vs-site-search-form__search-button search-button"
                            variant="primary"
                            size="lg"
                            id="search-button"
                        >
                            {{ submitButtonText }}
                        </VsButton>
                    </BForm>
                </VsCol>
            </VsRow>
        </VsContainer>

        <VsButton
            class="vs-site-search-form__close-button d-none d-lg-block"
            variant="transparent"
            icon-variant-override="dark"
            icon="close-circle"
            size="md"
            icon-only
            @click.native="closeSearchForm"
        >
            <span class="sr-only">
                {{ closeButtonText }}
            </span>
        </VsButton>
    </div>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
import VsInput from '@components/elements/input/Input';
import VsButton from '@components/elements/button/Button';
import {
    VsCol, VsRow, VsContainer,
} from '@components/elements/grid';

import { BForm } from 'bootstrap-vue';

/**
 * Search form used for the global site search.
 *
 * @displayName Site Search Form
 */
export default {
    name: 'VsSiteSearchForm',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
        BForm,
        VsInput,
        VsButton,
        VsCol,
        VsRow,
        VsContainer,
    },
    props: {
        /**
         * Text that renders in form label (sr-only) and input placeholder
         */
        labelText: {
            type: String,
            default: 'What are you looking for?',
        },
        /**
         * Text that renders inside the submit button
         */
        submitButtonText: {
            type: String,
            default: 'Search',
        },
        /**
         * Text that renders inside the clear button (sr-only)
         */
        clearButtonText: {
            type: String,
            default: 'Clear form',
        },
        /**
         * Text that renders inside the close button (sr-only)
         */
        closeButtonText: {
            type: String,
            default: 'Close search form',
        },
    },
    data() {
        return {
            searchTerm: '',
            showSearchForm: true,
        };
    },
    methods: {
        /**
         * Update searchTerm value with returned data
         */
        updateVal(data) {
            this.searchTerm = data.value;
        },
        /**
         * Only submit if the field has a vale
         */
        onSubmit(e) {
            if (this.searchTerm === '') {
                e.preventDefault();
            }
        },
        /**
         * Closes the search form popover
         */
        closeSearchForm() {
            this.showSearchForm = !this.showSearchForm;
            this.$emit('toggleAction', this.showSearchForm);
        },
    },
};
</script>

<style lang="scss">

.vs-site-search-form {
    background-color: rgba(239, 239, 239, 0.5);
    backdrop-filter: blur(30px);
    padding: $spacer-7 0;
    position: absolute;
    width: 100%;

    @include media-breakpoint-up(xl) {
        padding: $spacer-9 0;
    }

    &__label {
        position: absolute;
        left: $spacer-2;
        top: 50%;
        transform: translate(0, -50%);

        @include media-breakpoint-up(lg) {
            left: $spacer-8;

            .vs-icon{
                height: $icon-size-lg;
                width: $icon-size-lg;
                font-size: $icon-size-lg;
            }
        }
    }

    &__input{
        &.vs-form-input.form-control {
            font-size: $font-size-4;
            height: auto;
            padding: $spacer-3 $spacer-7 $spacer-3 $spacer-6;
            border-color: $color-white;

            &:focus{
                box-shadow: $shadow-form-input inset;
                border-color: $color-pink;
            }

            @include media-breakpoint-up(lg) {
                padding: $spacer-4 $spacer-10 $spacer-4 $spacer-12;
                font-size: $font-size-9;
            }

            @include media-breakpoint-up(xl) {
                font-size: $font-size-10;
            }
        }
    }

    .vs-input--site-search.form-control {
        @extend %reset-clear;
        font-size: $font-size-body;
        height: 50px;
        padding: $spacer-3 $spacer-7 $spacer-3 $spacer-6;
        margin: 0;
        border-color: $color-white;

        @include media-breakpoint-up(lg) {
            padding: $spacer-4 $spacer-10 $spacer-4 $spacer-12;
            font-size: $display1-size;
            height: 79px;
        }

        @include media-breakpoint-up(xl) {
            font-size: $font-size-10;
            height: 94px;
        }
    }

    &__search-button{
        height: 50px;
        padding: $spacer-3 $spacer-2;
        font-size: $font-size-3;

        @include media-breakpoint-up(lg) {
            height: 79px;
            padding: $spacer-3 $spacer-8;
            font-size: $font-size-6;
        }

        @include media-breakpoint-up(xl) {
            height: 94px;
        }
    }

    &__close-button.vs-button.btn {
        position: absolute;
        right: $spacer-2;
        top: $spacer-2;

        @include media-breakpoint-up(xl) {
            right: $spacer-4;
            top: $spacer-4;
        }
    }
}
</style>
