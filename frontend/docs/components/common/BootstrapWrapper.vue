<template>
    <!-- eslint-disable-next-line vue/component-name-in-template-casing -->
    <component
        :is="tag"
        class="bs-wrapper"
    >
        <slot />
    </component>
</template>

<script>
/**
 * This component acts as a Bootstrap styles wrapper for use
 * in your component docs blocks.
 *
 * Import Bootstrap sass files that contain the classes you
 * want to use.
 *
 * NOTE: The imported styles will leak into the first level of
 * children being wrapped but shouldn't leak into deeper levels
 * of components.
 */
export default {
    name: "BsWrapper",
    props: {
        /**
         * Determines the HTML element that will be used for the wrapper
         */
        tag: {
            type: String,
            default: "div",
        },
    },
}
</script>

<style lang="scss" scoped>
@import "../../../node_modules/bootstrap/scss/bootstrap-grid";
@import "../../../node_modules/bootstrap/scss/card";
@import "../../../node_modules/bootstrap/scss/utilities/flex";
@import "../../../node_modules/bootstrap/scss/utilities/text";
@import "../../../node_modules/bootstrap/scss/utilities/display";
</style>

<docs>
  ```jsx
  <bs-wrapper class="container">
    <bs-wrapper class="row">
      <bs-wrapper class="col" tag="section">
        Section one takes up one column
      </bs-wrapper>
      <bs-wrapper class="col" tag="section">
        Section two takes up another column
      </bs-wrapper>
    </bs-wrapper>
  </bs-wrapper>
  ```
</docs>
