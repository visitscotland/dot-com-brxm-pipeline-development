import Vuex from 'vuex';
import Vue from 'vue';

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        itineraries: {
            namespaced: true,
            state: {
                highlightedStop: null,
            },
            mutations: {
                SET_STOP_HIGHLIGHTED: (state, payload) => {
                    /* eslint-disable no-param-reassign */
                    state.highlightedStop = payload;
                },
            },
            actions: {
                setStopHighlighted: ({ commit, getters }, payload) => {
                    if (getters.getHighlightedStop === payload) {
                        return false;
                    }
                    commit('SET_STOP_HIGHLIGHTED', payload);
                    return true;
                },
            },
            getters: {
                getHighlightedStop: (state) => state.highlightedStop,
                getHighlightedStopCoordinates: (state) => (state.highlightedStop === null
                    ? [-4.07083, 56.18882]
                    : state.highlightedStop.geometry.coordinates),
            },
        },
    },
});
