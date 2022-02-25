<template>
    <BContainer
        tag="div"
        v-bind="$attrs"
    >
        <!-- @slot Default slot for container content -->
        <slot />
    </BContainer>
</template>

<script>
import { BContainer } from 'bootstrap-vue';
/**
 * Containers are the primary structural element used to
 * organise page content. Our containers utilise
 * <a href="https://bootstrap-vue.js.org/docs/components/layout">Bootstrap Vue's b-container component</a>,
 * and can be fixed (default) or fluid.
 *
 * @displayName Container
 */

export default {
    name: 'VsContainer',
    status: 'prototype',
    release: '0.1.0',
    components: {
        BContainer,
    },
};
</script>

<docs>
  ```jsx
        <VsContainer fluid style="background:#eeeeee; padding: 12px"  class="mb-6">
            <VsRow>
                <VsCol>This is a fluid container</VsCol>
            </VsRow>
        </VsContainer>

        <VsContainer style="background:#eeeeee; padding: 12px">
            <VsRow>
                <VsCol cols="12">
                    This is a fixed-width container
                </VsCol>
                <VsCol cols="12">
                    <p class="mt-6" style="font-size: 1rem;">
                        Note: Enter full width view to see the difference between both containers.
                    </p>
                </VsCol>
            </VsRow>
        </VsContainer>
  ```
</docs>
