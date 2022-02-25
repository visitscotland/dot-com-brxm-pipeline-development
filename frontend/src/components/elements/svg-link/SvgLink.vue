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
        />
    </VsLink>
</template>

<script>
import VsSvg from '@components/elements/svg/Svg';
import VsLink from '@components/elements/link/Link';

/**
 * A component for an SVG wrapped in a link with accessible sr-only text
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
         * Accessiblity alt text for the svg link
         */
        linkAltText: {
            type: String,
            required: true,
        },
        /**
         * URL for the svg link
         */
        href: {
            type: String,
            required: true,
        },
        /**
         * Fill colour for svg
         */
        svgFill: {
            type: String,
            default: '',
        },
        /**
         * Path to svg to display on the svg link
         */
        svgPath: {
            type: String,
            required: true,
        },
    },
};
</script>

<docs>
```jsx
    <BsWrapper style="width: 200px;">
        <VsSvgLink
            link-alt-text="VisitScotland Home"
            href="/"
            svg-fill="#700e57"
            svg-path="visitscotland"
        />
    </BsWrapper>
```
</docs>
