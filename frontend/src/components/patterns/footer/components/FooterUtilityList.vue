<template>
    <div
        class="border-top border-secondary-light vs-footer-utility-list"
        data-test="vs-footer-utility-list"
        data-unique-id="vs-footer-utility_links"
    >
        <VsContainer>
            <VsRow>
                <VsCol
                    cols="12"
                    sm="7"
                    md="12"
                >
                    <VsList
                        unstyled
                        role="menu"
                    >
                        <!-- @slot The default slot is the content for the menu social items -->
                        <slot />
                    </VsList>
                </VsCol>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import VsList from '@components/elements/list/List';
import {
    VsRow, VsContainer, VsCol,
} from '@components/elements/grid';

/**
 * The FooterUtilityList is a list of FooterNavListItems
 * that hold general utility links for the footer.
 *
 * @displayName Footer Utility List
 */

export default {
    name: 'VsFooterUtilityList',
    components: {
        VsList,
        VsRow,
        VsContainer,
        VsCol,
    },
};
</script>

<style lang="scss">
.vs-footer-utility-list {
    @include media-breakpoint-down(xs) {
        .container {
            max-width: 100%;
            padding: 0 4px;
        }
    }

    .vs-list--unstyled {
        justify-content: space-between;
        display: flex;
        flex-wrap: wrap;
        padding: $spacer-3;

        @include media-breakpoint-up(sm) {
            padding: $spacer-3 $spacer-6;
        }

        @include media-breakpoint-up(md) {
            padding: $spacer-3 0;
        }

        @include media-breakpoint-up(lg) {
            justify-content: center;
            padding: $spacer-8 0;
        }

        @include media-breakpoint-up(xl) {
            padding: $spacer-9 0;
        }

        @include media-breakpoint-up(xxl) {
            justify-content: space-between;
            @supports not (-ms-ime-align: auto) {
                // See issue 2240 - on Edge space-evenly is supported in grid but not in flexbox.
                // This means that if we only target space-evenly here without this supports
                // it falls back to center rather than to space-between and looks very incorrect.
                justify-content: space-evenly;
            }
            padding: $spacer-9 0;
        }

        .vs-footer-nav-list-item {
            display: inline-block;
            font-size: $font-size-2;
            padding: $spacer-3;
            flex: 1 0 50%;

            @include media-breakpoint-down(sm) {
                &:nth-child(odd){
                    padding-left: 0;
                }
            }

            @include media-breakpoint-up(sm) {
                &:first-child {
                    padding-left: 0;
                }

                &:last-child {
                    padding-right: 0;
                }
            }

            @include media-breakpoint-up(md) {
                flex: none;
            }
            @include media-breakpoint-up(xl) {
                font-size: $font-size-4;
            }
        }
    }
}
</style>

<docs>
  ```js
    <VsFooter>
        <VsFooterUtilityList>
            <VsFooterNavListItem
                href="#"
                link-text="Brochures"
            ></VsFooterNavListItem>
            <VsFooterNavListItem
                href="#"
                link-text="VisitScotland iCentres"
            ></VsFooterNavListItem>
            <VsFooterNavListItem
                href="#"
                link-text="iKnow Scotland Community"
            ></VsFooterNavListItem>
            <VsFooterNavListItem
                href="#"
                link-text="VisitScotland Awards"
            ></VsFooterNavListItem>

        </VsFooterUtilityList>
    </VsFooter>

  ```
</docs>
