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
import { VsCol, VsRow } from '@components/elements/layout';
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

<docs>
  ```jsx
<VsArticle>
    <template slot="vsArticleImg">
        <VsImageWithCaption
            v-for="(item, index) in imageWithCaption.imageExamples.fullwidth"
            :altText="item.altText"
            :closedDefaultCaption="item.isSmall"
            :image-src="item.imageSrc"
            :key="`fullwidth1-${index}`"
        >
            <VsCaption
                slot="img-caption"
                variant="fullwidth"
            >
                <span slot="caption" v-if="item.caption">
                    {{ item.caption }}
                </span>

                <span slot="credit" v-if="item.credit">
                    {{ item.credit }}
                </span>
            </VsCaption>
        </VsImageWithCaption>
    </template>

    <template slot="vsArticleTitle">
        The mountain with its head in the clouds means that its raining
    </template>

    <template slot="vsArticleIntro">
        There are two main walking routes up Ben Nevis. The Mountain Track
        (sometimes called the Tourist Track or the Pony Track) is used by most walkers,
        whilst the Carn Mor Dearg Arête route presents a more challenging climb
        for more experienced hikers.
    </template>

    <VsArticleSection sidebar-align="left">
        <template slot="articleSidebar">
            <VsArticleSidebar>
                <template slot="vsArticleSidebarImg">
                    <VsImageWithCaption
                        altText="An image"
                        image-src="fixtures\image-with-caption\images\city-country-breaks.jpg"
                    >
                        <VsCaption
                            slot="img-caption"
                        >
                            <span slot="caption">
                                A nice image
                            </span>

                            <span slot="credit">
                                &copy; VisitScotland
                            </span>
                        </VsCaption>
                    </VsImageWithCaption>
                </template>
                <template slot="vsArticleSidebarQuote">
                    <VsQuote>
                        <p slot="quoteContent">
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi
                            ac urna non metus tempor accumsan ut non risus. In turpis est,
                            imperdiet eu sagittis ac, sodales quis nunc. Ut sagittis vulputate
                            lacinia. Vivamus faucibus lorem leo, nec laoreet ligula auctor a.
                            Donec id eros a ipsum facilisis lacinia nec ac nunc.
                        </p>
                        <span slot="quoteAuthorName">Penny</span>
                        <span slot="quoteAuthorTitle">
                            Visitor Services Advisor at Edinburgh iCentre
                        </span>
                    </VsQuote>
                </template>
            </VsArticleSidebar>
        </template>

        <VsHeading level="3">
            Experiencing Ben Nevis
        </VsHeading>

        <VsHeading level="6">
            How do I climb Ben Nevis safely?
        </VsHeading>

        <p>
            The difficulty of this hike is often under-estimated so always be
            prepared and take the walk at your own pace. If you are not confident in your
            own sense of direction, there are local guided walking tours available.
            Remember this is the UK's highest mountain!
        </p>
        <p>
            Make sure someone knows where you're headed and ensure that you have
            plenty of time to get back well before nightfall.
        </p>
        <p>
            Any ascent in snow requires a high degree of fitness, winter equipment and the
            skills to use them and mountaineering and navigation experience. Bear in mind
            snow can cover parts of the 'tourist' path into the summer months. If you're at
            all unsure, local mountain guides can advise and guide you to the summit and back.
        </p>
        <p>
            Make sure you fill in a mountain safety route card and leave it with someone you trust.
            For more information on keeping safe on Ben Nevis, see these tips from
            Mountaineering Scotland and Walk Highlands.
        </p>

        <VsHeading level="6">
            Is Ben Nevis suitable for children to climb?
        </VsHeading>
        <p>
            Absolutely, as long as they are prepared for all weather conditions and keen for
            a challenge. The Carn Mor Dearg Arete route is generally not advised for children.
        </p>
    </VsArticleSection>

    <VsArticleSection sidebar-align="right">
        <template slot="articleSidebar">
            <VsArticleSidebar>
                <template slot="vsArticleSidebarImg">
                    <VsImageWithCaption
                        altText="An image"
                        image-src="fixtures\image-with-caption\images\city-country-breaks.jpg"
                    >
                        <VsCaption
                            slot="img-caption"
                        >
                            <span slot="caption">
                                A nice image
                            </span>

                            <span slot="credit">
                                &copy; VisitScotland
                            </span>
                        </VsCaption>
                    </VsImageWithCaption>
                </template>
            </VsArticleSidebar>
        </template>

        <VsHeading level="6">
            How do I climb Ben Nevis safely?
        </VsHeading>

        <p>
            The difficulty of this hike is often under-estimated so always be
            prepared and take the walk at your own pace. If you are not confident in your
            own sense of direction, there are local guided walking tours available.
            Remember this is the UK's highest mountain!
        </p>
        <p>
            Make sure someone knows where you're headed and ensure that you have
            plenty of time to get back well before nightfall.
        </p>
        <p>
            Any ascent in snow requires a high degree of fitness, winter equipment and the
            skills to use them and mountaineering and navigation experience. Bear in mind
            snow can cover parts of the 'tourist' path into the summer months. If you're at
            all unsure, local mountain guides can advise and guide you to the summit and back.
        </p>
        <p>
            Make sure you fill in a mountain safety route card and leave it with someone you trust.
            For more information on keeping safe on Ben Nevis, see these tips from
            Mountaineering Scotland and Walk Highlands.
        </p>

        <VsHeading level="6">
            Is Ben Nevis suitable for children to climb?
        </VsHeading>
        <p>
            Absolutely, as long as they are prepared for all weather conditions and keen for
            a challenge. The Carn Mor Dearg Arete route is generally not advised for children.
        </p>
    </VsArticleSection>

    <VsArticleSection sidebar-align="right">
        <VsHeading level="6">
            How do I climb Ben Nevis safely?
        </VsHeading>

        <p>
            The difficulty of this hike is often under-estimated so always be
            prepared and take the walk at your own pace. If you are not confident in your
            own sense of direction, there are local guided walking tours available.
            Remember this is the UK's highest mountain!
        </p>
        <p>
            Make sure someone knows where you're headed and ensure that you have
            plenty of time to get back well before nightfall.
        </p>
        <p>
            Any ascent in snow requires a high degree of fitness, winter equipment and the
            skills to use them and mountaineering and navigation experience. Bear in mind
            snow can cover parts of the 'tourist' path into the summer months. If you're at
            all unsure, local mountain guides can advise and guide you to the summit and back.
        </p>
        <p>
            Make sure you fill in a mountain safety route card and leave it with someone you trust.
            For more information on keeping safe on Ben Nevis, see these tips from
            Mountaineering Scotland and Walk Highlands.
        </p>

        <VsHeading level="6">
            Is Ben Nevis suitable for children to climb?
        </VsHeading>
        <p>
            Absolutely, as long as they are prepared for all weather conditions and keen for
            a challenge. The Carn Mor Dearg Arete route is generally not advised for children.
        </p>
    </VsArticleSection>

    <VsArticleSection sidebar-align="right">
        <template slot="articleSidebar">
            <VsArticleSidebar>
                <template slot="vsArticleSidebarQuote">
                    <VsQuote>
                        <p slot="quoteContent">
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi
                            ac urna non metus tempor accumsan ut non risus. In turpis est,
                            imperdiet eu sagittis ac, sodales quis nunc. Ut sagittis vulputate
                            lacinia. Vivamus faucibus lorem leo, nec laoreet ligula auctor a.
                            Donec id eros a ipsum facilisis lacinia nec ac nunc.
                        </p>
                        <span slot="quoteAuthorName">Penny</span>
                        <span slot="quoteAuthorTitle">
                            Visitor Services Advisor at Edinburgh iCentre
                        </span>
                    </VsQuote>
                </template>
            </VsArticleSidebar>
        </template>

        <VsHeading level="6">
            How do I climb Ben Nevis safely?
        </VsHeading>

        <p>
            The difficulty of this hike is often under-estimated so always be
            prepared and take the walk at your own pace. If you are not confident in your
            own sense of direction, there are local guided walking tours available.
            Remember this is the UK's highest mountain!
        </p>
        <p>
            Make sure someone knows where you're headed and ensure that you have
            plenty of time to get back well before nightfall.
        </p>
        <p>
            Any ascent in snow requires a high degree of fitness, winter equipment and the
            skills to use them and mountaineering and navigation experience. Bear in mind
            snow can cover parts of the 'tourist' path into the summer months. If you're at
            all unsure, local mountain guides can advise and guide you to the summit and back.
        </p>
        <p>
            Make sure you fill in a mountain safety route card and leave it with someone you trust.
            For more information on keeping safe on Ben Nevis, see these tips from
            Mountaineering Scotland and Walk Highlands.
        </p>

        <VsHeading level="6">
            Is Ben Nevis suitable for children to climb?
        </VsHeading>
        <p>
            Absolutely, as long as they are prepared for all weather conditions and keen for
            a challenge. The Carn Mor Dearg Arete route is generally not advised for children.
        </p>
    </VsArticleSection>
</VsArticle>

  ```
</docs>
