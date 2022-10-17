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
        @keyup.tab="tabbedIn"
    >
        <VsIcon
            v-if="icon"
            class="align-self-center"
            :name="icon"
            :class="iconClasses"
            :size="calcIconSize"
            :padding="0"
            :orientation="iconOrientation"
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
         * Set this for a button on a dark background for better colour contrast
         */
        onDark: {
            type: Boolean,
            default: false,
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
         * If the button contains an icon with accessible text.
         */
        iconWithText: {
            type: Boolean,
            default: false,
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
        };
    },
    computed: {
        buttonClasses() {
            return [
                {
                    'vs-button--animated': this.animate && !this.iconOnly && !this.iconWithText,
                    'vs-button--is-animating': this.isAnimating,
                    'vs-button--on-dark': this.onDark,
                    'vs-button--icon-only': this.iconOnly,
                    'vs-button--icon-with-text': this.iconWithText,
                    'd-flex': this.icon && !this.iconOnly && !this.iconWithText,
                    'flex-row-reverse': this.iconPosition === 'right',
                    'text-uppercase': this.uppercase && !this.iconWithText,
                },
            ];
        },
        iconClasses() {
            return [
                {
                    'vs-icon--right': this.iconPosition === 'right',
                    'vs-icon--left': this.iconPosition === 'left',
                },
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
    .vs-button{
        @extend %button-default-styles;

        .vs-icon {
            margin-top: -.05rem;

            &--right{
                margin-left: $spacer-2;
            }

            &--left{
                margin-right: $spacer-2;
            }
        }

        &:focus {
            @extend %primary-button-focus;
        }

        &.vs-button--on-dark{
            &:focus{
                box-shadow: 0 0 0 4px $color-theme-dark, 0 0 0 8px $color-yellow;
            }
        }

        &:not(:disabled):not(.disabled):active:focus {
            box-shadow: none;
        }

        &.btn-primary, &.btn-secondary,
        &.btn-dark, &.btn-light{
            &:disabled {
                background-color: $color-secondary-gray-tint-4;
                color: $color-white;
                opacity: $opacity-100;
                border-width: 0;

                &:hover{
                    background-color: $color-secondary-gray-tint-4;
                    border-color: $color-secondary-gray-tint-4;
                }
            }
        }

        /* Button Variants
        ------------------------------------------ */
        &.btn-primary{
            @include vs-button-variant(
                $color-white, $color-pink, $color-pink,
                $color-white, $color-pink-shade-2, $color-pink-shade-2,
                $color-pink, $color-white, $color-pink,
            );

            &.vs-button--on-dark{
                @include vs-button-variant(
                    $color-theme-dark, $color-yellow, $color-yellow,
                    $color-theme-dark, $color-yellow-tint-2, $color-yellow-tint-2,
                    $color-yellow, $color-theme-dark, $color-yellow,
                );
            }
        }

        &.btn-secondary {
            @include vs-button-variant(
                $color-pink, $color-white, $color-pink,
                $color-white, $color-pink, $color-pink,
                $color-white, $color-pink, $color-pink,
            );

            &.vs-button--on-dark{
                @include vs-button-variant(
                    $color-yellow, $color-theme-dark, $color-yellow,
                    $color-theme-dark, $color-yellow, $color-yellow,
                    $color-theme-dark, $color-yellow, $color-yellow,
                );
            }
        }

        &.btn-dark{
            @include vs-button-variant(
                $color-white, $color-theme-dark, $color-theme-dark,
                $color-white, $color-secondary-gray-shade-1, $color-secondary-gray-shade-1,
                $color-theme-dark, $color-white, $color-secondary-gray-shade-1,
            );

            &:focus{
                box-shadow: $shadow-button-focus-on-dark, 0 0 0 8px $color-theme-dark;
            }
        }

        &.btn-light{
            @include vs-button-variant(
                $color-gray-shade-7, $color-gray-tint-7, $color-gray-tint-7,
                $color-gray-shade-7, $color-gray-tint-6, $color-gray-tint-6,
                $color-white, $color-gray-shade-7, $color-gray-shade-7,
            );
        }

        &.btn-transparent{
            @include vs-button-variant(
                $color-gray-shade-7, transparent, transparent,
                $color-pink, transparent, transparent,
                $color-pink, transparent, transparent,
            );

            &:focus{
                box-shadow: $shadow-button-focus;
            }

            &.vs-button--on-dark{
                @include vs-button-variant(
                    $color-white, transparent, transparent,
                    $color-gray-tint-6, transparent, transparent,
                    $color-white, transparent, transparent,
                );

                &:focus{
                    box-shadow: $shadow-button-focus-on-dark;
                }
            }
        }

        &.vs-button--icon-with-text{
            letter-spacing: initial;
            text-decoration: underline;
            font-weight: $font-weight-normal;
            font-size: $font-size-3;
            transition: none;

            &.btn-sm, &.btn-md, &.btn-lg{
                padding: $spacer-1;
            }

            .vs-icon{
                display: block;
                margin: 0 auto $spacer-1;
            }
        }

        &.vs-button--icon-only {
            line-height: 1;

            &.btn-sm, &.btn-md, &.btn-lg{
                padding: $spacer-1;
            }

            .vs-icon{
                margin: 0;
            }
        }

        /* Button Sizes
        ------------------------------------------ */
        &.btn-sm{
            padding: $spacer-1 $spacer-4;
        }

        &.btn-md{
            padding: $spacer-3 $spacer-8;
        }

        &.btn-lg{
            padding: $spacer-4 $spacer-9;
        }

        /* Button Animation
        ------------------------------------------ */
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
