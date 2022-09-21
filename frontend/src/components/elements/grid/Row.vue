<template>
    <BRow v-bind="$attrs">
        <!-- @slot Default slot for row content -->
        <slot />
    </BRow>
</template>

<script>
import { BRow } from 'bootstrap-vue';

/**
 * Rows are used to wrap column elements to ensure consistent alignment.
 *
 * @displayName Row
 */

export default {
    name: 'VsRow',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BRow,
    },
};
</script>

<style lang="scss">
// Setting gutter width for XS
@media (max-width: map-get($grid-breakpoints, sm)){
    .row:not(.no-gutters) {
        margin-right: -4px;
        margin-left: -4px;
    }
    .row:not(.no-gutters) > .col,
    .row:not(.no-gutters) > [class*="col-"] {
        padding-right: 4px;
        padding-left: 4px;
    }
}
</style>
