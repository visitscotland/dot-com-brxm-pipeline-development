import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({

    namespaced: true,
    state: {
        allowedCookies: [],
        cookiesTestRun: false,
    },
    mutations: {
        CREATE_COOKIE_ARRAY: (state, payload) => {
            Object.assign(state, {
                allowedCookies: [],
            });
            payload.forEach((value) => {
                state.allowedCookies.push(value);
            });
        },
        SET_TEST_RUN: (state, payload) => {
            // eslint-disable-next-line no-param-reassign
            state.cookiesTestRun = payload;
        },
    },
    actions: {
        setCookieArray: ({ commit }, payload) => {
            commit('CREATE_COOKIE_ARRAY', payload);
            return true;
        },
        setTestRun: ({ commit }, payload) => {
            commit('SET_TEST_RUN', payload);
            return true;
        },
    },
    getters: {
        // eslint-disable-next-line arrow-body-style
        getCookieValues: (state) => {
            return state.allowedCookies;
        },
        getTestRunStatus: (state) => state.cookiesTestRun,
    },
});
