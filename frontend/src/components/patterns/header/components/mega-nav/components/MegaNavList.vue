<template>
    <ul
        unstyled
        class="vs-mega-nav-list"
        data-test="vs-mega-nav-list"
    >
        <!-- @slot Slot for nav list heading  -->
        <slot name="navListHeading" />

        <!-- @slot Slot for nav list items  -->
        <slot name="navListItems" />

        <!-- @slot Slot for nav list heading cta link at bottom of the menu group  -->
        <slot name="navHeadingCtaLink" />
    </ul>
</template>

<script>
/**
 *  Meganav list wrapper with a slots list items
 *
 * @displayName MegaNav List
 */
export default {
    name: 'VsMegaNavList',
    status: 'prototype',
    release: '0.1.0',
};
</script>

<style lang="scss">
.vs-mega-nav-list {
    position: relative;
    padding: 0;
    margin: 0;
    width: 100%;

    @include media-breakpoint-up(lg) {
        color: $color-white;
        margin: 0 $spacer-6 $spacer-4 0;
        width: 23%;
    }

    @include media-breakpoint-up(xl) {
        margin-right: $spacer-9;
        width: 21.8%;
    }

    @include media-breakpoint-up(xxl) {
        margin-right: $spacer-10;
        width: 21.3%;
    }
}

@include no-js {
    .vs-mega-nav-list {
        width: 100%;
        margin: 0 $spacer-6 $spacer-4 0;

        @include media-breakpoint-up(sm) {
            width: 50%;
        }

        @include media-breakpoint-up(md) {
            width: 32%;
        }
    }
}
</style>

<docs>
  ```jsx
    <VsMegaNavList style="background-color: white;">
        <VsMegaNavListItem slot="navListHeading">
            Attractions
        </VsMegaNavListItem>

        <VsMegaNavListItem slot="navListItems" href="http://visitscotland.com">
            20 Top Attractions
        </VsMegaNavListItem>

        <VsMegaNavListItem slot="navListItems" href="http://visitscotland.com">
            Arts & Culture
        </VsMegaNavListItem>

        <VsMegaNavListItem slot="navListItems" href="http://visitscotland.com">
            Castles
        </VsMegaNavListItem>

        <VsMegaNavListItem slot="navListItems" href="http://visitscotland.com">
            Gardens & Parks
        </VsMegaNavListItem>

        <VsMegaNavListItem slot="navListItems" href="http://visitscotland.com">
            Museums
        </VsMegaNavListItem>

        <VsMegaNavListItem
            href="http://visitscotland.com"
            subheading-link
            slot="navHeadingCtaLink"
        >
            All Attractions
        </VsMegaNavListItem>
    </VsMegaNavList>
  ```
</docs>
