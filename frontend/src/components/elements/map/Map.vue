<template>
    <div
        class="vs-map"
        data-test="vs-map"
    >
        <div
            :id="mapId"
            class="vs-map__map"
            ref="mapbox"
        />
        <!-- TO DO: Warning to be added once the updated
        component is available in develop -->
        <VsWarning class="vs-map__no-js" />
    </div>
</template>

<script>
import Vue from 'vue';
import VsWarning from '@components/patterns/warning/Warning';
import osBranding from '@/utils/os-branding';
import VsMapMarker from './components/MapMarker';

let mapboxgl = null;
let geojsonExtent = null;

/**
 * Renders a MapBox map
 *
 * @displayName Map
 */

export default {
    name: 'VsMap',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsWarning,
    },
    props: {
        /**
         * Starting latitude for map view
         */
        overviewMapLatitude: {
            type: String,
            default: '57.81',
        },
        /**
         * Starting longitude for map view
         */
        overviewMapLongitude: {
            type: String,
            default: '-5.51748',
        },
        /**
         * Starting zoom level for map view
         */
        overviewMapZoom: {
            type: String,
            default: '5',
        },
        /**
         * Unique ID for the map
         */
        mapId: {
            type: String,
            required: true,
        },
        /**
         * Whether or not the map is visible
         * used for triggering resize
         */
        isVisible: {
            type: Boolean,
            required: true,
        },
        /**
         * Place data for markers
         */
        places: {
            type: Array,
            required: true,
        },
        /**
         * Whether the map should fit to marker bounds
         */
        fitToMarkers: {
            type: Boolean,
            default: false,
        },
        /**
         * The ID of the currently selected item
         */
        selectedItem: {
            type: String,
            default: '',
        },
    },
    data() {
        return {
            isDesktop: false,
            geojsonData: {
                type: 'FeatureCollection',
                features: [],
            },
            mapbox: {
                map: null,
                bounds: null,
                rotation: 0,
                config: {
                    container: this.$refs.mapbox,
                    style: 'https://api.visitscotland.com/maps/vector/v1/vts/resources/styles',
                    pitchWithRotate: false,
                    dragRotate: false,
                },
            },
            markers: [],
            popup: null,
        };
    },
    watch: {
        isVisible(newVal) {
            if (newVal && this.mapbox.map !== null) {
                this.mapbox.map.resize();
            }
        },
        places() {
            this.geojsonData.features.splice(0, this.geojsonData.features.length);
            this.addMapFeatures();
            this.addMapMarkers();
        },
    },
    mounted() {
        this.lazyloadMapComponent();
        this.isTablet = window.innerWidth >= 768;
        window.addEventListener('resize', this.onResize);

        /**
         * Initialise branding options when DOM loads
         */
        window.addEventListener('DOMContentLoaded', () => {
            osBranding.init({
                div: this.mapId,
            });
        });
    },
    methods: {
        /**
         * Adds a map to the page
         */
        addMap() {
            this.mapbox.config.container = this.$refs.mapbox;
            this.mapbox.map = new mapboxgl.Map({
                container: this.$refs.mapbox,
                style: 'https://api.visitscotland.com/maps/vector/v1/vts/resources/styles',
                bounds: [
                    [-7.555827, 55.308836], // south-west point.
                    [-0.778285, 60.830894], // north-east point.
                ],
            });
            this.mapbox.map.scrollZoom.disable();
            this.mapbox.map.on('rotate', () => {
                this.mapbox.rotation = this.mapbox.map.transform.angle;
            });

            this.mapbox.map.resize();
        },
        /**
         * Adds map to controls
         */
        addMapControls() {
            const nav = new mapboxgl.NavigationControl();
            this.mapbox.map.addControl(nav, 'top-right');
            this.mapbox.map.addControl(new mapboxgl.FullscreenControl());
        },
        /**
         * Adds map features
         */
        addMapFeatures() {
            this.places.map((place) => {
                if (typeof place.geometry !== 'undefined') {
                    return this.geojsonData.features.push({
                        type: 'Feature',
                        geometry: {
                            type: 'Point',
                            coordinates: [
                                place.geometry.coordinates[0],
                                place.geometry.coordinates[1],
                            ],
                        },
                        properties: {
                            title: place.properties.title,
                            imageSrc: place.image,
                            type: place.properties.category.id,
                            id: place.properties.id,
                        },
                    });
                }

                return false;
            });
        },
        /**
         * Adds map markers
         */
        addMapMarkers() {
            if (this.markers !== null) {
                for (let i = this.markers.length - 1; i >= 0; i--) {
                    this.markers[i].remove();
                }
            }

            this.geojsonData.features.forEach((feature) => {
                const markerComponent = new Vue({
                    ...VsMapMarker,
                    parent: this,
                    propsData: {
                        feature,
                        mapId: this.mapId,
                    },
                });

                markerComponent.$mount();

                const mapboxMarker = new mapboxgl.Marker(markerComponent.$el)
                    .setLngLat(feature.geometry.coordinates)
                    .addTo(this.mapbox.map);

                this.markers.push(mapboxMarker);
            });
        },
        /**
         * Adds map pop ups
         */
        addMapPopup() {
            this.removeMapPopup();

            if (this.highlightedStopCoordinates !== null && this.highlightedStop !== null) {
                this.popup = new mapboxgl.Popup({
                    closeButton: false,
                    offset: {
                        top: [0, 20],
                        'top-left': [0, 20],
                        'top-right': [0, 20],
                        bottom: [0, -50],
                        'bottom-left': [0, -50],
                        'bottom-right': [0, -50],
                        left: [30, -20],
                        right: [-30, -20],
                    },
                })
                    .setLngLat(this.highlightedStopCoordinates)
                    .setHTML(
                        `
                            <img class="vs-itinerary-map__popup-image" src="${this.highlightedStop.properties.imageSrc}" alt="${this.highlightedStop.properties.altText}" />
                            <div>
                            <h4 class="vs-itinerary-map__popup-stop-number mb-0">${this.labels.stopLabel} ${this.highlightedStop.properties.stopCount}</h4>
                            <p class="vs-itinerary-map__popup-stop-name">${this.highlightedStop.properties.title}</p>
                            </div>
                        `,
                    )
                    .addTo(this.mapbox.map);
            }
        },
        /**
         * Ensures map fits to bounds
         */
        fitToBounds() {
            this.mapbox.map.fitBounds(geojsonExtent(this.geojsonData), {
                padding: {
                    top: 100,
                    bottom: 100,
                    left: 100,
                    right: 100,
                },
            });
        },
        /**
         * Initialises the map component
         */
        initialiseMapComponent() {
            this.addMap();
            this.addMapControls();

            if (this.places.length) {
                this.addMapFeatures();
            }

            if (this.geojsonData.features.length) {
                this.addMapMarkers();
                if (this.fitToMarkers) {
                    this.fitToBounds();
                }
            }
        },
        /**
         * Initialises lazy loading
         */
        lazyloadMapComponent() {
            // ALL Mapbox dependency import and init must be done only in the mounted
            // lifecycle event so it doesn't break SSR

            mapboxgl = require('mapbox-gl'); // eslint-disable-line global-require
            geojsonExtent = require('@mapbox/geojson-extent'); // eslint-disable-line global-require

            // Disable WebGL if its causing performance problems.
            mapboxgl.supported({
                failIfMajorPerformanceCaveat: true,
            });

            if (!('IntersectionObserver' in window)) {
                this.initialiseMapComponent();
                return;
            }

            this.observer = new IntersectionObserver((entries) => {
                if (entries[0].intersectionRatio > 0) {
                    this.observer.unobserve(this.$el);
                    this.initialiseMapComponent();
                }
            });
            this.observer.observe(this.$el);

            if (this.places.length) {
                this.addMapFeatures();
            }
        },
        /**
         * Removes a map pop up
         */
        removeMapPopup() {
            if (this.popup !== null) {
                this.popup.remove();
                this.popup = null;
            } else {
                this.popup = null;
            }
        },
        /**
         * Checks for window size on resize
         */
        onResize() {
            this.isTablet = window.innerWidth >= 768;
        },
    },
};
</script>

