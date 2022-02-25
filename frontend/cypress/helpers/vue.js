/* eslint-disable no-use-before-define */
/* eslint-disable import/no-extraneous-dependencies */

const mountVue = require('cypress-vue-unit-test');

const { partial, each, clone, pickBy, has, get, defaults } = require('lodash');

const PROPS_DATA_KEY_NAME = 'componentProps';

export default {
    init,
    setProp,
};

/**
 *
 * @param {*} componentName String Name of the component that will be used in templates
 * @param {*} definition Function | Object 2nd arg of Vue.component
 * @param {*} options Object
 *    - props Object Map of props that will be passed to component and can be altered
 *    - childContent String Tag's child HTML markup
 *    - extensions Object Passed into mountVue options object
 *
 * Cypress usage example:
 *
 * const VsMainNavListItem = require("./MainNavListItem.vue").default
 * const VsMainNavPromoItem = require("./MainNavPromoItem.vue").default
 *
 * // These props are added to the component and later updates to them
 * // in the test causes component updates
 * const props = {
 *   level: 1,
 *   ...
 * }
 *
 * // This content is inserted between the opening and closing tags of
 * // the component. Any child components (i.e vs-main-nav-promo-item in
 * // this example) need to be defined in extensions.
 * const content = `
 *   Places to stay
 *       <vs-main-nav-promo-item
 *         v-slot:sunbnav
 *         ...
 *       />
 * `
 *
 * // Here's where to define components, plugins, filters and mixins for global registration
 * // https://github.com/bahmutov/cypress-vue-unit-test#global-vue-extensions
 * const extensions = {
 *   components: { VsMainNavPromoItem },
 * }
 *
 * // 1st arg here defines the component's tag name
 * // (i.e. <vs-main-nav-list-item></vs-main-nav-list-item> )
 * // 2nd arg here is the component object definition itself
 * vueHelper.init("vs-main-nav-list-item", VsMainNavListItem, props, content, extensions)
 *
 */
function init(componentName, definition, options) {
    const template = `
        <div id="app">
            <component is="${
     componentName
     }" v-bind="computedProps">${
     get(options, 'childContent')
     }</component>
        </div>
    `;

    const props = get(options, 'props');
    const extensions = get(options, 'extensions', {
    });

    const initialProps = clone(props);

    const app = defaults(get(options, 'mergeVue', {
    }), {
        template,
        data: {
            [PROPS_DATA_KEY_NAME]: props,
        },
        computed: {
            computedProps() {
                return pickBy(this[PROPS_DATA_KEY_NAME]);
            },
        },
    });

    if (!has(extensions, 'components')) {
        extensions.components = {
        };
    }

    if (definition) {
        extensions.components[componentName] = definition;
    }

    const mount = mountVue(app, {
        extensions,
    });

    beforeEach(partial(resetProps, initialProps));

    beforeEach(mount);
}

function resetProps(props) {
    if (!Cypress.vue) {
        return;
    }
    each(props, (value, propName) => {
        setProp(propName, value);
    });
}

function setProp(propName, value) {
    Cypress.vue.$set(Cypress.vue[PROPS_DATA_KEY_NAME], propName, value);
}
