<template>
    <div
        class="mega-nav-mobile-menu"
        data-test="vs-mega-nav-mobile-menu"
    >
        <!-- @slot default slot for mobile menu contents  -->
        <slot />
    </div>
</template>

<script>
export default {
    name: 'VsMegaNavMobileMenu',
    status: 'prototype',
    release: '0.1.0',
};
</script>

<style lang="scss">
    .mega-nav-mobile-menu {
        position: absolute;
        top: 100%;
        left: 0;
        width: 100%;
    }
</style>
