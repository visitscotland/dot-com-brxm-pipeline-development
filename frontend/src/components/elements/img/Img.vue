<template>
    <BImg
        v-bind="$attrs"
        :src="src"
        :alt="alt"
        :fluid="fluid"
        :fluid-grow="fluidGrow"
        loading="lazy"
    >
        <!-- @slot Default slot for image content -->
        <slot />
    </BImg>
</template>

<script>
import { BImg } from 'bootstrap-vue';
/**
 * This image component is used to render images in our products
 * to help support and clarify content.
 *
 * @displayName Img
 */

export default {
    name: 'VsImg',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BImg,
    },
    props: {
        /**
         * The source URL
         */
        src: {
            type: String,
            required: true,
        },

        /**
         * The alt text for accessibility
         */
        alt: {
            type: String,
            default: '',
        },

        /**
         * If true makes the image responsive, scaling with the parent
         * up to a max of the native width of the image itself
         */
        fluid: {
            type: Boolean,
        },

        /**
         * If true makes the image responsive, scaling with the parent
         * beyond the native image width if necessary
         */
        fluidGrow: {
            type: Boolean,
        },
    },
};
</script>
