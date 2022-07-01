<template>
    <BButton
        :variant="variant"
        :href="href"
        :tabindex="tabindex"
        class="vs-button"
        :class="buttonClasses"
        :size="size"
        v-bind="$attrs"
        @click="animateHandler"
        @mouseover="hovered = true"
        @focusin="hovered = true"
        @keyup.tab="tabbedIn"
        @mouseleave="hovered = false"
        @focusout="hovered = false"
    >
        <VsIcon
            v-if="icon"
            class="align-self-center"
            :name="icon"
            :size="iconSizeOverride || calcIconSize"
            :padding="0"
            :orientation="iconOrientation"
            :variant="iconVariantOverride || calcIconVariant"
        />
        <!-- @slot The button content goes here -->
        <slot />
    </BButton>
</template>
<script>
import { BButton } from 'bootstrap-vue';
import VsIcon from '@components/elements/icon/Icon';

/**
 * Buttons are used to let users carry out actions on
 * the page like toggle a modal or submit a form.
 *
 * @displayName Button
 */

export default {
    name: 'VsButton',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BButton,
        VsIcon,
    },
    props: {
        /**
         * Use this option to render the button as an anchor element with the given href.
         */
        href: {
            type: String,
            default: null,
        },
        /**
         * Tab index value - this is needed as tabindex attribute is sometimes stripped
         * from the button on first update with nested components.
         */
        tabindex: {
            type: String,
            default: null,
        },
        /**
         * Style variation to give additional meaning
         * `primary|secondary|transparent|dark|light`.
         */
        variant: {
            type: String,
            default: 'primary',
            validator: (value) => value.match(
                /(primary|secondary|transparent|dark|light)/,
            ),
        },
        /**
         * Background property used primarily for overrides on transparent variant
         * `white`.
         */
        background: {
            type: String,
            default: null,
            validator: (value) => value.match(/(white)/),
        },
        /**
         * Size of the button
         * `sm|md|lg`.
         */
        size: {
            type: String,
            default: 'md',
            validator: (value) => value.match(/(sm|md|lg)/),
        },
        /**
         * By default, buttons have an animation behaviour on click.
         * Pass `false` to disable.
         */
        animate: {
            type: Boolean,
            default: true,
        },
        /**
         * By default, button text is uppercase. Pass `false` to disable.
         */
        uppercase: {
            type: Boolean,
            default: true,
        },
        /**
         * Pass the name of the icon to add it to the button.
         */
        icon: {
            type: String,
            default: '',
        },
        /**
         * The icon orientation
         * `up|down|left|right`.
         */
        iconOrientation: {
            type: String,
            default: null,
            validator: (value) => value.match(/(up|down|left|right)/),
        },
        /**
         * If the button contains an icon only with no text.
         */
        iconOnly: {
            type: Boolean,
            default: false,
        },
        /**
         * Icon color is automatically set by the Button component, however if
         * needed for an edge case, this can be overriden here.
         * `primary|secondary|light|dark|color-white|secondary-teal`
         */
        iconVariantOverride: {
            type: String,
            default: null,
            validator: (value) => value.match(
                /(primary|secondary|light|dark|color-white|secondary-teal)/,
            ),
        },
        /**
         * Icon size is automatically set by the Button component, however if
         * needed for an edge case, this can be overriden here.
         * `xxs|xs|sm|md|lg|xl`
         */
        iconSizeOverride: {
            type: String,
            default: null,
            validator: (value) => value.match(/(xxs|xs|sm|md|lg|xl)/),
        },
        /**
         * The position of the icon
         * `left|right`
         */
        iconPosition: {
            type: String,
            default: 'left',
            validator: (value) => value.match(/(left|right)/),
        },
    },
    data() {
        return {
            isAnimating: false,
            hovered: false,
        };
    },
    computed: {
        buttonClasses() {
            return [
                {
                    'vs-button--animated': this.animate,
                    'vs-button--is-animating': this.isAnimating,
                    'vs-button--icon-only': this.iconOnly,
                    'd-flex': this.icon && !this.iconOnly,
                    'flex-row-reverse': this.iconPosition === 'right',
                },
                this.background ? [`btn-bg-${this.background}`] : '',
                this.uppercase ? 'text-uppercase' : '',
            ];
        },
        calcIconSize() {
            switch (this.size) {
            case 'sm':
                return 'xs';
            case 'md':
                return 'sm';
            case 'lg':
                return 'md';
            default:
                return 'md';
            }
        },
        calcIconVariant() {
            if (this.isOutline) {
                if (this.hovered) {
                    return 'light';
                }

                return this.outlineColour;
            }

            if (this.variant === 'secondary' || this.variant === 'light') {
                return 'dark';
            }

            if (this.variant === 'transparent') {
                return 'primary';
            }

            if (this.variant === 'light') {
                return 'dark';
            }

            return 'light';
        },
        isOutline() {
            return this.variant.match(/outline/) !== null;
        },
        outlineColour() {
            return this.variant.replace('outline-', '');
        },
    },
    methods: {
        animateHandler() {
            this.isAnimating = true;
            setTimeout(() => {
                this.isAnimating = false;
            }, 1000);
        },
        tabbedIn(event) {
            // provides option for parent component to listen to emitted event
            // when button is tabbed into
            this.$emit('btnFocus', event);
        },
    },
};
</script>

