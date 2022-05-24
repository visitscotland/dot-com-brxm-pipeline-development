## Usage
The article component should be used when there is significant amount of content to display about a particular topic. Within an article, the content can be split up into multiple sections which can have a sidebar containing an image and/or a quote.  

Multiple articles can be used on a page to help break lots of content up into bigger parts or topics. Each article must have a title and can include optional introduction text and cover image. 

### Standard
A standard article with a cover image, title, introduction text and one section that includes an image and quote.
```jsx
<VsArticle
    title="The mountain with its head in the clouds"
>
    <template slot="vsArticleImg">
        <VsImageWithCaption
            altText="Ben Nevis"
            image-src="../../../fixtures/article/images/nevis.jpg"
        >
            <VsCaption slot="img-caption">
                <span slot="caption">
                    Ben Nevis
                </span>

                <span slot="credit">
                    © John McSporran
                </span>
            </VsCaption>
        </VsImageWithCaption>
    </template>

    <template slot="vsArticleIntro">
        Ben Nevis requires little introduction. With a wild heart, 
        an adventurous spirit and a flair for drama, the legendary peak towers above 
        glistening lochans and deep glacial valleys. In Scotland, you can't get 
        any higher than this.
    </template>

    <VsArticleSection sidebar-align="left">
        <template slot="articleSidebar">
            <VsArticleSidebar sidebar-align="left">
                <template slot="vsArticleSidebarImg">
                    <VsImageWithCaption
                        altText="Mountain stream"
                        image-src="../../../fixtures/article/images/mountain-stream.jpg"
                    >
                        <VsCaption slot="img-caption">
                            <span slot="caption">
                                Mountain stream
                            </span>

                            <span slot="credit">
                                © CutMedia / Chris Rowland
                            </span>
                        </VsCaption>
                    </VsImageWithCaption>
                </template>
                <template slot="vsArticleSidebarQuote">
                    <VsQuote>
                        <p slot="quoteContent">
                            Scotland’s largest mountain was once a massive active volcano which exploded 
                            and collapsed inwards on itself millions of years ago.
                        </p>
                        <span slot="quoteAuthorName">
                            Penny
                        </span>
                        <span slot="quoteAuthorTitle">
                            Visitor Services Advisor at Fort William iCentre
                        </span>
                    </VsQuote>
                </template>
            </VsArticleSidebar>
        </template>

        <VsHeading level="3">
            Experiencing Ben Nevis
        </VsHeading>

        <VsHeading level="6">
            Scotland's landscape is scattered with Munros and mist-shrouded hills...
        </VsHeading>

        <p>
            But Ben Nevis is the king of them all. In the north west Highlands, near the town of Fort William and part of the Grampian Mountain range, the famous peak attracts 125k walkers a year. Whether you're an avid ambler or you just love beautiful landscapes, bagging 'the Ben' is likely to feature near the top of your Scottish bucket list.
        </p>

        <p>
            An ancient giant of the land, Ben Nevis was once a massive active volcano which exploded and collapsed inwards on itself millions of years ago. At the summit, there is evidence of an explosion in the form of light-coloured granite. The name itself has two translations from the ancient Gaelic language, meaning 'mountain with its head in the clouds', thanks to its iconic mist-shrouded peak, or it can also mean 'venomous mountain' – you can decide which translation you prefer after the climb!
        </p>

        <p>
            Read on for an overview of walking routes up the mountain, or <VsLink href="#">visit Walk Highlands</VsLink> for detailed maps, difficulty levels and walking advice.
        </p>

        <p>
            Remember it's never 'easy' to bag a Scottish Munro or Corbett. You'll need a good amount of hillwalking experience, fitness, hill craft and navigation skills using a map and compass, before attempting any Scottish mountains, even more so in winter.
        </p>

        <p>
            Looking for something different? Try these <VsLink href="#">9 alternative must-climb hills to Ben Nevis</VsLink> in Scotland.
        </p>
    </VsArticleSection>
</VsArticle>
  ```

