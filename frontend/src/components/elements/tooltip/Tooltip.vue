<template>
    <VsButton
        v-bind="$attrs"
        v-b-tooltip.hover
        class="vs-tooltip d-inline-block position-relative"
        :title="title"
    >
        <slot />
    </VsButton>
</template>

<script>
import { VBTooltip } from 'bootstrap-vue';
import VsButton from '@components/elements/button/Button';
/**
 * A button that renders a tooltip on hover or focus
 * of the element
 *
 * @displayName Tooltip
 */

export default {
    name: 'VsTooltip',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
    },
    directives: {
        'b-tooltip': VBTooltip,
    },
    props: {
        title: {
            type: String,
            default: '',
        },
    },
};
</script>

<style lang="scss"></style>

<docs>
  ```jsx
    <VsTooltip
        title="This is a bus icon"
        href="#"
        icon="bus"
        size="lg"
        icon-only
        icon-variant-override="dark"
        class="p-0"
        variant="transparent"
    />
  ```
</docs>
