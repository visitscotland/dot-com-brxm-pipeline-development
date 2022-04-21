<template>
    <div
        class="vs-canned-search-badges"
    >
        <div
            v-if="badgeOne"
            class="vs-canned-search-badges__badge vs-canned-search-badges__badge--teal
            vs-canned-search-badges__badge--tr"
        >
            {{ badgeOne }}
        </div>
        <div
            v-else-if="multiBadgeOne && multiBadgeOne.length"
            class="vs-canned-search-badges__multi-badge-holder"
        >
            <div
                v-for="(badge, index) in limitedMulti"
                :key="index"
                class="vs-canned-search-badges__badge vs-canned-search-badges__badge--teal
                vs-canned-search-badges__multi-badge"
            >
                {{ badge }}
            </div>
        </div>
        <div
            v-if="badgeTwo"
            class="vs-canned-search-badges__badge vs-canned-search-badges__badge--pink
            vs-canned-search-badges__badge--tr2"
        >
            {{ badgeTwo }}
        </div>
        <div
            v-if="badgeThree"
            class="vs-canned-search-badges__badge vs-canned-search-badges__badge--light-pink
            vs-canned-search-badges__badge--br"
        >
            {{ badgeThree }}
        </div>
    </div>
</template>

<script>

/**
* Badges to show information in the stretched link component
*
* @displayName Canned Search Badges
*/

export default {
    name: 'VsCannedSearchBadges',
    status: 'prototype',
    release: '0.0.1',
    props: {
        /**
        * Content for the top right, teal badge
        */
        badgeOne: {
            type: String,
            default: '',
        },
        /**
        * Content for multiple top right, teal badges
        * Only appears if badgeOne not set
        *
        * Expects an array of strings
        */
        multiBadgeOne: {
            type: Array,
            default: null,
        },
        /**
        * Content for the middle right, pink badge
        */
        badgeTwo: {
            type: String,
            default: '',
        },
        /**
        * Content for the bottom right, light pink badge
        */
        badgeThree: {
            type: String,
            default: '',
        },
    },
    computed: {
        /**
        * Limits multiBadgeOne to 3 entries
        */
        limitedMulti() {
            if (this.multiBadgeOne) {
                return this.multiBadgeOne
                    .slice(0, 3);
            }

            return [];
        },
    },
};

</script>

<style lang="scss">
    .vs-canned-search-badges {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;

        &__badge {
            position: absolute;
            padding: $spacer-0 $spacer-2;
            text-transform: uppercase;
            letter-spacing: $letter-spacing-l;
            max-height: 1.5em;
            text-align: right;
            text-overflow: ellipsis;
            max-width: calc(100% - #{$spacer-4});
            overflow: hidden;
            white-space: nowrap;
            font-size: $font-size-3;

            &--teal {
                color: $color-white;
                background-color: $color-secondary-teal-shade-2;
            }

            &--pink {
                color: $color-white;
                background-color: $color_pink;
            }

            &--light-pink {
                background-color: $color_pink_tint_6;
                color: $color_pink;
                font-weight: $font-weight-bold;
            }

            &--tr {
                top: $spacer-2;
                right: $spacer-2;
            }

            &--tr2 {
                top: 2.5em;
                right: $spacer-2;
            }

            &--br {
                bottom: $spacer-2;
                right: $spacer-2;
                padding: $spacer-0 $spacer-4;
            }
        }

        &__multi-badge-holder {
            position: absolute;
            text-align: right;
            max-width: calc(100% - #{$spacer-4});
            width: 100%;
            max-height: 1.5em;
            overflow: hidden;
            top: $spacer-2;
            right: $spacer-2;
        }

        &__multi-badge {
            position: relative;
            display: inline-block;
            margin-left: $spacer-2;
        }
    }

</style>
