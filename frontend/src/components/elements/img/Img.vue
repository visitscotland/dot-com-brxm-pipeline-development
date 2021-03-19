<template>
    <BImg
        v-bind="$attrs"
        :src="src"
        :alt="alt"
        :fluid="fluid"
        :fluid-grow="fluidGrow"
    >
        <slot />
    </BImg>
</template>

<script>
import { BImg } from 'bootstrap-vue';
/**
 * This image component is used to render images in the site. It utilises
 * <a href="https://bootstrap-vue.js.org/docs/components/image">the Bootstrap
 * Vue b-img component</a>.
 *
 * @displayName Img
 */

export default {
    name: 'VsImg',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BImg,
    },
    props: {
        /**
         * The source URL
         */
        src: {
            type: String,
            required: true,
        },

        /**
         * The alt text for accessibility
         */
        alt: {
            type: String,
            required: true,
        },

        /**
         * If true makes the image responsive, scaling with the parent
         * up to a max of the native width of the image itself
         */
        fluid: {
            type: Boolean,
        },

        /**
         * If true makes the image responsive, scaling with the parent
         * beyond the native image width if necessary
         */
        fluidGrow: {
            type: Boolean,
        },
    },
};
</script>

<docs>
  ```jsx

  <div>
    <VsContainer>
      <VsRow>
        <VsCol>
          <VsImg
            src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            alt="Claire standing stones"
          />
        </VsCol>
      </VsRow>
      <VsRow class="mt-5">
        <VsCol>
          <VsImg
            src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            alt="Claire standing stones"
            fluid
          />
        </VsCol>
      </VsRow>
      <VsRow class="mt-5">
        <VsCol>
          <VsImg
            src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            alt="Claire standing stones"
            fluid-grow
          />
        </VsCol>
      </VsRow>
    </VsContainer>
    <br />

  </div>

  ```
</docs>
