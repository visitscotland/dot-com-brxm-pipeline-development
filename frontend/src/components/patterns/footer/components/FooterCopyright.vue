<template>
    <div
        class="vs-footer-copyright border-top border-secondary-light"
        data-unique-id="vs-footer-sub_footer"
        data-test="vs-footer-copyright"
    >
        <VsContainer>
            <VsRow>
                <VsCol
                    cols="12"
                    class="text-center"
                >
                    <VsSvgLink
                        class="vs-footer-copyright__logo mb-4"
                        data-test="vs-footer-copyright-logo"
                        :link-alt-text="linkAltText"
                        :href="href"
                        svg-fill="#ffffff"
                        svg-path="vs-logo"
                    />

                    <p class="vs-footer-copyright__text">
                        &copy; {{ getCurrentYear }}

                        <!-- @slot contains translated copyright text -->
                        <slot name="copyright" />
                    </p>
                </VsCol>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import VsSvgLink from '@components/elements/svg-link/SvgLink';

import {
    VsRow, VsContainer, VsCol,
} from '@components/elements/grid';

/**
 * The VsFooterCopyright holds an image and copyright notices for the footer.
 *
 * @displayName Footer Copyright
 */

export default {
    name: 'VsFooterCopyright',
    components: {
        VsSvgLink,
        VsRow,
        VsContainer,
        VsCol,
    },
    props: {
        /**
         * Accessiblity alt text for the logo button
         */
        linkAltText: {
            type: String,
            required: true,
        },
        /**
         * URL for the logo button
         */
        href: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            currentYear: '',
        };
    },
    computed: {
        getCurrentYear() {
            return new Date().getFullYear();
        },
    },
};
</script>

<style lang="scss">
.vs-footer-copyright {
    padding: $spacer-6 0;

    .vs-footer-copyright__logo {
        display: inline-block;

        &.vs-link.primary{
            &:focus {
                outline-color: #ffffff;
            }
        }
    }

    .vs-footer-copyright__text {
        color: $color-white;
        margin: 0;
        font-size: $font-size-2;

        @include media-breakpoint-up(lg) {
            font-size: $font-size-3;
        }

        @include media-breakpoint-up(xl) {
            font-size: $font-size-4;
        }
    }
}
</style>

<docs>
  ```js
    <VsFooter>
        <VsFooterCopyright link-alt-text="VisitScotland Home" href="/">
            <span slot="copyright">
               VisitScotland. All rights reserved.
            </span>
        </VsFooterCopyright>
    </VsFooter>

  ```
</docs>
