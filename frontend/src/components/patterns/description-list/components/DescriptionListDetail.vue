<template>
    <VsCol
        tag="dd"
        class="vs-description-list-detail"
        :class="{
            'list-inline-item': inline,
            'vs-description-list-detail--styled': !inline
        }"
    >
        <div class="position-relative">
            <slot />
        </div>
    </VsCol>
</template>

<script>
import { VsCol } from '@components/elements/grid';
/**
 * TODO: Document usage
 *
 * @displayName Description List Detail
 */

export default {
    name: 'VsDescriptionListDetail',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsCol,
    },
    /**
     * Injects inline prop provided by DescriptionList
     */
    inject: ['inline'],
};
</script>

<style lang="scss">
.vs-description-list-detail {
    margin-bottom: 0;

    // Responsive columns to achieve default 2 column list layout
    &.vs-description-list-detail--styled {
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
}

</style>
