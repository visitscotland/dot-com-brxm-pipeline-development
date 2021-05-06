<template>
    <li
        class="vs-footer-social-item"
        data-test="vs-footer-social-item"
    >
        <VsButtonSquareSocial
            data-test="vs-footer-social-item__button"
            :href="href"
            :icon="icon"
        />
    </li>
</template>

<script>

import VsButtonSquareSocial from '@components/patterns/button-square-social/ButtonSquareSocial';

/**
 * The FooterSocialItem is used inside the FooterSocialMenu to
 * show a button for a social media link.
 *
 * @displayName Footer Social Item
 */

export default {
    name: 'VsFooterSocialItem',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButtonSquareSocial,
    },
    props: {
        /**
         * The URL the link will point to
         */
        href: {
            type: String,
            required: true,
        },
        /**
         * Social icon to show, will also be used to choose hover colour
         * `facebook, instagram, twitter, youtube`
         */
        icon: {
            type: String,
            required: true,
            validator: (value) => value.match(/(facebook|instagram|twitter|youtube)/),
        },
    },
};
</script>

<style lang="scss">
.vs-list.vs-list--unstyled  {
    .vs-footer-social-item{
        display: inline-block;
        margin-right: $spacer-3;
        margin-bottom: $spacer-3;
    }
}
</style>

<docs>
  ```js
    <VsFooter>
        <VsFooterNavList break-point="md">
            <VsCol cols="12" md="6">
                <VsAccordionItem
                    :open-by-default="true"
                    variant="dark"
                    control-id="accordion_item_1"
                    class="border-left-0"
                >
                    <span slot="title">
                        Find us on
                    </span>

                    <span slot="icon-open">
                        <VsIcon name="chevron" variant="light" size="xs" />
                    </span>

                    <span slot="icon-closed">
                        <VsIcon name="chevron" orientation="right" variant="light" size="xs" />
                    </span>

                    <VsList unstyled>
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
                    </VsList>
                </VsAccordionItem>
            </VsCol>
        </VsFooterNavList>
    </VsFooter>
  ```
</docs>
