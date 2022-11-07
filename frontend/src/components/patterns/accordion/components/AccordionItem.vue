<template>
    <BCard
        no-body
        class="vs-accordion-item"
        data-test="vs-accordion__item"
    >
        <BCardHeader
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
                    <slot name="icon-open">
                        <VsIcon
                            name="chevron"
                            variant="dark"
                            size="sm"
                            slot="icon-open"
                        />
                    </slot>
                </template>
                <template #icon-closed>
                    <!-- @slot Slot for the icon to show when accordion item is closed  -->
                    <slot name="icon-closed">
                        <VsIcon
                            name="chevron"
                            orientation="down"
                            variant="dark"
                            size="sm"
                            slot="icon-closed"
                        />
                    </slot>
                </template>
            </VsAccordionToggle>

            <VsHeading
                level="2"
                override-style-level="6"
                class="d-none vs-accordion-item__title"
                :class="toggleResponsiveItem"
                data-test="vs-accordion__item-title"
            >
                <!-- @slot Put the title here  -->
                <slot name="title" />
            </VsHeading>
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
import VsIcon from '@components/elements/icon/Icon';
import VsHeading from '@components/elements/heading/Heading';

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
        VsIcon,
        BCardHeader,
        BCardBody,
        VsHeading,
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
    border-top: 1px solid $color-gray-tint-5;

    .vs-accordion-item__card-header {
        padding: 0;
        border: 0;
        background-color: transparent;
    }

    .vs-accordion-toggle {
        text-align: left;
        font-weight: $font-weight-bold;
        font-size: $font-size-6;
        letter-spacing: normal;
        padding: $spacer-3;
        border: 0;

        &:focus{
            box-shadow: $shadow-button-focus inset;
        }
    }

    .vs-accordion-item__title {
        padding: $spacer-3;
        margin-bottom: 0;
    }

    .vs-accordion-item__panel.card-body {
        padding: 0 0 $spacer-2;
    }
}

@include no-js {
    .vs-accordion-item .vs-accordion-item__panel {
        display: block!important;
    }
}
</style>
