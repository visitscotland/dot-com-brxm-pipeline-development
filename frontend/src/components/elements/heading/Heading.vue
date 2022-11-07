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
.vs-heading {
    @extend %heading-default-styles;
}
</style>
