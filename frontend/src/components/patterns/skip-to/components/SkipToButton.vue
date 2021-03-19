<template>
    <VsButton
        class="vs-skip-to sr-only sr-only-focusable d-inline-flex
        p-absolute align-items-center flex-column py-2 px-3"
        variant="dark"
        :tabindex="tabindex"
        :animate="false"
    >
        <slot />
        <VsIcon
            name="chevron"
            orientation="down"
            size="xs"
            variant="reverse-white"
        />
    </VsButton>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
import VsButton from '@components/elements/button';

/**
 * The SkipToButton component is a button with specific style and
 * is not visible until focused on, for use as an accessibility
 * control.
 *
 * @displayName Skip To Button
 */
export default {
    name: 'VsSkipToButton',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
        VsButton,
    },
    props: {
        /**
         * The tabindex attribute for this element. For some reason
         * tabindex isn't passed to the root element so we must do
         * that manually.
         */
        tabindex: {
            type: String,
            default: '',
        },
    },
};
</script>

<style lang="scss">
.vs-skip-to {
    box-shadow: 0 8px 6px -6px rgba(0, 0, 0, 0.3);
    letter-spacing: 2px;
    left: 50%;
    transform: translate(-50%);
    top: 0;
    transition: background-color 250ms;
    z-index: $zindex-fixed;

    &:focus {
        outline: none;
        box-shadow: inset 0 -3px 0 0 $color-base-text;
    }

    &.sr-only-focusable:focus {
        position: absolute;
    }
}
</style>

<docs>
  ```jsx
    <h4 tabindex="1000">Click here then press tab to focus the button and make it visible</h4>
    <VsSkipToButton tabindex="1001">Skip to content</VsSkipToButton>
  ```
</docs>
