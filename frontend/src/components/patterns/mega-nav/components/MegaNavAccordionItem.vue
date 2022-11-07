<template>
    <VsAccordionItem
        class="vs-mega-nav-accordion-item"
        data-test="vs-mega-nav-accordion-item"
        :class="accordionItemClasses"
        :data-unique-id="getUniqueId"
        :control-id="`vs-mega-nav-accordion-item-${getUniqueId}-${controlId}`"
    >
        <template #title>
            {{ title }}
        </template>

        <template #icon-open>
            <VsIcon
                name="chevron"
                variant="secondary"
                orientation="up"
                size="xs"
            />
        </template>
        <template #icon-closed>
            <VsIcon
                name="chevron"
                orientation="down"
                variant="secondary"
                size="xs"
            />
        </template>

        <VsLink
            v-if="ctaLink && ctaText"
            class="vs-mega-nav-accordion-item__cta-link"
            data-test="vs-mega-nav-accordion-item__cta-link"
            :href="ctaLink"
        >
            {{ ctaText }}
        </VsLink>

        <!-- @slot The default slot is the content for the accordion  -->
        <slot />
    </VsAccordionItem>
</template>

<script>
import VsAccordionItem from '@components/patterns/accordion/components/AccordionItem';
import VsIcon from '@components/elements/icon/Icon';
import VsLink from '@components/elements/link/Link';

/**
 *  This component is used in the mobile menu for groups of links
 *
 * @displayName MegaNav Accordion Item
 */
export default {
    name: 'VsMegaNavAccordionItem',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsAccordionItem,
        VsIcon,
        VsLink,
    },
    props: {
        /**
         * The aria control ID used for panel ID to match button aria control
         */
        controlId: {
            type: String,
            required: true,
        },
        /**
         * The title for the submenu and alias for the testing data-unique-id
         */
        title: {
            type: String,
            required: true,
        },
        /**
         * Level of items in the nav
         * `1, 2`
         */
        level: {
            type: String,
            required: true,
            validator: (value) => value.match(/(1|2)/),
        },
        /**
         * The CTA link used at the top of the accordion dropdown
         */
        ctaLink: {
            type: String,
            default: '',
        },
        /**
         * The CTA text used for the CTA link at the top of the accordion dropdown
         */
        ctaText: {
            type: String,
            default: '',
        },
    },
    computed: {
        getUniqueId() {
            let transformedTitle = this.title.toLowerCase();
            transformedTitle = transformedTitle.replace(/\s+/g, '-');

            return `vs-mega-nav-${transformedTitle}`;
        },
        accordionItemClasses() {
            return [
                `vs-mega-nav-accordion-item--level-${this.level}`,
            ];
        },
    },
};
</script>

<style lang="scss">
.vs-mega-nav-accordion-item {

    &__cta-link{
        text-decoration: none;
        transition: $duration-base color;
        font-size: $font-size-5;
        line-height: $line-height-s;
        display: block;
        padding: $spacer-3 $spacer-8;
        border-top: 1px solid $color-gray-tint-6;

        &:hover{
            color: $color-secondary-gray-shade-3;
        }

        &:focus{
            outline-offset: -2px;
        }
    }

    &.vs-accordion-item.card {
        border-top: 0;

        &.vs-accordion-item__panel.card-body{
            padding: 0;
        }
    }

    &--level-1, &--level-2{
        > .vs-accordion-item__card-header{
            .vs-accordion-toggle{
                &[aria-expanded="true"]{
                    &::after{
                        content: "";
                        position: absolute;
                        display: block;
                        top: 0;
                        left: 0;
                        width: 12px;
                        height: 100%;
                        background: $color-pink;
                    }
                }

                &.btn-primary{
                    letter-spacing: normal;
                    text-align: left;
                    font-weight: $font-weight-normal;
                    transition: none;
                    border: 0;
                    border-top: 1px solid $color-gray-tint-6;

                    .vs-icon{
                        fill: $color-secondary-gray-shade-3;
                    }

                    &:focus, &:active, &:active:focus{
                        .vs-icon{
                            fill: $color-pink;
                        }
                    }
                }
            }
        }
    }

    &--level-1{
        > .vs-accordion-item__card-header{
            .vs-accordion-toggle.btn-primary{
                background-color: $color-white;
                color: $color-base-text;
                font-size: $font-size-6;
                padding-left: $spacer-5;
                padding-right: $spacer-5;

                &:hover, &:focus, &:active, &:active:focus {
                    background-color: $color-secondary-gray-tint-6;
                    color: $color-base-text;
                }
            }
        }
    }

    &--level-2{
        > .vs-accordion-item__card-header{
            .vs-accordion-toggle.btn-primary{
                background-color: $color-white;
                color: $color-secondary-gray-shade-3;
                font-size: $font-size-5;
                line-height: $line-height-s;
                padding-left: $spacer-8;
                padding-right: $spacer-5;

                &:hover {
                    background-color: $color-gray-tint-7;
                    color: $color-pink;
                }

                &:focus, &:active, &:active:focus {
                    background-color: $color-white;
                    color: $color-secondary-gray-shade-3;
                }
            }
        }
    }
}

</style>
