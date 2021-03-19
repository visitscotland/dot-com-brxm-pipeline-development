<template>
    <VsButton
        class="vs-site-search-toggle-button p-0 p-sm-1 pr-xl-3"
        :variant="isOn ? 'light' : 'primary'"
        :animate="false"
    >
        <VsIcon
            name="search"
            size="md"
            :variant="isOn ? 'secondary' : 'reverse-white'"
        />
        <span class="sr-only-xl-down d-xl-flex"><slot /></span>
    </VsButton>
</template>

<script>
/**
 * TODO: Document Usage
 *
 * @displayName Site Search Toggle Button
 */

export default {
    name: 'VsSiteSearchToggleButton',
    props: {
        isOn: {
            type: Boolean,
            default: false,
        },
    },
};
</script>

<style lang="scss">
.vs-site-search-toggle-button {
  font-size: 1.125rem;
  font-weight: $font-weight-normal;
  letter-spacing: 0px;
  position: relative;
  text-transform: none;
  transition: $transition-base;

  @include media-breakpoint-up(sm) {
    &:not(:focus) {
      box-shadow: 0 8px 0 0 $color-theme-light;
    }

    &.btn-primary:not(:focus) {
      box-shadow: 0 5px 0 0 $color-theme-primary;

      &:hover {
        box-shadow: 0 5px 0 0 #890056;
      }
    }
  }
}
</style>

<docs>
  ```jsx
    <VsSiteSearchToggleButton>Search</VsSiteSearchToggleButton>
  ```
</docs>
