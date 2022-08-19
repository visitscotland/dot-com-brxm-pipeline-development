<template>
    <VsButton
        data-test="vs-site-search"
        class="vs-site-search"
        icon="search"
        size="md"
        :animate="false"
        :variant="isShowing ? 'light' : 'primary'"
        @click.native="toggleAction"
    >
        <!-- Default slot for Search button text -->
        <span class="sr-only-lg-down">
            <slot />
        </span>
    </VsButton>
</template>

<script>
import VsButton from '@components/elements/button/Button';
/**
 * Site search lets users find relevant information using
 * a word or phrase to find content.
 *
 * @displayName Site Search
 */

export default {
    name: 'VsSiteSearch',
    components: {
        VsButton,
    },
    props: {
        /**
         * Used to know if the search form is currently showing
         */
        isShowing: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            showSearchForm: true,
        };
    },
    methods: {
        /**
         * Toggles search form and emits event with the data
        */
        toggleAction() {
            this.showSearchForm = !this.showSearchForm;

            /**
             * @event toggleAction
             * @type {boolean}
             * @property {boolean} showSearchForm - indicates whether
             * the search form should be hidden or not.
            */
            this.$emit('toggleAction', this.showSearchForm);
        },
    },
};
</script>

<style lang="scss">
.vs-site-search {
    &.vs-button.btn{
        z-index: 1;
        padding: $spacer-2;
        height: 45px;

        .vs-icon{
            margin-right: 0;
        }

        @include media-breakpoint-up(lg) {
            padding: $spacer-3;
            height: 55px;

            .vs-icon{
                margin-right: $spacer-2;
            }
        }
    }
}

@include no-js {
    .vs-site-search {
        display: none!important;
    }
}
</style>
