<template>
    <BListGroup
        data-test="vs-list-group"
        v-bind="$attrs"
        v-hand-down-focus
    >
        <slot />
    </BListGroup>
</template>

<script>
import { BListGroup } from 'bootstrap-vue';
import HandDownFocus from '@/directives/hand-down-focus';

/**
 * Wrapper for the BListGroup component.
 *
 * @displayName List Group
 */
export default {
    name: 'VsListGroup',
    components: {
        BListGroup,
    },
    directives: {
        HandDownFocus,
    },
};
</script>

<docs>
  ```js
  ```
</docs>
