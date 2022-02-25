<template>
    <VsSkipToButton
        @click.native.prevent="skipTo"
        :tabindex="tabindex"
    >
        <slot />
    </VsSkipToButton>
</template>

<script>
import {
    isFunction, get, isNumber,
} from 'lodash';
import VsSkipToButton from './components/SkipToButton';

/**
 * The SkipTo component provides users of assistive
 * technologies with a focusable control that moves the
 * focus to a provided target element when activated.
 *
 * @displayName Skip To
 */
export default {
    name: 'VsSkipTo',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsSkipToButton,
    },
    props: {
        /**
         * The tabindex attribute for this element. For some reason
         * tabindex isn't passed to the root element so we must do
         * that manually.
         */
        tabindex: {
            type: String,
            default: '',
        },
        /**
         * The target element to skip to: a Vue ref - e.g.
         * from this.$refs - or a DOM Element.
         */
        target: {
            type: Object,
            default: null,
        },
    },
    methods: {
        skipTo() {
            let element;

            if (isFunction(get(this.target, 'focus'))) {
                element = this.target;
            } else if (isFunction(get(this.target, '$el.focus'))) {
                element = this.target.$el;
            }

            if (element) {
                if (!isNumber(element.tabIndex)) {
                    element.tabIndex = -1;
                }
                element.focus();
            }
        },
    },
};
</script>

<style lang="scss"></style>

<docs>
  ```js

  new Vue({
    template: `
      <BsWrapper class="d-flex flex-row">
        <BsWrapper class="card mr-2" style="width:10rem" tabindex="10000">
          <BsWrapper class="card-body">
            Click here then press tab to navigate
          </BsWrapper>
        </BsWrapper>

        <VsSkipTo :target="ref" @activated="activated('ref')" tabindex="10001">
          Skip to Vue ref target
        </VsSkipTo>

        <VsSkipTo :target="element" @activated="activated('HTML element')" tabindex="10002">
          Skip to HTML Element target
        </VsSkipTo>

        <BsWrapper class="card mr-2" style="width:10rem" tabindex="10003">
          <BsWrapper class="card-body">
            Thing to skip
          </BsWrapper>
        </BsWrapper>

        <BsWrapper ref="refTarget" class="card mr-2" style="width:10rem" tabindex="10004">
          <BsWrapper class="card-body">
            This target was passed as a Vue ref
          </BsWrapper>
        </BsWrapper>

        <BsWrapper id="element-target" class="card" style="width:10rem" tabindex="10005">
          <BsWrapper class="card-body">
            This target was passed as an HTML Element
          </BsWrapper>
        </BsWrapper>
      </BsWrapper>
    `,
    data() {
      return {
        element: null,
        ref: null
      }
    },
    methods: {
      activated(target) {
        alert("Skip to with \"" + target + "\" target was clicked on")
      }
    },
    mounted: function() {
      this.element = document.getElementById("element-target")
      this.ref = this.$refs.refTarget
    },
  })

  ```
</docs>
