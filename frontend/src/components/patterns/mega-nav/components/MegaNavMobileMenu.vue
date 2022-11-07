<template>
    <div
        class="mega-nav-mobile-menu"
        data-test="vs-mega-nav-mobile-menu"
    >
        <!-- @slot default slot for mobile menu contents  -->
        <slot />
    </div>
</template>

<script>
import dataLayerMixin from '../../../../mixins/dataLayerMixin';

export default {
    name: 'VsMegaNavMobileMenu',
    status: 'prototype',
    release: '0.1.0',
    mixins: [
        dataLayerMixin,
    ],
    mounted() {
        this.$root.$on('navAccordionClick', (text) => {
            this.dataLayerSubmit(text);
        });
    },
    methods: {
        /**
         * Submit event to dataLayer for tracking
         */
        dataLayerSubmit(text) {
            const clickEvent = {
                target: {
                    text,
                },
            };

            this.createDataLayerObject(
                'menuNavigationDataEvent',
                clickEvent, null
            );
        },
    },
};
</script>

<style lang="scss">
    .mega-nav-mobile-menu {
        position: absolute;
        top: 100%;
        left: 0;
        width: 100%;
    }
</style>
