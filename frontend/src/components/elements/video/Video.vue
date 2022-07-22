<template>
    <div
        class="vs-video"
        data-test="vs-video"
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
                    ref="youtube"
                />
            </div>

            <div
                class="vs-video__fallback-wrapper"
                v-if="!requiredCookiesExist && cookiesSetStatus"
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
            playerRef: null,
        };
    },
    computed: {
        /**
         * Return the player instance
         */
        player() {
            if (typeof this.$refs.youtube !== 'undefined') {
                return this.$refs.youtube.player;
            }

            return null;
        },
    },
    mounted() {
        /**
         * Sets up listener for play/pause events
         * from $root
         */
        this.$root.$on('video-controls', (action, id, type) => {
            if (id === this.videoId) {
                if (action === 'play' && type === 'modal') {
                    // timeout allows for video in modal to appear
                    setTimeout(() => {
                        this.playVideo();
                    }, 1000);
                } else if (action === 'play') {
                    this.playVideo();
                } else if (action === 'pause') {
                    this.pauseVideo();
                }
            }
        });
    },
    methods: {
        ready() {
            this.playerRef = this.$refs.youtube.player;
            this.getPlayerDetails();
        },
        /**
         * Plays the video
         */
        playVideo() {
            this.playerRef.playVideo();
        },
        /**
         * Pauses the video
         */
        pauseVideo() {
            this.playerRef.pauseVideo();
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
    },
};
</script>
