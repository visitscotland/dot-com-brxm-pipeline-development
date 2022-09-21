<template>
    <div
        class="vs-video"
        data-test="vs-video"
        v-if="!reRendering"
    >
        <div
            class="vs-video__iframe-wrapper"
        >
            <div v-if="requiredCookiesExist">
                <!-- eslint-disable-next-line vue/component-name-in-template-casing -->
                <youtube
                    :video-id="videoId"
                    :player-vars="playerVars"
                    @ready="ready"
                    @playing="youtubePlaying"
                    @paused="youtubePaused"
                    @ended="youtubeEnded"
                    ref="youtube"
                />
            </div>

            <div
                class="vs-video__fallback-wrapper"
                v-if="!requiredCookiesExist && cookiesInitStatus"
                key="fallback"
            >
                <VsWarning
                    :warning-message="noCookiesMessage"
                    :warning-link-text="cookieLinkText"
                    :show-cookie-link="true"
                />
            </div>
        </div>
    </div>
</template>

<style lang="scss">
    .vs-video {
        &__iframe-wrapper,
        &__fallback-wrapper {
            position: relative;
            padding-bottom: 56.25%;
            height: 0;
            overflow: hidden;

            iframe {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
            }
        }
    }
</style>

<script>
// eslint-disable-next-line import/no-extraneous-dependencies
import VueYoutube from 'vue-youtube';
import Vue from 'vue';
import VsWarning from '@components/patterns/warning/Warning';
import videoStore from '../../../stores/video.store';
import verifyCookiesMixin from '../../../mixins/verifyCookiesMixin';
import requiredCookiesData from '../../../utils/required-cookies-data';
import dataLayerMixin from '../../../mixins/dataLayerMixin';

const cookieValues = requiredCookiesData.youtube;

Vue.use(VueYoutube, {
    global: false,
});

/**
 * Videos allow a user to engage with our
 * products and discover new information.
 *
 * @displayName Video
 */

