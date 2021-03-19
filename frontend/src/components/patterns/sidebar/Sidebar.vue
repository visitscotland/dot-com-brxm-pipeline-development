<template>
    <VsCol
        class="sidebar"
        data-test="vs-sidebar"
        :tag="tag"
        v-bind="$attrs"
    >
        <slot />
    </VsCol>
</template>

<script>
import { VsCol } from '@components/elements/layout';

/**
 * Sidebar component: a wrapper component for sidebar content, generally
 * wrapped in content sections
 *
 * @displayName Sidebar
 */
export default {
    name: 'VsSidebar',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsCol,
    },
    props: {
        /**
         * The html element name used for the component
         */
        tag: {
            type: String,
            default: 'aside',
        },
    },
};
</script>

<style lang="scss"></style>

<docs>
  ```jsx
  <VsContainer>
    <VsRow>
      <VsSidebar>
        <VsContentSection tag="aside" heading="Heading">
          <p style="margin-bottom:8px">Aside section content. Sidebar container has:</p>

          <pre style="white-space:normal">no cols setting - auto flex fit</pre>
        </VsContentSection>
      </VsSidebar>
    </VsRow>
    <VsRow>
      <VsSidebar sm="10" md="8" lg="6" xl="4" xxl="2">
        <VsContentSection tag="aside" heading="Heading">
          <p style="margin-bottom:8px">Aside section content. Sidebar container has:</p>

          <pre style="white-space:normal">sm="10" md="8" lg="6" xl="4" xxl="2"</pre>
        </VsContentSection>
      </VsSidebar>
    </VsRow>
    <VsRow>
      <VsSidebar cols="6">
        <VsContentSection tag="aside" heading="Heading">
          <p style="margin-bottom:8px">Aside section content. Sidebar container has:</p>

          <pre style="white-space:normal">cols="6"</pre>
        </VsContentSection>
      </VsSidebar>
    </VsRow>
  </VsContainer>
  ```
</docs>
