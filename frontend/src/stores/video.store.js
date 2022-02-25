import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({

    namespaced: true,
    state: {
        videos: [],
    },
    mutations: {
        CREATE_VIDEO_ENTRY: (state, payload) => {
            state.videos.push({
                videoId: payload.id,
                videoDurationMsg: payload.durationMsg,
                videoDuration: payload.duration,
            });
        },
    },
    actions: {
        newVideoRef: ({ commit }, payload) => {
            commit('CREATE_VIDEO_ENTRY', payload);
            return true;
        },
    },
    getters: {
        // eslint-disable-next-line arrow-body-style
        getVideoDetails: (state) => (id) => {
            return state.videos.find((video) => video.videoId === id);
        },
    },
});
