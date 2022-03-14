<template>
    <div
        class="vs-social-share"
        data-test="vs-social-share"
        :class="noJs ? 'vs-module-wrapper__outer--hidden' : 'vs-module-wrapper__outer--light'"
    >
        <VsButton
            class="vs-social-share__share-btn"
            variant="transparent"
            :uppercase="false"
            :id="`vs-social-share-popover--${id}`"
            v-if="!noJs"
            ref="shareButton"
        >
            <VsIcon
                name="share"
                variant="dark"
                size="md"
            />

            {{ shareBtnText }}
        </VsButton>

        <BPopover
            custom-class="vs-social-share__popover"
            :target="`vs-social-share-popover--${id}`"
            triggers="click blur"
            placement="leftbottom"
            @shown="onShown"
            @hidden="onHidden"
            @hide="onHide"
            ref="popover"
            v-if="!noJs"
        >
            <VsHeading
                thin
                level="3"
            >
                {{ sharePopoverTitle }}
            </VsHeading>

            <VsRow>
                <label for="hiddenAnchor">
                    <input
                        type="text"
                        class="hidden-anchor"
                        id="hiddenAnchor"
                        tabindex="0"
                        readonly
                        ref="hiddenAnchor"
                    >
                </label>

                <!-- @slot Default slot for SocialShareItems -->
                <slot :on-copy-link="onCopyLink" />
            </VsRow>

            <VsButton
                class="vs-social-share__close-btn"
                variant="transparent"
                @click.native="onClose"
                aria-label="Close"
            >
                <span class="sr-only">
                    {{ closeAltText }}
                </span>
                <VsIcon
                    name="close"
                    variant="dark"
                    size="md"
                    aria-hidden="true"
                />
            </VsButton>
        </BPopover>

        <VsModuleWrapper
            v-if="noJs"
            class="vs-social-share--module-list"
        >
            <VsContainer>
                <VsRow>
                    <VsCol cols="12">
                        <VsHeading
                            thin
                            level="3"
                            class="mb-9 mt-0"
                        >
                            {{ sharePopoverTitle }}
                        </VsHeading>
                    </VsCol>
                </VsRow>
                <VsRow class="justify-content-center">
                    <!-- @slot Default slot for SocialShareItems -->
                    <slot />
                </VsRow>
            </VsContainer>
        </VsModuleWrapper>
    </div>
</template>

<script>
import VsIcon from '@components/elements/icon/Icon';
import VsButton from '@components/elements/button/Button';
import VsHeading from '@components/elements/heading/Heading';
import VsModuleWrapper from '@components/patterns/module-wrapper/ModuleWrapper';
import {
    VsRow, VsContainer, VsCol,
} from '@components/elements/layout';
import { BPopover } from 'bootstrap-vue';

/**
 * This component includes a popover to share content to social
 * media channels.
 *
 * @displayName Social Share Button
 */
export default {
    name: 'VsSocialShare',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsIcon,
        VsButton,
        VsHeading,
        VsModuleWrapper,
        BPopover,
        VsRow,
        VsContainer,
        VsCol,
    },
    props: {
        /**
         * Text to display on share button
         */
        shareBtnText: {
            type: String,
            required: true,
        },
        /**
         * Title text for popover
         */
        sharePopoverTitle: {
            type: String,
            required: true,
        },
        /**
         * Page URL to share
         */
        pageUrl: {
            type: String,
            required: true,
        },
        /**
         * Page Title to include in share link
         */
        pageTitle: {
            type: String,
            default: '',
        },
        /**
         * Accessiblity alt text for the close button
         */
        closeAltText: {
            type: String,
            required: true,
        },
        /**
         * Unique element ID
         */
        id: {
            type: String,
            required: true,
        },
        /**
         * Displays in a list instead of a popover
         */
        noJs: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            copyLink: false,
        };
    },
    methods: {
        /**
         * Closes popover on close button click
         */
        onClose() {
            this.$refs.popover.$emit('close');
        },
        /**
         * When popover is shown, focuses on hidden anchor
         */
        onShown() {
            this.focusRef(this.$refs.hiddenAnchor);
        },
        /**
         * When popover is hidden, focuses back on share button
         */
        onHidden() {
            this.focusRef(this.$refs.shareButton);
        },
        onHide(bvEvent) {
            if (this.copyLink) {
                bvEvent.preventDefault();
                this.focusRef(this.$refs.hiddenAnchor);
                this.copyLink = false;
            }
        },
        onCopyLink() {
            this.copyLink = true;
        },
        /**
         * Check before focusing after popover has been positioned
         */
        focusRef(ref) {
            this.$nextTick(() => {
                this.$nextTick(() => {
                    ;(ref.$el || ref).focus();
                });
            });
        },
    },
    /**
     * Provides the URL properties to be injected into child component 'SocialShareItem'
     */
    provide() {
        return {
            pageUrl: this.pageUrl,
            pageTitle: this.pageTitle,
            noJs: this.noJs,
        };
    },
};
</script>

