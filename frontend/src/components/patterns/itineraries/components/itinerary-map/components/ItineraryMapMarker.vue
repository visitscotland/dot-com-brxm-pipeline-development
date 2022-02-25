<template>
    <button
        class="vs-itinerary-map-marker"
        :class="isHighlighted ? 'active' : ''"
        variant="transparent"
        @mouseenter="handleMouseEnter()"
        @mouseleave="handleMouseLeave()"
        @focus="handleClick()"
        @blur="handleClick()"
        @click="handleClick()"
        @keydown="handleClick()"
    >
        <div class="vs-map-marker__wrapper">
            <VsIcon
                name="map-marker-filled"
                :class="isHighlighted ? 'active' : ''"
                :variant="isHighlighted ? 'dark' : 'secondary-teal'"
                size="xl"
            />
            <span
                class="vs-itinerary-map-marker__count"
                :class="isHighlighted ? 'active' : ''"
            >
                <span class="sr-only">Stop</span>{{ feature.properties.stopCount }}
            </span>
        </div>
    </button>
</template>

<script>
import itinerariesStore from '@components/patterns/itineraries/itineraries.store';
import VsIcon from '@components/elements/icon/Icon';

/**
 * TODO: Document usage
 *
 * @displayName Itinerary Map Marker
 */

export default {
    name: 'VsItineraryMapMarker',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
    },
    props: {
        feature: {
            type: Object,
            required: true,
        },
    },
    data() {
        return {
            isHighlighted: false,
        };
    },
    itinerariesStore,
    computed: {
        highlightedStop() {
            return itinerariesStore.getters['itineraries/getHighlightedStop'];
        },
    },
    watch: {
        highlightedStop() {
            this.toggleHighlighted();
        },
    },
    methods: {
        handleClick() {
            if (this.highlightedStop === this.feature) {
                return itinerariesStore.dispatch('itineraries/setStopHighlighted', null);
            }
            return itinerariesStore.dispatch('itineraries/setStopHighlighted', this.feature);
        },
        handleMouseEnter() {
            return itinerariesStore.dispatch('itineraries/setStopHighlighted', this.feature);
        },
        handleMouseLeave() {
            return itinerariesStore.dispatch('itineraries/setStopHighlighted', null);
        },
        toggleHighlighted() {
            this.isHighlighted = this.highlightedStop === this.feature;
        },
    },
};
</script>

<style lang="scss">
.vs-itinerary-map-marker {
    background: transparent;
    border: none;
    display: block;
    font-weight: $font-weight-bold;
    padding: 0;
    position: absolute;

    &:hover,
    &:focus,
    &.active {
        z-index: 1 !important;
    }

    svg {
        transition: $transition-base;
    }

    &.active {
        .vs-map-marker__wrapper {
            transform: scale(1.2, 1.2) translateY(-10px);
        }

        svg {
            fill: $color-secondary-teal-shade-3 !important;
        }
    }
}

.vs-itinerary-map-marker__count {
    color: $color-white;
    display: block;
    font-size: 1rem;
    font-family: $headings-font-family;
    position: absolute;
    top: 4px;
    left: 0;
    transition: $transition-base;
    text-align: center;
    width: 100%;
}
</style>

<docs>
  ```jsx
    const sampleGeojsonData = require(
        "../../../../../../assets/fixtures/itineraries/sample-itinerary-geojson.json"
    )

    <vs-row class="py-5">
      <vs-col>
        <vs-itinerary-map-marker :feature="sampleGeojsonData.features[0]" />
      </vs-col>
      <vs-col>
        <vs-itinerary-map-marker :feature="sampleGeojsonData.features[1]" />
      </vs-col>
      <vs-col>
        <vs-itinerary-map-marker :feature="sampleGeojsonData.features[2]" />
      </vs-col>
    </vs-row>
  ```
</docs>
