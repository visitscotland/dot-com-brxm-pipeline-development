<template>
    <section
        class="content-section"
        data-test="vs-content-section"
        :class="{ lead: lead, ['content-section-' + tag]: true }"
    >
        <VsHeading
            level="3"
            v-if="heading"
        >
            {{ heading }}
        </VsHeading>

        <slot />
    </section>
</template>

<script>
import VsHeading from '@components/elements/heading';

/**
 * Section component
 *
 * @displayName Content Section
 */
export default {
    name: 'VsContentSection',
    components: {
        VsHeading,
    },
    status: 'prototype',
    release: '0.0.1',
    props: {
        /**
         * Section heading
         */
        heading: {
            type: String,
            default: '',
        },

        /**
         * Whether or not this section should be styled as leading content
         * via .lead class
         */
        lead: {
            type: Boolean,
        },

        /**
         * The element used for the container
         * `article, aside, div`
         */
        tag: {
            type: String,
            default: 'article',
            validator: (value) => value.match(/(article|aside|div)/),
        },
    },
};
</script>

<style lang="scss">
@import "~bootstrap/scss/utilities";

.content-section {
    @extend .mb-5;
}

.content-section-aside {
    @extend .p-4;
    background: #eee;
}
</style>

<docs>
  ```jsx
  <VsContainer>
    <VsRow>
      <VsArticle cols="8" md="12">
        <VsContentSection heading="Article section title">
          Article section content
        </VsContentSection>
      </VsArticle>
      <br /><br />
      <VsSidebar>
        <VsContentSection tag="aside" heading="Aside section title">
          Aside section content
        </VsContentSection>
      </VsSidebar>
    </VsRow>
  </VsContainer>

  ```
</docs>
