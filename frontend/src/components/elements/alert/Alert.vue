<template>
    <div
        class="vs-alert"
        data-test="vs-alert"
    >
        <div class="vs-alert__icon-holder">
            <VsIcon
                class="vs-alert__icon"
                name="information"
                variant="light"
            />
        </div>
        <div class="vs-alert__content-wrapper">
            <div class="vs-alert__content">
                <!-- @slot Default slot for alert text -->
                <slot />
            </div>
        </div>
    </div>
</template>
<script>
import VsIcon from '@components/elements/icon';

/**
 * The alert component makes text more prominent to notify
 * the user of important information.
 *
 * @displayName Alert
 */
export default {
    name: 'VsAlert',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsIcon,
    },
};
</script>

<style lang="scss">
    .vs-alert {
        display: flex;
        flex-flow: row;
    }

    .vs-alert__icon-holder {
        background-color: $color-pink-shade-1;
        display: inline-flex;
        justify-content: center;
        align-items: flex-start;
        padding: $spacer-4 $spacer-0;
        width: 2.5rem;
        min-height: 2.5rem;

        @include media-breakpoint-up(lg) {
            width: $spacer-9;
            min-height: $spacer-9;
        }
    }

    .vs-alert__icon {
        width: $spacer-4;
        width: $spacer-4;

        @include media-breakpoint-up(lg) {
            width: $spacer-6;
            width: $spacer-6;
        }
    }

    .vs-alert__content-wrapper {
        flex: 1;
        display: flex;
        align-items: flex-start;
    }

    .vs-alert__content {
        display: flex;
        align-items: center;
        padding: $spacer-3;
        background-color: $color-pink-tint-6;
        border: 1px solid $color-pink-tint-1;
        min-height: 100%;
    }
</style>
