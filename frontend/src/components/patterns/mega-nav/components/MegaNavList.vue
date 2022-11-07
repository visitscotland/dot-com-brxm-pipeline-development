<template>
    <nav
        class="vs-mega-nav-list"
        data-test="vs-mega-nav-list"
        :aria-label="listHeadingId"
    >
        <VsHeading
            v-if="listHeading"
            class="vs-mega-nav-list__heading"
            :id="listHeadingId"
            level="2"
        >
            {{ listHeading }}
        </VsHeading>

        <VsList class="vs-mega-nav-list__list">
            <!-- @slot Slot for nav list items  -->
            <slot name="navListItems" />

            <!-- @slot Slot for nav list heading cta link at bottom of the menu group  -->
            <slot name="navHeadingCtaLink" />
        </VsList>
    </nav>
</template>

<script>
import VsList from '@components/elements/list/List';
import VsHeading from '@components/elements/heading/Heading';
/**
 *  Meganav list wrapper with a slots list items
 *
 * @displayName MegaNav List
 */
export default {
    name: 'VsMegaNavList',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsList,
        VsHeading,
    },
    props: {
        /**
         * The URL for the nav list link
         */
        listHeading: {
            type: String,
            default: '',
        },
    },
    computed: {
        listHeadingId() {
            if (this.listHeading) {
                const transformedHeading = this.listHeading.replace(/\W/g, '');

                return `vsMeganav${transformedHeading}`;
            }

            return '';
        },
    },
};
</script>

<style lang="scss">
.vs-mega-nav-list {
    position: relative;
    padding: 0;
    margin: 0;
    width: 100%;

    @include media-breakpoint-up(lg) {
        margin: 0 $spacer-6 $spacer-4 0;
        width: 23%;
    }

    @include media-breakpoint-up(xl) {
        margin-right: $spacer-9;
        width: 21.8%;
    }

    @include media-breakpoint-up(xxl) {
        margin-right: $spacer-10;
        width: 21.3%;
    }

    .vs-list.vs-mega-nav-list__list{
        padding-inline-start: 0;
        margin: 0;

        li::before{
            display: none;
        }
    }

    .vs-heading.vs-mega-nav-list__heading {
        display: none;
        padding: $spacer-1 $spacer-4;
        margin: 0 0 $spacer-1;
        border-bottom: 1px solid $color-gray-tint-5;
        color: $color-secondary-gray-shade-3;
        font-size: $font-size-6;
        font-family: $font-family-sans-serif;
        line-height: $line-height-standard;
        letter-spacing: normal;

        @include media-breakpoint-up(lg) {
            display: flex;
        }
    }
}

@include no-js {
    .vs-mega-nav-list {
        width: 100%;
        margin: 0 $spacer-6 $spacer-4 0;

        @include media-breakpoint-up(sm) {
            width: 50%;
        }

        @include media-breakpoint-up(md) {
            width: 32%;
        }
    }
}
</style>
