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
 * The Tabs component contains a list of horizontally sequenced tabs, one of which displays
 * content on the page at a time
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
