<template>
    <VsCol
        class="vs-social-share-item"
        data-test="vs-social-share-item"
        :cols="4"
        :md="noJs ? 2 : 4"
    >
        <VsLink
            class="vs-social-share-item__link"
            :href="shareUrl"
            @click.native="copyToClipboard"
            :id="`vs-share-${name}`"
        >
            <VsIcon
                :name="name"
                :custom-colour="iconColour"
                variant="dark"
                size="xl"
                small-size="lg"
            />

            {{ show ? linkCopiedText : linkText }}
        </VsLink>
    </VsCol>
</template>

<script>
import VsLink from '@components/elements/link/Link';
import VsIcon from '@components/elements/icon/Icon';
import { VsCol } from '@components/elements/grid';

/**
 * This component displays an icon and link to a social sharing channel
 *
 * @displayName Social Share Item
 */
export default {
    name: 'VsSocialShareItem',
    status: 'prototype',
    release: '0.0.1',
    components: {
        VsCol,
        VsLink,
        VsIcon,
    },
    props: {
        /**
        * Type of link and icon to display
        */
        name: {
            type: String,
            required: true,
            validator: (value) => value.match(/(facebook|twitter|pinterest|link|email|whatsapp)/),
        },
        /**
        * Text to display within the link
        */
        linkText: {
            type: String,
            required: true,
        },
        /**
        * Text to display when link is copied
        */
        linkCopiedText: {
            type: String,
            default: '',
        },
    },
    inject: {
        /**
        * Page URL to share - provided by parent SocialShare
        */
        pageUrl: {
            default: '',
        },
        /**
        * Page Title to share - provided by parent SocialShare
        */
        pageTitle: {
            default: '',
        },
        /**
        * Page Title to share - provided by parent SocialShare
        */
        noJs: {
            default: '',
        },
    },
    data() {
        return {
            show: false,
        };
    },
    computed: {
        iconColour() {
            let colour = '';

            switch (this.name) {
            case 'facebook':
                colour = '#1877f2';
                break;
            case 'twitter':
                colour = '#08A0E9';
                break;
            case 'pinterest':
                colour = '#E60023';
                break;
            case 'whatsapp':
                colour = '#455a64';
                break;
            default:
                colour = '#000000';
            };
            return colour;
        },
        encodedPageTitle() {
            return encodeURI(this.pageTitle);
        },
        shareUrl() {
            let url = '';

            switch (this.name) {
            case 'facebook':
                url = `https://www.facebook.com/sharer/sharer.php?u=${this.pageUrl}&t=${this.encodedPageTitle}`;
                break;
            case 'twitter':
                url = `https://twitter.com/intent/tweet?text=${this.encodedPageTitle}%20@VisitScotland&url=${this.pageUrl}`;
                break;
            case 'pinterest':
                url = `https://www.pinterest.com/pin/create/button/?url=${this.pageUrl}`;
                break;
            case 'email':
                url = `mailto:?body=${this.encodedPageTitle}%20-%20${this.pageUrl}&subject=${this.encodedPageTitle}`;
                break;
            case 'whatsapp':
                url = `https://wa.me/?text=${this.encodedPageTitle}%20-%20${this.pageUrl}`;
                break;
            case 'link':
                url = '#';
                break;
            default:
                url = '#';
            };
            return url;
        },
    },
    methods: {
        copyToClipboard() {
            if (this.name === 'link') {
                this.$emit('copyLinkClicked');

                // Clipboard API supported?
                if (!navigator.clipboard) return;

                // copy text to clipboard
                if (navigator.clipboard.writeText) {
                    navigator.clipboard.writeText(this.pageUrl);
                }

                // show success text on button
                this.show = true;
                setTimeout(() => { this.show = false; }, 2000);
            }
        },
    },
};
</script>

<style lang="scss">
.vs-social-share-item{
    &__link.vs-link.vs-link--variant-primary{
        border: 1px solid $color-secondary-gray-tint-4;
        display: block;
        margin-bottom: $spacer-2;
        padding: $spacer-4 $spacer-2;
        text-align: center;

        @include media-breakpoint-up(md) {
            padding: $spacer-6;
            margin-bottom: $spacer-6;
        }

        &:focus, &:focus-visible{
            outline-offset: 0px;
            border: 1px solid $color-pink;
            outline: 3px solid rgba($color-pink, .3);
        }

        .vs-icon{
            display: block;
            margin: 0 auto $spacer-3;
        }
    }
}

@include no-js {
    .vs-social-share-item{
        &__link.vs-link.vs-link--variant-primary{
            margin-bottom: $spacer-4;

            @include media-breakpoint-up(sm) {
                margin-bottom: $spacer-6;
            }

            @include media-breakpoint-up(md) {
                margin-bottom: 0;
            }

            @include media-breakpoint-up(md) {
                padding: $spacer-6 $spacer-2;
            }
        }
    }
}
</style>

<docs>
```jsx
    <BsWrapper class="d-flex my-3 mx-3">
        <VsSocialShareItem
            name="twitter"
            link-text="Twitter"
        />
    </BsWrapper>
```
</docs>
