<template>
    <div
        class="vs-iknow-partner-item"
        data-test="vs-iknow-partner-item"
    >
        <VsContainer>
            <VsRow>
                <VsCol
                    cols="10"
                    md="6"
                    offset="1"
                    offset-md="0"
                    order="1"
                    order-md="2"
                    class="vs-iknow-partner-item__main-content"
                >
                    <!-- @slot Holds the component heading -->
                    <slot name="iknowHeading" />

                    <VsSvg
                        class="vs-iknow-partner-item__logo"
                        path="iKnowScotland"
                    />

                    <div class="vs-iknow-partner-item__text">
                        <!-- @slot Holds the component text -->
                        <slot name="iknowText" />

                        <div
                            v-if="!!this.$slots['iknowCta']"
                            class="vs-iknow-partner-item__cta"
                        >
                            <!-- @slot Optional slot for link/button CTA -->
                            <slot name="iknowCta" />
                        </div>
                    </div>
                </VsCol>
                <VsCol
                    cols="4"
                    md="2"
                    offset="4"
                    offset-md="1"
                    order="2"
                    order-md="1"
                    class="vs-iknow-partner-item__coo-container"
                >
                    <VsSvg
                        class="vs-iknow-partner-item__coo"
                        path="highland-cow"
                    />
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
 * Reusable component to promote iKnow partners to visitors as
 * a way for them to access information from local experts.
 *
 * @displayName Iknow Partner Item
 */

export default {
    name: 'VsIknowPartnerItem',
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
.vs-iknow-partner-item {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    padding: $spacer-8;
    background: $white;

    @include media-breakpoint-up(md) {
        padding: $spacer-8 $spacer-8 $spacer-0;
    }

    @include media-breakpoint-up(lg) {
        padding: $spacer-11 $spacer-11 $spacer-0;
    }

    >.container {
        @include media-breakpoint-down(md) {
            padding: 0;
        }
    }

    &__main-content {
        text-align: center;

        @include media-breakpoint-up(md) {
            padding-bottom: $spacer-8;
        }

        @include media-breakpoint-up(lg) {
            padding-bottom: $spacer-11;
        }
    }

    &__logo {
        display: inline-block;
        margin-bottom: $spacer-8;
        max-height: 45px;
        max-width: 160px;

        @include media-breakpoint-up (md) {
            max-height: 30px;
            max-width: 105px;
        }

        @include media-breakpoint-up(xl) {
            max-height: 40px;
            max-width: 138px;
        }
    }

    &__text {
        font-size: $display3-size;
        margin-bottom: 0;
        text-align: center;
    }

    &__coo-container {
        display: flex;
        align-items: flex-end;
    }

    &__coo {
        display: block;
        width: 100%;
        height: auto;
        max-width: 115px;
        max-height: 100px;
        margin: $spacer-8 auto 0;

        @include media-breakpoint-up(lg) {
            max-height: 210px;
            max-width: 185px;
        }
    }

    &__cta {
        margin-top: $spacer-8;
    }
}
</style>

<docs>
```jsx
    <VsIKnowPartnerItem>
        <VsHeading level="2" slot="iknowHeading">Help and advice</VsHeading>
        <div slot="iknowText">
            You can also get advice from our accredited
            <a href="#">iKnow partners in Edinburgh</a>,
            with excellent local expertise,
            travel advice and insider tips about must-see
            attractions and great accommodation in the local area.
        </div>

        <a href="#" slot="iknowCta">Find our iKnow partners</a>
    </VsIKnowPartnerItem>

    <VsIKnowPartnerItem>
        <VsHeading level="2" slot="iknowHeading">Help and advice</VsHeading>
        <div slot="iknowText">
            iKnow Scotland is a network of accredited
            VisitScotland partners, with excellent local
            expertise.

            Wherever you see the iKnow logo you can
            trust that you'll receive excellent advice on
            how to get the most from your visit, with
            travel advice and insider tips about the
            must-see attractions in the local area.
        </div>

        <a href="#" slot="iknowCta">Find our iKnow partners</a>
    </VsIKnowPartnerItem>

    <VsIKnowPartnerItem>
        <VsHeading level="2" slot="iknowHeading">Help and advice</VsHeading>
        <div slot="iknowText">
            iKnow Scotland is a network of accredited
            VisitScotland partners, with excellent local
            expertise.

            Wherever you see the iKnow logo you can
            trust that you'll receive excellent advice on
            how to get the most from your visit, with
            travel advice and insider tips about the
            must-see attractions in the local area.
        </div>

        <VsButton slot="iknowCta">Find our iKnow partners</VsButton>
    </VsIKnowPartnerItem>
```
</docs>
