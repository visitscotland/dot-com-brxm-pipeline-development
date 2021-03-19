<template>
    <div class="vs-itinerary-tips position-relative my-7">
        <div class="vs-itinerary-tips__text">
            <slot name="text" />
        </div>
        <div class="vs-itinerary-tips__svg position-absolute">
            <slot name="svg" />
        </div>
    </div>
</template>

<script>
/**
 * Itinerary tips that optionally display within an itinerary stop
 *
 * @displayName Itinerary Tips
 */

export default {
    name: 'VsItineraryTips',
    status: 'prototype',
    release: '0.0.1',
    components: {
    },
    props: {
    },
};
</script>

<style lang="scss">
.vs-itinerary-tips {
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
    padding: $spacer-4 $spacer-4 $spacer-4 $spacer-9;
}
.vs-itinerary-tips__text {
    p:last-of-type {
        margin-bottom: 0;
    }
}
.vs-itinerary-tips__svg {
    left: -10px;
    bottom: -5px;
}
</style>

<docs>
  ```jsx
    <VsItineraryTips>
      <div slot="text">
        <strong>Don't miss:</strong>
        <p>Make sure you visit one of the many lovely cafés and restaurants that
        the city has to offer, or venture out to experience Edinburgh’s lively nightlife
        in the many bars, clubs and restaurants across the city.</p>
      </div>
      <VsSvg slot="svg" path="highland-cow" />
    </VsItineraryTips>
  ```
</docs>
