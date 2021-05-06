<template>
    <VsButton
        class="px-3"
        variant="transparent"
        :animate="false"
        size="sm"
        icon="close"
        :icon-only="true"
        icon-variant-override="secondary"
    >
        <span class="sr-only"><slot /></span>
    </VsButton>
</template>

<script>
import VsButton from '@components/elements/button/Button';

/**
 * TODO: Document usage
 *
 * @displayName Close Button
 */
export default {
    name: 'VsCloseButton',
    components: {
        VsButton,
    },
};
</script>

<style lang="scss"></style>

<docs>
  ```js
    <VsCloseButton />
  ```
</docs>
