<template>
    <VsDropdown class="vs-global-menu__languages">
        <template #button-content>
            <VsIcon
                class="vs-global-menu__languages__icon"
                name="globe"
                variant="light"
                size="xxs"
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
            />
            {{ languageLabel }}
        </span>

        <!-- @slot Default slot for Global Menu Language Items  -->
        <slot />
    </VsDropdown>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
import VsDropdown from '../../../dropdown/Dropdown';

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
    props: {
        languageLabel: {
            type: String,
            default: 'Language',
        },
        currentLocale: {
            type: String,
            default: 'en_GB',
        },
    },
    computed: {
        selectedLanguage() {
            return this.currentLocale.substr(0, 2).toUpperCase();
        },
        localeCookie() {
            return this.currentLocale.replace('_', '-').toLowerCase();
        },
        translationCookie() {
            return `/en/${this.currentLocale.substr(0, 2).toLowerCase()}`;
        },
    },
    mounted() {
        const expiryDate = new Date();
        expiryDate.setFullYear(expiryDate.getFullYear() + 1);
        document.cookie = `vs_locale=${this.localeCookie}; expires=${expiryDate}`;
        document.cookie = `googtrans=${this.translationCookie}; expires=${expiryDate}`;
    },
};
</script>

<style lang="scss">
.vs-global-menu__languages {
    position: static;

    .dropdown-menu {
        width: 100%;
        background: $color-purple;
        font-size: $font-size-sm;
        margin: 0;
        max-height: 0;
        display: block;
        overflow: hidden;
        opacity: 0;
        transform: translate3d(0px, 0px, 0px) !important;

        &.show {
            max-height: 700px;
            opacity: 1;
            transform: translate3d(0px, 28px, 0px) !important;
            border: none;
            padding: 0;

            @include media-breakpoint-up(lg) {
                transform: translate3d(0px, 34px, 0px) !important;
            }
        }

        @include media-breakpoint-up(lg) {
            width: auto;
            min-width: 130px;
            transform: translate3d(0px, 0px, 0px) !important;
        }
    }

    .dropdown-toggle {
        padding: 0.3rem $spacer-4;
        font-size: $font-size-sm;
        background: $color-purple;
        border: none;
        max-width: 130px;

        @include media-breakpoint-up(lg) {
            padding: 0.3rem $spacer-2;
        }

        &-secondary:not(:disabled):not(.disabled):active {
            background: $color-purple-shade-2;
        }

        &:focus {
            outline: 3px solid $color-purple-tint-5;
            outline-offset: -3px;
            box-shadow: none !important;
        }

        &:hover {
            background: $color-purple-shade-2;
        }
    }

    &.show {
        .dropdown-toggle,
        .dropdown-toggle:active,
        .dropdown-toggle:focus {
            background: $color-purple-shade-2;
        }
    }

    @include media-breakpoint-up(lg) {
        position: relative;
    }

    .dropdown-toggle {
        @include media-breakpoint-up(lg) {
            padding: $spacer-2 $spacer-3;
        }
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
            font-size: $h4-font-size;
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
        <VsGlobalMenuLanguageItem language-name="English" />
        <VsGlobalMenuLanguageItem language-name="Deutsch" />
        <VsGlobalMenuLanguageItem language-name="Español" />
        <VsGlobalMenuLanguageItem language-name="Français" />
        <VsGlobalMenuLanguageItem language-name="Italiano" />
        <VsGlobalMenuLanguageItem language-name="Nederlands" />
    </VsGlobalMenuLanguage>
  ```
</docs>
