<template>
    <li
        class="vs-footer-social-item"
        data-test="vs-footer-social-item"
    >
        <VsLink
            data-test="vs-footer-social-item__link"
            :href="href"
            type="external"
            :class="icon"
            :aria-label="icon"
        >
            <VsIcon
                :name="icon"
                variant="light"
                size="md"
            />
        </VsLink>
    </li>
</template>

<script>

import VsLink from '@components/elements/link/Link';
import VsIcon from '@components/elements/icon/Icon';

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
        VsLink,
        VsIcon,
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

        .vs-link.vs-link--variant-primary {
            background: $color-gray-shade-1;
            box-shadow: 0 0 0 2px $color-gray-shade-1;
            border: 1px solid $color-gray-shade-1;
            border-radius: $border-radius-xl;
            display: block;
            height: 42px;
            width: 42px;
            transition: $transition-base;

            &.facebook {
                &:hover {
                    background: $color-facebook;
                    box-shadow: 0 0 0 2px $color-facebook;
                    border-color: $color-facebook;
                }
            }
            &.instagram {
                &:hover {
                    background: $color-instagram;
                    box-shadow: 0 0 0 2px $color-instagram;
                    border-color: $color-instagram;
                }
            }
            &.twitter {
                &:hover {
                    background: $color-twitter;
                    box-shadow: 0 0 0 2px $color-twitter;
                    border-color: $color-twitter;
                }
            }
            &.youtube {
                &:hover {
                    background: $color-youtube;
                    box-shadow: 0 0 0 2px $color-youtube;
                    border-color: $color-youtube;
                }
            }

            &:focus {
                box-shadow: $shadow-button-focus-dark;
                background-color: $color-white;
                color: $color-gray-shade-1;
                outline: none;

                .vs-icon {
                    fill: $color-gray-shade-1;
                }
            }

            &:hover {
                text-decoration: underline;

                .vs-icon {
                    fill: $color-white;
                }
            }

            .vs-icon {
                height: 100%;
                margin: 0 auto;
                fill: $color-white;
                display: block;

                &.vs-icon--external-link {
                    display: none;
                }
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
