<template>
    <VsLink
        :href="href"
        type="external"
        :class="icon"
        class="button-square-social"
        :aria-label="icon"
    >
        <VsIcon
            :name="icon"
            variant="light"
            size="md"
        />
    </VsLink>
</template>

<script>
import VsLink from '@components/elements/link/Link';
import VsIcon from '@components/elements/icon/Icon';

/**
 * The ButtonSquareSocial is a square button that takes an icon
 * and href to display button with corresponding social colour.
 *
 * @displayName Button Square Social
 */
export default {
    name: 'VsButtonSquareSocial',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsLink,
        VsIcon,
    },
    props: {
        /**
         * The external URL the button will point to
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
.button-square-social {
    &.vs-link.vs-link--variant-primary {
        background: $color-gray-shade-1;
        box-shadow: 0 0 0 2px $color-gray-shade-1;
        border: 1px solid $color-gray-shade-1;
        border-radius: 0.5rem;
        display: block;
        height: 42px;
        width: 42px;
        transition: all 0.2s;

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

        &:hover {
            text-decoration: underline;
        }

        &:focus {
            outline: none;
            border: 1px solid $color-pink;
            box-shadow: 0 0 0 2px $color-pink-tint-5;
        }

        .vs-icon {
            &.vs-icon--size-md {
                height: 100%;
                margin: 0 auto;
                fill: $color-white;
                display: block;
            }

            &.vs-icon--external-link {
                display: none;
            }
        }
    }
}
</style>
<docs>
  ```js
    <VsButtonSquareSocial
        href="#"
        icon="youtube"
    >
    </VsButtonSquareSocial>
  ```
</docs>
