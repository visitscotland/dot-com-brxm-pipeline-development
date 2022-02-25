<template>
    <BTab
        class="vs-tab__item"
        data-test="vs-tab__item"
        title-link-class="vs-tab-link"
        :title="title"
    >
        <VsHeading
            level="3"
            class="vs-tab__item__heading"
        >
            {{ title }}
        </VsHeading>
        <!-- @slot default slot for tab item body content -->
        <slot />
    </BTab>
</template>

<script>

import VsHeading from '@components/elements/heading/Heading';
import { BTab } from 'bootstrap-vue';
/**
 * Tab item for use within the Tabs component.
 *
 * @displayName Tab Item
 */
export default {
    name: 'VsTabItem',
    components: {
        BTab,
        VsHeading,
    },
    props: {
        /**
         * Title of the tab button
         */
        title: {
            type: String,
            required: true,
        },
    },
};
</script>

<style lang="scss">
    .vs-tabs{
        .nav-item{
            flex: auto;
            align-self: flex-end;
        }

        .vs-tab-link.nav-link{
            background-color: $color-secondary-gray-tint-6;
            color: $color-base-text;
            font-weight: $font-weight-bold;
            border-color: $color-secondary-gray-tint-6 $color-secondary-gray-tint-6 $color-white;
            padding: $spacer-3;
            font-size: $font-size-lg;

            @include media-breakpoint-up(sm) {
                padding: $spacer-5;
                font-size: $display3-size;
            }

            &:focus-visible{
                outline: 2px solid $color-secondary-teal-tint-3;
                outline-offset: 0;
            }

            &.active{
                position: relative;
                z-index: 2;
                background-color: $color-white;
                padding: $spacer-5 $spacer-3;

                @include media-breakpoint-up(sm) {
                    padding: $spacer-7 $spacer-5;
                }
            }
        }

        .vs-tab__item{
            background-color: $color-white;
            color: $color-base-text;
            text-align: left;

            &__heading{
                display: none;
            }
        }
    }

@include no-js {
    .vs-tabs{
        .tab-content > .tab-pane{
            position: relative;
            display: block!important;
            margin-bottom: $spacer-9;
        }

        .nav-tabs{
            display: none;
        }

        .vs-tab__item{
            &__heading{
                display: block;
                margin: 0;
                background-color: $color-white;
                padding: $spacer-7 $spacer-5;
                font-family: $font-family-sans-serif;
                color: $color-base-text;
                font-size: $display3-size;
                font-weight: $font-weight-bold;
                text-align: center;
                letter-spacing: normal;
            }
        }
    }
}
</style>

<docs>
  ```js
  <VsContainer class="mt-7">
        <VsRow>
            <VsCol>
                <VsTabs>
                    <VsTabItem title="First" active>
                        <div class="px-5 py-4">
                            <p>
                                Lorem ipsum dolor sit amet, consectetur adipiscing
                                elit. Sed porta nec mauris id vulputate. Nullam hendrerit
                                libero pharetra dolor iaculis, ac feugiat nisi pharetra.
                                Aliquam erat volutpat.
                            </p>
                            <p>
                                Suspendisse rhoncus turpis vel felis consectetur consectetur.
                                Pellentesque justo dolor, accumsan eget tincidunt sit amet,
                                ultricies tempus urna. Pellentesque convallis tincidunt
                                pellentesque. Donec rhoncus mi non dolor lacinia dapibus.
                                Aenean vehicula tempor fringilla. Morbi nibh nulla, ornare nec
                                lacus at, gravida pharetra mi.
                            </p>
                        </div>
                    </VsTabItem>
                    <VsTabItem title="Second">
                        <div class="px-5 py-4">
                            <p>
                                Proin vitae ultrices nibh. Nulla scelerisque mi vitae cursus
                                vulputate. In hac habitasse platea dictumst. Praesent faucibus
                                sit amet magna nec imperdiet. Donec a tristique est,
                                eu imperdiet elit.
                            </p>

                            <p>
                                Pellentesque auctor eu lorem ut aliquet. Nulla lobortis
                                mauris ligula, sit amet volutpat purus tincidunt at. Fusce
                                dui sapien, malesuada apellentesque at, placerat vel nisi.
                                Aliquam sit amet velit maximus risus lacinia
                                aliquam eget ut arcu.
                            </p>
                        </div>
                    </VsTabItem>
                </VsTabs>
            </VsCol>
        </VsRow>
    </VsContainer>
  ```
</docs>
