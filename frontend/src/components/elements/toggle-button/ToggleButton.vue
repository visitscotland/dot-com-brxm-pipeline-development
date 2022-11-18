<template>
    <VsButton
        class="vs-toggle-btn"
        data-test="vs-toggle-btn"
        variant="transparent"
        icon-only
        :aria-controls="toggleId"
        :aria-expanded="show ? 'true' : 'false'"
        @click.native="toggleAction"
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
            <VsIcon
                name="information-filled"
                size="md"
            />
        </slot>
    </VsButton>
</template>

<style lang="scss">
    .vs-toggle-btn {
        z-index: 3;
        display: block;
    }
</style>

<script>
import VsButton from '@components/elements/button/Button';
import VsIcon from '@components/elements/icon/Icon';

/**
 * A simple button to control interactive behaviour in other components.
 *
 * @displayName Toggle Button
 */
export default {
    name: 'VsToggleButton',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
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
