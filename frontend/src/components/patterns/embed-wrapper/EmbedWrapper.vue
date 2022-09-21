<template>
    <div
        data-test="vs-embed-wrapper"
        class="vs-embed-wrapper"
    >
        <VsContainer>
            <VsRow>
                <VsCol
                    cols="12"
                    sm="10"
                    offset-sm="1"
                    md="6"
                    offset-md="3"
                >
                    <VsRichTextWrapper
                        class="vs-module-wrapper__intro vs-embed-wrapper__intro"
                        v-if="!!this.$slots['embedIntroCopy']
                            && cookiesInitStatus !== 'error'"
                        data-test="vs-module-wrapper__intro"
                    >
                        <!-- @slot Slot to contain intro text -->
                        <slot name="embedIntroCopy" />
                    </VsRichTextWrapper>
                    <VsRichTextWrapper
                        class="vs-module-wrapper__intro vs-embed-wrapper__no-js"
                        v-if="!!this.$slots['embedIntroCopyNoJs']"
                        data-test="vs-module-wrapper__intro"
                    >
                        <!-- @slot Slot to contain intro text if js is disabled -->
                        <slot name="embedIntroCopyNoJs" />
                    </VsRichTextWrapper>
                    <VsRichTextWrapper
                        class="vs-module-wrapper__intro"
                        v-if="showNoCookieText"
                        data-test="vs-module-wrapper__intro"
                    >
                        <!-- @slot Slot to contain intro text if cookies aren't enabled -->
                        <slot name="embedIntroCopyNoCookies" />
                    </VsRichTextWrapper>

                    <div
                        v-else-if="cookiesInitStatus === 'error'"
                        class="mb-4"
                    >
                        <!-- @slot Slot to contain intro text if there's
                        an error with a third party -->
                        <slot name="embedIntroCopyError" />
                    </div>
                </VsCol>
                <VsCol
                    cols="12"
                >
                    <div
                        class="vs-embed-wrapper__container"
                        :class="requiredCookiesExist ? '' : 'd-none'"
                        key="embeddedContent"
                    >
                        <!--
                            @slot Takes the dom element for the embedded javascript widget, any
                            associated script tags can't be passed into the vue element and should
                            be loaded further down the page outside of the vue components
                        -->
                        <slot name="embedWidget" />
                    </div>
                    <div class="vs-embed-wrapper__no-js">
                        <VsSvg
                            class="vs-embed-wrapper__error-image"
                            path="no-js-coo"
                        />
                    </div>
                    <div
                        v-if="showErrorIcon"
                        key="fallback"
                    >
                        <VsSvg
                            class="vs-embed-wrapper__error-image"
                            :path="cookiesInitStatus === true ? 'cookie-coo' : 'no-js-coo'"
                        />
                    </div>
                </VsCol>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import {
    VsContainer, VsRow, VsCol,
} from '@components/elements/grid';
import VsSvg from '@components/elements/svg/Svg';
import VsRichTextWrapper from '@components/elements/rich-text-wrapper/RichTextWrapper';
import verifyCookiesMixin from '../../../mixins/verifyCookiesMixin';
import requiredCookiesData from '../../../utils/required-cookies-data';

const cookieValues = requiredCookiesData.embed;

/**
 * This component acts as a wrapper for an embedded javascript widget and provides
 * no-js and no-cookies functionality for that widget
 *
 * @displayName Embed Wrapper
 */
export default {
    name: 'VsEmbedWrapper',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsContainer,
        VsRow,
        VsCol,
        VsSvg,
        VsRichTextWrapper,
    },
    mixins: [
        verifyCookiesMixin,
    ],
    data() {
        return {
            requiredCookies: cookieValues,
        };
    },
    computed: {
        showNoCookieText() {
            if (!!this.$slots.embedIntroCopyNoCookies
                && !this.requiredCookiesExist
                && this.cookiesInitStatus === true) {
                return true;
            }

            return false;
        },
        showErrorIcon() {
            if ((!this.requiredCookiesExist
                && this.cookiesInitStatus === true)
                || this.cookiesInitStatus === 'error') {
                return true;
            }

            return false;
        },
    },
};
</script>

<style lang="scss">
    .vs-embed-wrapper {
        &__error-image {
            max-width: 100%;
        }

        .vs-embed-wrapper__no-js, .vs-embed-wrapper__no-cookies {
            display: none;
        }
    }

    .no-cookies {
        .vs-embed-wrapper {
            &__container {
                display: none;
            }

            &__intro {
                display: none;
            }

            &__no-cookies {
                display: block;
            }
        }
    }

    @include no-js {
        .vs-embed-wrapper {
            &__container {
                display: none;
            }

            &__intro {
                display: none;
            }

            &__no-cookies {
                display: none;
            }

            &__no-js {
                display: block;
                text-align: center;
            }
        }
    }
</style>

<docs>
```jsx
    <VsModuleWrapper>
        <template slot="vsModuleWrapperHeading">
            Your Pictures Of Scottish Castles
        </template>

        <VsEmbedWrapper>
            <template slot="embedIntroCopy">
                Share your snaps with us by using #ScottishCastle or #VisitScotland
            </template>

            <template slot="embedIntroCopyNoJs">
                <p>JavaScript needs to be enabled to see social media images for this place.
You can turn this on in your browser settings.</p>
            </template>

            <template slot="embedIntroCopyNoCookies">
                <p>Cookies are needed to see social media images from this place.</p>
                <p><a target="_blank" href="#">Update my cookie settings</a></p>
            </template>

            <template slot="embedIntroCopyError">
                <p>Sorry, somethings gone wrong. We can't display this content at the moment.</p>
            </template>

            <template slot="embedWidget">
                Embed Tag Goes Here
            </template>
        </VsEmbedWrapper>
    </VsModuleWrapper>
```
</docs>
