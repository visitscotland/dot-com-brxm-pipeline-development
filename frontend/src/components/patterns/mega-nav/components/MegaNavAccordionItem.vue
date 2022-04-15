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

        <!-- @slot The default slot is the content for the accordion  -->
        <slot />
    </VsAccordionItem>
</template>

<script>
import VsAccordionItem from '@components/patterns/accordion/components/AccordionItem';
import VsIcon from '@components/elements/icon/Icon';

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

<docs>
  ```jsx
    <VsAccordion>
        <VsMegaNavAccordionItem
            :title="item.title"
            level="1"
            :control-id="mobileItemIndex.toString()"
            v-for="(item, mobileItemIndex) in header.mainNav"
            :key="mobileItemIndex"
        >
            <VsMegaNavAccordionItem
                :title="subHeading.title"
                level="2"
                :control-id="subHeadingIndex.toString()"
                v-for="(subHeading, subHeadingIndex) in item.dropdownNav"
                :key="subHeadingIndex"
            >
                <VsMegaNavList>
                    <VsMegaNavListItem
                        slot="navListItems"
                        v-for="(navLink, navLinkIndex)
                            in subHeading.dropdownNav"
                        :key="navLinkIndex"
                        :href="navLink.href"
                    >
                        {{ navLink.title }}
                    </VsMegaNavListItem>

                    <VsMegaNavListItem
                        v-if="subHeading.href"
                        :href="subHeading.href"
                        subheading-link
                        slot="navHeadingCtaLink"
                    >
                        {{ subHeading.cta }}
                    </VsMegaNavListItem>
                </VsMegaNavList>
            </VsMegaNavAccordionItem>
            <div class="featured-items">
                <template
                    v-if="item.title === 'Accommodation' || item.title === 'Inspiration'"
                >
                    <VsMegaNavFeaturedItem
                        link="www.visitscotland.com"
                        img-url="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                        img-alt="Alt text"
                    >
                        <template slot="vsFeaturedItemHeader">
                            From our home to yours – see Scotland virtually
                        </template>

                        <template slot="vsFeaturedItemContent">
                            <p>
                                Although it’s not possible to come to
                                Scotland at the moment.
                            </p>
                        </template>

                        <template slot="vsFeaturedItemLink">
                            A link to a page
                        </template>
                    </VsMegaNavFeaturedItem>
                </template>
            </div>
        </VsMegaNavAccordionItem>
    </VsAccordion>
  ```
</docs>