export default {
    name: 'VsVideo',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsWarning,
    },
    mixins: [
        verifyCookiesMixin,
        dataLayerMixin,
    ],
    props: {
        /**
        * The YouTube ID for the video
        */
        language: {
            type: String,
            default: 'en',
        },
        /**
        * The YouTube ID for the video
        */
        videoId: {
            type: String,
            required: true,
        },
        /**
        * The title of the video, set in the CMS
        */
        videoTitle: {
            type: String,
            default: '',
        },
        /**
         * A string to be shown with the rounded time, when the rounded
         * minute value is singular. Should contain '%s' to be replaced by the
         * number of minutes
         *
         * Eg: '%s minute video', 'Video de %s minuto'
         */
        singleMinuteDescriptor: {
            type: String,
            default: '%s minute video',
        },
        /**
         * A string to be shown with the rounded time, when the rounded
         * minute value is plural. Should contain '%s' to be replaced
         * by the number of minutes
         *
         * Eg: '%s minute video', 'Video de %s minutos'
         */
        pluralMinuteDescriptor: {
            type: String,
            default: '%s minute video',
        },
        /**
        * A message explaining why the component has been disabled with disabled cookies, is
        * provided for descendent components to inject
        */
        noCookiesMessage: {
            type: String,
            default: '',
        },
        /**
        * Text used for the link which opens the cookie preference centre.
        */
        cookieLinkText: {
            type: String,
            default: '',
        },
        /**
        * A message explaining why the component has been disabled when js is disabled,
        * is provided for descendent components to inject
        */
        noJsMessage: {
            type: String,
            default: '',
        },
    },
    data() {
        return {
            duration: {
                minutes: 0,
                seconds: 0,
                roundedMinutes: '',
            },
            playerVars: {
                hl: this.language,
            },
            requiredCookies: cookieValues,
            player: null,
            reRendering: false,
            shouldAutoPlay: false,
        };
    },
    mounted() {
        this.setEventListeners();
    },
    methods: {
        ready() {
            this.player = this.$refs.youtube.player;
            this.getPlayerDetails();

            if (this.shouldAutoPlay) {
                this.shouldAutoPlay = false;
                this.playVideo();
            }
        },
        /**
         * Plays the video
         */
        playVideo() {
            if (this.player) {
                this.player.playVideo();
            }
        },
        /**
         * Pauses the video
         */
        pauseVideo() {
            if (this.player) {
                this.player.pauseVideo();
            }
        },
        /**
         * Triggered by video status events from the vue-youtube component. When any of these
         * occur an appropriate analytics event is dispatched to the datalayer.
         */
        youtubePlaying() {
            this.analyticsEvent('play');
        },
        youtubePaused() {
            this.analyticsEvent('pause');
        },
        youtubeEnded() {
            this.analyticsEvent('ended');
        },
        /**
         * Submits an event to the datalayer mixin when the video is played or paused
         */
        analyticsEvent(videoStatus) {
            let currentTime = 0;
            let duration = 0;

            this.player.getCurrentTime()
                .then((time) => {
                    currentTime = time;
                    return this.player.getDuration();
                })
                .then((length) => {
                    duration = length;
                })
                .then(() => {
                    const videoPercent = (currentTime / duration) * 100;

                    this.createDataLayerObject(
                        'videoTrackingDataEvent',
                        {
                            title: this.videoTitle,
                            status: videoStatus,
                            percent: Math.round(videoPercent),
                        },
                    );
                });
        },
        getPlayerDetails() {
            /**
             * Upon promise resolution, if the video ID returns
             * a YouTube video, process the time into the desired format.
             */
            if (typeof this.player !== 'undefined') {
                this.player.getDuration().then((response) => {
                    this.formatTime(response);
                    this.storeVideoDetails();
                });
            }
        },
        /**
         * Converts time in seconds to minutes and seconds,
         * returns an object.
         */
        formatTime(timeInSeconds) {
            const minutes = Math.floor(timeInSeconds / 60);
            const seconds = timeInSeconds - (minutes * 60);

            this.duration.minutes = minutes;
            this.duration.seconds = seconds;

            const roundedMinutes = this.getRoundedMinutes(minutes, seconds);

            this.duration.roundedMinutes = this.formatSingularOrPlural(roundedMinutes);
        },
        /**
         * Takes a time expressed as minutes and seconds and returns the number of minutes rounded
         * to the nearest one. Any time less than one minute is rounded up to one.
         */
        getRoundedMinutes(minutes, seconds) {
            if (seconds < 30 && minutes !== 0) {
                return minutes;
            }

            return minutes + 1;
        },
        /**
         * Checks if the number of (rounded) minutes the video is long is singular or plural, then
         * returns the appropriate descriptor string with the duration subbed in
         */
        formatSingularOrPlural(minutes) {
            if (minutes === 1) {
                return this.singleMinuteDescriptor.replace('%s', minutes);
            }

            return this.pluralMinuteDescriptor.replace('%s', minutes);
        },
        /**
         * Takes a number, returns a string padded with a
         * leading 0 if the number is less than 10
         */
        pad(toPad) {
            if (toPad >= 10) {
                return toPad;
            }

            return `0${toPad}`;
        },
        /**
         * Send video details to Vuex store
         */
        storeVideoDetails() {
            videoStore.dispatch('newVideoRef', {
                id: this.videoId,
                durationMsg: this.duration.roundedMinutes,
                duration: (this.duration.minutes * 60) + this.duration.seconds,
                fullDuration: this.duration,
            });
        },
        /**
         * Attaches event listeners upon mounting video. These include play and pause functions,
         * for external play buttons and re-render + autoplay functionality for a video inside
         * a modal.
         */
        setEventListeners() {
            this.$root.$on('video-controls', (action, id) => {
                if (id === this.videoId) {
                    if (action === 'modal-opened') {
                        this.reRenderVideo();
                    }

                    if (action === 'play') {
                        this.playVideo();
                    } else if (action === 'pause') {
                        this.pauseVideo();
                    }
                }
            });
        },
        /**
         * Upon opening a vs-modal with a video, the video must be briefly removed and re-rendered
         * to ensure that all event triggers in the video fire properly.
         */
        reRenderVideo() {
            this.reRendering = true;
            this.$nextTick(() => {
                this.reRendering = false;
                this.$nextTick(() => {
                    this.shouldAutoPlay = true;
                });
            });
        },
    },
};
</script>
