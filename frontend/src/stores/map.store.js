import Vuex from 'vuex';
import Vue from 'vue';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        maps: [],
    },
    mutations: {
        addMapInstance: (state, payload) => {
            state.maps.push({
                id: payload.id,
                filters: payload.filters,
                places: payload.places,
                activePins: payload.activePins,
                hovered: '',
            });
        },
        SET_HOVERED_PLACE: (state, payload) => {
            state.maps.forEach((map) => {
                if (map.id === payload.mapId) {
                    /* eslint-disable no-param-reassign */
                    map.hovered = payload.hoveredId;
                }
            });
        },
        SET_ACTIVE_PLACE: (state, payload) => {
            state.maps.forEach((map) => {
                if (map.id === payload.mapId) {
                    /* eslint-disable no-param-reassign */
                    map.activePlace = payload.placeId;
                }
            });
        },
    },
    actions: {
        setHoveredPlace: ({ commit }, payload) => {
            commit('SET_HOVERED_PLACE', payload);
        },
        setActivePlace: ({ commit }, payload) => {
            commit('SET_ACTIVE_PLACE', payload);
        },
    },
    getters: {
        getMaps: (state) => state,
        getMapById: (state) => (id) => state.maps.find((map) => map.id === id),
        getHoveredStop: (state) => (mapId) => {
            const activeMap = state.maps.find((map) => map.id === mapId);
            return activeMap.hovered;
        },
        getActivePlace: (state) => (mapId) => {
            const activeMap = state.maps.find((map) => map.id === mapId);
            return activeMap.activePlace;
        },
    },
});
