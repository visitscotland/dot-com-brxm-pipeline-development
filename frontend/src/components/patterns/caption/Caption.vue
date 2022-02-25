<template>
    <figcaption
        ref="figcaption"
        data-test="vs-caption"
        class="vs-caption"
        :class="captionClasses"
    >
        <VsRow class="justify-content-center justify-content-sm-start">
            <VsCol
                class="order-2 order-sm-1"
                :class="[!showMap ? 'align-self-center' : '']"
            >
                <div class="vs-caption__caption-info">
                    <p
                        class="vs-caption__image-caption"
                        v-if="!!this.$slots.caption"
                    >
                        <!-- @slot Slot to display caption  -->
                        <slot name="caption" />
                    </p>

                    <p
                        class="vs-caption__image-credit"
                        v-if="!!this.$slots.credit"
                    >
                        <!-- @slot Slot to display credit  -->
                        <slot name="credit" />
                    </p>

                    <!-- @slot Slot to display social media credit link  -->
                    <slot name="socialLink" />
                </div>
            </VsCol>
            <VsCol
                class="col-12 col-sm-auto order-1
                order-sm-2 pl-sm-0 align-self-end
                align-self-sm-start"
                v-if="showMap && isLargeCaption"
            >
                <div class="vs-caption__map-wrapper pt-3 pt-sm-2 pb-sm-2 pr-sm-4 mx-auto">
                    <VsCaptionImageMap
                        :latitude="latitude"
                        :longitude="longitude"
                        :map-outline-color="tokens.color_white"
                        :map-marker-color="tokens.color_secondary_teal_tint_3"
                    />
                </div>
            </VsCol>
        </VsRow>
    </figcaption>
</template>

<script>
import designTokens from '@/assets/tokens/tokens.json';
import { VsRow, VsCol } from '@components/elements/layout';
import VsCaptionImageMap from './components/CaptionImageMap';

/**
 * TODO: Document usage
 *
 * @displayName Caption
 */
export default {
    name: 'VsCaption',
    components: {
        VsRow,
        VsCol,
        VsCaptionImageMap,
    },
    props: {
        /**
         * Option to choose which variant to show
         * `fullwidth, large`
         */
        variant: {
            type: String,
            default: 'fullwidth',
            validator: (value) => value.match(/(fullwidth|large)/),
        },
        /**
         * The image latitude
         */
        latitude: {
            type: String,
            default: '',
        },

        /**
         * The image longitude
         */
        longitude: {
            type: String,
            default: '',
        },

        /**
         * Option to choose text alignment
         * `left, right`
         */
        textAlign: {
            type: String,
            default: 'left',
            validator: (value) => value.match(/(left|right)/),
        },
    },
    data() {
        return {
            tokens: designTokens,
        };
    },
    computed: {
        showMap() {
            return !!(this.longitude && this.latitude);
        },
        isLargeCaption() {
            return this.variant === 'large';
        },
        captionClasses() {
            return [
                {
                    'vs-caption--large': this.isLargeCaption,
                    'vs-caption--fullwidth': !this.isLargeCaption,
                },
                `vs-caption--${this.textAlign}`,
            ];
        },
    },
};
</script>

<style lang="scss">
.vs-caption {
    background-color: $color-gray-shade-6;
    color: $color-white;

    &--large,
    &--fullwidth {
        position: relative;

        @include media-breakpoint-up(sm) {
            display: block;
        }

        > .row {
            margin: 0 auto;
        }
        .vs-caption{
            &__image-caption {
                margin-bottom: $spacer-2;
            }

            &__map-wrapper {
                max-width: 54px;
            }
        }
    }

    &--fullwidth{
        @include media-breakpoint-up(sm) {
            width: 100%;
            height: auto;
            min-height: 64px;
            text-align: left;
        }
        .vs-caption__caption-info{
            padding: $spacer-3 0;
        }
    }

    &--large {
        text-align: center;

        @include media-breakpoint-up(sm) {
            width: 310px;
            height: auto;
            min-height: 96px;
            text-align: left;

            > .row {
                margin: 0 -16px;
            }

            .vs-caption{
                &__image-caption {
                   margin-bottom: $spacer-8;
                }

                &__map-wrapper {
                    max-width: 74px;
                }
            }
        }

        .vs-caption__caption-info{
            padding: $spacer-4;
        }
    }

    &__image-caption,
    &__image-credit {
        font-size: $small-font-size;
        line-height: $line-height-standard;
    }

    &__image-caption {
        font-weight: $font-weight-semi-bold;
    }

    &__image-credit {
        font-weight: $font-weight-light;
        margin-bottom: $spacer-0;
    }

    @include media-breakpoint-up(md) {
        &--right {
            .vs-caption__caption-info{
                text-align: right;
            }
        }
    }

}
@include no-js {
    .vs-image-with-caption__caption-wrapper{
        @include media-breakpoint-down(xs) {
            .vs-caption{
                &--large {
                    position: relative;
                    top: 0;
                    width: 100%;
                    height: auto;
                    text-align: left;
                    display: block;

                    .order-2 {
                        order: 1;
                    }

                    .order-1 {
                        order: 2;
                        flex: 0 0 auto;
                        width: auto;
                        max-width: 100%;
                        align-self: auto!important;
                    }

                    .vs-caption{
                        &__map-wrapper {
                            padding-top: 0!important;
                        }

                        &__image-caption {
                            margin-bottom: $spacer-5;
                        }

                        &__caption-info{
                            padding: $spacer-3 $spacer-2;
                        }
                    }
                }
            }
        }

        .vs-caption{
            &--large,
            &--fullwidth {
                @include media-breakpoint-down(xs) {
                    position: relative;
                }
            }

            &--fullwidth {
                display: block;
                text-align: left;
            }
        }
    }
}
</style>

<docs>
  ```js

    <BsWrapper style="max-width: 500px">
        <h3>Fullwidth Caption</h3>
        <VsCaption>
            <span slot="caption">
                A Scottish Castle
            </span>

            <span slot="credit">
                VisitScotland
            </span>
        </VsCaption>

        <h3 style="margin-top: 3rem;">Fullwidth Caption (Right)</h3>
        <VsCaption text-align="right">
            <span slot="caption">
                A Scottish Castle
            </span>

            <span slot="credit">
                VisitScotland
            </span>
        </VsCaption>

        <h3 style="margin-top: 3rem;">Fullwidth Social Image</h3>
        <VsCaption>
            <span slot="caption">
                A Scottish Castle
            </span>

            <VsSocialCreditLink
                slot="socialLink"
                credit="VisitScotland"
                socialPostUrl="http://www.visitscotland.com"
                source="instagram"
            >
            </VsSocialCreditLink>
        </VsCaption>

        <h3 style="margin-top: 3rem;">Large Caption Style</h3>
        <VsCaption
            latitude="55.9485947"
            longitude="-3.2021022"
            variant="large"
        >
            <span slot="caption">
                A Scottish Castle
            </span>

            <span slot="credit">
                VisitScotland
            </span>
        </VsCaption>
    </BsWrapper>
  ```
</docs>
