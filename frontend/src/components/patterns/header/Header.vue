<template>
    <header
        class="vs-header"
        data-test="vs-header"
    >
        <!-- @slot Global menu bar slot  -->
        <slot name="globalMenu" />

        <!-- @slot Main navigation bar slot  -->
        <slot name="megaNav" />
    </header>
</template>

<script>
/**
 * The main header for the site,
 * includes slots for global menu and meganav
 *
 * @displayName Header
 */

export default {
    name: 'VsHeader',
    status: 'prototype',
    release: '0.1.0',
};
</script>

<style lang="scss">
.vs-header {
    position: relative;
    z-index: 10;
}

.vs-header__top {
    z-index: $zindex-sticky;

    .header__top-main {
        height: 28px;
            font-size: $font-size-base;
            font-weight: $font-weight-normal;

        @include media-breakpoint-up(md) {
            height: 35px;
        }

        @include media-breakpoint-up(lg) {
            font-size: $font-size-sm;
        }
    }
}

</style>

<docs>
    ```[import](./__examples__/header.example.vue)
    ```
</docs>
