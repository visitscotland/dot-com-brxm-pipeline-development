<template>
    <VsCol
        class="article"
        data-test="vs-article"
        :tag="tag"
        v-bind="$attrs"
    >
        <slot />
    </VsCol>
</template>

<script>
import { VsCol } from '@components/elements/layout';

/**
 * The Article component is a wrapper for article content, generally wrapped in content sections
 *
 * @displayName Article
 */
export default {
    name: 'VsArticle',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsCol,
    },
    props: {
        /**
         * The html element name used for the component
         */
        tag: {
            type: String,
            default: 'article',
        },
    },
};
</script>

<style lang="scss"></style>

<docs>
  ```jsx

<VsContainer>
    <VsRow>
        <VsArticle md="12" xl="8" >
            <VsContentSection title="First section" lead>
                <p>This is the lead section of the article. </p>
                <p>This article has breakpoint-specific column widths,
                meaning it's width is dynamic according to the xs, sm, md,
                lg, xl and xxl attributes applied to it and the browser width.</p>
            </VsContentSection>
            <VsContentSection title="Second section">
                <p>This is the second section of the article.</p>

                <p>Theme whimsical whimsical colorful simple organized
                darn cute, fun adventure simple organized. Traveler wanderlust modern
                blogger fun, website wanderlust theme design fun. Design darn colorful
                whimsical traveling wanderlust whimsical traveler clean, colorful
                website pretty excursion simple.</p>

                <p>Travel Travel wanderlust adventure colorful traveling modern Travel expedition.
                Theme Travel expedition expedition cute traveler expedition. Excursion
                traveling traveling travelblogger darn clean fun. Adventure website pretty
                WordPress design, design whimsical webdesign expedition whimsical. Design
                traveling darn website simple website pretty traveler.</p>
            </VsContentSection>
        </VsArticle>

        <VsSidebar>
            <VsContentSection tag="aside" heading="Aside section title">
                Sidebar/aside content section
            </VsContentSection>
        </VsSidebar>
    </VsRow>
</VsContainer>

  ```
</docs>
