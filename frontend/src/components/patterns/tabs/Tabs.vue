<template>
    <div
        class="vs-tabs"
        data-test="vs-tabs"
    >
        <BTabs>
            <!-- @slot default slot for VsTabItems -->
            <slot />
        </BTabs>
    </div>
</template>

<script>
import { BTabs } from 'bootstrap-vue';
/**
 * Tabs group related content together on the same page and allow a user to navigate through one
 * section of content at a time.
 *
 * @displayName Tabs
 */
export default {
    name: 'VsTabs',
    components: {
        BTabs,
    },
};
</script>

<style lang="scss">
</style>
