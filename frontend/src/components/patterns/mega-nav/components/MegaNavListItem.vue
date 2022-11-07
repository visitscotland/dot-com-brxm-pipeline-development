<template>
    <li
        class="vs-mega-nav-list-item"
        data-test="vs-mega-nav-list-item"
        :class="navItemClasses"
        role="presentation"
    >
        <VsLink
            :href="href"
            role="menuitem"
            data-layer-value="menuNavigationDataEvent"
        >
            <!-- @slot Default slot for nav link content  -->
            <slot />
        </VsLink>
    </li>
</template>

<script>
import VsLink from '@components/elements/link/Link';
import dataLayerMixin from '../../../../mixins/dataLayerMixin';

/**
 *  Meganav list items with link and slot for link content
 *
 * @displayName MegaNav List Item
 */
export default {
    name: 'VsMegaNavListItem',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsLink,
    },
    mixins: [dataLayerMixin],
    props: {
        /**
         * The URL for the nav list link
         */
        href: {
            type: String,
            default: '#',
        },
        /**
         * Check if link is for subheading
         */
        subheadingLink: {
            type: Boolean,
            default: false,
        },
        /**
         * Check if link is a CTA link
         */
        ctaLink: {
            type: Boolean,
            default: false,
        },
    },
    computed: {
        navItemClasses() {
            return {
                'vs-mega-nav-list-item__heading': this.href === '#',
                'vs-mega-nav-list-item__link': this.href !== '#',
                'vs-mega-nav-list-item__subheading-link': this.subheadingLink,
                'vs-mega-nav-list-item__cta-link': this.ctaLink,
            };
        },
    },
};
</script>

<style lang="scss">
.vs-mega-nav-list-item{
    position: relative;
    list-style-type: none;
    background-color: $color-gray-tint-7;
    border-top: 1px solid $color-gray-tint-6;

    @include media-breakpoint-up(lg) {
        border-top: 0;
        background-color: $color-white;
    }

    .vs-link--variant-primary{
        text-decoration: none;
        display: block;
        padding: $spacer-3 $spacer-9;
        border: 2px solid $color-gray-tint-7;
        line-height: 1.4;
        font-size: $font-size-4;

        @include media-breakpoint-up(lg) {
            border: 2px solid $color-white;
            line-height: $line-height-s;
            padding: 0.125rem $spacer-4;
        }

        &:hover{
            border: 2px solid $color-white;
        }

        &:focus{
            @extend %outline-link-focus;
            outline-offset: -2px;
        }
    }

    &__link{
        .vs-link--variant-primary{
            color: $color-secondary-gray-shade-3;
            transition: $duration-base color;

            @include media-breakpoint-up(lg) {
                color: $color-secondary-gray;
            }

            &:hover{
                background-color: $color-white;
                color: $color-pink;

                @include media-breakpoint-up(lg) {
                    background-color: transparent;

                    &:after{
                        opacity: $opacity-100;
                    }
                }
            }

            &::after{
                content: "";
                position: absolute;
                display: block;
                top: 0;
                left: 0;
                width: 12px;
                height: 100%;
                background: #AAA9A7;
            }

            @include media-breakpoint-up(lg) {
                &:after{
                    width: 6px;
                    height: 100%;
                    opacity: $opacity-0;
                    background: $color-pink;
                    transition: opacity 0.16s ease-in-out;
                }
            }
        }
    }

    &__subheading-link{
        .vs-link--variant-primary{
            color: $color-pink;
        }
    }

    &__cta-link{
        .vs-link--variant-primary{
            background-color: $color-white;
            color: $color-pink;
            font-size: $font-size-5;
            line-height: $line-height-s;
            padding-left: $spacer-8;
            padding-right: $spacer-5;
            border: 0;

            &::after{
                display: none;
            }

            &:hover {
                border: 0;
                background-color: $color-gray-tint-7;
            }

            &:focus, &:active, &:active:focus {
                border: 0;
                background-color: $color-white;
                box-shadow: 0 0 0 0.1rem $color-pink inset;
            }
        }
    }
}

@include no-js {
    .vs-mega-nav-list-item{
        background-color: transparent;
        border: 0;

        .vs-link--variant-primary{
            border: 2px solid $color-white;
            line-height: $line-height-s;
            padding: $spacer-1 $spacer-5;
        }

        &__link{
            .vs-link--variant-primary{
                &:hover{
                    background-color: transparent;

                    &:after{
                        opacity: $opacity-100;
                    }
                }

                &::after{
                    width: 6px;
                    height: 100%;
                    opacity: $opacity-0;
                    background: $color-pink;
                    transition: opacity 0.16s ease-in-out;
                }
            }
        }
    }
}

</style>
