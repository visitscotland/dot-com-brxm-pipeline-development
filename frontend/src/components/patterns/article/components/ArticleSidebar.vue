<template>
    <div
        class="vs-article-sidebar"
        :class="sidebarAlignClass"
        data-test="vs-article-sidebar"
    >
        <div
            :class="sidebarImgClasses"
            class="vs-article-sidebar__img-wrapper"
        >
            <!-- @slot Slot to contain an image for this article section -->
            <slot name="vsArticleSidebarImg" />
        </div>
        <div
            v-if="!!this.$slots['vsArticleSidebarQuote']"
            :class="sidebarArticleClasses"
            class="vs-article-sidebar__quote-wrapper"
        >
            <!-- @slot Slot to contain an quote for this article section -->
            <slot name="vsArticleSidebarQuote" />
        </div>
    </div>
</template>

<script>
/**
 * The ArticleSidebar component is used with an ArticleSection to hold an image or quote
 * which will alternate being displayed left or right of the section.
 *
 * @displayName Article Sidebar
 */
export default {
    name: 'VsArticleSidebar',
    status: 'prototype',
    release: '0.0.1',
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
                ? 'vs-article-sidebar--right'
                : 'vs-article-sidebar--left';
        },
        sidebarImgClasses() {
            return !this.$slots.vsArticleSidebarQuote
                ? 'pb-8'
                : '';
        },
        sidebarArticleClasses() {
            return !this.$slots.vsArticleSidebarImg
                ? 'pt-0'
                : '';
        },
    },
};
</script>

<style lang="scss">
.vs-article-sidebar {
    background: $color-white;

    &__quote-wrapper{
        background: $color-white;
        margin-left: -1px;
        padding: $spacer-8 $spacer-6;

        @include media-breakpoint-up(md) {
            margin-left: 0;
            padding: $spacer-8 $spacer-0 $spacer-1;
        }
    }

    @include media-breakpoint-up(md) {
        &--right{
            .vs-article-sidebar__quote-wrapper{
                padding-left: $spacer-0;
                padding-right: $spacer-8;
            }
        }

        &--left{
            .vs-article-sidebar__quote-wrapper{
                padding-left: $spacer-8;
                padding-right: $spacer-0;
            }
        }
    }
}
</style>
