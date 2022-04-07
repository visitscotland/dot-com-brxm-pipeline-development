<template>
    <div
        class="vs-quote"
        :class="variantClass"
        data-test="vs-quote"
    >
        <div class="vs-quote__speech-container">
            <span class="vs-quote__speech-mark">“</span>
        </div>
        <div
            class="vs-quote__author-container"
            v-if="hasAuthorImage"
        >
            <!-- @slot Holds the author image (vs-image expected) -->
            <slot name="quoteImage" />
        </div>
        <div class="vs-quote__content-container">
            <div class="vs-quote__content">
                <!-- @slot Holds the main body of the quote (html expected) -->
                <slot name="quoteContent" />
            </div>
            <p
                class="vs-quote__author-name"
                v-if="hasAuthorName"
            >
                <!-- @slot Holds the name of the author (text expected) -->
                <slot name="quoteAuthorName" />
            </p>
            <p
                class="vs-quote__author-title"
                v-if="hasAuthorTitle"
            >
                <!-- @slot Holds the job title of the author (text expected) -->
                <slot name="quoteAuthorTitle" />
            </p>
            <!-- @slot Optional slot that holds a cta for the block (vs-button expected) -->
            <slot name="quoteLink" />
        </div>
    </div>
</template>

<script>
/**
 * An embeddable quote with an author, links to icentre information and an optional CTA
 *
 * @displayName Quote
 */
export default {
    name: 'VsQuote',
    status: 'prototype',
    release: '0.0.1',
    props: {
        variant: {
            type: String,
            default: 'narrow',
            validator: (value) => value.match(/(narrow|wide)/),
        },
    },
    computed: {
        hasAuthorName() {
            return !!this.$slots.quoteAuthorName;
        },
        hasAuthorTitle() {
            return !!this.$slots.quoteAuthorTitle;
        },
        hasAuthorImage() {
            return !!this.$slots.quoteImage;
        },
        variantClass() {
            return this.variant ? `vs-quote--${this.variant}` : '';
        },
    },
};
</script>

<style lang="scss">
.vs-quote {
    &__content {
        font-size: $font-size-8;
        font-weight: $font-weight-light;
        line-height: $line-height-s;
    }

    &__author-name {
        font-weight: $font-weight-bold;
        margin-bottom: $spacer-0;

        @include media-breakpoint-up(md) {
            margin-bottom: $spacer-1;
            font-size: $font-size-5;
        }
    }

    &__author-title {
        line-height: $spacer-5;

        @include media-breakpoint-up(md) {
            line-height: $line-height-s;
            font-size: $font-size-5;
            margin-bottom: $spacer-7;
        }
    }

    &__speech-mark {
        font-weight: $font-weight-bold;
        color: $color-gray;
        font-size: 7rem;
        height: 5rem;
        line-height: $line-height-xs;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
    }

    &__speech-container {
        display: block;
        margin-bottom: $spacer-4;
        margin-right: $spacer-5;
        width: 3.5rem;
        height: 2.5rem;
        position: relative;

        @include media-breakpoint-up(sm) {
            display: inline-block;
            vertical-align: top;
        }
    }

    &__author-container {
        display: block;
        width: 50%;
        max-width: 8rem;
        margin-bottom: $spacer-4;

        img {
            width: 100%;
        }

        @include media-breakpoint-up(lg) {
            margin-right: $spacer-5;
        }
    }

    &__content-container {
        display: block;
    }

    &--wide{
        .vs-quote__author-container {
            @include media-breakpoint-up(sm) {
                display: inline-block;
                vertical-align: top;
                margin-bottom: $spacer-5;
            }
        }

        .vs-quote__content-container{
            @include media-breakpoint-up(sm) {
                display: inline-block;
                vertical-align: top;
            }

            @include media-breakpoint-up(xl) {
                width: calc(100% - 15rem);
                margin-top: -7px;
            }
        }
    }
}
</style>

<docs>
```jsx
    <VsQuote>
        <VsImg
            src="https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm"
            alt="This is the alt text"
            data-sizes="auto"
            class="lazyload"
            slot="quoteImage">
        </VsImg>
        <p slot="quoteContent">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi
            ac urna non metus tempor accumsan ut non risus. In turpis est,
            imperdiet eu sagittis ac, sodales quis nunc. Ut sagittis vulputate
            lacinia. Vivamus faucibus lorem leo, nec laoreet ligula auctor a.
            Donec id eros a ipsum facilisis lacinia nec ac nunc.
        </p>
        <span slot="quoteAuthorName">Penny</span>
        <span slot="quoteAuthorTitle">
            Visitor Services Advisor at Edinburgh iCentre
        </span>
        <VsButton
            href="#"
            slot="quoteLink"
        >
            Signet Library
        </VsButton>
    </VsQuote>
```
</docs>
