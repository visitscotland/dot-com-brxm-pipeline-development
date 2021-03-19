<template>
    <div
        class="vs-summary-box-icon-with-label d-flex align-items-end
        position-absolute justify-content-center w-100"
        data-test="vs-summary-box-icon-with-label"
    >
        <div class="icon-wrapper text-right">
            <VsIcon
                slot="icon"
                :name="icon"
                variant="dark"
                size="xl"
            />
        </div>
        <div class="text-wrapper d-block w-auto mx-1">
            <span>{{ label }}</span>
        </div>
    </div>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
/**
 * Summary Box Icon With Label
 *
 * @displayName Summary Box Icon With Label
 */
export default {
    name: 'VsSummaryBoxIconWithLabel',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
    },
    props: {
        icon: {
            type: String,
            default: '',
        },
        label: {
            type: String,
            default: '',
        },
    },
};
</script>

<style lang="scss">
.vs-summary-box-icon-with-label {
    bottom: 35%;

    .icon-wrapper {
        max-width: 40%;
        width: auto;
    }
    .text-wrapper {
        font-size: 0.875rem;
        line-height: 1rem;
        max-width: 50%;
        word-wrap: break-word;
        @include media-breakpoint-up(lg) {
            font-size: 1rem;
            line-height: 1.25rem;
        }
    }
}
</style>
<docs>

  ```jsx
    <div class="position-relative mt-5">
        <VsSummaryBoxIconWithLabel
            :icon=itineraries.sampleItinerary.transport.key
            :label=itineraries.sampleItinerary.transport.value
        />
    </div>
  ```
</docs>
