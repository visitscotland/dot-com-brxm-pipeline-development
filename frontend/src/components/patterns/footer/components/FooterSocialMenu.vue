<template>
    <div
        class="vs-footer-social-menu px-0 py-6 py-md-8 py-lg-0 px-lg-3"
        data-test="vs-footer-social-menu"
    >
        <VsHeading
            level="2"
            override-style-level="6"
            class="vs-footer-social-menu__title"
        >
            <!-- @slot Put the menu title here  -->
            <slot name="title" />
        </VsHeading>

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
import VsHeading from '@components/elements/heading/Heading';

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
        VsHeading,
    },
};
</script>

<style lang="scss">
.vs-footer-social-menu {
    border-left: 1px solid $color-gray-shade-2;
    background: $color-gray-shade-7;

    &__title.vs-heading{
        background: $color-gray-shade-7;
        color: $color-white;
        margin: 0 $spacer-3;

        @include media-breakpoint-up(sm) {
            margin: 0 $spacer-6;
        }

        @include media-breakpoint-up(md) {
            margin: 0;
            padding-top: $spacer-3;
        }
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
