import Vue from 'vue';
import {
    split,
    initial,
    trimStart,
    last,
    set,
    merge,
    camelCase,
} from 'lodash';

const fixtures = require.context('./', true, /^\.\/.*\.json$/);

const processFixture = (path) => {
    const parts = split(trimStart(path, './'), '/');

    const keys = [...initial(parts), last(parts).replace(new RegExp('.json$'), '')].map(camelCase);

    return set({
    }, keys, fixtures(path));
};

const data = merge(...fixtures.keys().map(processFixture));

Vue.mixin({
    data() {
        return data;
    },
});