<style lang="scss">
    .vs-button.btn {
        font-family: $font-family-base;
        font-weight: $font-weight-light;
        transition: $transition-base;
        text-decoration: none;
        letter-spacing: $letter-spacing-m;
        position: relative;
        overflow: hidden;

        .vs-icon {
            margin-top: -.05em;
            margin-right: $spacer-2;
        }

        &:focus {
            box-shadow: $shadow-button-focus;
            background-color: $color-white;
            color: $color-theme-primary;

            .vs-icon {
                fill: $color-theme-primary;
            }
        }

        &.btn-outline-primary {
            &:focus {
                background-color: $color-theme-primary;
                color: $color-white;

                .vs-icon {
                    fill: $color-white;
                }
            }
        }

        &.btn-secondary {
            color: $color-black;
            background-color: $color-yellow;
            border-color: $color-yellow;

            &:not(:disabled) {
                &:hover {
                    background-color: darken($color-yellow, 10%);
                    border-color: darken($color-yellow, 12%);
                }

                &:active {
                    color: $color-black;
                }

                &:focus {
                    color: $color-yellow;
                    border-color: $color-yellow;
                    background-color: transparent;

                    .vs-icon {
                        fill: $color-yellow;
                    }
                }
            }
        }

        &.btn-outline-secondary {
            color: $color-yellow;
            border-color: $color-yellow;

            .vs-icon {
                fill: $color-yellow;
            }

            &:hover, &:focus {
                color: $color-black;
                background-color: $color-yellow;
                border-color: $color-yellow;

                .vs-icon {
                    fill: $color-black;
                }
            }
        }

        &.btn-dark {
            &:hover {
                background-color: $color-gray-shade-5;
            }
        }

        &.btn-light,
        &.btn-transparent {
            &::after {
                background: rgba(187, 38, 132, 0.3);
            }
        }

        &.btn-bg-white:not(:hover) {
            background-color: $color-white;
        }

        // This is to match bootstrap specificity, otherwise it forces
        // a pink shadow on primary buttons when active + focussing where we want
        // no shadow
        &:not(:disabled):not(.disabled):active:focus {
            box-shadow: none;
        }

        &:disabled {
            background-color: $color-secondary-gray-tint-4;
            color: $color-white;
            opacity: $opacity-100;
            border-width: 0;
        }

        &.vs-button--icon-only {
            padding: $spacer-1;
            line-height: 1;

            .vs-icon{
                margin-right: 0;
            }
        }

        &.vs-button--animated {
            @keyframes bubble {
                0% {
                    transform: scale(0, 0);
                    opacity: $opacity-100;
                }
                100% {
                    opacity: $opacity-0;
                    transform: scale(100, 100);
                }
            }

            &::after {
                background: rgba(255, 255, 255, 0.1);
                border-radius: 50%;
                bottom: 0;
                content: "";
                height: 5px;
                opacity: $opacity-0;
                position: absolute;
                right: 0;
                transform-origin: 50% 50%;
                transform: scale(1, 1) translate(-50%);
                width: 5px;
            }

            &.vs-button--is-animating::after {
                animation: bubble 500ms ease-in-out;
            }
        }
    }
</style>
