<template>
    <div
        class="vs-canned-search-duration"
    >
        <div
            v-if="durationIntro"
            class="vs-canned-search-duration__intro"
        >
            {{ durationIntro }}
        </div>
        <div
            v-if="duration"
            class="vs-canned-search-duration__main"
        >
            {{ duration }}
        </div>
    </div>
</template>

<script>
/**
* Shows a formatted tour duration with intro text
*
* @displayName Canned Search Duration
*/

export default {
    name: 'VsCannedSearchDuration',
    status: 'prototype',
    release: '0.0.1',
    props: {
        /**
        * The duration of the product
        */
        duration: {
            type: String,
            default: '',
        },
        /**
        * A line of text that precedes the product duration
        */
        durationIntro: {
            type: String,
            default: '',
        },
    },
};

</script>

<style lang="scss">
    .vs-canned-search-duration {
        &__main {
            font-weight: $font-weight-bold;;
        }
    }
</style>
