import Vuex from 'vuex';
import Vue from 'vue';

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        summaryBox: {
            namespaced: true,
            state: {
                showMiles: true,
            },
            mutations: {
                SET_SHOW_MILES: (state, payload) => {
                    /* eslint-disable no-param-reassign */
                    state.showMiles = payload;
                },
            },
            actions: {
                setShowMiles: ({ commit, getters }, payload) => {
                    if (getters.getShowMiles === payload) {
                        return false;
                    }
                    commit('SET_SHOW_MILES', payload);
                    return true;
                },
            },
            getters: {
                getShowMiles: (state) => state.showMiles,
            },
        },
    },
});
