<template>
    <div
        class="vs-accordion"
        data-test="vs-accordion"
    >
        <!-- @slot Default slot to contain a list of *AccordionItem* components -->
        <slot />
    </div>
</template>

<script>

/**
 * The accordion component is a list of stacked items that show or
 * hide content on the page.
 *
 * @displayName Accordion
 */

export default {
    name: 'VsAccordion',
    props: {
        /**
         * If this is provided, the accordion expands above
         * the specified viewport `xs, sm, md, lg, xl, xxl`
         */
        breakPoint: {
            type: String,
            default: null,
            validator: (value) => value.match(/(xs|sm|md|lg|xl|xxl)/),
        },
    },
    /**
     * Provides breakPoint prop to be injected to child component AccordionItem
     */
    provide() {
        return {
            breakPoint: this.breakPoint,
        };
    },
};
</script>
