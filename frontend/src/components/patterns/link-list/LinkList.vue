<template>
    <ul class="link-list">
        <!-- @slot Slot for link list items -->
        <slot />
    </ul>
</template>

<script>
/**
 * This component is a list of links.
 *
 * @displayName Link List
 */

export default {
    name: 'VsLinkList',
    status: 'prototype',
    release: '0.0.1',
};
</script>

<style lang="scss">
    .link-list {
        list-style: none;
        padding: 0;
        margin-bottom: $spacer-7;
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
            <VsLinkListItem
                videoId="g-Fhvj7vW-E"
                videoDescriptor="Video"
                type="video"
                href="#"
            >
                This is a video link
            </VsLinkListItem>
        </VsLinkList>
        <VsModal
            modalId="g-Fhvj7vW-E"
            closeBtnText="Close"
            :isVideoModal="true"
        >
            <VsRow>
                <VsCol cols="12">
                    <VsVideo
                        videoId="g-Fhvj7vW-E"
                        class="mb-8"
                    />
                </VsCol>
            </VsRow>
        </VsModal>
    ```
</docs>
