<template>
    <VsButton
        class="vs-favourites-view__button p-0 p-sm-1 position-relative"
        @click.native="openFavourites"
        variant="transparent"
        :animate="false"
        v-if="favouritesCount > 0"
    >
        <span class="sr-only">View favourites</span>
        <span
            class="vs-favourites-view-button-count"
            v-if="favouritesCount > 0"
        >
            <span class="sr-only">Current favourites count:</span> {{ favouritesCount }}
        </span>
        <VsIcon
            v-if="favouritesCount > 0"
            name="favourite-filled"
            size="sm"
            variant="primary"
        />
        <VsIcon
            v-else
            name="favourite"
            size="sm"
            variant="dark"
        />
    </VsButton>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
import store from '../favourites.store';

/**
 * TODO: Document usage
 *
 * @displayName Favourites View Button
 */
export default {
    name: 'VsListFavouritesViewButton',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
    },
    props: {
    },
    // store,
    computed: {
        favouritesCount() {
            return store.getters['favourites/getFavouritesCount'];
        },
        favourites() {
            return store.getters['favourites/getFavourites'];
        },
    },
};
</script>

<style lang="scss">
.vs-favourites-view-button-count {
    color: $color-white;
    display: block;
    font-size: 0.75rem;
    width: 100%;
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
}
</style>

<docs>
  ```jsx
    <VsFavouritesViewButton />
  ```
</docs>
