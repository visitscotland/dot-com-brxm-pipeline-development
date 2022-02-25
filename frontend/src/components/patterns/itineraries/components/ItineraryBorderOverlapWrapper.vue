<template>
    <div
        class="vs-itinerary-border-overlap__wrapper
        d-flex
        d-sm-inline-flex
        flex-column
        justify-content-start
        border-left"
    >
        <slot />
    </div>
</template>

<script>
/**
 * Wraps the nearby links at the bottom of each day and applies border styling
 *
 * @displayName Itinerary Border Overlap Wrapper
 */
export default {
    name: 'VsItineraryBorderOverlapWrapper',
    status: 'prototype',
    release: '0.0.1',
    components: {
    },
    props: {
    },
};
</script>

<style lang="scss">
.vs-itinerary-border-overlap__wrapper {
    min-height: 200px;
    margin-left: 2.5rem;

    @include media-breakpoint-up(sm) {
        margin-left: 6rem;
    }

    .btn {
        margin-left: -2.5rem;
        @include media-breakpoint-up(sm) {
            margin-left: -1rem;
            &:first-of-type {
                margin-top: -1rem;
            }
        }
    }
}
</style>
<docs>

  ```jsx
    <VsItineraryBorderOverlapWrapper>
        <VsButton
            class="mb-3"
            background="white"
            button-size="md"
            variant="outline-primary"
            href="#"
            icon="food"
        >
            Nearby places to eat
        </VsButton>
        <VsButton
            background="white"
            variant="outline-primary"
            href="#"
            icon="product-accommodation"
        >
            Nearby places to stay
        </VsButton>
    </VsItineraryBorderOverlapWrapper>
  ```
</docs>
