<template>
    <VsHeading
        level="4"
        class="vs-canned-search-sub-heading"
        :class="lineLimit ? 'vs-canned-search-sub-heading--lines-' + lineLimit : ''"
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
        font-size: $font-size-4;
        color: $color-secondary-teal-shade-2;
        letter-spacing: normal;
        margin: $spacer-0;
        margin-bottom: $spacer-3;

        &--lines-1 {
            @include truncate-text(1);
        }

        &--lines-2 {
            @include truncate-text(2);
        }
    }
</style>
