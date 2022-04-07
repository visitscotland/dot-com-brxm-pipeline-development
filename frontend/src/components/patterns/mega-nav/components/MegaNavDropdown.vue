<template>
    <div
        class="vs-mega-nav-dropdown"
        data-test="vs-mega-nav-dropdown"
    >
        <BDropdown
            variant="transparent"
            ref="dropdown"
        >
            <template #button-content>
                <!-- @slot For dropdown toggle button content  -->
                <slot name="buttonContent" />
            </template>

            <li role="menuitem">
                <VsContainer
                    fluid="lg"
                    class="px-0 px-lg-3"
                >
                    <VsRow class="no-gutters">
                        <VsCol cols="12">
                            <!-- @slot Used to display the top menu link
                            at the top of the dropdown menu  -->
                            <slot name="ctaLink" />

                            <!-- @slot The rest of the mega nav links put
                            here in the dropdown menu  -->
                            <slot name="dropdownContent" />

                            <VsButton
                                class="vs-mega-nav-dropdown__close-btn
                                d-none d-lg-block position-absolute"
                                icon="close"
                                icon-only
                                icon-variant-override="dark"
                                size="sm"
                                variant="transparent"
                                @click.native="closeMenu"
                            >
                                <span class="sr-only">
                                    {{ menuToggleAltText }}
                                </span>
                            </VsButton>
                        </VsCol>
                    </VsRow>
                </VsContainer>
            </li>
        </BDropdown>
    </div>
</template>

<script>
import {
    VsCol,
    VsRow,
    VsContainer,
} from '@components/elements/grid';
import { BDropdown } from 'bootstrap-vue';
import VsButton from '@components/elements/button/Button';

/**
 *  This component includes a slot for toggle button content
 *  and slots for the mega nav dropdown menu content
 *
 * @displayName MegaNav Dropdown
 */
export default {
    name: 'VsMegaNavDropdown',
    status: 'prototype',
    release: '0.1.0',
    components: {
        BDropdown,
        VsCol,
        VsContainer,
        VsRow,
        VsButton,
    },
    props: {
        /**
         * Accessiblity alt text for the menu button
         */
        menuToggleAltText: {
            type: String,
            required: true,
        },
    },
    mounted() {
        // Listen for dropdown opening and closing and emit event
        this.$root.$on('bv::dropdown::show', () => {
            /**
             * Triggers when the dropdown is about to show
             */
            this.$emit('menuToggled');
        });

        this.$root.$on('bv::dropdown::hide', () => {
            /**
             * Triggers when the dropdown is about to close
             */
            this.$emit('menuToggled');
        });

        // Close menu on resize screen to fix toggle btn issues
        window.addEventListener('resize', this.closeMenu);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.closeMenu);
    },
    methods: {
        closeMenu() {
            this.$refs.dropdown.hide(true);
        },
    },
};
</script>

<style lang="scss">

.vs-mega-nav-dropdown {
    padding: $spacer-2 0 0 $spacer-2;

    @include media-breakpoint-up(lg) {
        padding: 0;
    }

    .dropdown {
        position: static;

        &.show {
            .btn.dropdown-toggle {
                color: $color-pink;

                &::after {
                    width: 100%;
                }
            }
        }
    }

    .btn.vs-mega-nav-dropdown__close-btn{
        border: 0;
        height: 26px;
        width: 26px;
        font-size: 0;
        right: $spacer-3;
        top: -36px;

        &:hover {
            .vs-icon{
                fill: $color-pink;
            }
        }

        @include media-breakpoint-up(lg) {
            right: $spacer-1;
            top: -10px;
        }
    }

    .btn.dropdown-toggle {
        position: relative;
        letter-spacing: 0;
        font-weight: $font-weight-normal;
        line-height: $line-height-standard;
        border-radius: 0;
        border: 0;
        height: $spacer-7;
        width: $spacer-7;
        font-size: 0;
        padding: .125rem;

        &:focus, &:active, &:active:focus {
            box-shadow: $shadow-button-focus;
            z-index: 1001;
        }

        &:hover, &:focus {
            .vs-icon {
                fill: $color-pink;
            }
        }

        &::after {
            display: none;
        }

        @include media-breakpoint-up(lg) {
            padding: $spacer-3 $spacer-2;
            height: auto;
            width: auto;
            font-size: $font-size-4;

            &::after {
                content: '';
                position: absolute;
                display: block;
                bottom: 0;
                left: 0;
                width: 0;
                height: 4px;
                background: $color-pink;
                transition: width $duration-base linear;
                border: 0;
                margin: 0;
            }

            &:hover {
                color: $color-pink;

                &::after {
                    width: 100%;
                }
            }
        }

        @include media-breakpoint-up(xl) {
            margin: 0 $spacer-3;
        }
    }

    .dropdown-menu {
        background: $color-gray-tint-8;
        width: 100%;
        padding: 0;
        margin: 0;
        border: 0;
        box-shadow: 0px 9px 5px -7px rgba(0,0,0,0.1),
        inset 0px 10px 6px -8px rgba(0, 0, 0, 0.16);
        transform: translate3d(0px, 45px, 0px) !important;

        @include media-breakpoint-up(lg) {
            padding: $spacer-5 0 $spacer-8;
            max-height: 595px;
            transform: translate3d(0px, 55px, 0px) !important;
        }

        .vs-mega-nav-accordion-item--level-1:first-child{
            > .vs-accordion-item__card-header{
                > .vs-accordion-toggle.btn-primary{
                    box-shadow: inset 0px 10px 6px -8px rgba(0, 0, 0, 0.16);
                }
            }
        }

    }

}

@include no-js {
    .vs-mega-nav-dropdown{
        .btn.dropdown-toggle {
            padding: $spacer-3 $spacer-2;
            height: auto;
            width: auto;
            font-size: $font-size-4;
            margin-bottom: $spacer-2;

            @include media-breakpoint-up(lg) {
                margin-bottom: 0;
            }

            &.disabled {
                opacity: $opacity-100;
            }

            &:focus {
                box-shadow: $shadow-button-focus inset;

                &::after{
                    display: none;
                }
            }

            &::after {
                content: '';
                position: absolute;
                display: block;
                bottom: 0;
                left: 0;
                width: 100%;
                height: 4px;
                background: $color-base-text;
                transition: width $duration-base linear;
                border: 0;
                margin: 0;
            }

            &:hover {
                color: $color-pink;

                &::after {
                    width: 100%;
                    background: $color-pink;
                }
            }
        }

        &__close-btn{
            display: none!important;
        }

        .dropdown-menu{
            display: block;
            max-height: none;
            position: relative;
            border: 0;
            box-shadow: none;
            transform: none!important;
        }
    }
}
</style>

<docs>
   ```[import](../__examples__/meganav.example.vue)
    ```
</docs>
