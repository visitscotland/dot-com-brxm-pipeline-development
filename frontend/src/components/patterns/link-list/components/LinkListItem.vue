<template>
    <li class="link-list-item">
        <VsLink
            :href="href"
            :type="type == 'video' ? 'default' : type"
            :variant="variant"
            :role="type == 'video' ? 'button' : 'link'"
            @click.native="emitShowModal"
        >
            <!-- @slot Slot for link list item text -->
            <slot />
            <template
                v-if="type == 'video' && videoLoaded"
            >
                | {{ videoDescriptor }} {{ formattedVideoDuration }}
            </template>
        </VsLink>
    </li>
</template>

<script>
import VsLink from '@components/elements/link/Link';
import videoStore from '../../../../stores/video.store';

/**
 * This component is an item appearing in a list of links.
 *
 * @displayName Link List Item
 */

export default {
    name: 'VsLinkListItem',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsLink,
    },
    props: {
        /**
         * The URL the link will point to
         */
        href: {
            type: String,
            default: null,
        },
        /**
         * Option to create link type which defines icon and whether it opens in a new tab
         * `external, internal, download, video`
         */
        type: {
            type: String,
            default: 'default',
            validator: (value) => value.match(/(default|external|internal|download|video)/),
        },
        /**
         * Option to choose a pre-defined style variant
         * `primary, on-dark`
         */
        variant: {
            type: String,
            default: 'primary',
            validator: (value) => value.match(/(primary|on-dark)/),
        },
        /**
         * The video id that a video type link should open
         */
        videoId: {
            type: String,
            default: null,
        },
        /**
         * The localised word for video, gets attached to the link label for video type links
         */
        videoDescriptor: {
            type: String,
            default: null,
        },
    },
    computed: {
        formattedVideoDuration() {
            let seconds = `${this.videoDetails.videoFullDuration.seconds}`;

            if (seconds.length === 1) {
                seconds = `0${seconds}`;
            }

            return `${this.videoDetails.videoFullDuration.minutes}:${seconds}`;
        },
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
        emitShowModal() {
            if (!this.videoId) {
                return;
            }

            /**
             * Triggers when the video button is clicked, requests that the appropriate
             * video modal becomes visible
             *
             * @event bv::show::modal
             *
             * @property {string} videoId the id of the video to show, acts as a key for the modal
             * @property {string} triggerRef the #ref of the button that triggered the event,
             * focus is returned here after the modal closes
             */
            this.$root.$emit('bv::show::modal', this.videoId, '#videoShow');
        },
    },
};
</script>

<style lang="scss">
    .link-list-item {
        margin-top: $spacer-4;
        font-size: $font-size-5;

        &:first-of-type {
            margin-bottom: $spacer-0;
        }
    }
</style>

<docs>
    ```js
        <VsLinkList>
            <VsLinkListItem
                href="www.visitscotland.com"
                type="external"
            >
                This is a link
            </VsLinkListItem>
            <VsLinkListItem
                href="www.visitscotland.com"
                type="download"
            >
                This is another link
            </VsLinkListItem>
            <VsLinkListItem
                href="www.visitscotland.com"
            >
                This is a third link
            </VsLinkListItem>
        </VsLinkList>
    ```
</docs>
