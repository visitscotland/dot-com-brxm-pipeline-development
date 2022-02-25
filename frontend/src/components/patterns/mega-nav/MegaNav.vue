<template>
    <nav
        class="vs-mega-nav bg-white"
        aria-label="main nav"
        data-test="vs-mega-nav"
    >
        <VsContainer fluid="lg">
            <VsRow class="align-items-center">
                <!-- Logo Link -->
                <VsCol
                    cols="8"
                    md="4"
                    lg="3"
                >
                    <VsSvgLink
                        class="vs-mega-nav__logo"
                        data-test="vs-mega-nav__logo"
                        link-alt-text="VisitScotland Home"
                        :href="href"
                        svg-fill="#700e57"
                        svg-path="visitscotland"
                    />
                </VsCol>

                <!-- Desktop Top Menu Toggles -->
                <VsCol
                    cols="4"
                    md="8"
                    lg="9"
                    class="vs-mega-nav__menu d-none d-lg-block"
                >
                    <VsMegaNavTopMenu>
                        <!-- @slot For top menu list items in navbar  -->
                        <slot name="megaNavTopMenuItems" />
                    </VsMegaNavTopMenu>
                </VsCol>

                <!-- Mobile Toggle and Menu -->
                <VsCol
                    cols="4"
                    md="8"
                    lg="9"
                    class="vs-mega-nav__menu__mobile
                    d-flex d-lg-none justify-content-end position-static"
                >
                    <VsMegaNavDropdown
                        @menuToggled="menuToggle"
                        :menu-toggle-alt-text="menuToggleAltText"
                    >
                        <template #buttonContent>
                            <span class="sr-only">
                                {{ menuToggleAltText }}
                            </span>
                            <VsIcon
                                v-if="isOpen"
                                name="close"
                                size="xs"
                                variant="dark"
                            />

                            <VsIcon
                                v-else
                                name="bars-mobile-menu"
                                size="md"
                                variant="dark"
                            />
                        </template>

                        <template #dropdownContent>
                            <!-- @slot For mobile list items  -->
                            <slot name="megaNavAccordionItems" />
                        </template>
                    </VsMegaNavDropdown>
                </VsCol>
            </VsRow>
        </VsContainer>
    </nav>
</template>

<script>
import {
    VsCol, VsRow, VsContainer,
} from '@components/elements/layout';
import VsSvgLink from '@components/elements/svg-link/SvgLink';
import VsMegaNavDropdown from '@components/patterns/mega-nav/components/MegaNavDropdown';
import VsMegaNavTopMenu from '@components/patterns/mega-nav/components/MegaNavTopMenu';
import VsIcon from '@components/elements/icon/Icon';

/**
 *  The Mega Nav bar component includes main VS logo and slots for
 *  top menu items on desktop and dropdown toggle with menu items for mobile
 *
 * @displayName Mega Navigation
 */
export default {
    name: 'VsMegaNav',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsCol,
        VsRow,
        VsContainer,
        VsSvgLink,
        VsMegaNavDropdown,
        VsMegaNavTopMenu,
        VsIcon,
    },
    props: {
        /**
         * The URL for the VS logo link
         */
        href: {
            type: String,
            required: true,
        },
        /**
         * Accessiblity alt text for the menu button
         */
        menuToggleAltText: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            isOpen: false,
        };
    },
    methods: {
        /**
         * Toggles dropdown menu property
         * @returns {Boolean} true if menu is open
        */
        menuToggle() {
            this.isOpen = !this.isOpen;
        },
    },
};
</script>

<style lang="scss">
@import '~bootstrap/scss/navbar';

.vs-mega-nav {
    position: relative;
    display: flex;
    align-items: center;
    height: 55px;
    box-shadow: 0 2px 6px 0px rgba(0, 0, 0, 0.16);

    &__logo svg {
        width: 184px;
        height: 20px;
    }

    .vs-mega-nav__menu {
        position: static;
    }
}

@include no-js {
    .vs-mega-nav {
        box-shadow: none;
        height: auto;
    }

    .vs-mega-nav__menu__mobile {
        display: none!important;
    }

    .vs-mega-nav__menu{
        display: block!important;
        width: 100%;
        max-width: 100%;
        flex: 0 0 100%;
        position: relative;
    }
}
</style>

<docs>
    ```[import](./__examples__/meganav.example.vue)
    ```
</docs>
