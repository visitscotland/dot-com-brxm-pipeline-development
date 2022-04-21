<template>
    <div
        class="vs-article-section mb-8 mb-md-10"
        :class="sidebarAlignClass"
        data-test="vs-article-section"
    >
        <VsRow>
            <VsCol
                cols="12"
                md="5"
                xl="4"
                data-test="vs-article-section__sidebar"
                class="col-xxl-4"
                :class="sidebarAlign === 'right' ? 'pr-md-0' : 'pl-md-0'"
                :offset-xl="sidebarAlign === 'right' ? '1' : ''"
                :order-md="sidebarAlign === 'right' ? '2' : ''"
            >
                <!-- @slot Slot to contain the article sidebar -->
                <slot name="articleSidebar" />
            </VsCol>
            <VsCol
                cols="12"
                md="7"
                data-test="vs-article-section__content"
                :offset-xl="sidebarAlign === 'left' ? '1' : ''"
            >
                <div class="mx-6 mx-md-0">
                    <VsRichTextWrapper>
                        <!-- @slot Default slot to contain the copy for this article -->
                        <slot />
                    </VsRichTextWrapper>
                </div>
            </VsCol>
        </VsRow>
    </div>
</template>

<script>
import { VsCol, VsRow } from '@components/elements/grid';
import VsRichTextWrapper from '@components/elements/rich-text-wrapper/RichTextWrapper';
/**
 * The ArticleSection component is used within an Article to alternate paragraphs.
 * Each section can have one ArticleSidebar which is automatically
 * displayed left or right of the section.
 *
 * @displayName Article Section
 */
export default {
    name: 'VsArticleSection',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsCol,
        VsRow,
        VsRichTextWrapper,
    },
    props: {
        /**
         * This sets the alignment of the sidebar left or right of the section
         */
        sidebarAlign: {
            type: String,
            default: 'left',
            validator: (value) => value.match(/(left|right)/),
        },
    },
    computed: {
        sidebarAlignClass() {
            return this.sidebarAlign === 'right'
                ? 'vs-article-section--sidebar-right'
                : 'vs-article-section--sidebar-left';
        },
    },
};
</script>

<style lang="scss">
.vs-article-section {
    &--sidebar-right{
        @include media-breakpoint-up(md) {
            margin-right: -14.8%;
        }

        @include media-breakpoint-up(lg) {
            margin-right: -12.5%;
        }

        @include media-breakpoint-up(xl) {
            margin-right: -13.6%;
        }

        @include media-breakpoint-up(xxl) {
            margin-right: -23.2%;
        }
    }

    &--sidebar-left{
        @include media-breakpoint-up(md) {
            margin-left: -14.8%;
        }

        @include media-breakpoint-up(lg) {
            margin-left: -12.5%;
        }

        @include media-breakpoint-up(xl) {
            margin-left: -13.6%;
        }

        @include media-breakpoint-up(xxl) {
            margin-left: -23.2%;
        }
    }
}
</style>
