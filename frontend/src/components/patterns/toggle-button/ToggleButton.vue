<template>
    <VsButton
        variant="outline-transparent"
        class="vs-toggle-btn"
        aria-label="Expand caption"
        :animate="false"
        :aria-controls="toggleId"
        :aria-expanded="show ? 'true' : 'false'"
        @click.native="toggleAction"
        data-test="vs-toggle-btn"
    >
        <!-- @slot Default slot for screenreader text -->
        <span class="sr-only">
            <slot />
        </span>

        <VsIcon
            v-if="show"
            name="close-circle-filled"
            size="md"
        />

        <!-- @slot Slot for custom toggle icon - used for social images -->
        <slot
            v-else
            name="toggle-icon"
        >
            <VsSvg
                path="info-toggle"
                height="24"
                width="24"
            />
        </slot>
    </VsButton>
</template>

<style lang="scss">
    .vs-toggle-btn {
        padding: 0;
        line-height: $line_height_xs;
        z-index: 3;
        display: block;

        .vs-icon, svg {
            margin-top: 0;
        }

        &:hover {
            .vs-icon, svg {
                fill: $color-theme-primary;
            }
        }
    }
</style>

<script>
import VsButton from '@components/elements/button/Button';
import VsSvg from '@components/elements/svg/Svg';
import VsIcon from '@components/elements/icon/Icon';

/**
 * Toggle button to toggle elements in other components.
 * The component emits an event on click that can be listened
 * for in a parent component.
 *
 * @displayName Toggle Button
 */
export default {
    name: 'VsToggleButton',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
        VsSvg,
        VsIcon,
    },
    props: {
        /**
         * ID for the toggled element
         */
        toggleId: {
            type: String,
            default: '',
        },
    },
    data() {
        return {
            show: false,
        };
    },
    methods: {
        toggleAction() {
            this.show = !this.show;
            this.$emit('toggleAction', this.show);
        },
    },
};
</script>

<docs>
    ```jsx
    <VsToggleButton />
    ```
</docs>
