<template>
    <VsHeading
        level="4"
        class="vs-canned-search-sub-heading"
        :class="lineLimit ? 'text-truncate text-truncate--' + lineLimit : ''"
        data-test="vs-canned-search-sub-heading"
    >
        {{ subHeading }}
    </VsHeading>
</template>

<script>
import VsHeading from '@components/elements/heading/Heading';

/**
* Shows a formatted sub-heading for a canned search product card,
* usually contains an address or list of themes
*
* @displayName Canned Search Sub Heading
*/

export default {
    name: 'VsCannedSearchSubHeading',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsHeading,
    },
    props: {
        /**
        * The string to display
        */
        subHeading: {
            type: String,
            default: '',
        },
        /**
         * The number of lines of text to limit the subHeading to, if
         * this is un-set it will not limit
         *
         * 1, 2
         */
        lineLimit: {
            type: Number,
            default: null,
            validator: (value) => [1, 2].includes(value),
        },
    },
};

</script>

<style lang="scss">
    h4.vs-canned-search-sub-heading {
        font-family: $font-family-base;
        font-size: $font-size-base;
        color: $color-secondary-teal-shade-2;
        letter-spacing: normal;
        margin: $spacer-0;
        margin-bottom: $spacer-3;
    }

    .text-truncate {
        text-overflow: ellipsis;
        overflow: hidden;
        display: -webkit-box !important;
        -webkit-box-orient: vertical;
        white-space: normal;

        &--1 {
            -webkit-line-clamp: 1;
        }

        &--2 {
            -webkit-line-clamp: 2;
        }
    }
</style>

<docs>
```jsx
    <VsCannedSearchSubHeading
        style="max-width: 25rem"
        sub-heading="Callander, Loch Lomond, The Trossachs, Stirling & Forth Valley"
    />
    <VsCannedSearchSubHeading
        style="max-width: 25rem"
        sub-heading="A very very long list of themes, it's so long it should overflow onto a second
        row but it has a line limit"
        :line-limit="1"
    />
```
</docs>
