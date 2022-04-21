<template>
    <div class="vs-canned-search-dates">
        <div
            class="vs-canned-search-dates__intro"
        >
            {{ label }}
        </div>
        <div
            class="vs-canned-search-dates__main"
        >
            {{ datePeriod }}
        </div>
    </div>
</template>

<script>
/**
* Shows a formatted set of dates for a product card
*
* @displayName Canned Search Dates
*/

export default {
    name: 'VsCannedSearchDates',
    status: 'prototype',
    release: '0.0.1',
    props: {
        /**
        * The date range an event is occurring in, should contain a `startDay` as a
        * YYYY-MM-DD formatted String, and an optional `endDay` if on more than one
        * day.
        */
        period: {
            type: Object,
            default: null,
        },
        /**
        * A line of text that precedes the dates
        */
        label: {
            type: String,
            default: '',
        },
    },
    computed: {
        datePeriod() {
            if (this.period && this.period.startDay) {
                if (this.period.endDay) {
                    return `${this.period.startDay} - ${this.period.endDay}`;
                }

                return this.period.startDay;
            }

            return '';
        },
    },
};

</script>

<style lang="scss">
    .vs-canned-search-dates__intro {
        font-size: $font-size-3;
    }

    .vs-canned-search-dates__main {
        font-weight: bold;
    }
</style>
