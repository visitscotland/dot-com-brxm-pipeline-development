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
        <VsWarning class="vs-map__no-js">
            <!-- @slot Message to show when JS is disabled  -->
            <slot name="noJs" />
        </VsWarning>
    </div>
</template>

<script>
import Vue from 'vue';
import VsWarning from '@components/patterns/warning/Warning';
import osBranding from '@/utils/os-branding';
import VsMapMarker from './components/MapMarker';
import mapStore from '../../../stores/map.store';

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
         * Whether the map should show regions polygons
         */
        showPolygons: {
            type: Boolean,
            default: false,
        },
        /**
         * The current selectdd item
         */
        selectedItem: {
            type: String,
            default: null,
        },
    },
    data() {
        return {
            isDesktop: false,
            geojsonData: {
                type: 'FeatureCollection',
                features: [],
            },
            polygons: {
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
            hoveredStateId: null,
            activeStateId: null,
        };
    },
    computed: {
        highlightedPlace() {
            if (this.mapbox.map) {
                return mapStore.getters.getHoveredStop(this.mapId);
            }

            return '';
        },
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
        showPolygons(val) {
            if (val === true) {
                this.showMapPolygons();
            } else {
                this.hideMapPolygons();
            }
        },
        highlightedPlace(newVal) {
            if (newVal.length === 0) {
                this.removeHoveredPolygon();
            } else {
                this.addHoveredPolygon(newVal);
            }
        },
        selectedItem(newVal) {
            const isPolygon = this.polygons.features
                .filter((feature) => feature.properties.id === newVal);

            if (!newVal) {
                this.removeActivePolygon();
            } else if (isPolygon.length > 0) {
                this.addActivePolygon(newVal);
            }
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

            this.mapbox.map.on('load', () => {
                this.addMapPolygons();
            });
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
                    let coordinateArray = [
                        place.geometry.coordinates[0],
                        place.geometry.coordinates[1],
                    ];

                    if (place.geometry.type === 'Polygon') {
                        coordinateArray = [
                            place.geometry.coordinates[0],
                        ];
                    }

                    return this.geojsonData.features.push({
                        type: 'Feature',
                        geometry: {
                            type: place.geometry.type,
                            coordinates: coordinateArray,
                        },
                        properties: {
                            title: place.properties.title,
                            imageSrc: place.image,
                            type: place.properties.category.id,
                            id: place.properties.id,
                        },
                        id: place.properties.id,
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
                if (feature.geometry.type === 'Point') {
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
                }
            });
        },
        /**
         * Hide all polygons
         */
        hideMapPolygons() {
            this.mapbox.map.setLayoutProperty('regions-fills', 'visibility', 'none');
            this.mapbox.map.setLayoutProperty('regions-borders', 'visibility', 'none');
        },
        /**
         * Show all polygons
         */
        showMapPolygons() {
            this.mapbox.map.setLayoutProperty('regions-fills', 'visibility', 'visible');
            this.mapbox.map.setLayoutProperty('regions-borders', 'visibility', 'visible');
        },
        /**
         * Add polygons from data into map
         */
        addMapPolygons() {
            this.geojsonData.features.forEach((feature) => {
                if (feature.geometry.type === 'Polygon') {
                    if (!this.polygons.features.includes(feature.properties.id)) {
                        this.polygons.features.push(feature);
                    }
                }
            });

            this.mapbox.map.addSource('regions', {
                type: 'geojson',
                data: this.polygons,
                promoteId: 'id',
            });

            this.mapbox.map.addLayer({
                id: 'regions-borders',
                type: 'line',
                source: 'regions',
                layout: {
                },
                paint: {
                    'line-color': '#fff',
                    'line-width': 2,
                },
            });

            this.mapbox.map.addLayer({
                id: 'regions-fills',
                type: 'fill',
                source: 'regions',
                layout: {
                },
                paint: {
                    'fill-color': [
                        'case',
                        ['==', ['feature-state', 'interaction-state'], 'hover'],
                        'rgba(173,14,110,0.698)',
                        ['==', ['feature-state', 'interaction-state'], 'active'],
                        'rgba(173,14,110,0.533)',
                        '#A5A5A5',
                    ],
                    'fill-opacity': 0.8,
                },
            });

            // Hide map polygons by default
            this.hideMapPolygons();

            // When the user moves their mouse over the state-fill layer,
            // we'll update the feature state for the feature under the mouse.
            this.mapbox.map.on('mousemove', 'regions-fills', (e) => {
                if (e.features.length > 0) {
                    this.addMapPopup(e);
                    this.removeHoveredPolygon();
                    this.addHoveredPolygon(e.features[0].id);
                }
            });

            // When the mouse leaves the state-fill layer, update the
            // feature state of the previously hovered feature.
            this.mapbox.map.on('mouseleave', 'regions-fills', () => {
                this.removeMapPopup();
                this.removeHoveredPolygon();
            });

            // When the clicks the the state-fill layer, update the
            // feature state of the active feature.
            this.mapbox.map.on('click', 'regions-fills', (e) => {
                this.removeActivePolygon();
                this.addActivePolygon(e.features[0].id);
            });
        },
        /**
         * Remove the current active polygon
         */
        removeActivePolygon() {
            this.mapbox.map.setFeatureState(
                {
                    source: 'regions',
                    id: this.activeStateId,
                },
                {
                    'interaction-state': '',
                },
            );

            this.activeStateId = null;
        },
        /**
         * Add a new active polygon
         */
        addActivePolygon(polyId) {
            this.activeStateId = polyId;

            this.mapbox.map.setFeatureState(
                {
                    source: 'regions',
                    id: this.activeStateId,
                },
                {
                    'interaction-state': 'active',
                },
            );

            mapStore.dispatch('setActivePlace', {
                mapId: this.mapId,
                placeId: this.activeStateId,
            });
            this.$emit('show-detail', this.activeStateId);

            this.$emit('set-category', 'regions');
        },
        /**
         * Remove the current hovered polygon
         */
        removeHoveredPolygon() {
            if (this.hoveredStateId) {
                let state = '';
                if (this.hoveredStateId === this.activeStateId) {
                    state = 'active';
                }

                this.mapbox.map.setFeatureState(
                    {
                        source: 'regions',
                        id: this.hoveredStateId,
                    },
                    {
                        'interaction-state': state,
                    },
                );
            }

            mapStore.dispatch('setHoveredPlace', {
                mapId: this.mapId,
                hoveredId: '',
            });

            this.hoveredStateId = null;
        },
        /**
         * Add a hovered polygon
         */
        addHoveredPolygon(polyId) {
            this.hoveredStateId = polyId;
            this.mapbox.map.setFeatureState(
                {
                    source: 'regions',
                    id: this.hoveredStateId,
                },
                {
                    'interaction-state': 'hover',
                },
            );

            mapStore.dispatch('setHoveredPlace', {
                mapId: this.mapId,
                hoveredId: polyId,
            });
        },
        /**
         * Adds map pop ups
         */
        addMapPopup(e) {
            function findCenter(markers) {
                const lats = [];
                const lngs = [];
                markers[0].forEach((m) => {
                    lats.push(m[1]);
                    lngs.push(m[0]);
                });
                return {
                    lat: (Math.min(...lats) + Math.max(...lats)) / 2,
                    lng: (Math.min(...lngs) + Math.max(...lngs)) / 2,
                };
            };

            const detectEsc = (keyEvent) => {
                if (keyEvent.key === 'Escape') {
                    this.removeMapPopup();
                    document.body.removeEventListener('keyup', detectEsc);
                }
            };

            const centerPoint = findCenter(e.features[0].geometry.coordinates);

            if (e.features[0].id !== this.hoveredStateId
                && e.features[0].id !== this.activeStateId) {
                this.removeMapPopup();
                document.body.removeEventListener('keyup', detectEsc);

                this.popup = new mapboxgl.Popup()
                    .setLngLat(centerPoint)
                    .setHTML(e.features[0].properties.title)
                    .addTo(this.mapbox.map);
            }

            document.body.addEventListener('keyup', detectEsc);
        },
        /**
         * Remove the popup from the map
         */
        removeMapPopup() {
            if (this.popup) {
                this.popup.remove();
                this.popup = null;
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
        &__map {
            display: none;
        }

        &__no-js {
            display: flex;
        }
    }
}
</style>
