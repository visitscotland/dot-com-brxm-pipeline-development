<template>
    <div
        class="vs-main-map-wrapper-buttons"
        data-test="vs-main-map-wrapper-buttons"
    >
        <VsButton
            block
            :href="contentData.properties.link.link"
            data-test="vs-main-map-wrapper-buttons__discover"
        >
            {{ contentData.properties.link.label }}
        </VsButton>
    </div>
</template>

<script>
import VsButton from '@components/elements/button/Button';

/**
 * Renders link buttons for a map place
 *
 * @displayName Main Map Wrapper Buttons
 */
export default {
    name: 'VsMainMapWrapperButtons',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
    },
    props: {
        /**
         * Data for component content
         */
        contentData: {
            type: Object,
            required: true,
        },
    },
};
</script>

<style lang="scss">
    .vs-main-map-wrapper-buttons {
        position: sticky;
        bottom: 0;
        padding: $spacer-4 0;
        width: 100%;
        background: $color-white;
        text-align: center;
        box-shadow: 0px -12px 10px -10px #c69fbc;

        @include media-breakpoint-up(lg) {
            padding: $spacer-4;
        }
    }
</style>
