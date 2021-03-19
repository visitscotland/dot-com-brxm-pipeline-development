import Vuex from 'vuex';
import Vue from 'vue';
import { find, matches } from 'lodash';

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        favourites: {
            namespaced: true,
            state: {
                favourites: [],
            },
            mutations: {
                ADD_FAVOURITE: (state, payload) => {
                    const newFavourite = {
                        href: payload.href,
                        title: payload.title,
                    };
                    state.favourites.unshift(newFavourite);
                },
                DELETE_FAVOURITE: (state, payload) => {
                    const index = state.favourites.findIndex(
                        (favourite) => favourite.href === payload,
                    );
                    state.favourites.splice(index, 1);
                },
            },
            actions: {
                addFavourite: ({ commit, getters }, payload) => {
                    if (getters.getFavourite(payload)) {
                        return false;
                    }

                    commit('ADD_FAVOURITE', payload);

                    return true;
                },
                deleteFavourite: (context, payload) => {
                    context.commit('DELETE_FAVOURITE', payload);
                },
            },
            getters: {
                getFavourites: (state) => state.favourites,
                getFavouritesCount: (state) => state.favourites.length,
                getFavourite: (state) => (subject) => find(state.favourites, matches(subject)),
                isEmpty: (state) => state.favourites.length === 0,
            },
        },
    },
});
