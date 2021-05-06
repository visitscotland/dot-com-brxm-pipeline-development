<template>
    <span
        v-b-tooltip.hover
        class="vs-tooltip d-inline-block position-relative"
        :title="title"
    >
        <slot />
    </span>
</template>

<script>
import { VBTooltip } from 'bootstrap-vue';
/**
 * TODO: Document usage
 *
 * @displayName Tooltip
 */

export default {
    name: 'VsTooltip',
    status: 'prototype',
    release: '0.0.1',
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

    <VsTooltip title="This is a bus icon">
      <VsIcon name="bus" variant="primary" size="lg" />
    </VsTooltip>
  ```
</docs>
