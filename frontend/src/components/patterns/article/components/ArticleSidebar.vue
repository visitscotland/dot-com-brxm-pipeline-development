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

<docs>
  ```jsx

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