<style lang="scss">
    .vs-social-share{
        &--module-list{
            display: none;
        }

        &__share-btn.vs-button.btn{
            padding: 0 $spacer-1;
            letter-spacing: initial;
            text-decoration: underline;
            font-weight: $font-weight-normal;
            font-size: $small-font-size;
            line-height: $line_height_l;

            svg {
                display: block;
                margin: 0 auto;
            }

            &:hover{
                color: $color-pink;
            }
        }

        &__popover{
            max-width: 600px;
            width: 96%;
            font-size: inherit;
            text-align: center;
            border: 0;
            top: -35px!important;
            left: 0!important;

            .arrow{
                display: none;
            }

            &.bs-popover-right{
                margin-left: 0;
            }

            .hidden-anchor {
                position: absolute;
                left: 0;
                top: 0;
                width: 0px;
                height: 0px;
                border: none;
                background: transparent!important;

                &:focus{
                    outline: none;
                    border: 0;
                }
            }

            @include media-breakpoint-up(sm) {
                max-width: 450px;
                width: 450px;
                top: -25px!important;
                left: 57px!important;
                box-shadow: -25px 25px 20px 1px rgba(0, 0, 0, 0.1);
            }

            @include media-breakpoint-up(md) {
                max-width: 600px;
                width: 600px;
            }

            @include media-breakpoint-up(lg) {
                max-width: 700px;
                width: 700px;
            }

            @include media-breakpoint-up(xl) {
                max-width: 800px;
                width: 800px;
            }

            .popover-body{
                padding: $spacer-8 $spacer-2;

                @include media-breakpoint-only(sm) {
                    padding: $spacer-4 $spacer-7 $spacer-8;

                    .row{
                        margin-right: -4px;
                        margin-left: -4px;

                        > [class*=col-]{
                            padding-right: 4px;
                            padding-left: 4px;
                        }
                    }
                }

                @include media-breakpoint-up(md) {
                    padding: $spacer-9;
                }

                @include media-breakpoint-up(xl) {
                    padding: $spacer-9 $spacer-12;
                }

                h3.vs-heading{
                    margin-bottom: $spacer-9;
                }
            }
        }

        &__close-btn.vs-button.btn{
            position: absolute;
            right: $spacer-4;
            top: $spacer-4;
            border: 0;
            padding: $spacer-1;

            &:hover{
                .vs-icon.vs-icon--variant-dark{
                    fill: $color-pink;
                }
            }
        }
    }

    @include no-js {
        .vs-social-share{
            &--module-list{
                display: block;
            }

            &__share-btn.vs-button.btn{
               display: none;
            }

            &__popover{
                position: relative!important;
                display: block!important;
                opacity: $opacity-100!important;
                outline: 0!important;
            }
        }
    }
</style>

<docs>
```jsx
    <BsWrapper class="d-flex justify-content-end my-3 mx-3">
        <VsSocialShare
            id="default"
            page-url="http://www.visitscotland.com"
            page-title="VisitScotland - Scotland's National Tourist Organisation"
            share-popover-title="Share On"
            share-btn-text="Share"
            close-alt-text="Close"
        >
            <template slot-scope="{onCopyLink}">
                <VsSocialShareItem
                    name="facebook"
                    link-text="Facebook"
                />
                <VsSocialShareItem
                    name="pinterest"
                    link-text="Pinterest"
                />
                <VsSocialShareItem
                    name="whatsapp"
                    link-text="WhatsApp"
                />
                <VsSocialShareItem
                    name="twitter"
                    link-text="Twitter"
                />
                <VsSocialShareItem
                    name="email"
                    link-text="Email"
                />
                <VsSocialShareItem
                    @copyLinkClicked="onCopyLink"
                    name="link"
                    link-text="Copy Link"
                    link-copied-text="Link Copied!"
                />
            </template>
        </VsSocialShare>
    </BsWrapper>
```
</docs>
