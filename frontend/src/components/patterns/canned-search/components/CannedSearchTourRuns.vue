<template>
    <p
        class="text-truncate text-truncate--2 vs-canned-search-tour-runs"
    >
        {{ formattedPeriod }}
    </p>
</template>

<script>
/**
* Shows the time period that a tour runs
*
* @displayName Canned Search Tour Runs
*/

export default {
    name: 'VsCannedSearchTourRuns',
    status: 'prototype',
    release: '0.0.1',
    props: {
        /**
        * A string to precede the time period
        */
        label: {
            type: String,
            default: '',
        },
        /**
        * The first time in the period, if an endDay isn't provided
        * this will be shown on its own (as in the case `all year round`)
        */
        startDay: {
            type: String,
            default: '',
        },
        /**
        * The last time in the period, if set this appears - separated from
        * the start day
        */
        endDay: {
            type: String,
            default: '',
        },
    },
    computed: {
        /**
         * Returns a formatted string for the date range
         */
        formattedPeriod() {
            let output = `${this.label}: ${this.startDay}`;

            if (this.endDay) {
                output = `${output} - ${this.endDay}`;
            }

            return output;
        },
    },
};

</script>

<style lang="scss">
    .vs-canned-search-tour-runs {
        font-weight: $font-weight-bold;
    }
</style>

<docs>
```jsx
    const sampleTour = require("../../../../assets/fixtures/canned-search/sample-tour.json");

    <VsCannedSearchTourRuns
        :label="sampleTour.opening.period.label"
        :startDay="sampleTour.opening.period.startDay"
        :endDay="sampleTour.opening.period.endDay"
    />
```
</docs>
