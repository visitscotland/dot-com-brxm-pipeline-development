<template>
    <BCol v-bind="$attrs">
        <!-- @slot Default slot for column content -->
        <slot />
    </BCol>
</template>

<script>
import { BCol } from 'bootstrap-vue';

/**
 * Column components are used to organise, align and space sibling blocks of content within row
 * elements.
 *
 * @displayName Col
 */

export default {
    name: 'VsCol',
    status: 'prototype',
    release: '0.1.0',
    components: {
        BCol,
    },
};
</script>