### Header Options
The cover image and introduction are optional for an article item.

```jsx
<VsArticle
    title="Routes to the Summit"
>
    <VsArticleSection sidebar-align="left">
        <template slot="articleSidebar">
            <VsArticleSidebar sidebar-align="left">
                <template slot="vsArticleSidebarQuote">
                    <VsQuote>
                        <p slot="quoteContent">
                            This spectacular route can take between 10 – 11 hours with scrambles across boulders. 
                            It requires a good head for heights and careful navigation across the trickier exposed sections.
                        </p>
                    </VsQuote>
                </template>
            </VsArticleSidebar>
        </template>

        <VsHeading level="3">
            The Mountain Track
        </VsHeading>

        <VsHeading level="6">
            Best route for: Beginners
        </VsHeading>

        <p>
            The easiest route up the mountain, the track begins at the Glen Nevis Visitor Centre car park, at Achintee on the east side of Glen Nevis about 2 km from the town centre of Fort William, and approximately 20 m above sea level. The track starts with a steep climb to the halfway lochan', or Lochan Meall an t-Suidhe, and then the ascent features snaking zig-zag paths up to the summit.
        </p>
        <p>
            At the summit, there's a cairn that marks the highest point and your reward on a clear day will be the incredible 360° panoramic vistas which can stretch as far as Northern Ireland. From the top, see if you can point out other peaks including the Torridon hills, Ben Lomond and Morven at Caithness.
        </p>
        <p>
            A unique feature of the summit is the Old Observatory, which was opened in 1883. It provided hourly meteorological data for almost 20 years, recording some of the UK's most useful information about mountain weather to date. It closed in 1904 and it now lies in ruin, but can be used for shelter in emergencies.
        </p>
    </VsArticleSection>
</VsArticle>
```



### Sidebar
Each `ArticleSection` can have an `ArticleSidebar` that can include one image, one quote or one of each. 

The alignment of the sidebar should alternate between right and left by setting the `sidebarAlign` prop on both the section and sidebar components. If a section has no image or quote, the text should be left aligned.  

