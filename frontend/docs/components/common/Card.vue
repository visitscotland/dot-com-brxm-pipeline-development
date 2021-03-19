<template>
    <div class="card">
        <slot />
    </div>
</template>

<script>
export default {
    name: "Card",
}
</script>

<style lang="scss" scoped>
@import "../../styles/docs.tokens.scss";
@import "../../styles/docs.mixins.scss";

.card {
  @include reset;
  @include inset-space($space-s);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  margin-bottom: $space-m;
  box-shadow: 0 0 0 1px rgba(63, 63, 68, 0.05), 0 1px 3px 0 rgba(63, 63, 68, 0.15);
  font-size: $size-s;
  font-family: $font-text;
  color: $docs-color-rich-black;
  border-radius: $radius-default;
  overflow: hidden;
  text-align: left;
  @supports (display: grid) {
    width: 100%;
    float: left;
  }
  @media (max-width: 400px) {
    margin-bottom: $space-m;
  }
}
</style>

<docs>
  ```jsx
  <card>
    some stuff
  </card>
  ```
</docs>
