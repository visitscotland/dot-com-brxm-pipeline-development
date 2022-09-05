<template>
    <div>
        <nav
            class="vs-mega-nav bg-white"
            aria-label="main nav"
            data-test="vs-mega-nav"
            id="main-nav"
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
                        class="justify-content-end align-items-center position-static d-flex h-100"
                    >
                        <VsSiteSearch
                            @toggleAction="toggleSearch"
                            :is-showing="showSearch"
                        >
                            {{ searchButtonText }}
                        </VsSiteSearch>

                        <div
                            class="vs-mega-nav__menu-mobile d-lg-none"
                            id="vs-mega-nav__menu-mobile"
                            data-test="vs-mega-nav-mobile-container"
                        >
                            <VsButton
                                class="vs-mega-nav__mobile-menu-toggle p-0"
                                :size="isOpen ? 'sm' : 'lg'"
                                icon-only
                                :icon="isOpen ? 'close' : 'bars-mobile-menu'"
                                variant="transparent"
                                @click.native="menuToggle()"
                                ref="toggleButton"
                                aria-haspopup="true"
                            >
                                <span class="sr-only">
                                    {{ menuToggleAltText }}
                                </span>
                            </VsButton>

                            <VsMegaNavMobileMenu
                                v-if="isOpen"
                                v-click-outside="closeMenu"
                            >
                                <slot name="megaNavAccordionItems" />
                            </VsMegaNavMobileMenu>
                        </div>
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
import VsMegaNavTopMenu from '@components/patterns/mega-nav/components/MegaNavTopMenu';
import VsMegaNavMobileMenu from '@components/patterns/mega-nav/components/MegaNavMobileMenu';
import VsButton from '@components/elements/button/Button';
import VsSiteSearch from '@components/patterns/site-search/SiteSearch';
import VsSiteSearchForm from '@components/patterns/site-search/components/SiteSearchForm';
import Vue from 'vue';

Vue.directive('click-outside', {
    bind(el, binding, vnode) {
        /* eslint-disable-next-line */
        el.clickOutsideEvent = (event) => {
            if (!(el === event.target || el.contains(event.target))) {
                vnode.context[binding.expression](event);
            }
        };
        document.body.addEventListener('click', el.clickOutsideEvent);
    },
    unbind(el) {
        document.body.removeEventListener('click', el.clickOutsideEvent);
    },
});

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
        VsMegaNavTopMenu,
        VsMegaNavMobileMenu,
        VsSiteSearch,
        VsSiteSearchForm,
        VsButton,
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
    mounted() {
        this.addTabClose();
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
        /**
         * Closes the menu as long as the open button hasn't just been clicked
         */
        closeMenu(event) {
            const mobileNavContainer = document.getElementsByClassName('vs-mega-nav__menu-mobile')[0];

            if (this.isOpen && !mobileNavContainer.contains(event.target)) {
                this.isOpen = false;
            }
        },
        /**
         * adds event listener for tabbing out of mobile menu
        */
        addTabClose() {
            document.body.addEventListener('focusin', (event) => {
                if (this.isOpen) {
                    const theTarget = event.target;
                    if (!theTarget.closest('#vs-mega-nav__menu-mobile')) {
                        this.isOpen = false;
                    }
                }
            });
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

    .vs-mega-nav__menu-mobile {
        margin-left: .5rem;
    }

    &__mobile-menu-toggle {
        position: relative;
        letter-spacing: 0;
        font-weight: $font-weight-normal;
        line-height: $line-height-standard;
        border-radius: 0;
        border: 0;
        height: $spacer-7;
        width: $spacer-7;
        font-size: 0;
        padding: 0;
    }

    &__mobile-menu {
        display: none
    }
}

@include no-js {
    .vs-mega-nav {
        box-shadow: none;
        height: auto;
    }

    .vs-mega-nav__menu-mobile {
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
