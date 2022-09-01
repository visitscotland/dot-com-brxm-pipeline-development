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

                        {{ title }}
                    </VsHeading>

                    <VsRichTextWrapper
                        class="vs-banner__text"
                        v-if="!!this.$slots['bannerText'] || !!this.$slots['bannerCta']"
                    >
                        <!-- @slot Slot to contain banner text -->
                        <slot name="bannerText" />

                        <span
                            class="vs-banner__cta-link"
                            v-if="!!this.$slots['bannerCta']"
                        >
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
                        icon-only
                        @click.native="hideBanner"
                    >
                        <span class="sr-only">
                            {{ closeBtnText }}
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
} from '@components/elements/grid';
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
    props: {
        /**
         * Accessible text for close button
         */
        closeBtnText: {
            type: String,
            required: true,
        },
        /**
         * Title for the banner
         */
        title: {
            type: String,
            required: true,
        },
        /**
         * Set to false to let the banner show again on page refresh
         */
        dontShowAgain: {
            type: Boolean,
            default: true,
        },
    },
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

            if (this.dontShowAgain) {
                this.setHiddenCookie();
            }
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

    &__text.vs-rich-text-wrapper--normal{
        p{
            display: inline;

            &:last-of-type{
                margin-bottom: $spacer-2;
            }
        }
    }

    &__text.vs-rich-text-wrapper--normal,
    &__cta-link{
        font-size: $font-size-4;
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
