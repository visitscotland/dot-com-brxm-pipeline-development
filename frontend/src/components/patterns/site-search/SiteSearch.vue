<template>
    <BForm
        role="search"
        class="d-flex align-items-start py-2 py-md-4 vs-site-search"
        action
        method="get"
        :novalidate="true"
        :validated="validated"
        @focus="focusOnInput"
        @submit="onSubmit"
        tabindex="-1"
    >
        <div class="d-flex flex-column flex-grow-1 position-relative">
            <label
                for="search-input"
                class="position-absolute vs-site-search__label m-0"
            >
                <span class="sr-only">{{ labelText }}</span>
                <VsIcon
                    name="search"
                    size="md"
                    variant="secondary"
                />
            </label>

            <VsFormInput
                type="search"
                class="px-9 vs-site-search__input"
                :placeholder="labelText"
                autocomplete="off"
                v-model="searchTerm"
                :state="validated"
                ref="searchInput"
                id="search-input"
                @input.native="onInput"
            />

            <BFormInvalidFeedback
                v-if="validated === false"
                :state="validated"
            >
                {{ validationText }}
            </BFormInvalidFeedback>
            <div
                v-if="searchTerm.length"
                class="position-absolute vs-site-search__clear-container"
            >
                <VsButton
                    variant="transparent"
                    type="button"
                    class="px-1"
                    size="md"
                    :animate="false"
                    @click.native.prevent="clearSearchFieldAndFocus()"
                >
                    <span class="sr-only-sm-down d-sm-block">{{ clearButtonText }}</span>
                    <VsIcon
                        class="d-sm-none"
                        name="close"
                        size="xs"
                        variant="dark"
                    />
                </VsButton>
            </div>
        </div>
        <VsButton
            type="submit"
            class="px-md-5"
            variant="primary"
        >
            {{ submitButtonText }}
        </VsButton>
    </BForm>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
import VsFormInput from '@components/elements/form-input/FormInput';

import { BForm, BFormInvalidFeedback } from 'bootstrap-vue';

/**
 * TODO: Document Usage
 *
 * @displayName Site Search
 */
export default {
    name: 'VsSiteSearch',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
        BForm,
        VsFormInput,
        BFormInvalidFeedback,
    },
    props: {
        /**
         * Text that renders in form label (sr-only) and input placeholder
         */
        labelText: {
            type: String,
            default: 'Enter a search term',
        },
        /**
         * Text that renders inside the clear button once users start typing
         */
        clearButtonText: {
            type: String,
            default: 'Clear',
        },
        /**
         * Text that renders inside the submit button
         */
        submitButtonText: {
            type: String,
            default: 'Go',
        },
        /**
         * Validation text that renders when an empty form is submitted
         */
        validationText: {
            type: String,
            default: 'Please enter a search term.',
        },
    },
    data() {
        return {
            searchTerm: '',
            validated: null,
        };
    },
    computed: {
        isValid() {
            return this.searchTerm.length > 0;
        },
    },
    methods: {
        clearSearchField() {
            this.searchTerm = '';
        },
        focusOnInput() {
            this.$refs.searchInput.$el.focus();
        },
        clearSearchFieldAndFocus() {
            this.clearSearchField();
            this.focusOnInput();
        },
        onSubmit($event) {
            if (!this.isValid) {
                $event.preventDefault();
                this.validated = false;
            } else {
                return true;
            }

            return false;
        },
        onInput() {
            this.validated = this.isValid ? null : false;
        },
        resetValidation() {
            this.validated = null;
        },
    },
};
</script>

<style lang="scss">

.vs-site-search__input {
    &::placeholder {
        color: transparent;
        padding-left: 0.625rem;

        @include media-breakpoint-up(sm) {
            color: inherit;
        }
    }
}

.vs-site-search__clear-container {
    right: 0.3125rem;
    top: 0.5rem;

    @include media-breakpoint-up(sm) {
        top: 0.75rem;
    }
}

.vs-site-search__label {
    left: 5px;
    top: 5px;
}
</style>

<docs>
  ```jsx
  <div>
    <VsSiteSearch />
  </div>
  ```
</docs>
