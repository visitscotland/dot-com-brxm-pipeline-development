<template>
    <VsAccordionItem
        class="vs-footer-accordion-item mb-md-6"
        data-test="vs-footer-accordion-item"
        :data-unique-id="getUniqueId"
        :control-id="controlId"
        :item-break-point="itemBreakPoint"
        :open-by-default="openByDefault"
        :variant="variant"
    >
        <template #title>
            <!-- @slot Put the title here  -->
            {{ title }}
        </template>

        <template #icon-open>
            <!-- @slot Slot for the icon to show when accordion item is open  -->
            <slot name="icon-open" />
        </template>
        <template #icon-closed>
            <!-- @slot Slot for the icon to show when accordion item is closed  -->
            <slot name="icon-closed" />
        </template>

        <!-- @slot The default slot is the content for the accordion  -->
        <slot />
    </VsAccordionItem>
</template>

<script>
import VsAccordionItem from '@components/patterns/accordion/components/AccordionItem';

/**
 * The FooterAccordionItem is an accordion item used inside the FooterNavList.
 * It's a wrapper for FooterNavListItems in the footer.
 *
 * @displayName Footer Accordion Item
 */

export default {
    name: 'VsFooterAccordionItem',
    components: {
        VsAccordionItem,
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
         * If this is provided, the accordion expands above
         * the specified viewport `xs, sm, md, lg, xl, xxl`
         */
        itemBreakPoint: {
            type: String,
            default() {
                return this.breakPoint;
            },
            validator: (value) => value.match(/(xs|sm|md|lg|xl|xxl)/),
        },
        /**
         * Choose to show accordion open or closed by default
         */
        openByDefault: {
            type: Boolean,
            default: false,
        },
        /**
         * The title for the submenu and alias for the testing data-unique-id
         */
        title: {
            type: String,
            default: '',
        },
        /**
         * Variant for which button to show in headers
         */
        variant: {
            type: String,
            default: 'primary',
        },
    },
    computed: {
        getUniqueId() {
            let transformedTitle = this.title.toLowerCase();
            transformedTitle = transformedTitle.replace(/\s+/g, '-');

            return `vs-footer-${transformedTitle}`;
        },
    },
};
</script>

<style lang="scss">
.vs-footer-accordion-item.card {
    border-bottom: 1px solid $color-gray-shade-2;
    border-top: 0;

      .vs-accordion-item__card-header{
        background-color: $color-gray-shade-7;
    }

    .vs-accordion-item__title {
        margin: $spacer-3 0;
        background-color: $color-gray-shade-7;
        color: $color-white;
        font-weight: $font-weight-semi-bold;
        font-size: $font-size-4;
        line-height: $line-height-xs;
        padding: 0;
    }

     .vs-accordion-item__panel.card-body {
        background-color: $color-gray-shade-6;
        color: $color-white;
        border-top: 1px solid $color-gray-shade-2;
    }

    .btn.vs-accordion-toggle {
        text-transform: none !important;
        letter-spacing: initial;
        padding: $spacer-3;
        font-size: $font-size-4;
        line-height: $line-height-xs;
        font-weight: $font-weight-semi-bold;
        text-align: left;

        &:hover {
            background: $color-theme-dark;
            border-color: $color-theme-dark;
            text-decoration: underline;
        }

        &:focus {
            box-shadow: 0 0 0 1px $color-yellow;
        }

         .icon.icon-xs {
            right: $spacer-3;
        }
    }

    @include media-breakpoint-up(sm) {
        .btn.vs-accordion-toggle {
            padding: $spacer-3 $spacer-6;
        }
    }

    @include media-breakpoint-up(md) {
        border: 0;
        border-left: 1px solid $color-gray-shade-2;
        padding: 0 $spacer-3;
        background-color: $color-theme-dark;

        .vs-accordion-item__title {
            margin-top: $spacer-1;
        }

        .vs-accordion-item__panel.card-body {
            padding: 0;
            background-color: $color-theme-dark;
            border-top: 0;
        }
    }
}
</style>

<docs>
  ```js
    <VsFooter>
        <VsFooterNavList break-point="md">
            <VsCol cols="12" md="6">
                <VsFooterAccordionItem
                    title="Visitor information"
                    variant="dark"
                    control-id="footer_accordion_item_1"
                    class="border-left-0"
                >
                    <span slot="icon-open">
                        <VsIcon name="chevron" variant="light" size="xs" />
                    </span>

                    <span slot="icon-closed">
                        <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
                    </span>

                    <VsList unstyled>
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
                    </VsList>
                </VsFooterAccordionItem>
            </VsCol>
        </VsFooterNavList>
    </VsFooter>
  ```
</docs>
