<template>
    <div>
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
                </VsArticle>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import {
    VsContainer, VsRow, VsCol,
} from '../elements/grid';
import VsHeading from '../elements/heading/Heading';
import VsArticle from '../patterns/article/Article';

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
        VsHeading,
        VsArticle,
    },
};
</script>

<docs>
  ```jsx
  <VsExampleTemplate />
  ```
</docs>
