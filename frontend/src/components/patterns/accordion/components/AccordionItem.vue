<template>
    <BCard
        no-body
        class="vs-accordion-item"
        data-test="vs-accordion__item"
    >
        <BCardHeader
            role="tab"
            class="vs-accordion-item__card-header"
            data-test="vs-accordion__item-header"
        >
            <!-- @slot Slot to contain Header for accordion item.
            Defaults to Accordion Toggle button. If component is responsive
            and has a breakPoint it will show title instead of button on larger screens. -->
            <VsAccordionToggle
                :aria-controls="controlId"
                :visible="show"
                :variant="variant"
                :class="toggleAccordionBtn"
                @toggle-panel="onButtonClick"
            >
                <!-- @slot Put the title here  -->
                <slot name="title" />

                <template #icon-open>
                    <!-- @slot Slot for the icon to show when accordion item is open  -->
                    <slot name="icon-open" />
                </template>
                <template #icon-closed>
                    <!-- @slot Slot for the icon to show when accordion item is closed  -->
                    <slot name="icon-closed" />
                </template>
            </VsAccordionToggle>

            <h4
                class="d-none vs-accordion-item__title"
                :class="toggleResponsiveItem"
                data-test="vs-accordion__item-title"
            >
                <!-- @slot Put the title here  -->
                <slot name="title" />
            </h4>
        </BCardHeader>

        <BCardBody
            v-show="show"
            :id="controlId"
            class="vs-accordion-item__panel"
            :class="toggleResponsiveItem"
            data-test="vs-accordion__item-body"
        >
            <!-- @slot The default slot is the content for the accordion  -->
            <slot />
        </BCardBody>
    </BCard>
</template>

<script>
import VsAccordionToggle from '@components/patterns/accordion/components/AccordionToggle';

import {
    BCard, BCardHeader, BCardBody,
} from 'bootstrap-vue';

/**
 * Accordion item for use within the Accordion component.
 * Contains a button to toggle the panel open or closed.
 *
 * @displayName Accordion Item
 */
export default {
    name: 'VsAccordionItem',
    components: {
        VsAccordionToggle,
        BCard,
        BCardHeader,
        BCardBody,
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
            default: true,
        },
        /**
         * Variant for which button to show in headers
         */
        variant: {
            type: String,
            default: 'primary',
        },
    },
    data() {
        return {
            show: this.openByDefault,
        };
    },
    computed: {
        toggleAccordionBtn() {
            if (!this.itemBreakPoint) {
                return 'd-flex';
            }

            return this.itemBreakPoint === 'xs' ? 'd-none' : `d-${this.itemBreakPoint}-none`;
        },
        toggleResponsiveItem() {
            if (!this.itemBreakPoint) {
                return '';
            }

            return this.itemBreakPoint === 'xs' ? 'd-block' : `d-${this.itemBreakPoint}-block`;
        },
    },
    /**
     * Injects breakPoint prop provided by Accordion
     */
    inject: {
        breakPoint: {
            default: 'lg',
        },
    },
    methods: {
        onButtonClick() {
            this.show = !this.show;
        },
    },
};
</script>

<style lang="scss">
.vs-accordion-item.card {
    border: 0;

    .vs-accordion-item__card-header {
        padding: 0;
        border: 0;
        background-color: transparent;
    }

    .btn.vs-accordion-item__toggle-btn {
        text-align: left;

    }

    .vs-accordion-item__title {
        margin: $spacer-3;
        line-height: 1;
        font-weight: 500;
    }

    .vs-accordion-item__panel.card-body {
        padding: 0 0 $spacer-2;
    }
}

@include no-js {
    .vs-accordion-toggle .vs-icon {
        display: none;
    }

    .vs-accordion-item .vs-accordion-item__panel {
        display: block!important;
    }
}
</style>

<docs>
  ```js
    <VsAccordion>
        <VsAccordionItem :open-by-default="true" variant="dark" control-id="accordion_item_1">
            <span slot="title">
                This is a title
            </span>

            <VsIcon
                name="chevron"
                orientation="down"
                variant="light"
                size="xs"
                slot="icon-open"
            />

            <VsIcon
                name="chevron"
                variant="light"
                size="xs"
                slot="icon-closed"
            />

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

            <VsIcon
                name="chevron"
                orientation="down"
                variant="light"
                size="xs"
                slot="icon-open"
            />

            <VsIcon
                name="chevron"
                variant="light"
                size="xs"
                slot="icon-closed"
            />

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

            <VsIcon
                name="chevron"
                orientation="down"
                variant="light"
                size="xs"
                slot="icon-open"
            />

            <VsIcon
                name="chevron"
                variant="light"
                size="xs"
                slot="icon-closed"
            />

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
