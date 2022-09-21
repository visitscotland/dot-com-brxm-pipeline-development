<template>
    <div data-test="video-caption">
        <div
            class="vs-video-caption"
            v-if="videoLoaded && requiredCookiesExist"
            key="video-caption"
        >
            <div
                v-if="videoBtnText"
                class="vs-video-caption__buttons-container"
            >
                <div class="container">
                    <VsButton
                        class="vs-video-caption__button"
                        icon="play"
                        icon-position="left"
                        size="md"
                        ref="videoShow"
                        @click.native="emitShowModal"
                    >
                        {{ videoBtnText }}
                    </VsButton>
                </div>

                <VsToggleButton
                    v-if="withToggleBtn"
                    @toggleAction="emitToggle"
                />
            </div>

            <div
                class="vs-video-caption__details container"
                v-if="requiredCookiesExist"
            >
                <p class="vs-video-caption__title">
                    <!-- @slot Slot for video title -->
                    <slot name="video-title" />
                </p>

                <p class="vs-video-caption__duration">
                    {{ videoDetails.videoDurationMsg }}
                </p>
            </div>
        </div>

        <div
            v-else-if="showCookieMessage"
            class="vs-video-caption vs-video-caption--warning"
        >
            <VsWarning
                :warning-message="noCookiesMessage"
                :show-cookie-link="true"
                :cookie-link-text="cookieLinkText"
                variant="row"
            />
        </div>
        <div
            v-else-if="cookiesInitStatus === 'error'"
            class="vs-video-caption vs-video-caption--warning"
        >
            <VsWarning
                :warning-message="errorMessage"
                variant="row"
            />
        </div>
        <div
            class="vs-video-caption vs-video-caption--no-js vs-video-caption--warning"
            data-test="video-caption-nojs"
        >
            <VsWarning
                :warning-message="noJsMessage"
                :show-cookie-link="false"
                :cookie-link-text="cookieLinkText"
                variant="row"
            />
        </div>
    </div>
</template>

<script>
import VsButton from '@components/elements/button/Button';
import VsToggleButton from '@components/patterns/toggle-button/ToggleButton';
import VsWarning from '@components/patterns/warning/Warning';
import verifyCookiesMixin from '../../../mixins/verifyCookiesMixin';
import videoStore from '../../../stores/video.store';
import requiredCookiesData from '../../../utils/required-cookies-data';

const cookieValues = requiredCookiesData.youtube;

/**
 * Caption to be used for opening a video
 *
 * @displayName Video Caption
 */
export default {
    name: 'VsVideoCaption',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
        VsToggleButton,
        VsWarning,
    },
    mixins: [
        verifyCookiesMixin,
    ],
    props: {
        /**
         * Text for the play video button
         */
        videoBtnText: {
            type: String,
            default: '',
        },
        /**
         * If the video button should include a toggle button
         * for another caption
         */
        withToggleBtn: {
            type: Boolean,
            default: false,
        },
        /**
         * The YouTube ID for the video
         */
        videoId: {
            type: String,
            required: true,
        },
        /**
        * Text used for the link which opens the cookie preference centre.
        */
        cookieLinkText: {
            type: String,
            default: '',
        },
        /**
         * Message to show when there's an error with a third party
        */
        errorMessage: {
            type: String,
            required: true,
        },
    },
    inject: {
        noJsMessage: {
            default: '',
        },
        noCookiesMessage: {
            default: '',
        },
    },
    data() {
        return {
            requiredCookies: cookieValues,
            showErrorMessage: false,
        };
    },
    computed: {
        videoDetails() {
            return videoStore.getters.getVideoDetails(this.videoId);
        },
        videoLoaded() {
            if (typeof this.videoDetails !== 'undefined' && this.videoDetails.videoDuration > 0) {
                return true;
            }

            return false;
        },
        showCookieMessage() {
            if (!this.requiredCookiesExist
                && this.cookiesSet.length > 0
                && this.noCookiesMessage) {
                return true;
            }

            return false;
        },
    },
    methods: {
        emitToggle() {
            this.$emit('toggleAction');
        },
        emitShowModal() {
            this.$root.$emit('bv::show::modal', this.videoId, '#videoShow');
        },
    },
};
</script>

