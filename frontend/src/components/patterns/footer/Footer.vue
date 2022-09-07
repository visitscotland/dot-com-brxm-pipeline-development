<template>
    <footer
        class="vs-footer pt-6 pt-md-9"
        data-unique-id="vs-footer"
        role="navigation"
        id="site-footer"
    >
        <nav>
            <VsFooterNavList break-point="md">
                <!-- @slot Contains accordion item for each menu list  -->
                <slot name="accordion-items" />

                <VsCol
                    cols="12"
                    lg="3"
                    xl="2"
                    class="d-none d-lg-block"
                    data-unique-id="vs-footer-social_links"
                >
                    <!-- @slot Contains social menu for large screens  -->
                    <slot name="social-menu" />
                </VsCol>
            </VsFooterNavList>

            <div class="vs-footer__social-menu-wrapper border-top border-secondary-light">
                <VsContainer>
                    <VsRow>
                        <VsCol
                            cols="12"
                            class="d-block d-lg-none"
                            data-unique-id="vs-footer-mobile_social_links"
                        >
                            <!-- @slot Contains social menu for screens medium and below -->
                            <slot name="social-menu" />
                        </VsCol>
                    </VsRow>
                </VsContainer>
            </div>

            <slot />
        </nav>
    </footer>
</template>

<script>
import VsFooterNavList from '@components/patterns/footer/components/FooterNavList';
import {
    VsContainer, VsRow, VsCol,
} from '@components/elements/grid';

/**
 * This component is the main Footer wrapper for the bottom of the page.
 *
 * @displayName Footer
 */
export default {
    name: 'VsFooter',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsFooterNavList,
        VsCol,
        VsContainer,
        VsRow,
    },
};
</script>

<style lang="scss">
.vs-footer {
    background: $color-theme-dark;

    .vs-footer__social-menu-wrapper {
        @include media-breakpoint-down(sm) {
            .container {
                max-width: 100%;
                padding: 0;

                .row {
                    margin-right: 0;
                    margin-left: 0;

                    > .col,
                    > [class*="col-"] {
                        padding-right: 0;
                        padding-left: 0;
                    }
                }
            }
        }

        @include media-breakpoint-up(lg) {
            &.border-top {
                border-top: 0 !important;
            }
        }
    }
}
</style>

<docs>
  ```[import](./__examples__/footer.example.vue)
  ```
</docs>
