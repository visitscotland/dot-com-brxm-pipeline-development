<template>
    <div>
        <nav
            class="vs-mega-nav bg-white"
            aria-label="main nav"
            data-test="vs-mega-nav"
        >
            <VsContainer
                fluid="lg"
                class="h-100"
            >
                <VsRow
                    class="align-items-center h-100"
                >
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
                        md="6"
                        lg="7"
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
                        lg="2"
                        class="justify-content-end position-static d-flex h-100"
                    >
                        <VsSiteSearch
                            @toggleAction="toggleSearch"
                            :is-showing="showSearch"
                        >
                            {{ searchButtonText }}
                        </VsSiteSearch>

                        <VsMegaNavDropdown
                            @menuToggled="menuToggle"
                            :menu-toggle-alt-text="menuToggleAltText"
                            class="vs-mega-nav__menu__mobile d-lg-none"
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

        <VsSiteSearchForm
            v-show="showSearch"
            @toggleAction="toggleSearch"
            :is-showing="showSearch"
            :label-text="searchLabelText"
            :submit-button-text="searchButtonText"
            :clear-button-text="searchClearButtonText"
            :close-button-text="searchCloseButtonText"
        />
    </div>
</template>

<script>
import {
    VsCol, VsRow, VsContainer,
} from '@components/elements/grid';
import VsSvgLink from '@components/elements/svg-link/SvgLink';
import VsMegaNavDropdown from '@components/patterns/mega-nav/components/MegaNavDropdown';
import VsMegaNavTopMenu from '@components/patterns/mega-nav/components/MegaNavTopMenu';
import VsIcon from '@components/elements/icon/Icon';
import VsSiteSearch from '@components/patterns/site-search/SiteSearch';
import VsSiteSearchForm from '@components/patterns/site-search/components/SiteSearchForm';

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
        VsSiteSearch,
        VsSiteSearchForm,
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
        /**
         * Search button text
         */
        searchButtonText: {
            type: String,
            required: true,
        },
        /**
         * Text that renders in search form label (sr-only)
         * and input placeholder
         */
        searchLabelText: {
            type: String,
            default: 'What are you looking for?',
        },
        /**
         * Text that renders inside the clear button
         * on the search form (sr-only)
         */
        searchClearButtonText: {
            type: String,
            default: 'Clear form',
        },
        /**
         * Text that renders inside the close button on
         * the search form (sr-only)
         */
        searchCloseButtonText: {
            type: String,
            default: 'Close search form',
        },
    },
    data() {
        return {
            isOpen: false,
            showSearch: false,
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
        /**
         * Toggles site search box when event emitted from button
        */
        toggleSearch() {
            this.showSearch = !this.showSearch;
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
    height: 45px;
    box-shadow: 0 2px 6px 0px rgba(0, 0, 0, 0.16);

    @include media-breakpoint-up(lg) {
        height: 55px;
    }

    &__logo svg {
        width: 184px;
        height: 16px;
        vertical-align: top;
        margin-top: $spacer-1;

        @include media-breakpoint-up(lg) {
            margin-top: $spacer-2;
        }
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