On smaller devices, the sidebar is displayed above the related content in its section. 

  ```jsx
<VsArticle
    title="Frequently asked questions"
>
    <template slot="vsArticleIntro">
        Turn your Munro-bagging dreams into reality and find out everything you need to know before you lace up your walking boots. Remember, discovering the sheer magic of Scotland's most famous mountain is all the more fun when you're completely prepared for anything.
    </template>

    <VsArticleSection sidebar-align="left">
        <template slot="articleSidebar">
            <VsArticleSidebar sidebar-align="left">
                <template slot="vsArticleSidebarImg">
                    <VsImageWithCaption
                        altText="Nevis ridge"
                        image-src="../../../fixtures/article/images/nevis-ridge.jpg"
                    >
                        <VsCaption slot="img-caption">
                            <span slot="caption">
                                Nevis ridge
                            </span>

                            <span slot="credit">
                                © iStockphoto.com / fotoVoyager
                            </span>
                        </VsCaption>
                    </VsImageWithCaption>
                </template>
                <template slot="vsArticleSidebarQuote">
                    <VsQuote>
                        <p slot="quoteContent">
                            Scotland’s largest mountain was once a massive active volcano which exploded 
                            and collapsed inwards on itself millions of years ago.
                        </p>
                        <span slot="quoteAuthorName">Penny</span>
                        <span slot="quoteAuthorTitle">
                            Visitor Services Advisor at Fort William iCentre
                        </span>
                    </VsQuote>
                </template>
            </VsArticleSidebar>
        </template>

        <VsHeading level="3">
            Plan your Ben Nevis adventure
        </VsHeading>

        <VsHeading level="6">
            How long does it take to climb Ben Nevis?
        </VsHeading>

        <p>
            It really depends on your level of fitness, the weather conditions and how many breaks you take to admire the views. It will usually take between 7 - 9 hours to complete following the Mountain Track, with an approximate ascent of 3.5 - 4.5 hours to the summit.
        </p>

        <VsHeading level="6">
            How tough is it to climb?
        </VsHeading>
        <p>
            It's a long and arduous climb and you might have stiff legs the following day, but the feeling of accomplishment when you scale the tallest mountain in the United Kingdom is pretty tough to beat.
        </p>

        <VsHeading level="6">
            How high is it?
        </VsHeading>
        <p>
            A lofty 1,345 m. To put it into perspective, the London Eye stands at 135 m and Big Ben at 96 m high.
        </p>

        <VsHeading level="6">
            Do I need a map and a compass?
        </VsHeading>
        <p>
            Although the Mountain Track is reasonably easy to follow on a clear day, it's essential to have both a map and a compass and know how to use them especially if there is poor visibility during the climb.
        </p>

        <VsHeading level="6">
            Can I camp at Ben Nevis?
        </VsHeading>
        <p>
            Although the Mountain Track is reasonably easy to follow on a clear day, it's essential to have both a map and a compass and know how to use them especially if there is poor visibility during the climb.
        </p>
    </VsArticleSection>

    <VsArticleSection sidebar-align="right">
        <template slot="articleSidebar">
            <VsArticleSidebar sidebar-align="right">
                <template slot="vsArticleSidebarImg">
                    <VsImageWithCaption
                        altText="Rock climbing"
                        image-src="../../../fixtures/article/images/rock-climbing.jpg"
                    >
                        <VsCaption slot="img-caption">
                            <span slot="caption">
                                Rock climbing
                            </span>

                            <span slot="credit">
                                © Sportscotland / Glenmore Lodge
                            </span>
                        </VsCaption>
                    </VsImageWithCaption>
                </template>
            </VsArticleSidebar>
        </template>

        <VsHeading level="6">
            What will the weather be like?
        </VsHeading>
        <p>
            The weather on Ben Nevis is extremely changeable, with glorious sunshine one moment then fog and gale force winds the next. Even if you set out on the sunniest of days, the temperatures at the summit can be at sub-zero, so it’s important to take appropriate all-weather gear. Always check the mountain weather page before you go, and if you’re in any doubt, always turn back.
        </p>
        <p>
            Watch the live conditions and weather from Ben Nevis with the HD webcam, which is situated at Tomacharich, Fort William.
        </p>

        <VsHeading level="6">
            What should I bring?
        </VsHeading>
        <p>
            Warm and waterproof clothing is essential, and it’s wise to avoid cotton as it absorbs moisture. A pair of good walking boots are also an absolute must. Don’t forget to pack a few useful hiking tools, such as a map, compass, torch, whistle, and food and water.
        </p>
        <p>
            Always remember to bring all of your litter back down the mountain with you. There are no bins on Ben Nevis so it’s important to take care of the landscape.
        </p>
        <p>
            Find out more about the Nevis Landscape Partnership, an organisation that works to preserve the Mountain Track for future generations.
        </p>
    </VsArticleSection>

    <VsArticleSection sidebar-align="right">
        <VsHeading level="6">
            How do I climb Ben Nevis safely?
        </VsHeading>
        <VsList>
            <li>
                The difficulty of this hike is often under-estimated so always be prepared and take the walk at your own pace. If you are not confident in your own sense of direction, there are local guided walking tours available. Remember this is the UK's highest mountain!
            </li>
            <li>
                Make sure someone knows where you're headed and ensure that you have plenty of time to get back well before nightfall.
            </li>
            <li>
                Any ascent in snow requires a high degree of fitness, winter equipment and the skills to use them and mountaineering and navigation experience. Bear in mind snow can cover parts of the 'tourist' path into the summer months. If you're at all unsure, local mountain guides can advise and guide you to the summit and back.
            </li>
            <li>
                Make sure you fill in a mountain safety route card and leave it with someone you trust.
            </li>
        </VsList>
    </VsArticleSection>
</VsArticle>

  ```