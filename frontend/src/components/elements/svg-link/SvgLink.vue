<template>
    <VsLink
        class="vs-svg-link"
        data-test="vs-svg-link"
        :href="href"
    >
        <span
            class="sr-only"
            data-test="link-alt-text"
        >
            {{ linkAltText }}
        </span>
        <VsSvg
            data-test="vs-svg-link__svg"
            focusable="false"
            :path="svgPath"
            :fill="svgFill"
            :width="svgWidth"
            :height="svgHeight"
        />
    </VsLink>
</template>

<script>
import VsSvg from '@components/elements/svg/Svg';
import VsLink from '@components/elements/link/Link';

/**
 * An SVG link component provides an SVG wrapped in a
 * link with accessible screen reader text.
 *
 * @displayName SVG Link
 */
export default {
    name: 'VsSvgLink',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsSvg,
        VsLink,
    },
    props: {
        /**
         * Accessiblity alt text for the SVG link
         */
        linkAltText: {
            type: String,
            required: true,
        },
        /**
         * The URL for the SVG link
         */
        href: {
            type: String,
            required: true,
        },
        /**
         * The fill color of the SVG
         */
        svgFill: {
            type: String,
            default: '',
        },
        /**
         * The path of the SVG to display, relative to /src/assets
         */
        svgPath: {
            type: String,
            required: true,
        },
        /**
         * The height attribute of the SVG
         */
        svgHeight: {
            type: [Number, String],
            default: '',
        },
        /**
         * The width attribute of the SVG
         */
        svgWidth: {
            type: [Number, String],
            default: '',
        },
    },
};
</script>
