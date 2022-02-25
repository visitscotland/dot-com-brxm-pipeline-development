<template>
    <div>
        <BCard :title="title">
            <BCardText>
                The count is {{ count }}
            </BCardText>
            <VsButton @click.native="increment">
                <slot />
            </VsButton>
        </BCard>
    </div>
</template>

<script>
import { BCard, BCardText } from 'bootstrap-vue';
import VsButton from '../../elements/button';
import store from './example.store';

/**
 * This is an example component to demonstrate how VueX can be used with
 * components of the design system.
 *
 * @displayName VueX Example
 */
export default {
    name: 'VsVueXExample',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
        BCard,
        BCardText,
    },
    store,
    props: {
        title: {
            type: String,
            default: 'Vuex Example component',
        },
    },
    data() {
        return {
            showAlert: false,
        };
    },
    computed: {
        count() {
            return this.$store.state.example.count;
        },
    },
    methods: {
        increment() {
            const message = this.showAlert
                ? `Incrementing count from ${this.count} to ${this.count + 1}`
                : false;

            this.$store.dispatch('example/increment', message);
        },
    },
};
</script>

<docs>
  ```vue

  <template>
  <div>
    <vs-row>
        <vs-vue-x-example class="col-3" title="Example component 1">
            Increment count
        </vs-vue-x-example>
        <vs-vue-x-example class="col-3" title="Example component 2">
            Increment count
        </vs-vue-x-example>
    </vs-row>
    <div style="margin-top:24px">
      <span>Outside the components, the count is {{ count }}</span>
    </div>
  </div>
  </template>

  <script>
  import store from './example.store'

  export default {
    store,
    computed: {
      count() {
        return this.$store.state.example.count
      }
    }
  }
  </script>

  ```
</docs>
