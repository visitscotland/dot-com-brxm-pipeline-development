<template>
    <div
        class="vs-footer-social-menu px-0 py-6 py-md-8 py-lg-0 px-lg-3"
        data-test="vs-footer-social-menu"
    >
        <h4 class="vs-footer-social-menu__title mx-3 mx-sm-6 m-md-0 mb-lg-6">
            <!-- @slot Put the menu title here  -->
            <slot name="title" />
        </h4>

        <VsList
            unstyled
            class="mt-5 mt-md-0 mt-lg-5 pr-lg-8 pr-xl-4"
        >
            <!-- @slot The default slot is the content for the menu social items -->
            <slot />
        </VsList>
    </div>
</template>

<script>
import VsList from '@components/elements/list/List';

/**
 * The FooterSocialMenu is used inside the FooterNavList.
 * It's a wrapper for social media links in the footer.
 *
 * @displayName Footer Social Menu
 */

export default {
    name: 'VsFooterSocialMenu',
    components: {
        VsList,
    },
};
</script>

<style lang="scss">
.vs-footer-social-menu {
    border-left: 1px solid $color-gray-shade-2;
    background: $color-gray-shade-7;

    &__title{
        font-size: $font-size-4;
        background: $color-gray-shade-7;
        color: $color-white;
        line-height: $line-height-xs;
        font-weight: $font-weight-semi-bold;
        font-family: $font-family-sans-serif;
    }

    @include media-breakpoint-down(md) {
        border-left: 0;

        .vs-list {
            display: flex;
            justify-content: center;

            .vs-footer-social-item {
                margin-bottom: 0;
            }
        }
    }
}
</style>

<docs>
  ```js
    <VsFooter>
        <VsFooterNavList>
            <VsCol cols="12" md="6">
                <VsFooterSocialMenu>
                    <span slot="title">
                        Find us on
                    </span>

                    <VsFooterSocialItem
                        href="#"
                        icon="facebook"
                    ></VsFooterSocialItem>
                    <VsFooterSocialItem
                        href="#"
                        icon="twitter"
                    ></VsFooterSocialItem>
                    <VsFooterSocialItem
                        href="#"
                        icon="youtube"
                    ></VsFooterSocialItem>
                    <VsFooterSocialItem
                        href="#"
                        icon="instagram"
                    ></VsFooterSocialItem>
                </VsFooterSocialMenu>
            </VsCol>
        </VsFooterNavList>
    </VsFooter>
  ```
</docs>
