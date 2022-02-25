<template>
    <div
        v-if="showBanner"
        class="vs-banner"
        data-test="vs-banner"
        role="banner"
    >
        <VsContainer>
            <VsRow>
                <VsCol
                    cols="11"
                    lg="10"
                >
                    <VsHeading
                        level="4"
                        class="vs-banner__title"
                    >
                        <VsIcon
                            size="sm"
                            name="review"
                            custom-colour="#700E57"
                        />

                        <!-- @slot Slot to contain banner title -->
                        <slot name="bannerTitle" />
                    </VsHeading>

                    <VsRichTextWrapper
                        class="vs-banner__text"
                        v-if="!!this.$slots['bannerText']"
                    >
                        <!-- @slot Slot to contain banner text -->
                        <slot name="bannerText" />

                        <span class="vs-banner__cta-link">
                            <!-- @slot Slot to contain CTA link -->
                            <slot name="bannerCta" />
                        </span>
                    </VsRichTextWrapper>
                </VsCol>
                <VsCol
                    cols="1"
                    lg="2"
                >
                    <VsButton
                        class="vs-banner__close-btn"
                        data-test="vs-banner__close-btn"
                        variant="transparent"
                        icon="close-circle"
                        size="md"
                        icon-variant-override="dark"
                        icon-only
                        @click.native="hideBanner"
                    >
                        <!-- @slot Default slot to contain screenreader-only text for button-->
                        <span class="sr-only">
                            <slot name="closeBtnText" />
                        </span>
                    </VsButton>
                </VsCol>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import VsButton from '@components/elements/button/Button';
import VsHeading from '@components/elements/heading/Heading';
import VsRichTextWrapper from '@components/elements/rich-text-wrapper/RichTextWrapper';
import VsIcon from '@components/elements/icon/';
import {
    VsContainer, VsRow, VsCol,
} from '@components/elements/layout';
import cookieMixin from '../../../mixins/cookieMixin';

/**
 * A banner used globally at the top of a page to display
 * important information.
 *
 * @displayName Banner
 */

export default {
    name: 'VsBanner',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
        VsContainer,
        VsRow,
        VsCol,
        VsHeading,
        VsRichTextWrapper,
        VsIcon,
    },
    mixins: [
        cookieMixin,
    ],
    data() {
        return {
            showBanner: true,
        };
    },
    mounted() {
        // Check if cookie exists and hides banner if true
        const cookieExists = this.cookieExists('vs_showbanner');
        this.showBanner = !cookieExists;
    },
    methods: {
        /**
         * Hides banner from view
         */
        hideBanner() {
            this.showBanner = !this.showBanner;
            this.setHiddenCookie();
        },
        /**
         * Sets cookie to hide the banner for the user's session
         */
        setHiddenCookie() {
            const cookieExists = this.cookieExists('vs_showbanner');
            this.setCookie('vs_showbanner', this.showBanner, !cookieExists, true);
        },
    },
};
</script>

<style lang="scss">
.vs-banner{
    background: $color-secondary-indigo-tint-6;
    padding: $spacer-2 0 $spacer-5;

    &__title.vs-heading{
        color: $color-purple;
        margin-bottom: $spacer-2;

        .vs-icon{
            vertical-align: bottom;
            margin-right: $spacer-1;
        }
    }

    &__text.vs-rich-text-wrapper--variant-normal{
        p{
            display: inline;

            &:last-of-type{
                margin-bottom: $spacer-2;
            }
        }
    }

    &__text.vs-rich-text-wrapper--variant-normal,
    &__cta-link{
        font-size: $body-font-size;
        line-height: $line-height-s;
    }

    &__close-btn{
        margin-top: $spacer-3;

        @include media-breakpoint-up(lg) {
            float: right;
        }
    }
}

</style>

<docs>
  ```jsx
    <VsBanner>
        <template slot="bannerTitle">
            Covid-19 Travel Advice
        </template>

        <template slot="bannerText">
            <p>
                Find the latest information on travel, and Good to Go (Covid-safe)
                businesses. This is a test to check what would be the maximum number
                of characters we could fit on this banner… Is it ok
                to add two lines? Let's see how it looks on mobile.
            </p>
        </template>

        <template slot="bannerCta">
            <VsLink href="#">
                View Covid-19 Travel Advice
            </VsLink>
        </template>

        <template slot="closeBtnText">
            Close
        </template>
    </VsBanner>
  ```
</docs>