<style lang="scss">
@import "mapbox-gl/dist/mapbox-gl.css";

@import "https://labs.os.uk/public/os-api-branding/v0.2.0/os-api-branding.css";

.os-api-branding.logo {
    margin: $spacer-2;
}

.vs-map {
    height: 100%;
    position: relative;

    &__map {
        height: 100%;
        position: relative;
    }

    &__no-js {
        display: none;
    }

    .mapboxgl-ctrl-top-right .mapboxgl-ctrl {
        margin: $spacer-4;

        & > button {
            border: 2px solid $color-theme-primary;
            background-position: center center;
            background-repeat: no-repeat;
            background-size: 1rem 1rem;
        }
    }

    .mapboxgl-ctrl-group:not(:empty) {
        background: transparent;
        border: none;
        box-shadow: none;

        & > button {
            width: 32px;
            height: 32px;
            background-color: $color-white;
            border-radius: 0 !important;

            &:active {
                background-color: $color-white;
            }

            &:focus {
                @extend %primary-button-focus;
            }

            &:not(:disabled):hover {
                background-color: $color-theme-primary;
            }

            &.mapboxgl-ctrl-zoom-in {
                margin-bottom: $spacer-2;
            }

            @include media-breakpoint-up(lg) {
                width: 36px;
                height: 36px;
            }
        }
    }

    .mapboxgl-ctrl-compass {
        display: none;
    }

    .mapboxgl-ctrl-fullscreen {
        background-image: url('~@/assets/images/icons/expand-primary.svg');

        &:hover,
        &:focus {
            background-image: url('~@/assets/images/icons/expand-white.svg');
        }
    }

    .mapboxgl-ctrl-shrink {
        background-image: url('~@/assets/images/icons/compress-primary.svg');

        &:hover,
        &:focus {
            background-image: url('~@/assets/images/icons/compress-white.svg');
        }
    }

    .mapboxgl-ctrl-zoom-in {
        background-image: url('~@/assets/images/icons/add-primary.svg');

        &:hover,
        &:focus {
            background-image: url('~@/assets/images/icons/add-white.svg');
        }
    }

    .mapboxgl-ctrl-zoom-out {
        background-image: url('~@/assets/images/icons/subtract-primary.svg');

        &:hover,
        &:focus {
            background-image: url('~@/assets/images/icons/subtract-white.svg');
        }
    }

    .mapboxgl-ctrl {
        & > button {
            &:hover,
            &:focus {
                background-color: $color-theme-primary;
            }
        }

        .mapboxgl-ctrl-icon {
            display: none;
        }
    }
}

@include no-js {
    .vs-map {
        display: none;

        &__no-js {
            display: flex;
        }
    }
}
</style>
