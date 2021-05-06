<template>
    <li class="link-list-item">
        <VsLink
            :href="href"
            :type="type"
            :variant="variant"
        >
            <!-- @slot Slot for link list item text -->
            <slot />
        </VsLink>
    </li>
</template>

<script>
import VsLink from '@components/elements/link/Link';

/**
 * This component is an item appearing in a list of links.
 *
 * @displayName Link List Item
 */

export default {
    name: 'VsLinkListItem',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsLink,
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
         * Option to create link type which defines icon and whether it opens in a new tab
         * `external, internal, download`
         */
        type: {
            type: String,
            default: 'default',
            validator: (value) => value.match(/(default|external|internal|download)/),
        },
        /**
         * Option to choose a pre-defined style variant
         * `primary, dark`
         */
        variant: {
            type: String,
            default: 'primary',
            validator: (value) => value.match(/(primary|dark)/),
        },
    },
};
</script>

<style lang="scss">
    .link-list-item {
        margin-top: $spacer-4;
        font-size: $lead-font-size;

        &:first-of-type {
            margin-bottom: $spacer-0;
        }
    }
</style>

<docs>
    ```js
        <VsLinkList>
            <VsLinkListItem
                href="www.visitscotland.com"
                type="external"
            >
                This is a link
            </VsLinkListItem>
            <VsLinkListItem
                href="www.visitscotland.com"
                type="download"
            >
                This is another link
            </VsLinkListItem>
            <VsLinkListItem
                href="www.visitscotland.com"
            >
                This is a third link
            </VsLinkListItem>
        </VsLinkList>
    ```
</docs>
