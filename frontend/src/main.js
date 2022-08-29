import Vue from 'vue';
import { isObject, each } from 'lodash-es';

import './styles/core.styles.scss';
import noJsClass from './utils/no-js-class.js';

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
    if (!skipRemoveNoJsClass) {
        removeNoJSClass();
    }

    return new Vue({
        ...defaultVueOptions,
        ...isObject(options) ? options : {
        },
    });
};

export { Vue };
