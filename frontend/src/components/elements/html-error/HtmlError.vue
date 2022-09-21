<template>
    <span class="d-none" />
</template>

<script>
import dataLayerMixin from '../../../mixins/dataLayerMixin';

/**
 * A simple, renderless component that reports an HTML Error code to the datalayer
 *
 * @displayName HtmlError
 */
export default {
    name: 'VsHtmlError',
    status: 'prototype',
    release: '0.1.0',
    mixins: [
        dataLayerMixin,
    ],
    props: {
        /**
         * The error status code to report
         */
        statusCode: {
            type: String,
            required: true,
        },
    },
    mounted() {
        this.createDataLayerObject('errorDataEvent', {
            error_type: 'System Error',
            error_details: this.statusCode,
        });
    },
};
</script>

<docs>
    ```js
        <HtmlError statusCode="404" />
    ```
</docs>
