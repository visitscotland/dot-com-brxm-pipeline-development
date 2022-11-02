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
                    <div
                        v-if="showError"
                        key="fallback"
                    >
                        <VsWarning
                            :type="cookiesInitStatus === true ? 'cookie' : 'normal'"
                            theme="light"
                            data-test="vs-embed-wrapper__error"
                            class="vs-embed-wrapper__error"
                        >
                            {{ warningText }}

                            <template
                                v-if="!requiredCookiesExist
                                    && cookiesInitStatus === true"
                                slot="button-text"
                            >
                                <slot name="embedButtonText" />
                            </template>
                        </VsWarning>
                    </div>

                    <VsWarning
                        type="normal"
                        theme="light"
                        data-test="vs-embed-wrapper__error--no-js"
                        class="vs-embed-wrapper__error vs-embed-wrapper__error--no-js"
                    >
                        {{ noJsText }}
                    </VsWarning>
                </VsCol>
            </VsRow>
        </VsContainer>
    </div>
</template>

<script>
import {
    VsContainer, VsRow, VsCol,
} from '@components/elements/grid';
import VsRichTextWrapper from '@components/elements/rich-text-wrapper/RichTextWrapper';
import VsWarning from '@components/patterns/warning/Warning';
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
        VsRichTextWrapper,
        VsWarning,
    },
    mixins: [
        verifyCookiesMixin,
    ],
    props: {
        noCookieText: {
            type: String,
            required: true,
        },
        errorText: {
            type: String,
            required: true,
        },
        noJsText: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            requiredCookies: cookieValues,
        };
    },
    computed: {
        showError() {
            if ((!this.requiredCookiesExist
                && this.cookiesInitStatus === true)
                || this.cookiesInitStatus === 'error') {
                return true;
            }

            return false;
        },
        warningText() {
            let text = '';

            if (this.cookiesInitStatus === 'error') {
                text = this.errorText;
            }

            if (!this.requiredCookiesExist
                && this.cookiesInitStatus === true) {
                text = this.noCookieText;
            }

            return text;
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

        &__error--no-js {
            display: none;
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

            &__error {
                display: none;

                &--no-js {
                    display: block;
                }
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

        <VsEmbedWrapper
            noCookieText="You need cookies enabled to view this content"
            errorText="Sorry, there's been an error, please try again later"
            noJsText="You need Javascript enabled to see this content"
        >
            <template slot="embedIntroCopy">
                Share your snaps with us by using #ScottishCastle or #VisitScotland
            </template>
            <template slot="embedWidget">
                Embed Tag Goes Here
            </template>
        </VsEmbedWrapper>
    </VsModuleWrapper>
```
</docs>
