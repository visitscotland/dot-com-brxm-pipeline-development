<template>
    <div
        class="vs-warning"
        :class="[warningClasses, transparent ? 'vs-warning--transparent' : '']"
        data-test="vs-warning"
    >
        <div class="vs-warning__content">
            <VsIcon
                class="vs-warning__icon"
                :name="icon"
                v-bind="iconAttrs"
            />

            <div>
                <p>
                    <slot />
                </p>

                <p
                    v-if="!!this.$slots['extra-content']"
                >
                    <slot name="extra-content" />
                </p>
            </div>
        </div>

        <VsButton
            v-bind="btnAttrs"
            variant="secondary"
            v-if="!!this.$slots['button-text']"
            class="vs-warning__button"
        >
            <slot name="button-text" />
        </VsButton>
    </div>
</template>
<script>

import VsIcon from '@components/elements/icon';
import VsButton from '@components/elements/button';

/**
 * A generic warning component that expands to cover whatever component
 * contains it
 *
 * @displayName Warning
 */
export default {
    name: 'VsWarning',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsIcon,
        VsButton,
    },
    props: {
        /**
        * The warning message to display to the user
        */
        icon: {
            type: String,
            default: 'review',
        },
        /**
        * Type of warning
        */
        type: {
            type: String,
            default: 'normal',
            validator: (value) => value.match(
                /(normal|cookie)/,
            ),
        },
        /**
        * Color theme - can be `light`, `dark` or `transparent`
        */
        theme: {
            type: String,
            default: 'dark',
            validator: (value) => value.match(
                /(light|dark)/,
            ),
        },
        /**
        * Message size - can be `small` or `normal`
        */
        size: {
            type: String,
            default: 'normal',
            validator: (value) => value.match(
                /(xs|small|normal)/,
            ),
        },
        /**
        * Alignment of message - can be `left` or `right`
        */
        align: {
            type: String,
            default: 'left',
            validator: (value) => value.match(
                /(left|right)/,
            ),
        },
        /**
        * Whether the background should be semi-transparent
        */
        transparent: {
            type: Boolean,
            default: true,
        },
    },
    computed: {
        warningClasses() {
            return [
                `vs-warning--${this.theme}`,
                `vs-warning--${this.size}`,
                `vs-warning--${this.align}`,
            ];
        },
        btnAttrs() {
            const attrsObj = {
            };
            if (this.type === 'cookie') {
                attrsObj.class = 'ot-sdk-show-settings vs-warning__cookie-trigger';
            }
            if (this.theme === 'dark') {
                attrsObj.onDark = '';
            }
            if (this.size === 'small') {
                attrsObj.size = 'sm';
            }

            return attrsObj;
        },
        iconAttrs() {
            const iconAttrs = {
            };

            if (this.theme === 'dark') {
                iconAttrs.customColour = '#FCCA1B';
            } else {
                iconAttrs.variant = 'primary';
            }

            if (this.size === 'small') {
                iconAttrs.size = 'md';
            } else {
                iconAttrs.size = 'lg';
            }

            return iconAttrs;
        },
    },
};
</script>

<style lang="scss">
    .vs-warning {
        position: relative;
        display: flex;
        align-items: flex-end;
        justify-content: flex-end;
        flex-direction: column;
        text-align: left;
        padding: $spacer-5;
        background: $color-gray-tint-7;
        height: 100%;
        line-height: 1.1;

        @include media-breakpoint-up(lg) {
            padding: $spacer-9;
        }

        &--left {
            align-items: flex-start;
        }

        &--small {
            padding: $spacer-5;

            .vs-warning__content {
                max-width: 100%;

                @include media-breakpoint-up(lg) {
                    & > div p:first-of-type {
                        font-size: $font-size-5;
                    }
                }
            }

            .vs-warning__button {
                margin-top: $spacer-3;
            }
        }

        &--dark {
            color: $color-white;
            background: $color-gray-shade-6;

            &.vs-warning--transparent {
                background: rgba(0,0,0,0.8);
            }
        }

        &--right {
            justify-content: flex-end;

            .vs-warning__button {
                display: flex;
                margin-left: auto;
            }
        }

        &__icon {
            margin-right: $spacer-3;
        }

        &__button {
            margin-top: $spacer-6;

            @include media-breakpoint-up(lg) {
                margin-top: $spacer-9;
            }
        }

        &__content {
            display: flex;
            // flex-direction: row;
            align-items: flex-start;
            justify-content: flex-start;
            max-width: 80%;
            line-height: 1.5;

            & > div p:last-of-type {
                margin-bottom: 0;
                font-size: $font-size-5;
            }

            & > div p:first-of-type {
                font-size: $font-size-5;

                @include media-breakpoint-up(lg) {
                    font-size: $font-size-6;
                }
            }

            &--xs {
                .vs-warning__icon {
                    width: 2rem !important;
                    height: 2rem !important;
                    margin-bottom: $spacer-4;
                }
            }
        }
    }
</style>
