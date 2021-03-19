<template>
    <li
        class="vs-mega-nav-top-menu-item"
        data-test="vs-mega-nav-top-menu-item"
        ref="menuToggle"
    >
        <VsMegaNavDropdown
            menu-toggle-alt-text="Toggle Menu"
        >
            <template #buttonContent>
                <!-- @slot For top menu item button content -->
                <slot name="buttonContent" />
            </template>

            <template #ctaLink>
                <VsLink
                    v-if="href && ctaText"
                    :href="href"
                    role="menuitem"
                    data-test="vs-mega-nav-top-menu-item__cta-link"
                    class="vs-mega-nav-top-menu-item__cta-link"
                >
                    {{ ctaText }}
                </VsLink>
                <hr class="vs-mega-nav-top-menu-item__divider">
            </template>

            <template #dropdownContent>
                <!-- @slot Slot for dropdown menu list content -->
                <div class="vs-mega-nav-top-menu-item__columns-wrapper">
                    <slot name="dropdownContent" />
                </div>
            </template>
        </VsMegaNavDropdown>
    </li>
</template>

<script>
import VsMegaNavDropdown from '@components/patterns/header/components/mega-nav/components/MegaNavDropdown';
import VsLink from '@components/elements/link/Link';

/**
 *  Mega nav top level menu items with a slots for toggle button and dropdown content
 *
 * @displayName MegaNav Top Menu Item
 */
export default {
    name: 'VsMegaNavTopMenuItem',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsMegaNavDropdown,
        VsLink,
    },
    props: {
        /**
         * The URL for the top level CTA link
         */
        href: {
            type: String,
            default: '',
        },
        /**
         * The text to display for the CTA link
         */
        ctaText: {
            type: String,
            default: '',
        },
    },
};
</script>

<style lang="scss">

.vs-mega-nav-top-menu-item{
    @include media-breakpoint-up(xl) {
        margin-right: $spacer-6;

        &:last-of-type {
            margin-right: 0;
        }
    }

    &__cta-link{
        text-decoration: none;
        padding: 0.12rem $spacer-5;
        transition: 0.2s color;

        &:hover{
            color: $color-secondary-gray-shade-3;
        }
    }

    &__divider{
        margin: $spacer-3 0 $spacer-4;
        border-color: $color-gray-tint-6;
    }

    &__columns-wrapper{
        display: flex;
        width: 100%;
        flex-flow: column wrap;
        height: 515px;
        overflow: hidden;
        align-content: flex-start;
    }
}

@include no-js {
    .vs-mega-nav-top-menu-item{
        &__divider {
            margin-bottom: $spacer-4;
        }
        &__columns-wrapper{
            display: block;
            height: auto;
            padding-left: $spacer-8;

            @include media-breakpoint-up(lg) {
                padding-left: $spacer-10;
            }
        }
    }
}
</style>

<docs>
    ```[import](../__examples__/meganav.example.vue)
    ```
</docs>
