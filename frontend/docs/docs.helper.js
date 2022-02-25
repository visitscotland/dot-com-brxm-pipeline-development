/**
 * This is Vue Design System’s JS helper file for docs.
 * You can add more things if/when needed.
 */
import Vue from 'vue';
import WebFontLoaderDocs from './utils/webFontLoader'; // eslint-disable-line no-unused-vars
import statusLabels from './utils/statusLabels';
import activeNav from './utils/activeNav';
import filterSearch from './utils/filterSearch';
import sortTocList from './utils/sortTocList';
import componentSectionHeader from './utils/componentSectionHeader';
import 'codemirror/mode/jsx/jsx';

Vue.config.productionTip = false;
Vue.mixin(statusLabels);

/**
 * These should be refactored into proper rsg-components so we don't
 * have to use this hacky hack. The handlers don't work without
 * the setTimeout because the React components re-render AFTER
 * these handlers have updated their targets.
 */
document.addEventListener('DOMContentLoaded', () => {
    setTimeout(() => {
        filterSearch.methods.init();
        activeNav.methods.init();
        sortTocList.methods.init();
        componentSectionHeader.methods.init();
    });
});

window.addEventListener('hashchange', () => {
    setTimeout(() => {
        filterSearch.methods.init();
        activeNav.methods.init();
        componentSectionHeader.methods.init();
    });
});
