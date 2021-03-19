<template>
    <div
        class="vs-accordion"
        role="tablist"
        data-test="vs-accordion"
    >
        <!-- @slot Default slot to contain Accordion Items -->
        <slot />
    </div>
</template>

<script>

/**
 * Accordion wrapper used with AccordionItems.
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

<style lang="scss">
</style>

<docs>
  ```js

    <h3>Basic Accordion</h3>
    <VsAccordion>
        <VsAccordionItem :open-by-default="true" variant="dark" control-id="accordion_item_1">
            <span slot="title">
                This is a title
            </span>

            <span slot="icon-open">
                <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
            </span>

            <span slot="icon-closed">
                <VsIcon name="chevron" variant="light" size="xs" />
            </span>

            <div class="p-3">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. In luctus quam non
                enim commodo consectetur. Curabitur accumsan non mauris et laoreet. Praesent
                maximus sagittis mauris a finibus. Morbi fringilla, lorem ut fringilla sollicitudin,
                turpis enim venenatis ipsum, vitae finibus sem tellus sit amet mauris.
            </div>
        </VsAccordionItem>

        <VsAccordionItem :open-by-default="false" variant="dark" control-id="accordion_item_2">
            <span slot="title">
                This is a title
            </span>

            <span slot="icon-open">
                <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
            </span>

            <span slot="icon-closed">
                <VsIcon name="chevron" variant="light" size="xs" />
            </span>

            <div class="p-3">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. In luctus quam non
                enim commodo consectetur. Curabitur accumsan non mauris et laoreet. Praesent
                maximus sagittis mauris a finibus. Morbi fringilla, lorem ut fringilla sollicitudin,
                turpis enim venenatis ipsum, vitae finibus sem tellus sit amet mauris.
            </div>
        </VsAccordionItem>
        <VsAccordionItem :open-by-default="false" variant="dark" control-id="accordion_item_3">
            <span slot="title">
                This is a title
            </span>

            <span slot="icon-open">
                <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
            </span>

            <span slot="icon-closed">
                <VsIcon name="chevron" variant="light" size="xs" />
            </span>

            <div class="p-3">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. In luctus quam non
                enim commodo consectetur. Curabitur accumsan non mauris et laoreet. Praesent
                maximus sagittis mauris a finibus. Morbi fringilla, lorem ut fringilla sollicitudin,
                turpis enim venenatis ipsum, vitae finibus sem tellus sit amet mauris.
            </div>
        </VsAccordionItem>
    </VsAccordion>

    <h3  class="mt-6">Responsive Accordion</h3>
    <VsAccordion break-point="md">
        <VsAccordionItem :open-by-default="true" variant="dark" control-id="accordion_item_4">
            <span slot="title">
                This is a title
            </span>

            <span slot="icon-open">
                <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
            </span>

            <span slot="icon-closed">
                <VsIcon name="chevron" variant="light" size="xs" />
            </span>

            <div class="p-3">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. In luctus quam non
                enim commodo consectetur. Curabitur accumsan non mauris et laoreet. Praesent
                maximus sagittis mauris a finibus. Morbi fringilla, lorem ut fringilla sollicitudin,
                turpis enim venenatis ipsum, vitae finibus sem tellus sit amet mauris.
            </div>
        </VsAccordionItem>

        <VsAccordionItem :open-by-default="false" variant="dark" control-id="accordion_item_5">
            <span slot="title">
                This is a title
            </span>

            <span slot="icon-open">
                <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
            </span>

            <span slot="icon-closed">
                <VsIcon name="chevron" variant="light" size="xs" />
            </span>

            <div class="p-3">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. In luctus quam non
                enim commodo consectetur. Curabitur accumsan non mauris et laoreet. Praesent
                maximus sagittis mauris a finibus. Morbi fringilla, lorem ut fringilla sollicitudin,
                turpis enim venenatis ipsum, vitae finibus sem tellus sit amet mauris.
            </div>
        </VsAccordionItem>
        <VsAccordionItem :open-by-default="false" variant="dark" control-id="accordion_item_6">
            <span slot="title">
                This is a title
            </span>

            <span slot="icon-open">
                <VsIcon name="chevron" orientation="down" variant="light" size="xs" />
            </span>

            <span slot="icon-closed">
                <VsIcon name="chevron" variant="light" size="xs" />
            </span>

            <div class="p-3">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. In luctus quam non
                enim commodo consectetur. Curabitur accumsan non mauris et laoreet. Praesent
                maximus sagittis mauris a finibus. Morbi fringilla, lorem ut fringilla sollicitudin,
                turpis enim venenatis ipsum, vitae finibus sem tellus sit amet mauris.
            </div>
        </VsAccordionItem>
    </VsAccordion>

  ```
</docs>
