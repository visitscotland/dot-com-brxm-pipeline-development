<template>
    <div class="vs-stretched-link-panels">
        <div
            class="vs-stretched-link-panels__panel vs-stretched-link-panels__panel--days"
            data-test="vs-stretched-link-panel--days"
        >
            <p data-test>
                <span class="vs-stretched-link-panels__days">{{ days }} </span>{{ daysLabel }}
            </p>
        </div>

        <div
            class="vs-stretched-link-panels__panel"
            data-test="vs-stretched-link-panel"
        >
            <VsIcon
                :name="transport"
                variant="reverse-white"
                class="vs-stretched-link-panels__icon"
            />
            <p data-test="vs-stretched-link-panel__icon">
                {{ transportName }}
            </p>
        </div>
    </div>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
/**
 * Panels to show information in the stretched link component
 *
 * @displayName Stretched Link Panels
 */
export default {
    name: 'VsStretchedLinkPanels',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
    },
    props: {
        /**
        * Optional prop for number of days
        */
        days: {
            type: String,
            default: '',
        },
        /**
        * Optional prop for transport type (will show a the transport icon if used)
        */
        transport: {
            type: String,
            default: '',
        },
        /**
        * Text to use for 'days' label - to allow for CMS translation
        */
        daysLabel: {
            type: String,
            default: 'days',
        },
        /**
        * Display-friendly transport name
        * to allow for translation
        */
        transportName: {
            type: String,
            default: '',
        },
    },
};
</script>

<style lang="scss">
    .vs-stretched-link-panels {
        position: absolute;
        right: $spacer-4;
        top: $spacer-4;
        display: flex;
        flex-direction: row;

        &__panel {
            width: 55px;
            height: 55px;
            background: $color-black;
            margin-left: $spacer-2;
            color: $color-white;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;

            &--days {
                background: $color-secondary-teal-shade-3;
            }

            p {
                margin-bottom: 0;
                text-align: center;
                line-height: $line_height_xs;
                font-size: $xs-font-size;
            }
        }

        &__days {
            font-size: $font-size-lg;
            font-family: "Source Sans Pro";
            font-weight: $font-weight-bold;
            display: block;
            margin-bottom: $spacer-1;
        }

        &__icon {
            height: 20px;
            width: auto;
            margin-bottom: $spacer-1;
        }
    }
</style>

<docs>
    ```
        <VsContainer>
        <VsRow>
            <VsCol cols="12" md="6">
                <VsStretchedLinkCard
                    link="https://visitscotland.com"
                    type="external"
                    imgSrc="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
                    imgAlt="This is the alt text"
                >
                    <template slot="stretchedCardCategory">
                        A category header
                    </template>
                    <template slot="stretchedCardPanels">
                        <VsStretchedLinkPanels
                            days="14"
                            transport="car"
                            transportName="Car"
                            daysLabel="days"
                        />
                    </template>

                    <template slot="stretchedCardHeader">
                        A Title Would Go Here
                    </template>

                    <VsRichTextWrapper slot="stretchedCardContent">
                        <p>The content for the card goes here</p>

                        <p>A second line of content</p>
                    </VsRichTextWrapper>
                </VsStretchedLinkCard>
            </VsCol>
        </VsRow>
        </VsContainer>
    ```
</docs>
