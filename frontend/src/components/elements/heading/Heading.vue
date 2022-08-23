<template>
    <Component
        :is="type"
        class="vs-heading"
        :class="headingClasses"
    >
        <!-- @slot The main heading content goes here -->
        <slot />

        <span
            class="vs-heading__sub-heading"
            v-if="!!this.$slots['sub-heading']"
        >
            <!-- @slot Slot for sub-heading content -->
            <slot name="sub-heading" />
        </span>
    </Component>
</template>

<script>
import { isNumber } from 'lodash';
/**
 * Headings are used to create visual hierarchy on a page.
 * This component uses the correct heading tags for semantic markup.
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
         * `1|2|3|4|5|6`
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
        /**
         * Alternative font
         */
        alternative: {
            type: Boolean,
            default: false,
        },
        /**
         * Heading override style
         * `1|2|3|4|5|6`
         */
        overrideStyleLevel: {
            type: [String, Number],
            default: null,
            validator: (value) => (isNumber(value) ? value > 0 && value < 7 : value.match(/(1|2|3|4|5|6)/)),
        },
    },
    computed: {
        hasSubtitle() {
            return !!this.$slots['sub-heading'];
        },
        headingClasses() {
            return [
                this.overrideStyleLevel ? `vs-heading--style-level-${this.overrideStyleLevel}` : '',
                {
                    'vs-heading--thin': this.thin,
                    'vs-heading--alternative': this.alternative,
                },
            ];
        },
        type() {
            return `h${this.level}`;
        },
    },
};
</script>

<style lang="scss">
@import "../../../assets/fonts/fonts.css";

$font-sizes: (
    1: $font-size-h1,
    2: $font-size-h2,
    3: $font-size-h3,
    4: $font-size-h4,
    5: $font-size-h5,
    6: $font-size-h6,
);

$md-font-sizes: (
    1: $font-size-h1-md,
    2: $font-size-h2-md,
    3: $font-size-h3-md,
    4: $font-size-h4,
    5: $font-size-h5-md,
    6: $font-size-h6-md,
);

/* Update font sizes */
$sub-font-sizes: (
    2: $font-size-h2-sub,
    3: $font-size-h3-sub,
);

.vs-heading {
    font-family: $headings-font-family;

    @each $level, $size in $font-sizes {
        @at-root h#{$level}#{&},
        &.vs-heading--style-level-#{$level} {
            letter-spacing: $size * 0.1;
            margin-bottom: $size;
            margin-top: $size;
            font-size: $size;

            @include media-breakpoint-up(md) {
                letter-spacing: $size * 0.1;
                margin-bottom: $size;
                margin-top: $size;
                font-size: $size;
            }
        }
    }

    @at-root h#{5}#{&}, h#{6}#{&},
    &.vs-heading--style-level-#{5},
    &.vs-heading--style-level-#{6}{
        font-family: $font-family-sans-serif;
        letter-spacing: normal;
        font-weight: $font-weight-bold;
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

    &--alternative {
        font-family: $font_family_sans_serif;
        font-weight: $font-weight-light;
        letter-spacing: normal;
    }
}
</style>
