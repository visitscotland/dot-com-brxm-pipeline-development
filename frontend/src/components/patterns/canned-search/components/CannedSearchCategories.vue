<template>
    <div
        class="vs-canned-search-categories"
        data-test="vs-canned-search-categories"
    >
        {{ transformedCategories }}
    </div>
</template>

<script>

/**
* Component that displays a | separated list of categories for a product
* card in canned search
*
* @displayName Canned Search Categories
*/

export default {
    name: 'VsCannedSearchCategories',
    status: 'prototype',
    release: '0.0.1',
    props: {
        /**
        * A list of categories that describe the product, should contain
        * objects, each of which has a `name` prop
        */
        categories: {
            type: Array,
            default: null,
        },
    },
    computed: {
        /**
        * Returns the first 3 provided categories as a pipe separated string for
        * display
        */
        transformedCategories() {
            if (this.categories) {
                return this.categories
                    .slice(0, 3)
                    .map((cat) => cat.name)
                    .join(' | ');
            }

            return '';
        },
    },
};

</script>

<style lang="scss">
    .vs-canned-search-categories {
        font-size: $font-size-4;
        line-height: $line-height-s;
        font-weight: $font-weight-semi-bold;
        margin-bottom: $spacer-1;
    }
</style>
