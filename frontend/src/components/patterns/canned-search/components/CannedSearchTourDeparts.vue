<template>
    <p
        class="vs-canned-search-tour-departs"
    >
        <span class="vs-canned-search-tour-departs__label">{{ label }}:</span>
        {{ transformedOrigins }}
    </p>
</template>

<script>

/**
* Component that displays a , separated list of origins for a tour on a
* product card in canned search
*
* @displayName Canned Search Tour Departs
*/

export default {
    name: 'VsCannedSearchTourDeparts',
    status: 'prototype',
    release: '0.0.1',
    props: {
        /**
        * A string to precede the list of origins
        */
        label: {
            type: String,
            default: null,
        },
        /**
        * A list of places that a tour departs from, should contain
        * objects, each of which has a `name` prop
        */
        origins: {
            type: Array,
            default: null,
        },
    },
    computed: {
        /**
        * Returns the first 3 provided categories as a pipe separated string for
        * display
        */
        transformedOrigins() {
            if (this.origins) {
                return this.origins
                    .map((origin) => origin.name)
                    .join(', ');
            }

            return '';
        },
    },
};

</script>

<style lang="scss">
    .vs-canned-search-tour-departs {
        font-size: $font-size-4;
        line-height: $line-height-s;
        margin-bottom: $spacer-1;

        @include truncate-text(2);

        &__label {
            font-weight: $font-weight-bold;
        }
    }
</style>
