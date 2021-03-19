<template>
    <div
        class="stretched-link-panel"
        :class="days ? 'stretched-link-panel--days' : ''"
        :data-test="days ? 'stretched-link-panel--days' : 'stretched-link-panel'"
    >
        <p v-if="days">
            <span class="stretched-link-panel__days">{{ days }}</span> days
        </p>

        <template v-if="!days">
            <VsIcon
                name="transport"
                variant="reverse-white"
                class="stretched-link-panel__icon"
            />
            <p data-test="stretched-link-panel__icon">
                {{ text }}
            </p>
        </template>
    </div>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
/**
 * Panels to show information in the stretched link component
 */
export default {
    name: 'VsStretchedLinkPanel',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
    },
    props: {
        /**
        * Optional prop for number of days
        */
        days: {
            type: String,
            default: '',
        },
        /**
        * Optional prop for text (will show a transport icon if used)
        */
        text: {
            type: String,
            default: '',
        },
    },
};
</script>

<style lang="scss">
    .stretched-link-panel {
            width: 55px;
            height: 55px;
            background: $color-black;
            font-size: $xs-font-size;
            margin-left: $spacer-1;
            color: $color-white;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;

            &--days {
                background: $color-secondary-teal-shade-2;
            }

            .stretched-link-panel__days {
                font-size: $font-size-lg;
                font-weight: bold;
                display: block;
            }

            p {
                margin-bottom: 0;
                text-align: center;
                line-height: $line_height_xs;
            }

        @include media-breakpoint-up(sm) {
            margin-left: $spacer-2;
        }
    }
</style>

<docs>
    ```jsx
        <VsStretchedLinkPanel days="14" />
        <VsStretchedLinkPanel text="Panel text" />
    ```
</docs>
