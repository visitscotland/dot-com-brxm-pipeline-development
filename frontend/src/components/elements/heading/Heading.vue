<template>
    <Component
        :is="type"
        class="vs-heading"
        :class="{
            'vs-heading--thin': thin,
        }"
    >
        <!-- @slot The main heading content goes here -->
        <slot />

        <span class="vs-heading__sub-heading">
            <slot name="sub-heading" />
        </span>
    </Component>
</template>

<script>
import { isNumber } from 'lodash';
/**
 * Headings are used as the titles of each major section of a page in the
 * interface. For example, templates generally use headings as their title.
 * Heading element provides an option to change the level of the heading.
 *
 * @displayName Heading
 */
export default {
    name: 'VsHeading',
    status: 'prototype',
    release: '0.1.0',
    props: {
        /**
         * The heading level used for the heading.
         * `1, 2, 3, 4, 5, 6`
         */
        level: {
            type: [String, Number],
            default: '1',
            validator: (value) => (isNumber(value) ? value > 0 && value < 7 : value.match(/(1|2|3|4|5|6)/)),
        },

        /**
         * Use the thin font
         */
        thin: {
            type: Boolean,
        },
    },
    computed: {
        type() {
            return `h${this.level}`;
        },
        hasSubtitle() {
            return !!this.$slots['sub-heading'];
        },
    },
};
</script>

<style lang="scss">
@import "../../../assets/fonts/fonts.css";

$font-sizes: (
    1: $h1-font-size,
    2: $h2-font-size,
    3: $h3-font-size,
    4: $h4-font-size,
    5: $h5-font-size,
    6: $h6-font-size,
);

/* Update font sizes */
$sub-font-sizes: (
    2: $h2-sub-font-size,
    3: $h3-sub-font-size,
);

.vs-heading {
    font-family: $headings-font-family;

    @each $level, $size in $font-sizes {
        @at-root h#{$level}#{&} {
            letter-spacing: $size * 0.1;
            margin-bottom: $size;
        }
    }

    &.vs-heading--thin {
        font-family: $headings-font-family-thin;
    }

    .vs-heading__sub-heading {
        font-family: $headings-font-family-thin;
        display: block;

        @each $level, $size in $sub-font-sizes {
            @at-root h#{$level}#{&} {
                letter-spacing: $size * 0.1;
                font-size: $size;
                margin-top: $size * 0.5;
            }
        }
    }
}

h6.vs-heading, h5.vs-heading{
    font-family: $font-family-sans-serif;
    letter-spacing: normal;
    font-weight: $font-weight-bold;
}

</style>

<docs>
  ```jsx
  <div>
    <VsHeading>H1 Heading</VsHeading>
    <VsHeading thin>H1 Heading Thin</VsHeading>

    <hr/>

    <VsHeading level="2">H2 Heading</VsHeading>
    <VsHeading thin level="2">H2 Heading  Thin</VsHeading>

    <hr/>

    <VsHeading level="3">H3 Heading</VsHeading>
    <VsHeading thin level="3">H3 Heading Thin</VsHeading>

    <hr/>

    <VsHeading level="4">H4 Heading</VsHeading>
    <VsHeading thin level="4">H4 Heading Thin</VsHeading>

    <hr/>

    <VsHeading level="5">H5 Heading</VsHeading>

    <hr/>

    <VsHeading level="6">H6 Heading</VsHeading>

    <hr/>

    <VsHeading level="3">
        H3 Heading With Subtitle
        <span slot="sub-heading">This Is a Subtitle</span>
    </VsHeading>
  </div>

  ```
</docs>
