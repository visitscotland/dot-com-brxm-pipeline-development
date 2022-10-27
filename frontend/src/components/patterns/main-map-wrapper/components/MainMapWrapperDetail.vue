<template>
    <div
        class="vs-main-map-wrapper-detail"
        data-test="vs-main-map-wrapper-detail"
    >
        <VsImg
            :src="contentData.properties.image"
            class="vs-main-map-wrapper-detail__image mb-5 mb-lg-3"
        />

        <VsHeading
            level="2"
            override-style-level="4"
            class="vs-main-map-wrapper-detail__heading mt-0 mb-4 d-lg-none"
            data-test="vs-main-map-wrapper-detail__heading"
        >
            {{ contentData.properties.title }}
        </VsHeading>

        <!-- eslint-disable vue/no-v-html -->
        <p
            data-test="vs-main-map-wrapper-detail__description"
            class="vs-main-map-wrapper-detail__description"
            v-html="contentData.properties.description"
        />
        <!-- eslint-enable vue/no-v-html -->
    </div>
</template>

<script>
import VsImg from '@components/elements/img/Img';
import VsHeading from '@components/elements/heading/Heading';

/**
 * Renders a details of a place in the map component.
 * Content is received by a data object
 *
 * @displayName Main Map Wrapper Detail
 */
export default {
    name: 'VsMainMapWrapperDetail',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsImg,
        VsHeading,
    },
    props: {
        /**
         * Data for component content
         */
        contentData: {
            type: Object,
            required: true,
        },
    },
};
</script>

<style lang="scss">
    .vs-main-map-wrapper-detail {
        &__image {
            max-width: 100%;
        }

        @include media-breakpoint-up(lg) {
            font-size: $font-size-4;
            margin: 0 $spacer-2;
        }

        &__description {
            text-align: left;
        }
    }
</style>
