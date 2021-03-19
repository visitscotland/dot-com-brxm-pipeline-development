<template>
    <BRow v-bind="$attrs">
        <!-- @slot The row's contents go here -->
        <slot />
    </BRow>
</template>

<script>
import { BRow } from 'bootstrap-vue';
/**
 * Rows are used to organise content into rows. Their parent must be a container or column
 * element and they must always have column elements as their direct children. Our row component
 * utilises the <a href="https://bootstrap-vue.js.org/docs/components/layout">b-row Bootstrap Vue component</a>.
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

<docs>
  ```jsx
  <div>
    <VsContainer>
      <VsRow>
        <VsCol>
          This is a row with a single column
        </VsCol>
      </VsRow>
    </VsContainer>
  </div>
  ```
</docs>
