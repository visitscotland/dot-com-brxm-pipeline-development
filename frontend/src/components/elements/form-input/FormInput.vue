<template>
    <BFormInput
        v-model="inputVal"
        class="vs-form-input"
        :size="size"
        v-bind="$attrs"
    />
</template>

<script>
import { BFormInput } from 'bootstrap-vue';

/**
 * https://bootstrap-vue.js.org/docs/components/form-input
 * https://getbootstrap.com/docs/4.3/components/forms/
 *
 * @displayName Form Input
 */

export default {
    name: 'VsFormInput',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BFormInput,
    },
    props: {
        /**
         * Set the form field size.
         * `sm, md, lg`
         */
        size: {
            default: 'md',
            validator: (value) => value.match(/(sm|md|lg)/),
        },
        value: {
            type: String,
            default: '',
        },
    },
    data() {
        return {
            inputVal: this.value,
        };
    },
    watch: {
        inputVal(newValue) {
            this.$emit('input', newValue);
        },
        value(newValue) {
            this.inputVal = newValue;
        },
    },
};
</script>

<style lang="scss">
.vs-form-input {
  &.form-control {
    border-color: $color-gray-tint-1;
    transition: box-shadow $duration-base;

    &:focus {
      border-color: $color-gray-tint-1;
      box-shadow: 0 0 0 0.2rem rgba(187, 38, 132, 0.5); // primary rgb equivalent
    }

    &[type="search"] {
      @extend %reset-clear;
    }
  }
}
</style>

<docs>
```jsx
<BsWrapper>
  <label for="small">Small</label>
  <VsFormInput id="small" placeholder="Enter your name" class="mb-5" size="sm" />
  <label for="medium">Medium (default)</label>
  <VsFormInput id="medium" placeholder="Enter your name" class="mb-5" size="md" />
  <label for="large">Large</label>
  <VsFormInput id="large" placeholder="Enter your name" class="mb-5" size="lg" />

  <label for="input-none">No State</label>
  <VsFormInput id="input-none" :state="null" placeholder="No validation" class="mb-5"/>
  <label for="input-valid">Valid state</label>
  <VsFormInput id="input-valid" :state="true" placeholder="Valid" class="mb-5" />
  <label for="input-invalid">Invalid state</label>
  <VsFormInput id="input-invalid" :state="false" placeholder="Invalid" class="mb-5" />
</BsWrapper>
```
</docs>
