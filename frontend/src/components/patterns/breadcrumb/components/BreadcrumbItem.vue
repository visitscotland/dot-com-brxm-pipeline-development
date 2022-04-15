<template>
    <BBreadcrumbItem
        class="vs-breadcrumb-item"
        :active="active"
        :aria-current="ariaCurrent"
        :href="href"
        :text="text"
        v-bind="$attrs"
    />
</template>

<script>
import { BBreadcrumbItem } from 'bootstrap-vue';

/**
 * https://bootstrap-vue.js.org/docs/components/breadcrumb#comp-ref-b-breadcrumb-item
 *
 * @displayName Breadcrumb Item
 */

export default {
    name: 'VsBreadcrumbItem',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BBreadcrumbItem,
    },
    props: {
        /**
         * The breadcrumb anchor link href.
         */
        href: {
            type: String,
            default: '',
        },
        /**
         * The active breadcrumb item (last item). By default, BootstrapVue renders as a span.
         */
        active: {
            type: Boolean,
            default: false,
        },
        /**
         * The breadcrumb aria-current attribute. Only visible when the item is `active`.
         */
        ariaCurrent: {
            type: String,
            default: 'page',
        },
        /**
         * The breadcrumb text.
         */
        text: {
            type: String,
            default: '',
            required: true,
        },
    },
};
</script>

<style lang="scss">
.vs-breadcrumb-item {
    color: $color-base-text;
    font-size: $font-size-4;
    font-weight: $font-weight-normal;
    white-space: nowrap;
    overflow: hidden;
    padding: 1px; // Prevents breadcrumb link focus state from being clipped

    &:last-child {
        color: $color-base-text;
        font-weight: $font-weight-light;
    }

    @include media-breakpoint-down(md) {
        font-size: $font-size-2;
    }

    @include media-breakpoint-down(sm) {
        display: none;

        &:last-child,
        &:nth-last-child(2) {
            display: block;
            max-width: 75%;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        &:nth-last-child(2) {
            padding: 0;

            &::before {
                content: "";
                padding: 0;
            }
        }
    }

    a {
        color: $color-base-text;
    }

    a, span {
        text-overflow: ellipsis;
        overflow: hidden;
    }
}

</style>

<docs>
  ```jsx
  <VsBreadcrumbItem href="#" text="Inactive"></VsBreadcrumbItem>
  <VsBreadcrumbItem active href="#" text="Active"></VsBreadcrumbItem>
  ```
</docs>