<style lang="scss">
    .vs-video-caption {
        width: 100%;
        position: relative;

        &--no-js {
            display: none;
        }

        &--warning {
            background-color: $color-gray-shade-6;
        }

        &__details {
            background-color: $color-gray-shade-6;
            color: $color-white;
            padding: $spacer-4 $spacer-2 $spacer-3;
        }

        &__buttons-container {
            position: absolute;
            transform: translateY(-100%);
            width: 100%;

            .vs-toggle-btn {
                display: block;
                position: absolute;
                right: $spacer-2;
                top: calc(-24px - #{$spacer-3});
            }
        }

        &__title {
            font-size: $font-size-4;
            font-weight: $font-weight-bold;
            margin-bottom: $spacer-2;
        }

        &__duration {
            font-size: $font-size-4;
            font-weight: $font-weight-light;
            margin: 0;
        }

        &__alert {
            display: flex;
            justify-content: flex-start;

            .vs-icon {
                margin-right: $spacer-7;
            }

            p {
                font-size: $font-size-4;
                line-height: 2;
                margin: -10px 0 0;
            }

            // override OneTrust styles
            #ot-sdk-btn.ot-sdk-show-settings {
                color: $color-white;
                text-decoration: underline;

                &:hover {
                    color: $color-yellow;
                }
            }
        }

        &__button {
            min-height: 50px;
            display: flex;
            align-items: center;
            padding-top: $spacer-1;
            padding-bottom: $spacer-1;
            min-height: 53px;
            text-align: left;
            line-height: 1.1;

            .vs-icon {
                margin-right: $spacer-6;
            }
        }

        .vs-caption--large .vs-caption__image-caption {
            margin-bottom: $spacer-2;
        }

        @include media-breakpoint-up(sm) {
            &__details {
                display: flex;
                align-items: baseline;
                padding: $spacer-4 $spacer-5 $spacer-5;
            }

            &__title {
                font-size: $font-size-lead;
                margin-right: $spacer-4;
                margin-bottom: 0;
            }

            &__buttons-container {
                & > .container {
                    padding: 0;
               }

                &__button {
                    max-width: 400px;
                }

                .vs-toggle-btn {
                    top: calc(50% - 12px);
                }
            }
        }

        @include media-breakpoint-up(lg) {
            &__details {
                display: block;
                padding: $spacer-4 $spacer-6 $spacer-5;
            }

            &__title {
                margin-bottom: $spacer-1;
            }

            &__button {
                max-width: 360px;
            }
        }
    }

    @include no-js {
        .vs-video-caption {
            display: none;

            &--no-js {
                display: block;
            }

            @include media-breakpoint-up(sm) {
                &__details {
                    max-width: 84%;
                    margin: 0 auto;
                    border-bottom: $color-secondary-gray 1px solid;
                }
            }

            @include media-breakpoint-up(lg) {
                &__details {
                    max-width: 100%;
                    margin: 0;
                    border-bottom: none;
                }
            }

            &__title,
            &__duration,
            &__button,
            .vs-toggle-btn {
                display: none;
            }
        }
    }
</style>

<docs>
    ``` jsx
    <VsVideoCaption
        class="mt-5 mb-5"
        videoBtnText="Play video this is a longer caption"
        videoId="c05sg3G4oA4"
        error-message="Something's gone wrong"
    >
        <template slot="video-title">
            This is the video title
        </template>
        <template slot="video-no-js-alert">
            JavaScript needs to be enabled to watch this video.
            You can turn this on in your browser settings.
        </template>

        <template slot="video-no-cookies-alert">
            You need cookies enabled to watch this video.
            <span id="ot-sdk-btn" class="ot-sdk-show-settings">Manage Cookie Settings</span>.
        </template>

        <button id="ot-sdk-btn" class="ot-sdk-show-settings">
            Cookie Settings
        </button>
    </VsVideoCaption>

    <VsVideoCaption
        withToggleBtn
        class="mb-5 mt-12"
        videoBtnText="Play video"
        videoId="FlG6tbYaA88"
        error-message="Something's gone wrong"
    >
        <template slot="video-title">
            This video caption has a toggle button
        </template>
        <template slot="video-no-js-alert">
            JavaScript needs to be enabled to watch this video.
            You can turn this on in your browser settings.
        </template>
    </VsVideoCaption>

    <div class="no-js">
        <VsVideoCaption
            withToggleBtn
            videoBtnText="Play video"
            videoId="FlG6tbYaA88"
            class="mt-12"
            error-message="Something's gone wrong"
            noJs-message="You don't have JS enabled"
        >
            <template slot="video-title">
                This is the video title
            </template>
            <template slot="video-no-js-alert">
                This is display when JS is turned off.<br />
                JavaScript needs to be enabled to watch this video.
                You can turn this on in your browser settings.
            </template>
        </VsVideoCaption>
    </div>

    <VsModal
        modalId="c05sg3G4oA4"
        closeBtnText="Close"
        :isVideoModal="true"
    >
        <VsRow>
            <VsCol cols="12">
                <VsVideo
                    video-id="c05sg3G4oA4"
                    class="mb-8"
                />
            </VsCol>
        </VsRow>
    </VsModal>

    <VsModal
        modalId="FlG6tbYaA88"
        closeBtnText="Close"
        :isVideoModal="true"
    >
        <VsRow>
            <VsCol cols="12">
                <VsVideo
                    video-id="FlG6tbYaA88"
                    class="mb-8"
                />
            </VsCol>
        </VsRow>
    </VsModal>
    ```
</docs>
