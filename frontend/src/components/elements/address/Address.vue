<template>
    <div
        class="vs-address"
        data-test="vs-address"
    >
        <VsIcon
            name="map-marker"
            class="vs-address__map-marker mr-2"
        />
        <address class="vs-address__address-box">
            <!-- @slot Default slot for address items -->
            <slot />
        </address>
    </div>
</template>
<script>
import VsIcon from '@components/elements/icon';

/**
 * The address component uses the HTML address tag
 * to display the data semantically.
 *
 * @displayName Address
 */
export default {
    name: 'VsAddress',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsIcon,
    },
};
</script>

<style lang="scss">
    .vs-address {
        padding: $spacer-2 $spacer-0;

        span {
            display: block;
        }

        &__map-marker {
            display: inline-block;
            vertical-align: top;
        }

        &__address-box {
            line-height: $line-height-s;
            display: inline-block;
            vertical-align: top;
        }
    }
</style>
