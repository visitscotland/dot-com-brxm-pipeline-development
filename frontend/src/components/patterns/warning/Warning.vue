<template>
    <div
        class="vs-warning"
        :class="{
            [`vs-warning--${variant}`]: true
        }"
        data-test="vs-warning"
    >
        <VsIcon
            class="vs-warning__icon"
            name="review"
            custom-colour="#FCCA1B"
            :size="iconSize"
        />
        <div>
            <p class="vs-warning__message">
                {{ warningMessage }}
            </p>
            <button
                v-if="showCookieLink"
                class="ot-sdk-show-settings vs-warning__cookie-trigger"
                id="ot-sdk-btn"
            >
                {{ cookieLinkText }}
            </button>
        </div>
    </div>
</template>
<script>

import VsIcon from '@components/elements/icon';

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
    },
    props: {
        /**
        * The warning message to display to the user
        */
        warningMessage: {
            type: String,
            required: true,
        },
        /**
        * If set to true, the cookie preference centre link is displayed
        */
        showCookieLink: {
            type: Boolean,
            default: false,
        },
        /**
        * Text for the link to the cookies preference centre.
        */
        cookieLinkText: {
            type: String,
            required: true,
        },
        variant: {
            type: String,
            default: 'normal',
            validator: (value) => value.match(
                /(small|normal|row)/,
            ),
        },
    },
    computed: {
        iconSize() {
            return this.variant === 'row' ? 'lg' : 'md';
        },
    },
};
</script>

<style lang="scss">
    .vs-warning {
        position: absolute;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        padding: $spacer-4;
        display: flex;
        flex-direction: column;
        align-items: center;
        text-align: center;
        justify-content: center;
        color: $color-white;

        &--normal {
            .vs-warning__icon {
                width: 4rem !important;
                height: 4rem !important;
                margin-bottom: 2rem;
            }
        }

        &--small {
            .vs-warning__icon {
                width: 3rem !important;
                height: 3rem !important;
                margin-bottom: 1rem;
            }
        }

        &--row {
            position: relative;
            flex-direction: row;
            align-items: flex-start;
            justify-content: flex-start;
            text-align: left;
            padding: $spacer-6;

            .vs-warning__icon {
                margin: 0 $spacer-8 0 0;
            }
        }

        &__message {
            margin-bottom: $spacer-0;
        }

        /** override OneTrust styles **/
        &__cookie-trigger {
            color: $color-yellow !important;
            font-weight: $font-weight-normal !important;
            background: transparent;
            border: none;
            box-shadow: none;

            &:focus {
                @extend %outline-link-focus-dark;
            }
        }

        @include media-breakpoint-up(sm) {
            &--row {
                padding: $spacer-5;
            }
        }

        @include media-breakpoint-up(md) {
            &--row {
                .vs-warning__icon {
                    margin: 0 $spacer-5 0 0;
                }
            }
        }
    }
</style>
