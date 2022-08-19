<template>
    <span
        data-test="vs-tag-manager-wrapper"
        class="d-none"
    />
</template>

<script>
import dataLayerStore from '../../../stores/dataLayer.store';
import dataLayerMixin from '../../../mixins/dataLayerMixin';

/**
 * This is a hidden "Global" component that sits on
 * a higher level of the page hierarchy
 * and controls reading and updating global analytic details
 * to and from the Google Tag Manager (GTM) Vuex Store
 * created by dataLayer.store.js
 *
 * @displayName Tag Manager Wrapper
 */
export default {
    name: 'VsTagManagerWrapper',
    status: 'prototype',
    release: '0.0.1',
    mixins: [dataLayerMixin],
    props: {
        /**
         * Receive an external payload to be pushed through
         * the data layer by this component
         */
        payload: {
            type: Object,
            default: () => {
            },
        },
    },
    mounted() {
        this.processPayload(this.payload);

        document.addEventListener('DOMContentLoaded', () => {
            dataLayerStore.dispatch('setTestRun', true);
            dataLayerStore.dispatch('setPageUrl', window.location.href);
            this.pageViewTemplateDataEvent();
        });
    },
    methods: {
        /**
         * This function receives a payload as props
         * then replaces all "-" for "_" to match the keys on the templates from iProspect
         * and after that pushes all the key:values pairs to the store
         * so any component can retrieve it using the general getter function:
         * dataLayerStore.getters.getValueFromKey("key_name")
         */
        processPayload(payload) {
            if (payload === undefined) return;

            // Convert all the keys from kebab-case to snake_case
            Object.keys(payload).forEach((key) => {
                const newKey = key.replaceAll('-', '_');

                // Pushing the new payload with processed key names to the store
                dataLayerStore.dispatch('processPayload', {
                    key: newKey,
                    value: payload[key],
                });
            });
        },
    },
};
</script>

<style>
</style>
