<template>
    <VsCol
        :tag="title ? 'dt' : 'dd'"
        :class="{
            'list-inline-item': inline,
            'vs-description-list__detail': !title,
            'vs-description-list__detail--styled': !inline && !title,
            'vs-description-list__term': title,
            'vs-description-list__term--styled': !inline && title,
        }"
    >
        <!-- @slot The list content goes here -->
        <slot />
    </VsCol>
</template>

<script>
import { VsCol } from '@components/elements/layout';
/**
 * TODO: Document usage
 *
 * @displayName Description List Item
 */

export default {
    name: 'VsDescriptionListItem',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsCol,
    },
    /**
     * Injects inline prop provided by DescriptionList
     */
    inject: ['inline'],
    props: {
        /**
         * Use this option to render this component with a title styling.
         */
        title: {
            type: Boolean,
            default: false,
        },
    },
};
</script>

<style lang="scss" scoped>
.vs-description-list__detail {
    margin-bottom: 0;
}

// Responsive columns to achieve default 2 column list layout
.vs-description-list__detail--styled {
    position: relative;
    @include make-col(8);
    @include make-col-offset(4);

    @include media-breakpoint-down(md) {
        @include make-col(6);
        @include make-col-offset(6);
    }

    @include media-breakpoint-down(sm) {
        @include make-col(12);
        @include make-col-offset(0);
    }
}

.vs-description-list__term {
    // Responsive columns to achieve default 2 column list layout
    &.vs-description-list__term--styled {
        font-family: $headings-font-family;
        @include make-col(4);

        @include media-breakpoint-down(md) {
            @include make-col(6);
        }

        @include media-breakpoint-down(sm) {
            border-bottom: 1px solid $color-gray-tint-5;
            margin-bottom: $spacer-2;
            @include make-col(12);
        }
    }

    // Adds colon separator after inline term for consistency
    &.list-inline-item {
        &::after {
            content: ": ";
        }
    }
}
</style>
