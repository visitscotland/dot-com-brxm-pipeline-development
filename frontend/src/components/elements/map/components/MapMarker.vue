<template>
    <button
        class="vs-map-marker"
        :class="isActive ? 'vs-map-marker--active' : ''"
        data-test="vs-map-marker"
        variant="transparent"
        @click="handleClick"
        @keydown.enter="handleClick"
        @mouseover="handleHover(feature.properties.id)"
        @mouseleave="handleHover('')"
        @focusin="handleHover(feature.properties.id)"
        @focusout="handleHover('')"
    >
        <VsSvg
            class="vs-map-marker__icon"
            slot="svg"
            :path="`marker-${feature.properties.type}`"
        />
    </button>
</template>

<script>
import VsSvg from '@components/elements/svg/Svg';
import mapStore from '../../../../stores/map.store';

/**
 * A marker for a map compenent
 *
 * @displayName Map Marker
 */

export default {
    name: 'VsMapMarker',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsSvg,
    },
    props: {
        /**
         * Details for the marker
         */
        feature: {
            type: Object,
            required: true,
        },
        /**
         * Id for the map - to be used with
         * the map store
         */
        mapId: {
            type: String,
            required: true,
        },
    },
    computed: {
        isActive() {
            if (this.activePlace === this.feature.properties.id
                || this.highlightedPlace === this.feature.properties.id) {
                return true;
            }

            return false;
        },
        highlightedPlace() {
            return mapStore.getters.getHoveredStop(this.mapId);
        },
        activePlace() {
            return mapStore.getters.getActivePlace(this.mapId);
        },
    },
    methods: {
        /**
         * Fires on click of the marker
         */
        handleClick() {
            mapStore.dispatch('setActivePlace', {
                mapId: this.mapId,
                placeId: this.feature.properties.id,
            });
            this.$parent.$emit('show-detail', this.feature.properties.id);
            this.$parent.$emit('set-category', this.feature.properties.type);
        },
        /**
         * Fires on hover over the maker
         */
        handleHover(id) {
            mapStore.dispatch('setHoveredPlace', {
                mapId: this.mapId,
                hoveredId: id,
            });
        },
    },
};
</script>

<style lang="scss">
.vs-map-marker {
    background: transparent;
    border: none;
    display: block;
    font-weight: $font-weight-bold;
    padding: 0;
    position: absolute;

    &:hover,
    &:focus,
    &--active {
        z-index: 1 !important;

        .vs-map-marker__icon {
            transform: scale(1.2, 1.2) translateY(-10px);
        }
    }

    &__icon {
        transition: $transition-base;
    }

    &__count {
        color: $color-white;
        display: block;
        font-size: $font-size-4;
        font-family: $headings-font-family;
        position: absolute;
        top: 4px;
        left: 0;
        transition: $transition-base;
        text-align: center;
        width: 100%;
    }
}
</style>
