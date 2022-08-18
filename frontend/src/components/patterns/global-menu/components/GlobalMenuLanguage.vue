<template>
    <VsDropdown
        variant="primary"
        class="vs-global-menu__languages"
    >
        <template #button-content>
            <VsIcon
                class="vs-global-menu__languages__icon"
                name="globe"
                variant="light"
                size="xxs"
                focusable="false"
            />
            <!-- Tablet/Desktop -->
            <span class="vs-global-menu__languages__text">{{ languageLabel }}</span>
            <span class="vs-global-menu__languages__selected">{{ selectedLanguage }}</span>
        </template>

        <!-- No JS Version -->
        <span
            class="vs-global-menu__languages__label"
            role="menuitem"
        >
            <VsIcon
                name="globe"
                variant="light"
                size="xxs"
                focusable="false"
            />
            {{ languageLabel }}
        </span>

        <!-- @slot Default slot for Global Menu Language Items  -->
        <slot />
    </VsDropdown>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
import VsDropdown from '@components/patterns/dropdown/Dropdown';
import cookieMixin from '../../../../mixins/cookieMixin';

/**
 * TODO: Document usage
 *
 * @displayName Global Menu Language
 */
export default {
    name: 'VsGlobalMenuLanguage',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsDropdown,
        VsIcon,
    },
    mixins: [
        cookieMixin,
    ],
    props: {
        /**
         * 'Language' label to be shown or translated before language name.
         * Example: "Language": English.
         */
        languageLabel: {
            type: String,
            default: 'Language',
        },
        /**
         * Language locale string.
         * Example: "en_GB"
         */
        language: {
            type: String,
            default: 'en',
        },
    },
    mounted() {
        if (!this.cookieExists('vs_locale')) {
            this.setCookie('vs_locale', this.localeCookie, true);
        };

        if (!this.cookieExists('googtrans')) {
            this.setCookie('googtrans', this.translationCookie, true);
        };
    },
};
</script>

<style lang="scss">
.vs-global-menu__languages {
    position: static;

    .dropdown-menu {
        width: 100%;
        background: $color-purple;
        font-size: $font-size-2;
        margin: 0;
        max-height: 0;
        overflow: hidden;
        opacity: $opacity-0;
        transform: translate3d(0px, 0px, 0px) !important;

        &.show {
            max-height: 700px;
            opacity: $opacity-100;
            transform: translate3d(0px, 28px, 0px) !important;
            border: none;
            padding: 0;

            @include media-breakpoint-up(lg) {
                transform: translate3d(0px, 34px, 0px) !important;
            }
        }

        @include media-breakpoint-up(lg) {
            width: 100%;
            min-width: 130px;
            transform: translate3d(0px, 0px, 0px) !important;
        }
    }

    .btn-primary.dropdown-toggle {
        padding: 0.3rem $spacer-4;
        font-size: $font-size-2;
        background: $color-purple;
        border: none;

        @include media-breakpoint-up(lg) {
            padding: $spacer-2 $spacer-4 $spacer-2 $spacer-3;
        }

        &:not(:disabled):not(.disabled):active {
            background: $color-purple-shade-2;
        }

        &:focus {
            box-shadow: $shadow-button-focus-dark inset;
        }

        &:hover {
            background: $color-purple-shade-2;
        }
    }

    &.show {
        .btn-primary.dropdown-toggle,
        .btn-primary.dropdown-toggle:active,
        .btn-primary.dropdown-toggle:focus {
            background: $color-purple-shade-2;
        }
    }

    @include media-breakpoint-up(lg) {
        position: relative;
    }

    &__icon, &__text {
        display: none;

        @include media-breakpoint-up(lg) {
            display: inline;
        }
    }

    &__text {
        padding-left: $spacer-1;
    }

    &__selected {
        @include media-breakpoint-up(lg) {
            padding-left: $spacer-1;
        }
    }

    &__label {
        display: none;
    }

}

@include no-js {
    .vs-global-menu__languages {
        display: block;
        width: 100%;

        .dropdown-menu {
            @extend .show;
            position: initial;
            display: flex;
            width: 100%;
            flex-wrap: wrap;
            transform: translate3d(0px, 0px, 0px) !important;
            text-align: center;
            max-height: none;
            border-top: 1px solid $color-white;
            padding: $spacer-3 0;

            @include media-breakpoint-up(md) {
                display: inline-flex;
                flex-wrap: wrap;
                text-align: left;
            }
        }

        &__label {
            display: inline;
            color: $color-white;
            padding: $spacer-3;
            line-height: $line-height-s;
            font-size: $font-size-4;
            margin: 0 auto;

            @include media-breakpoint-up(md) {
                padding: $spacer-4 $spacer-3;
                margin: 0;
            }

            @include media-breakpoint-up(lg) {
                padding: $spacer-4 $spacer-3 $spacer-4 0;
            }
        }
    }
}
</style>

<docs>
  ```jsx
    <VsGlobalMenuLanguage>
        <VsGlobalMenuLanguageItem language-name="English" language="en_EN" />
        <VsGlobalMenuLanguageItem language-name="Deutsch" language="de_DE" />
        <VsGlobalMenuLanguageItem language-name="Español" language="es_ES" />
        <VsGlobalMenuLanguageItem language-name="Français" language="fr_FR" />
        <VsGlobalMenuLanguageItem language-name="Italiano" language="it_IT" />
        <VsGlobalMenuLanguageItem language-name="Nederlands" language="nl_NL" />
    </VsGlobalMenuLanguage>
  ```
</docs>
