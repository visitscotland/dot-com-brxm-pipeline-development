<template>
    <VsButton
        class="vs-favourites-toggle-button"
        @click.native="toggleFavourite"
        variant="transparent"
        :animate="false"
        size="sm"
    >
        <span
            class="sr-only"
            v-if="favourited"
        >Remove from Favourites</span>
        <span
            class="sr-only"
            v-else
        >Add to Favourites</span>
        <VsIcon
            v-if="favourited"
            name="favourite-filled"
            size="lg"
            variant="primary"
        />
        <VsIcon
            v-else
            name="favourite"
            size="lg"
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
 * @displayName Favourites Toggle Button
 */
export default {
    name: 'VsFavouritesListToggleButton',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
    },
    props: {
        title: {
            type: String,
            default: '',
        },
        href: {
            type: String,
            default: '',
        },
    },
    // store,
    computed: {
        favourites() {
            return store.getters['favourites/getFavourites'];
        },
        favouriteItem() {
            return {
                title: this.title,
                href: this.href,
            };
        },
        favourited() {
            return (
                this.favourites.findIndex(
                    (favourite) => favourite.href === this.favouriteItem.href,
                ) !== -1
            );
        },
    },
    methods: {
        toggleFavourite() {
            if (!this.favourited) {
                store.dispatch('favourites/addFavourite', this.favouriteItem);
            } else {
                store.dispatch('favourites/deleteFavourite', this.favouriteItem);
            }
        },
    },
};
</script>

<style lang="scss">
.vs-favourites-toggle-button {
    display: block;
    position: relative;
    height: 40px;
    padding: 0 10px;
    margin: 0 0 $spacer-2 $spacer-2;
}
</style>

<docs>
  ```jsx

    <VsFavouritesToggleButton
      :href="favourite.href"
      :title="favourite.title"
    />
  ```
</docs>
