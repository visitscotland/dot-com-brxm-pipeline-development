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
            <!-- @slot default slot containing main body text -->
            <slot />
        </BCardText>
    </BCard>
</template>

<script>
import { BCard, BCardText } from 'bootstrap-vue';

/**
 * The panel is used for simple content and contains slots for title and content.
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
                font-size: $font-size-base;
                line-height: $line-height-m;
            }
        }
    }

    .card-header {
        text-align: center;
    }
}
</style>

<docs>
```jsx
    <VsPanel>
        <VsHeading thin level="4" slot="vs-panel-title">A Title Would Go Here</VsHeading>

        <VsRichTextWrapper variant="lead">
             <!-- eslint-disable-next-line max-len -->
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras maximus est congue nunc fermentum, eu hendrerit odio fringilla. Donec porta eleifend ipsum Praesent in magna fringilla.</p>

             <!-- eslint-disable-next-line max-len -->
            <p>Morbi faucibus risus in varius sollicitudin. Aenean eget leo ullamcorper, scelerisque urna eu, imperdiet nibh. Duis sit amet nibh tortor. Donec ac ante eros. Praesent in magna fringilla, consectetur eros id, fermentum nibh. Duis luctus ante sapien, scelerisque fermentum massa tincidunt eu. Nullam sed arcu a justo auctor consectetur.</p>
        </VsRichTextWrapper>
    </VsPanel>
```
</docs>
