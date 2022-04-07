<template>
    <BCard
        class="vs-panel"
        :class="[this.$slots['vs-panel__title'] ? '' : 'pt-10']"
    >
        <template v-slot:header>
            <!-- @slot Contains an optional title for the panel  -->
            <slot name="vs-panel-title" />
        </template>

        <BCardText class="vs-panel__text">
            <!-- @slot Default slot containing main body text -->
            <slot />
        </BCardText>
    </BCard>
</template>

<script>
import { BCard, BCardText } from 'bootstrap-vue';

/**
 * The panel component is a container
 * that helps to emphasise content.
 *
 * @displayName Panel
 */
export default {
    name: 'VsPanel',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BCard,
        BCardText,
    },
};
</script>

<style lang="scss">
.vs-panel.card {
    background-color: $color-theme-light;
    padding: $spacer-9 $spacer-4 $spacer-9;

    @include media-breakpoint-up(md) {
        padding: $spacer-9 $spacer-8 $spacer-10;
    }

    @include media-breakpoint-up(lg) {
        padding: $spacer-9 $spacer-11 $spacer-10;
    }

    @include media-breakpoint-up(xxl) {
        padding: $spacer-9 $spacer-12 $spacer-10;
    }

    .vs-panel__title {
        display: block;
        text-align: center;
    }

    .vs-panel__text {
        @include media-breakpoint-down(xs) {
            .vs-rich-text-wrapper.lead {
                font-size: $font-size-4;
                line-height: $line-height-m;
            }
        }
    }

    .card-header {
        text-align: center;
        background: none;
        border-bottom-width: 0;
    }
}
</style>
