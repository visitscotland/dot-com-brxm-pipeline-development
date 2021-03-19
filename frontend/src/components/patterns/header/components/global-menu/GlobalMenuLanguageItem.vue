<template>
    <VsDropdownItem
        class="vs-global-menu__languages__item"
        :href="languageLink"
    >
        {{ languageName }}
    </VsDropdownItem>
</template>

<script>
import VsDropdownItem from '../../../dropdown/components/DropdownItem';

/**
 * TODO: Document usage
 *
 * @displayName Global Menu Language Item
 */
export default {
    name: 'VsGlobalMenuLanguageItem',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsDropdownItem,
    },
    props: {
        languageName: {
            type: String,
            default: null,
        },
        languageLink: {
            type: String,
            default: null,
        },
    },
};
</script>

<style lang="scss">
.vs-global-menu__languages__item {
    &:not(:last-of-type) {
        border-bottom: 1px solid $color-purple-tint-3;
    }

    .dropdown-item {
        padding: $spacer-3;
        color: white;
        text-decoration: none;
        line-height: $line-height-s;

        &:hover {
            background: $color-purple-shade-2;
        }

        &:focus {
            outline: 3px solid $color-purple-tint-5;
            outline-offset: -3px;
            background: $color-purple;
        }
    }
}

@include no-js {
    .vs-global-menu__languages__item {
        width: 100%;
        border: none;

        @include media-breakpoint-up(md) {
            width: auto;
            padding: $spacer-3;
        }

        .dropdown-item {
            color: $color-white;
            text-decoration: underline;
            font-size: $h4-font-size;

            &:hover {
                background:transparent;
                text-decoration: none;
            }

            @include media-breakpoint-up(md) {
                padding: $spacer-1 $spacer-2;
            }

            @include media-breakpoint-up(lg) {
                padding: $spacer-1 $spacer-4;
            }
        }
    }
}
</style>

<docs>
  ```jsx
    <VsGlobalMenuLanguageItem language-name="English" languageLink="site/"/>
  ```
</docs>
