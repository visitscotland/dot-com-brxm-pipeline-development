<template>
    <nav
        aria-label="breadcrumbs"
        class="vs-breadcrumb py-4"
    >
        <BBreadcrumb
            class="flex-nowrap p-0 mb-0"
            v-bind="$attrs"
        >
            <!-- @slot The breadcrumb content goes here -->
            <slot />
        </BBreadcrumb>
    </nav>
</template>

<script>
import { BBreadcrumb } from 'bootstrap-vue';

/**
 * Breadcrumbs are used to indicate the current page position within the navigational hierarchy.
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

<docs>
  ```jsx
  <VsBreadcrumb>
    <VsBreadcrumbItem
      v-for="(item, index) in breadcrumb.breadcrumb"
      :key="index"
      :href="item.href"
      :active="item.active"
      :text="item.name"
      >
    </VsBreadcrumbItem>
  </VsBreadcrumb>
  ```
</docs>
