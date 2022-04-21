<template>
    <article
        class="vs-article"
        data-test="vs-article"
        v-bind="$attrs"
    >
        <VsContainer>
            <VsRow>
                <VsCol
                    cols="12"
                    class="col-xxl-10 offset-xxl-1"
                >
                    <div class="vs-article__wrapper mb-9 mb-md-11">
                        <!-- @slot Slot to contain the cover image for this article -->
                        <slot name="vsArticleImg" />

                        <VsRow>
                            <VsCol
                                cols="12"
                                md="10"
                                offset-md="1"
                            >
                                <div class="vs-article__header mx-6 mx-md-0 mt-9 mt-lg-11">
                                    <VsHeading
                                        level="2"
                                        class="text-center mb-8 mb-lg-9"
                                    >
                                        <!-- @slot Slot to contain the title for this article -->
                                        <slot name="vsArticleTitle" />
                                    </VsHeading>

                                    <VsRichTextWrapper
                                        variant="lead"
                                        class="text-center mb-9 mb-lg-10"
                                    >
                                        <!-- @slot Slot to contain the introduction
                                        for this article -->
                                        <slot name="vsArticleIntro" />
                                    </VsRichTextWrapper>
                                </div>

                                <div class="vs-article__content">
                                    <!-- @slot Default slot to contain ArticleSections
                                    for this article -->
                                    <slot />
                                </div>
                            </VsCol>
                        </VsRow>
                    </div>
                </VsCol>
            </VsRow>
        </VsContainer>
    </article>
</template>

<script>
import {
    VsCol, VsRow, VsContainer,
} from '@components/elements/grid';
import VsRichTextWrapper from '@components/elements/rich-text-wrapper/RichTextWrapper';
import VsHeading from '@components/elements/heading/Heading';

/**
 * The article component is used for in-depth editorial style
 * content.
 *
 * @displayName Article
 */
export default {
    name: 'VsArticle',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsCol,
        VsRow,
        VsContainer,
        VsRichTextWrapper,
        VsHeading,
    },
};
</script>

<style lang="scss">
.vs-article {
    &__wrapper{
        border: 1px solid $color-gray-tint-5;

        figure{
            margin-bottom: 0;
        }
    }
}
</style>
