<template>
    <div
        class="vs-canned-search-price"
    >
        <div
            v-if="priceIntro"
            class="vs-canned-search-price__intro"
        >
            {{ priceIntro }}
        </div>
        <div
            v-if="price"
            class="vs-canned-search-price__main"
        >
            {{ price }}
        </div>
        <div
            v-if="priceOutro"
            class="vs-canned-search-price__outro"
        >
            {{ priceOutro }}
        </div>
    </div>
</template>

<script>

/**
* Shows a formatted price with intro and outro
*
* @displayName Canned Search Price
*/

export default {
    name: 'VsCannedSearchPrice',
    status: 'prototype',
    release: '0.0.1',
    props: {
        /**
        * The price of the product
        */
        price: {
            type: String,
            default: '',
        },
        /**
        * A line of text that precedes the product price
        */
        priceIntro: {
            type: String,
            default: '',
        },
        /**
        * A line of text that appears after the product price
        */
        priceOutro: {
            type: String,
            default: '',
        },
    },
};

</script>

<style lang="scss">
    .vs-canned-search-price {
        &__main {
            font-weight: $font-weight-bold;;
        }

        &__outro {
            font-size: $font-size-3;
        }
    }
</style>
