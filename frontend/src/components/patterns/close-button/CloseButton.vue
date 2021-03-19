<template>
    <VsButton
        class="px-3"
        variant="transparent"
        :animate="false"
    >
        <span class="sr-only"><slot /></span>
        <VsIcon
            name="close"
            size="xs"
            variant="secondary"
        />
    </VsButton>
</template>

<script>
import VsButton from '@components/elements/button/Button';
import VsIcon from '@components/elements/icon/Icon';

/**
 * TODO: Document usage
 *
 * @displayName Close Button
 */
export default {
    name: 'VsCloseButton',
    components: {
        VsButton,
        VsIcon,
    },
};
</script>

<style lang="scss"></style>
