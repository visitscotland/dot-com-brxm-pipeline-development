<template>
    <section
        class="vs-module-wrapper"
        data-test="vs-module-wrapper"
        :class="`vs-module-wrapper--${theme}`"
        v-bind="$attrs"
    >
        <VsContainer
            v-if="!!this.$slots['vsModuleWrapperHeading'] || !!this.$slots['vsModuleWrapperIntro']"
        >
            <VsRow>
                <VsCol
                    cols="10"
                    offset="1"
                    md="8"
                    offset-md="2"
                >
                    <VsHeading
                        level="2"
                        class="vs-module-wrapper__heading"
                        v-if="!!this.$slots['vsModuleWrapperHeading']"
                        data-test="vs-module-wrapper__heading"
                    >
                        <!-- @slot Slot to contain heading -->
                        <slot name="vsModuleWrapperHeading" />
                    </VsHeading>
                </VsCol>
                <VsCol
                    cols="12"
                    sm="10"
                    offset-sm="1"
                    md="8"
                    offset-md="2"
                >
                    <VsRichTextWrapper
                        class="vs-module-wrapper__intro"
                        v-if="!!this.$slots['vsModuleWrapperIntro']"
                        data-test="vs-module-wrapper__intro"
                    >
                        <!-- @slot Slot to contain intro text -->
                        <slot name="vsModuleWrapperIntro" />
                    </VsRichTextWrapper>
                </VsCol>
            </VsRow>
        </VsContainer>

        <!-- @slot Default slot for module content -->
        <slot />
    </section>
</template>

<script>
import VsHeading from '@components/elements/heading/Heading';
import VsRichTextWrapper from '@components/elements/rich-text-wrapper/RichTextWrapper';
import {
    VsContainer, VsRow, VsCol,
} from '@components/elements/layout';

/**
* Header intro and button wrapper for module content
*
* @displayName Module Wrapper
*/
export default {
    name: 'VsModuleWrapper',
    status: 'protolink-type',
    release: '0.0.1',
    components: {
        VsHeading,
        VsRichTextWrapper,
        VsContainer,
        VsRow,
        VsCol,
    },
    props: {
        /**
        * Theme of module wrapper to use
        */
        theme: {
            type: String,
            default: 'light',
            validator: (value) => value.match(/(light|grey|dark)/),
        },
    },
};
</script>

<style lang="scss">
    .vs-module-wrapper {
        padding-top: $spacer-9;
        padding-bottom: $spacer-9;
        text-align: center;

        &__heading.vs-heading {
            margin-bottom: $spacer-8;
        }

        &__intro {
            display: block;
            margin-bottom: $spacer-9;

            p:last-of-type {
                margin-bottom: 0;
            }
        }

        @include media-breakpoint-up(sm) {
            padding-top: $spacer-10 + $spacer-2;
            padding-bottom: $spacer-12;
        }

        &--grey {
            background-color: $theme-grey;
        }

        &--dark {
            background-color: $theme-dark;
            color: $color-white;

            .vs-module-wrapper__heading.vs-heading {
                color: $color-yellow;
            }
        }
    }

    .vs-module-wrapper__outer--light + .vs-module-wrapper__outer--light {
        .vs-module-wrapper {
            padding-top: $spacer-4;

            @include media-breakpoint-up(sm) {
                padding-top: $spacer-2;
            }
        }
    }

    // The fixed modules at the bottom of each page (no-js social share, newsletter
    // and otyml) don't use the module-wrapper__outer normally and need these special
    // cases. Reassess as and when those get refactored whether these can be removed.

    .vs-module-wrapper__outer--light + .vs-module-wrapper__outer--hidden +
        .vs-module-wrapper--light,
    .vs-module-wrapper__outer--light + .vs-module-wrapper--light,
    .vs-module-wrapper--light + .vs-module-wrapper--light {
        padding-top: $spacer-4;

        @include media-breakpoint-up(sm) {
            padding-top: $spacer-2;
        }
    }
</style>

<docs>
    ```
        <VsModuleWrapper>
            <template slot="vsModuleWrapperHeading">
                Proin interdum quam non semper consequat
            </template>

            <template slot="vsModuleWrapperIntro">
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                Nullam condimentum eu ligula sed tristique.
                Maecenas et sem non libero gravida vulputate vel sit amet diam.
                Pellentesque cursus ex aliquam aliquam dignissim.
                Nullam dui risus, pulvinar sit amet nibh a, ultrices tempor purus.</p>
                <p>Facilisi. In porttitor congue massa, id porttitor sem mattis sit amet.</p>
            </template>

            <h1>Main content would start here</h1>
        </VsModuleWrapper>

        <VsModuleWrapper theme="dark">
            <template slot="vsModuleWrapperHeading">
                Proin interdum quam non semper consequat
            </template>

            <template slot="vsModuleWrapperIntro">
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                Nullam condimentum eu ligula sed tristique.
                Maecenas et sem non libero gravida vulputate vel sit amet diam.
                Pellentesque cursus ex aliquam aliquam dignissim.
                Nullam dui risus, pulvinar sit amet nibh a, ultrices tempor purus.</p>
                <p>Facilisi. In porttitor congue massa, id porttitor sem mattis sit amet.</p>
            </template>

            <h1>Main content would start here</h1>
        </VsModuleWrapper>

        <VsModuleWrapper theme="grey">
            <template slot="vsModuleWrapperHeading">
                Proin interdum quam non semper consequat
            </template>

            <template slot="vsModuleWrapperIntro">
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                Nullam condimentum eu ligula sed tristique.
                Maecenas et sem non libero gravida vulputate vel sit amet diam.
                Pellentesque cursus ex aliquam aliquam dignissim.
                Nullam dui risus, pulvinar sit amet nibh a, ultrices tempor purus.</p>
                <p>Facilisi. In porttitor congue massa, id porttitor sem mattis sit amet.</p>
            </template>

            <h1>Main content would start here</h1>
        </VsModuleWrapper>
    ```
</docs>
