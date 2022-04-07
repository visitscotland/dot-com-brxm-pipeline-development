<template>
    <div
        class="vs-do-do-not"
    >
        <VsRow>
            <VsCol
                cols="12"
                md="6"
            >
                <BCard
                    header-bg-variant="success"
                    header-text-variant="white"
                    header="Do"
                    ref="thingToDo"
                >
                    <!-- @slot Holds the positive suggestion (html expected) -->
                    <slot
                        name="thingToDo"
                    />
                </BCard>
            </VsCol>
            <VsCol
                cols="12"
                md="6"
            >
                <BCard
                    header-bg-variant="danger"
                    header-text-variant="white"
                    header="Don't"
                    ref="thingToNotDo"
                >
                    <!-- @slot Holds the negative suggestion (html expected) -->
                    <slot
                        name="thingToNotDo"
                    />
                </BCard>
            </VsCol>
        </VsRow>
    </div>
</template>

<script>
import VsRow from '@components/elements/grid/Row';
import VsCol from '@components/elements/grid/Col';
import { BCard } from 'bootstrap-vue';

/**
 *
 */
export default {
    name: 'DoDoNot',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsRow,
        VsCol,
        BCard,
    },
    mounted() {
        if (!!this.$slots.thingToDo) {
            this.setImages(this.$refs.thingToDo);
        }

        if (!!this.$slots.thingToNotDo) {
            this.setImages(this.$refs.thingToNotDo);
        }
    },
    methods: {
        /**
         * Retrieve images from preformatted markup and convert them into img
         * tags
         *
         * Images are returned as `![](url)`
         */
        setImages (node) {
            const regexp = /!\[]\((?<url>.*)\)/g;
            const imgs = node.innerHTML.matchAll(regexp);

            for (let img of imgs) {
                const { url } = img.groups;

                node.innerHTML = node.innerHTML.replace(
                    img[0],
                    '<img src="' + url + '"/>'
                )
            }
        },
    }
};
</script>

<style lang="scss">
    .vs-do-do-not {
        text-align: left;
        max-width: 80%;

        .card-body img {
            max-width: 100%;
        }
    }
</style>

<docs>
  ```jsx
    <DoDoNot>
        <p slot="thingToDo">
            This is a good suggestion, we'd recommend you go ahead with it
        </p>
        <p slot="thingToNotDo">
            This is absolutely not acceptable, you should not do this
        </p>
    </DoDoNot>
  ```
</docs>
