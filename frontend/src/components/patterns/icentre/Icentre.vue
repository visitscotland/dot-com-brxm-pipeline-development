<template>
    <div
        class="vs-icentre"
        data-test="vs-icentre"
    >
        <VsContainer>
            <VsRow>
                <VsCol
                    cols="10"
                    offset="1"
                    md="8"
                    offset-md="2"
                    lg="6"
                    offset-lg="3"
                    class="vs-icentre__intro-content"
                >
                    <VsSvg
                        class="vs-icentre__logo"
                        path="iCentre"
                    />

                    <div
                        class="vs-icentre__links"
                        data-test="vs-icentre__standalone-links"
                        v-if="!!this.$slots['icentreLinks'] && !this.$slots['icentreQuote']"
                    >
                        <!-- @slot Holds the links (html expected) -->
                        <slot name="icentreLinks" />
                    </div>
                </VsCol>

                <VsCol
                    cols="12"
                    md="10"
                    offset-md="1"
                    :class="{'vs-icentre__image-offset': !!this.$slots['icentreQuote']}"
                >
                    <!-- @slot Holds the main icentre image (vs-image-with-caption expected) -->
                    <slot
                        name="icentreImageWithCaption"
                    />
                </VsCol>

                <VsCol
                    cols="12"
                    md="10"
                    offset-md="1"
                    lg="7"
                    offset-lg="5"
                    class="vs-icentre__quote-block-container"
                    v-if="!!this.$slots['icentreQuote']"
                >
                    <div class="vs-icentre__quote-block">
                        <!-- @slot Optional slot, holds the links (vs-quote expected) -->
                        <slot name="icentreQuote" />

                        <div
                            class="vs-icentre__links-contained"
                            data-test="vs-icentre__embedded-links"
                        >
                            <!-- @slot Holds the links (html expected) -->
                            <slot name="icentreLinks" />
                        </div>
                    </div>
                </VsCol>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import VsSvg from '@components/elements/svg/Svg';
import VsContainer from '@components/elements/layout/Container';
import VsRow from '@components/elements/layout/Row';
import VsCol from '@components/elements/layout/Col';

/**
 * A summary block for an icentre with a name, cover image and an optional slot for an
 * embedded quote (either just text or a vs-quote element)
 *
 * @displayName iCentre
 */
export default {
    name: 'VsIcentre',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsSvg,
        VsContainer,
        VsRow,
        VsCol,
    },
};
</script>

<style lang="scss">
.vs-icentre {
    &__heading {
        margin-bottom: $spacer-9;

        @include media-breakpoint-up(lg) {
            margin-bottom: $spacer-8;
        }
    }

    &__logo {
        height: $spacer-9;
        margin-bottom: $spacer-8;
    }

    &__links {
        font-size: $display4-size;
        margin-bottom: $spacer-9;

        @include media-breakpoint-up(lg) {
            line-height: $spacer-7;
        }
    }

    &__intro-content {
        text-align: center;
    }

    &__image-offset {
        @include media-breakpoint-down(xs) {
            padding: 0;

            figure {
                width: calc(100% + 2em);
                margin-left: -1em;
            }
        }

        @include media-breakpoint-up(lg) {
            margin-bottom: -22rem;
        }
    }

    &__quote-block-container {
        z-index: 2;
        text-align: left;
    }

    &__quote-block {
        background: $white;
        padding: $spacer-8 $spacer-8;

        @include media-breakpoint-up(md) {
            padding: $spacer-9 $spacer-10;
        }
    }

    &__links-contained {
        margin-bottom: $spacer-0;
        margin-top: $spacer-9;
        font-size: $display4-size;

        @include media-breakpoint-up(lg) {
            line-height: $spacer-7;
        }
    }

    figure {
        margin-bottom: $spacer-0;
    }
}
</style>

<docs>
```jsx
    <VsModuleWrapper theme="grey">
        <template slot="vsModuleWrapperHeading">
            A tip from your local experts
        </template>

        <VsIcentre>
            <VsImageWithCaption
                imageSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                altText="This is the alt text"
                slot="icentreImageWithCaption"
            >
                <VsCaption
                    slot="img-caption"
                >
                    <span slot="caption">
                        A test caption
                    </span>

                    <span slot="credit">
                        &copy; VisitScotland
                    </span>
                </VsCaption>
            </VsImageWithCaption>

            <span slot="icentreQuote">
                <VsQuote variant="wide">
                    <VsImg
                        src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                        alt="This is the alt text"
                        data-sizes="auto"
                        class="lazyload"
                        slot="quoteImage">
                    </VsImg>
                    <p slot="quoteContent">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi
                        ac urna non metus tempor accumsan ut non risus. In turpis est,
                        imperdiet eu sagittis ac, sodales quis nunc.
                    </p>
                    <span slot="quoteAuthorName">Penny</span>
                    <span slot="quoteAuthorTitle">
                        Visitor Services Advisor at Edinburgh iCentre
                    </span>
                    <VsButton
                        href="#"
                        slot="quoteLink"
                    >
                        Signet Library
                    </VsButton>
                </VsQuote>
            </span>

            <p slot="icentreLinks">Test content <a href="#">link</a></p>
        </VsIcentre>
    </VsModuleWrapper>
    <br>
    <VsModuleWrapper theme="grey">
        <template slot="vsModuleWrapperHeading">
            A tip from your local experts
        </template>

        <VsIcentre>
            <VsImageWithCaption
                imageSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                altText="This is the alt text"
                slot="icentreImageWithCaption"
            >
                <VsCaption
                    slot="img-caption"
                >
                    <span slot="caption">
                        A test caption
                    </span>

                    <span slot="credit">
                        &copy; VisitScotland
                    </span>
                </VsCaption>
            </VsImageWithCaption>

            <p slot="icentreLinks">Test content <a href="#">link</a></p>
        </VsIcentre>
    </VsModuleWrapper>
```
</docs>
