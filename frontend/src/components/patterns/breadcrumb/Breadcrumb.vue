<template>
    <nav
        aria-label="breadcrumbs"
        class="vs-breadcrumb py-2"
    >
        <BBreadcrumb
            class="flex-nowrap p-0 mb-0"
            v-bind="$attrs"
        >
            <!-- @slot Default slot for VsBreadcrumbItems -->
            <slot />
        </BBreadcrumb>
    </nav>
</template>

<script>
import { BBreadcrumb } from 'bootstrap-vue';

/**
 * Breadcrumbs indicate where a user is in the website’s
 * structure and lets them easily navigate between levels.
 *
 * @displayName Breadcrumb
 */

export default {
    name: 'VsBreadcrumb',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BBreadcrumb,
    },
};
</script>
