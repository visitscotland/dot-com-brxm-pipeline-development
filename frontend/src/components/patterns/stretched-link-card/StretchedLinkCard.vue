<template>
    <div
        class="card vs-stretched-link-card"
        :class="disabled ? 'vs-stretched-link-card--disabled': ''"
    >
        <template
            v-if="imgSrc"
        >
            <VsImg
                :src="imgSrc"
                :alt="imgAlt"
                class="vs-stretched-link-card__img"
                data-test="vs-stretched-link-card__img"
            />
        </template>

        <template
            v-if="!!this.$slots['stretchedCardPanels']"
        >
            <!-- @slot Contains optional content for overlaid panels  -->
            <slot name="stretchedCardPanels" />
        </template>

        <div class="card-body">
            <span
                class="vs-stretched-link-card__category"
                v-if="!!this.$slots['stretchedCardCategory']"
                data-test="vs-stretched-link-card__category"
            >
                <!-- @slot Contains a category header for the card  -->
                <slot name="stretchedCardCategory" />
            </span>
            <VsHeading
                level="3"
                class="card-title vs-stretched-link-card__title"
                data-test="vs-stretched-link-card__title"
            >
                <template v-if="!!this.$slots['stretchedCardLink']">
                    <slot name="stretchedCardHeader" />
                </template>

                <VsLink
                    v-else
                    :href="link"
                    :type="type"
                    class="stretched-link"
                    :class="disabled ? 'stretched-link--disabled' : ''"
                    :variant="theme === 'dark' ? 'dark' : 'primary'"
                    data-test="vs-stretched-link"
                    :disabled="disabled"
                >
                    <!-- @slot Contains header content for the card  -->
                    <slot name="stretchedCardHeader" />
                </VsLink>
            </VsHeading>
            <div
                class="vs-stretched-link-card__content"
                data-test="vs-stretched-link-card__content"
            >
                <!-- @slot Contains body content for the card  -->
                <slot name="stretchedCardContent" />
            </div>

            <VsLink
                :href="link"
                class="vs-stretched-link-card__link stretched-link"
                v-if="!!this.$slots['stretchedCardLink']"
                data-test="vs-stretched-link-card__link"
            >
                <!-- @slot Text for option fake link at bottom of the card -->
                <slot name="stretchedCardLink" />
            </VsLink>
        </div>
    </div>
</template>

<script>
import VsHeading from '@components/elements/heading/Heading';
import VsLink from '@components/elements/link/Link';
import VsImg from '@components/elements/img/Img';

/**
 * The Stretched Link Card is a block that stretches its nested link across its whole area
 * meaning that the whole block is clickable
 *
 * @displayName Stretched Link Card
 */
export default {
    name: 'VsStretchedLinkCard',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsHeading,
        VsLink,
        VsImg,
    },
    props: {
        /**
        * The link that the component will use
        */
        link: {
            type: String,
            required: true,
            default: '#',
        },
        /**
        * The type of link. This will set the icon.
        * `external, internal, download`
        */
        type: {
            type: String,
            required: true,
            validator: (value) => value.match(/(default|external|internal|download)/),
        },
        /**
        * The component color theme
        */
        theme: {
            type: String,
            default: 'light',
            validator: (value) => value.match(/(light|dark)/),
        },
        /**
        * The image to use in the component
        */
        imgSrc: {
            required: true,
            type: String,
        },
        /**
        * The image alt text to use in the component
        */
        imgAlt: {
            type: String,
            default: '',
        },
        /**
        * Prop to disable link functionality
        */
        disabled: {
            type: Boolean,
            default: false,
        },
    },
};
</script>

<style lang="scss">
    .card.vs-stretched-link-card {
        transition: box-shadow $duration-slowly;
        border: none;
        position: relative;
        line-height: $line-height-xs;

        &:hover {
            box-shadow: $shadow_card;

            .megalink-link-list__title {
                text-decoration: underline;
            }
        }

        &--disabled {
            &:hover {
                box-shadow: none;
            }

            .megalink-link-list__title {
                text-decoration: none;
            }
        }

        .stretched-link {
            color: $color-base-text;
            text-decoration: none;
            letter-spacing: 0;
            display: block;

            &--disabled {
                cursor: default;
            }

            &:focus {
                outline: 2px solid $color-pink;
            }
        }

        .vs-link__icon {
            height: 12px;
            width: 12px;
        }

        .vs-stretched-link-card__img {
            width: 100%;
            max-width: 100%;
            align-self: flex-start;
            flex-shrink: 0; // IE11 fix, prevents image vertical stretching
        }

        .vs-stretched-link-card__title {
            font-size: $font-size-3;
            line-height: $line-height-s;
            letter-spacing: $letter-spacing-l;
            color: $color-base-text;
            display: flex;

            a {
                letter-spacing: inherit;
            }
        }

        .vs-stretched-link-card__category {
            font-family: $font-family-base;
            font-size: $font-size-3;
            line-height: $line-height-xs;
            color: $color-secondary-teal-shade-2;
            letter-spacing: normal;
            margin-bottom: $spacer-4;
        }

        .vs-stretched-link-card__content {
            margin-top: $spacer-2;
            line-height: $line-height-s;
            font-size: $font-size-teaser;

            p:last-of-type {
                margin-bottom: 0;
            }
        }

        .vs-stretched-link-card__panels {
            position: absolute;
            top: $spacer-1;
            right: $spacer-1;
            display: flex;
            flex-direction: row;
        }

        .vs-stretched-link-card__link {
            margin: $spacer-4 $spacer-0 $spacer-0;
            color: $color-pink;
            text-decoration: underline;
        }

        @include media-breakpoint-up(sm) {
            .vs-stretched-link-card__panels {
                top: $spacer-2;
                right: $spacer-2;
            }
        }

        @include media-breakpoint-up(xl) {
            .vs-stretched-link-card__title {
                font-size: $font-size-3;
                line-height: $line-height-s;
            }

             .card-body {
                padding-bottom: $spacer-5;
            }
        }
    }
</style>

<docs>
  ```jsx
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

            <VsCol cols="12" md="6">
                <VsStretchedLinkCard
                    link="https://visitscotland.com"
                    type="internal"
                    imgSrc=""
                    imgAlt="This is the alt text"
                >
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
