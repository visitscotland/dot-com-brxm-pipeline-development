<template>
    <div
        class="vs-main-map-category"
        data-test="vs-main-map-category"
    >
        <VsButton
            class="vs-main-map-category__button text-capitalize"
            :class="`vs-main-map-category__button--${type}`"
            data-test="vs-main-map-category__button"
            variant="transparent"
            icon="internal-link"
            icon-position="right"
            size="sm"
            @click.native="selectCategory(type)"
        >
            <div>
                <VsSvg
                    class="vs-main-map-category__icon"
                    slot="svg"
                    :path="`marker-${type}`"
                />
                {{ categoryName }}
            </div>
        </VsButton>
    </div>
</template>

<script>
import VsButton from '@components/elements/button/Button';
import VsSvg from '@components/elements/svg/Svg';

export default {
    name: 'VsMainMapWrapperCategories',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
        VsSvg,
    },
    props: {
        /**
         * Name of category
         */
        categoryName: {
            type: String,
            required: true,
        },
        /**
         * Category type - for use with markers
         * and data
         */
        type: {
            type: String,
            required: true,
        },
    },
    methods: {
        /**
         * Emits category type
         */
        selectCategory(type) {
            this.$parent.$emit('set-category', type);
            this.$parent.$emit('set-stage', 1);
        },
    },
};
</script>

<style lang="scss">
    .vs-main-map-category {
        position: relative;
        display: flex;
        align-items: center;

        &::before {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            width: 100%;
            background: $color-gray-tint-5;
            height: 1px;
        }

        &__icon {
            width: 1.875rem;
            margin-right: $spacer-4;
        }

        @include map-button-themes;

        &__button.vs-button.btn-transparent {
            font-size: $font-size-5;
            font-weight: $font-weight-bold;
            text-transform: none;
            justify-content: space-between;
            width: 100%;
            text-align: left;
            padding: $spacer-4 $spacer-4;
            border: none;

            .vs-icon {
                fill: $color-pink;
            }

            &::after {
                display: none;
            }

            &:hover {
                color: $color-white;
                border: none;
                box-shadow: none;

                .vs-icon {
                    fill: $color-white;
                }

                &::after {
                    display: none;
                }
            }

            &:focus {
                box-shadow: inset 0px 0px 0px 2px $color-theme-primary;
                background: $color-white;
            }

            @include media-breakpoint-up(lg) {
                font-size: $font-size-6;
                font-weight: $font-weight-normal;
                padding: $spacer-2 $spacer-4;
            }
        }

        @include media-breakpoint-up(lg) {
            padding: $spacer-2 0;
        }
    }
</style>
