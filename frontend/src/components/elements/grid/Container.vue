<template>
    <BContainer
        tag="div"
        v-bind="$attrs"
    >
        <!-- @slot Default slot for container content -->
        <slot />
    </BContainer>
</template>

<script>
import { BContainer } from 'bootstrap-vue';

/**
 * Containers are the primary structural element used to
 * organise page content.
 *
 * @displayName Container
 */

export default {
    name: 'VsContainer',
    status: 'prototype',
    release: '0.1.0',
    components: {
        BContainer,
    },
};
</script>
