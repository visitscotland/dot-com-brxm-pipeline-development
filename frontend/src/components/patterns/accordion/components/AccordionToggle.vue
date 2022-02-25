<template>
    <VsButton
        :animate="false"
        :aria-expanded="visible ? 'true' : 'false'"
        aria-haspopup="true"
        @click.native="triggerToggle"
        class="vs-accordion-toggle clearfix"
        :variant="variant"
        :uppercase="false"
    >
        <!-- @slot Default slot contains text for the button -->
        <span class="vs-accordion-toggle__text">
            <slot />
        </span>
        <span class="vs-accordion-toggle__icon">
            <!-- @slot Put the icon to be used when panel is open  -->
            <slot
                v-if="visible"
                name="icon-open"
            />
            <!-- @slot Put the icon to be used when panel is closed  -->
            <slot
                v-else
                name="icon-closed"
            />
        </span>
    </VsButton>
</template>

<script>
import VsButton from '@components/elements/button/Button';

/**
 * Accordion toggle button used with AccordionItem
 * It emits an event to the parent to toggle the Accordion panel.
 *
 * @displayName Accordion Toggle
 */
export default {
    name: 'VsAccordionToggle',
    components: {
        VsButton,
    },
    props: {
        /**
         * Variant for which button to show in headers
         */
        variant: {
            type: String,
            default: 'primary',
        },
        /**
         * Choose to show accordion open or closed by default
         */
        visible: {
            type: Boolean,
            default: true,
        },
    },
    methods: {
        triggerToggle() {
            this.$emit('toggle-panel');
        },
    },
};
</script>

<style lang="scss">
.btn.vs-accordion-toggle {
    display: flex;
    width: 100%;

    .vs-accordion-toggle__text {
        flex-grow: 1;
    }

    .vs-accordion-toggle__icon {
        align-self: center;
    }
}

@include no-js {
    .btn.vs-accordion-toggle {
        .vs-accordion-toggle__icon {
            display: none;
        }
    }
}

</style>

<docs>
  ```js

  ```
</docs>
