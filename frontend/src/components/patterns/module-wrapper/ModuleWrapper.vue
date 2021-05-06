<template>
    <section class="vs-module-wrapper">
        <VsContainer>
            <VsRow>
                <VsCol
                    cols="10"
                    offset="1"
                    sm="8"
                    offset-sm="2"
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
            margin-bottom: $spacer-9;

            p:last-of-type {
                margin-bottom: 0;
            }
        }

        @include media-breakpoint-up(sm) {
            padding-top: $spacer-12;
            padding-bottom: $spacer-12;
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
    ```
</docs>
