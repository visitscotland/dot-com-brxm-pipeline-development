<template>
    <div class="card-grid">
        <slot />
    </div>
</template>

<script>
export default {
    name: "CardGrid",
}
</script>

<style lang="scss" scoped>
@import "../../styles/docs.tokens.scss";
@import "../../styles/docs.mixins.scss";

.card-grid {
  margin-top: $space-l;
  display: block;
  width: 100%;
  @supports (display: grid) {
    display: grid;
    max-width: 1200px;
    align-content: stretch;
    justify-content: left;
    grid-template-columns:
      calc(20% - #{$space-m})
      calc(20% - #{$space-m})
      calc(20% - #{$space-m})
      calc(20% - #{$space-m})
      calc(20% - #{$space-m});
    grid-column-gap: $space-m;
    @media (max-width: 1300px) {
      grid-template-columns:
        calc(25% - #{$space-m})
        calc(25% - #{$space-m})
        calc(25% - #{$space-m})
        calc(25% - #{$space-m});
    }
    @media (max-width: 1100px) {
      grid-template-columns:
        calc(33.333% - #{$space-m})
        calc(33.333% - #{$space-m})
        calc(33.333% - #{$space-m});
    }
    @media (max-width: 900px) {
      grid-template-columns:
        calc(50% - #{$space-m})
        calc(50% - #{$space-m});
    }
    @media (max-width: 400px) {
      grid-template-columns: 100%;
    }
  }
}
</style>

<docs>
  ```jsx
  <card-grid>
    some cards
  </card-grid>
  ```
</docs>
