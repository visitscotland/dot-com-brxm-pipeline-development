<template>
    <VsDropdown
        :text="dropdownLabel"
        variant="primary"
        class="vs-global-menu__dropdown"
    >
        <VsDropdownItem
            v-for="site in websites"
            :key="site.siteName"
            :href="site.siteUrl"
            :class="
                (site.siteUrl == activeSite)
                    ? 'd-none'
                    : ''
            "
            :target="
                (site.siteUrl == activeSite)
                    ? '_self'
                    : '_blank'
            "
            role="menuitem"
        >
            {{ site.siteName }}
        </VsDropdownItem>
    </VsDropdown>
</template>

<script>
import VsDropdown from '@components/patterns/dropdown/Dropdown';
import VsDropdownItem from '@components/patterns/dropdown/components/DropdownItem';

/**
 * This component is the dropdown used inside the Global Menu component.
 * It holds the list of websites to be shown on smaller devices.
 *
 * @displayName Global Menu Dropdown
 */

export default {
    name: 'VsGlobalMenuDropdown',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsDropdown,
        VsDropdownItem,
    },
    props: {
        /**
         * Determines the active website
         */
        activeSite: {
            type: String,
            required: true,
        },
        /**
         * Translation text for the "Our Websites" label
         */
        dropdownLabel: {
            type: String,
            default: 'Our Websites',
        },
        /**
         * List of websites to be rendered
         */
        websites: {
            type: Array,
            required: true,
        },
    },
};
</script>

<style lang="scss">
.vs-global-menu__dropdown,
.vs-global-menu__dropdown .btn {
    padding: 0;
    background: transparent;
    border: none;
}

.vs-global-menu__dropdown {
    position: initial;
    flex: auto;

    a {
        color: white;
        text-decoration: none;

        &:hover {
            background: $color-purple-shade-2;
        }
    }

    .btn-primary.dropdown-toggle {
        padding: 0.3rem $spacer-3;
        font-size: $font-size-2;
        align-items: center;

        &:not(:disabled):not(.disabled):active {
            background: $color-purple-shade-2;
        }

        &:focus {
            box-shadow: $shadow-button-focus inset;
        }
    }

    &.show{
        .btn-primary.dropdown-toggle,
        .btn-primary.dropdown-toggle:active,
        .btn-primary.dropdown-toggle:focus {
            background: $color-purple-shade-2;
        }

        .btn-primary.dropdown-toggle:focus {
            box-shadow: $shadow-button-focus inset;
        }
    }

    ul:focus {
        outline: 3px solid $color-pink-tint-5;
        outline-offset: -3px;
    }

    .dropdown-menu {
        min-width: auto;
        width: 100%;
        background: $color-purple;
        max-height: 0;
        overflow: hidden;
        opacity: $opacity-0;
        transform: translate3d(0px, 0px, 0px);

        li {
            border-bottom: 1px solid $color-purple-tint-3;

            &:last-of-type {
                border: none;
            }

            a {
                padding: $spacer-3;
                color: $color-white;
                text-decoration: none;
                font-size: $font-size-2;
                line-height: $line-height-s;

                &:hover, &:focus {
                    background: $color-purple-shade-2;
                }

                &:focus {
                    outline: 3px solid $color-pink-tint-5;
                    outline-offset: -3px;
                }
            }
        }

        &.show {
            max-height: 500px;
            opacity: $opacity-100;
            transform: translate3d(0px, 26px, 0px) !important;
            border: none;
            padding: 0;
        }
    }

    @include no-js {
        display: none;
    }
}
</style>

<docs>
  ```jsx
    <VsGlobalMenu
        dropdown-label="Our websites"
        active-site="https://www.visitscotland.com/"
    >
        <template
            slot="third-menu-item"
            style="min-width: 50px;"
        >
            EN
        </template>
    </VsGlobalMenu>
  ```
</docs>
