<template>
    <VsList
        unstyled
        inline
        class="
            vs-global-menu__list
        "
    >
        <li
            v-for="site in websites"
            :key="site.siteName"
            class="vs-global-menu__list_item"
            :class="[
                (site.siteUrl == activeSite)
                    ? 'vs-global-menu__list_item--active d-none d-lg-block'
                    : ''
            ]"
        >
            <a
                :target="
                    (site.siteUrl == activeSite)
                        ? '_self'
                        : '_blank'
                "
                :href="site.siteUrl"
            >{{ site.siteName }}</a>
        </li>
    </VsList>
</template>

<script>
import VsList from '@components/elements/list';

/**
 * This component is the list used inside the Global Menu component.
 * It holds the list of websites to be shown on bigger devices.
 *
 * @displayName Global Menu List
 */
export default {
    name: 'VsGlobalMenuList',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsList,
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
.vs-global-menu__list {
    position: initial;
    flex: auto;

    a {
        color: white;
        text-decoration: none;

        &:hover {
            background: $color-purple-shade-2;
        }
    }

    &_item {
        a {
            padding: 0 $spacer-5;
            line-height: 35px;
            height: 35px;
            display: inline-block;

            &:focus {
                outline: 3px solid $color-pink-tint-5;
                outline-offset: -3px;
            }

            &:active {
                background: white;
                color: $color-purple-shade-2;
                outline: none;
            }
        }

        &--active {
            a {
                background: $color-white;
                color: $color-purple-shade-2;

                &:hover {
                    color: white;
                }
                &:active {
                    background: $color-purple-shade-2;
                }
            }
        }
    }
}

@include no-js {
    .vs-global-menu__list {
        width: 100%;
        flex-wrap: wrap;
        display: flex !important;

        @include media-breakpoint-up(md) {
            flex-wrap: nowrap;
        }

        &_item {
            display: block;
            flex-basis: 100%;

            @include media-breakpoint-up(md) {
                flex-basis: auto;
                border: none;
                margin: 0 $spacer-2;
            }

            &:first-of-type{
                margin-left: 0;
            }

            &:last-of-type{
                margin-right: 0;
            }

            a {
                max-height: none;
                display: block;
                text-align: center;
                font-size: $font-size-4;
                padding: $spacer-5;
                line-height: $line-height-standard;
            }
        }
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
