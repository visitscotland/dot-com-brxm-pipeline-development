<template>
    <BCol v-bind="$attrs">
        <slot />
    </BCol>
</template>

<script>
import { BCol } from 'bootstrap-vue';

/**
 * Column components are used to organise, align and space sibling blocks of content within row
 * elements. They are always direct children of row elements/components. Our column component
 * utilises <a href="https://bootstrap-vue.js.org/docs/components/layout">the Bootstrap Vue BCol component</a>.
 *
 * NOTE: this component is a wrapper for the BCol component so any BCol prop can be applied to it.
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

<docs>
  ```jsx

  <div class="style-cols">
    <VsContainer>
      <VsRow>
        <VsCol cols="6" sm="12">
          <pre>cols="6" sm="12"</pre>
        </VsCol>
      </VsRow>
      <VsRow>
        <VsCol cols="8" lg="3">
          <pre>cols="8" lg="3"</pre>
        </VsCol>
        <VsCol>
          <pre>no cols props</pre>
        </VsCol>
      </VsRow>
    </VsContainer>
  </div>

  ```
</docs>
