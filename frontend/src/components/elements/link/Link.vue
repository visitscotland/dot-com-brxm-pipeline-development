<template>
    <BLink
        class="vs-link"
        :class="[
            `vs-link--variant-${variant}`,
        ]"
        :href="href"
        target="_self"
        :download="type === 'download'"
        :disabled="disabled"
        v-bind="$attrs"
    >
        <!-- @slot Default slot for link content -->
        <slot /><VsIcon
            v-if="type !== 'default'"
            :name="`${type}-link`"
            variant="primary"
            :size="iconSize"
            class="ml-1 vs-link__icon"
        />
    </BLink>
</template>

<script>
import { BLink } from 'bootstrap-vue';
import VsIcon from '@components/elements/icon/Icon';

/**
 * Links allow a user to navigate through
 * your application or to an external website.
 *
 * @displayName Link
 */

export default {
    name: 'VsLink',
    status: 'prototype',
    release: '0.0.2',
    components: {
        BLink,
        VsIcon,
    },
    props: {
        /**
         * The URL the link will point to
         */
        href: {
            type: String,
            default: null,
        },
        /**
         * Option to choose a type which gives the link an icon
         * `default|external|internal|download`
         */
        type: {
            type: String,
            default: 'default',
            validator: (value) => value.match(/(default|external|internal|download)/),
        },
        /**
         * Option to choose a pre-defined style variant
         * `primary|dark`
         */
        variant: {
            type: String,
            default: 'primary',
            validator: (value) => value.match(/(primary|dark)/),
        },
        /**
        * Size of icon
        * `xxs|xs|sm|md|lg|xl`
        */
        iconSize: {
            type: String,
            default: 'xxs',
            validator: (value) => value.match(/(xxs|xs|sm|md|lg|xl)/),
        },
        /**
        * Option to disable the link
        */
        disabled: {
            type: Boolean,
            default: false,
        },
    },
    computed: {
        variantClass() {
            return `vs-link--variant-${this.variant}`;
        },
    },
};
</script>

<style lang="scss">
.vs-link {
    &.vs-link--variant-primary {
        color: $color_pink;

        &:focus {
            outline: 2px solid $color_pink;
        }
    }

    &.vs-link--variant-dark {
        color: $color_yellow;

        .vs-icon {
            fill: $color_yellow;
        }

        &:focus {
            outline: 2px solid $color_yellow;
        }
    }

    .vs-link__icon {
        vertical-align: baseline;
    }
}

@include themed-text-editor("dark") {
    .vs-link {
        color: $color_yellow;

        .vs-icon {
            fill: $color_yellow;
        }

        &:focus {
            outline: 2px solid $color_yellow;
        }
    }
}
</style>
