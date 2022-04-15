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
                        <!-- @slot Slot to contain the header image for this article -->
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
                                        <!-- @slot Slot to contain the intro for this article -->
                                        <slot name="vsArticleIntro" />
                                    </VsRichTextWrapper>
                                </div>

                                <div class="vs-article__content">
                                    <!-- @slot Default slot to contain the main
                                    content for this article -->
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
 * The Article component is used for long content sections and is made up of ArticleSections
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
            <VsArticleSidebar sidebar-align="left">
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
</VsArticle>

<VsArticle>
    <template slot="vsArticleTitle">
        Routes to the Summit
    </template>

    <template slot="vsArticleIntro">
        There are two main walking routes up Ben Nevis. The Mountain Track
        (sometimes called the Tourist Track or the Pony Track) is used by most walkers,
        whilst the Carn Mor Dearg Arête route presents a more challenging climb for
        more experienced hikers.
    </template>

    <VsArticleSection sidebar-align="left">
        <template slot="articleSidebar">
            <VsArticleSidebar sidebar-align="left">
                <template slot="vsArticleSidebarQuote">
                    <VsQuote>
                        <p slot="quoteContent">
                            Scotland’s largest mountain was once a massive active volcano
                            which exploded and collapsed inwards on itself millions of years ago.”
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
            Carn Mor Dearg Arête
        </VsHeading>

        <VsHeading level="6">
            Best route for: Experienced hillwalkers
        </VsHeading>

        <p>
            Carn Mor Dearg Arête is the mountain’s other walking route , a challenging
            ridge climb which should only be attempted by experienced scramblers
            and physically-fit hill walkers. Though demanding, the route rewards
            walkers with the finest possible views of the mountain’s North face.
            Starting from the North Face car park at Torlundy, the trail traverses
            not one but two Munros, the Carn Mor Dearg and Ben Nevis. It can also
            be reached by following the Mountain Track to the ‘halfway lochan’,
            then taking the left fork whilst the right fork continues along the
            Mountain Track. You’ll pass the CIC Hut, a private shelter for mountaineers.
            A longer and more strenuous walk than the Mountain Track, this spectacular
            route can take between 10-11 hours with scrambles across boulders.
            It requires a good head for heights and careful navigation across the
            trickier exposed sections.
        </p>
    </VsArticleSection>

    <VsArticleSection sidebar-align="right">
        <template slot="articleSidebar">
            <VsArticleSidebar sidebar-align="right">
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
                            Scotland’s largest mountain was once a massive active volcano
                            which exploded and collapsed inwards on itself millions of years ago.”
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
            How tough is it to climb?
        </VsHeading>

        <p>
            It's a long and arduous climb and you might have stiff legs the following
            day, but the feeling of accomplishment when you scale the
            tallest mountain in the United Kingdom is pretty tough to beat.
        </p>

        <VsHeading level="6">
            How high is it?
        </VsHeading>

        <p>
            A lofty 1,345 m. To put it into perspective,
            the London Eye stands at 135 m and Big Ben at 96 m high.
        </p>

        <VsHeading level="6">
           Do I need a map and a compass?
        </VsHeading>

        <p>
            Although the Mountain Track is reasonably easy to follow on a clear day,
            it's essential to have both a map and a compass and know how to use them
            especially if there is poor visibility during the climb.
        </p>
    </VsArticleSection>
</VsArticle>

  ```
</docs>
