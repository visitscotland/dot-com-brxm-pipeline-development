## Usage

The VideoCaption component should be attached to another component when that component needs to
display information about a video. It takes a `videoId` prop, referencing the youtube id of the
video that it is captioning, and draws play time information automatically. It also takes a
`video-title` slot.

```js
    <VsVideoCaption
        class="mt-5 mb-5"
        videoId="FlG6tbYaA88"
    >
        <template slot="video-title">
            This is the video title
        </template>
    </VsVideoCaption>
```

If a `videoBtnText` prop is supplied then an additional button is rendered, which opens and
autoplays any *VsModal* components in the page that contains the same video the caption references.

```js
    <VsVideoCaption
        class="mt-5 mb-5"
        videoId="FlG6tbYaA88"
        videoBtnText="Play video"
    >
        <template slot="video-title">
            This is the video title
        </template>
    </VsVideoCaption>

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

## Accessibility

When JavaScript is disabled the video cannot instantiate and the entire caption will be hidden. A
`video-no-js-alert` slot can be provided in additional to the `video-title` which will display in
that case. 
