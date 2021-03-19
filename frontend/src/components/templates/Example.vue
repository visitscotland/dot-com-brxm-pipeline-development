<template>
    <div>
        <VsHero
            image-src="https://cimg.visitscotland.com/cms-images/holidays/new/camping-beach-dunes-dog?size=md"
            image-align-v="40%"
            type="section"
            display-text="Responsive template"
            display-text-sub="2 columns"
            display-align-v="top"
        />

        <VsContainer>
            <VsRow>
                <VsCol
                    lg="8"
                    xl="9"
                >
                    <VsHeading
                        display="4"
                        level="1"
                        sub="Holidays"
                    >
                        Pet friendly holidays in Scotland
                    </VsHeading>
                </VsCol>
            </VsRow>
            <VsRow>
                <VsArticle
                    md="7"
                    lg="8"
                    xl="9"
                >
                    <VsContentSection lead>
                        <p>
                            Have you ever been on holiday and not enjoyed it because your furry -
                            and, let's face it, quite possibly favourite (!) - family member was
                            left behind at home?
                        </p>
                        <p>
                            Here is a
                            <a
                                href="https://www.visitscotland.com/holidays-breaks/pet-friendly/"
                            >test link</a>
                            in the lead section.
                        </p>
                    </VsContentSection>

                    <VsContentSection>
                        <p>
                            Scotland's got everything you'll need for a relaxing pet friendly
                            holiday, with dog friendly attractions and scenic walking routes, as
                            well as lots of pubs and cafés where your pooch will feel right at home!
                        </p>
                        <p>
                            Whether you're looking for a cat or dog friendly hotel or somewhere with
                            on-site stables so that you can take your horse on holiday, Scotland
                            offers a wonderful range of pet friendly accommodation options that will
                            make your trip really special.
                        </p>
                        <p>
                            Check out our
                            <a
                                title="10 dog-friendly accommodations that will get your tail
                                 wagging"
                                href="http://ebooks.visitscotland.com/accommodation-guide/12/"
                            >10 dog friendly accommodations that will get your tail wagging!</a>
                        </p>
                    </VsContentSection>
                </VsArticle>

                <VsSidebar
                    md="5"
                    lg="4"
                    xl="3"
                >
                    <VsContentSection
                        tag="aside"
                        heading="11 pawesome ideas for dog friendly days out"
                    >
                        <p>
                            <!-- <b-link
                title="Dog friendly days out"
                href="/blog/activities/dog-friendly-days-out-in-scotland/"
              > -->
                            <VsImg
                                src="https://cimg.visitscotland.com/cms-images/holidays/casper-dunskey?size=sm"
                                alt="Casper dog at Dunskey Castle.jpg"
                                fluid
                            />
                            <!-- </b-link> -->
                        </p>
                        <p>
                            Get more&nbsp;inspiration from this paw-fect selection of
                            <a
                                title="Dog friendly days out"
                                href="/blog/activities/dog-friendly-days-out-in-scotland/"
                            >
                                activities for you and your pooch</a>&nbsp;- they've been
                            tried and tested by our four-legged friends over on Instagram!
                        </p>
                    </VsContentSection>
                </VsSidebar>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import {
    VsContainer, VsRow, VsCol,
} from '../elements/layout';
import VsHeading from '../elements/heading/Heading';
import VsImg from '../elements/img/Img';
import VsHero from '../patterns/hero/Hero';
import VsArticle from '../patterns/article/Article';
import VsSidebar from '../patterns/sidebar/Sidebar';
import VsContentSection from '../patterns/content-section/ContentSection';

/**
 * An example page template.
 *
 * @displayName Example
 */
export default {
    name: 'VsExampleTemplate',
    status: 'prototype',
    release: '0.0.1',
    metaInfo: {
        title: 'Template example',
        htmlAttrs: {
            lang: 'en',
        },
    },
    components: {
        VsContainer,
        VsRow,
        VsCol,
        VsImg,
        VsHero,
        VsHeading,
        VsArticle,
        VsSidebar,
        VsContentSection,
    },
};
</script>

<docs>
  ```jsx
  <VsExampleTemplate />
  ```
</docs>
