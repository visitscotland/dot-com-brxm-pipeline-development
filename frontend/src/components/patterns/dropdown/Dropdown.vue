<template>
    <BDropdown
        v-bind="$attrs"
        class="vs-dropdown"
    >
        <slot
            v-for="(_, name) in nonButtonContentSlots"
            :name="name"
            :slot="name"
        />
        <template #button-content>
            <slot name="button-content">
                {{ text }}
            </slot>
        </template>
        <slot />
    </BDropdown>
</template>

<script>
import { BDropdown } from 'bootstrap-vue';
import { reject } from 'lodash';

/**
 * Dropdown component for lists of links for example.
 *
 * @displayName Dropdown
 */
export default {
    name: 'VsDropdown',
    components: {
        BDropdown,
    },
    props: {
        text: {
            type: String,
            default: '',
        },
    },
    computed: {
        nonButtonContentSlots() {
            return reject(this.$slots, {
                name: 'button-content',
            });
        },
    },
};
</script>

<style lang="scss">
.vs-dropdown {
    .dropdown-toggle {
        &::after {
            display: inline-block;
            margin: 0.1rem 0 0 0.4rem;
            vertical-align: 0.155em;
            content: "";
            border: solid $color-white;
            border-width: 0 1px 1px 0;
            padding: 0.25rem;
            transform: rotate(45deg);
        }
    }
}
</style>

<docs>
  ```js
  <VsDropdown text="Dropdown">
    <VsDropdownItem href="https://www.google.com">Google</VsDropdownItem>
    <VsDropdownItem>bbbb</VsDropdownItem>
    <VsDropdownItem>feeeeee</VsDropdownItem>
  </VsDropdown>
  ```
</docs>
