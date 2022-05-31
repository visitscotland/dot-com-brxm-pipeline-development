<template>
    <div
        class="vs-video-caption"
        data-test="video-caption"
        v-if="videoLoaded"
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

        <div class="vs-video-caption__details container">
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
        v-else
        class="vs-video-caption vs-video-caption--no-js"
        data-test="video-caption-nojs"
    >
        <div class="vs-video-caption__details container">
            <div class="vs-video-caption__alert">
                <VsIcon
                    name="review"
                    custom-colour="gold"
                    size="lg"
                />

                <p>
                    <!-- @slot Slot for no-js alert message -->
                    <slot name="video-no-js-alert" />
                </p>
            </div>
        </div>
    </div>
</template>

<script>
import VsButton from '@components/elements/button/Button';
import VsIcon from '@components/elements/icon/Icon';
import VsToggleButton from '@components/patterns/toggle-button/ToggleButton';
import videoStore from '../../../stores/video.store';

/**
 * A caption element providing information about a video, and a play button that
 * opens that video in a modal
 *
 * @displayName Video Caption
 */
export default {
    name: 'VsVideoCaption',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsButton,
        VsIcon,
        VsToggleButton,
    },
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

            .vs-toggle-btn.vs-button.btn {
                .vs-icon {
                    margin: 0;
                }
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
             display: none;

            .vs-icon {
                margin-right: $spacer-7;
            }

            p {
                font-size: $font-size-4;
                line-height: 2;
                margin: -10px 0 0;
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

            &__alert {
                display: flex;
                justify-content: flex-start;
            }

            &__title,
            &__duration,
            &__button,
            .vs-toggle-btn {
                display: none !important;
            }
        }
    }
</style>
