## Usage

The VideoCaption component should be attached to another component when that component needs a
popup video, usually by embedding it in a position relative container. It takes a `videoId`
and should be accompanied by a *modal* with a matching `modalId`. It then displays a play button
which will toggle the appearance of that modal, and automatically show the video's duration.

```js
    <VsVideoCaption
        class="mt-5 mb-5"
        videoBtnText="Play video"
        videoId="c05sg3G4oA4"
    >
        <template slot="video-title">
            This is the video title
        </template>
        <template slot="video-no-js-alert">
            JavaScript needs to be enabled to watch this video.
            You can turn this on in your browser settings.
        </template>
    </VsVideoCaption>


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
```

## No JS

When JavaScript is disabled the video cannot instantiate and the contents of the video caption are
hidden

```js
    <div class="no-js">
        <VsVideoCaption
            withToggleBtn
            videoBtnText="Play video"
            videoId="FlG6tbYaA88"
            class="mt-12"
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
