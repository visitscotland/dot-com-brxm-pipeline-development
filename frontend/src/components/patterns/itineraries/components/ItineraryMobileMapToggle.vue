<template>
    <VsButton
        class="vs-itinerary-mobile-map-toggle"
        variant="primary"
        @click.native="toggleMap()"
    >
        <VsIcon
            :name="showMap ? 'list' : 'map'"
            variant="reverse-white"
            size="md"
        />

        {{ showMap ? 'List View' : 'Map View' }}
    </VsButton>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
import VsButton from '@components/elements/button/Button';

/**
 * TODO: Document usage
 *
 * @displayName Itinerary Mobile Map Toggle
 */

export default {
    name: 'VsItineraryMobileMapToggle',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
        VsButton,
    },
    data() {
        return {
            showMap: false,
        };
    },
    mounted() {
        window.addEventListener('resize', this.resizeWidth);

        this.resizeWidth();
    },
    methods: {
        toggleMap() {
            this.showMap = !this.showMap;
        },
        resizeWidth() {
            this.showMap = window.innerWidth >= 1200;
        },
    },
};
</script>

<style lang="scss">
.vs-itinerary-mobile-map-toggle {
    padding-left: 1rem;
    padding-right: 1rem;

    svg {
        margin-right: 10px;
    }
}
</style>

<docs>
  ```jsx

    <VsItineraryMobileMapToggle />

  ```
</docs>
