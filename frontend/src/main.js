import Vue from 'vue';
import { isObject, each } from 'lodash';

import '@/styles/core.styles.scss';
import noJsClass from '@/utils/no-js-class';

Vue.config.productionTip = false;

const defaultVueOptions = {
    comments: true,
};

const removeNoJSClass = () => {
    // remove no-js class
    const elements = document.getElementsByClassName(noJsClass);

    each(elements, (element) => {
        element.classList.remove(noJsClass);
    });
};

export const initApp = (options, skipRemoveNoJsClass) => {
    const app = new Vue({
        ...defaultVueOptions,
        ...isObject(options) ? options : {
        },
    });

    if (!skipRemoveNoJsClass) {
        removeNoJSClass();
    }

    return app;
};

export { Vue };
