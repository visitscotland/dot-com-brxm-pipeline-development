<template>
    <VsSvg
        :path="path"
        :class="{
            'vs-icon': true,
            [`vs-icon--size-${size}`]: true,
            [`vs-icon--sm-size-${smallSize}`]: smallSize,
            [`vs-icon--${formattedName}`]: true,
            [`vs-icon--variant-${variant}`]: variant,
            ['icon--' + orientation]: orientation,
        }"
        :style="[customColour ? {fill: customColour} : {}]"
        v-bind="$attrs"
        data-test="vs-icon"
    />
</template>

<script>
import { get } from 'lodash';
import designTokens from '@/assets/tokens/tokens.raw.json';
import VsSvg from '../svg';

const iconPath = 'icons/';

/**
 * Icons are used to visually communicate core parts of the product and
 * available actions. They can act as wayfinding tools to help users more
 * easily understand where they are in the product.
 *
 * Our icons come in specific sizes - xxs, xs, sm, md, lg and xl - and can be any colour,
 * including any of the theme colours.
 *
 * @displayName Icon
 */
export default {
    name: 'VsIcon',
    status: 'prototype',
    release: '0.1.0',
    components: {
        VsSvg,
    },
    props: {
        /**
         * The name of the icon to display, which will be the name of the icon file
         */
        name: {
            type: String,
            required: true,
            default: 'search',
        },
        /**
         * The fill color of the SVG icon.
         * `primary, secondary, light, dark,
         * reverse-white, secondary-teal`
         */
        variant: {
            type: String,
            default: null,
            validator: (value) => value.match(
                /(primary|secondary|light|dark|reverse-white|secondary-teal)/,
            ),
        },
        /**
         * Accepts a colour (hex code or colour name) to fill the icon, if this is
         * set it will override the given variant. This should be used for individual
         * exceptions but if one is being used regularly it should likely be a variant
         * instead.
         */
        customColour: {
            type: String,
            default: null,
        },
        /**
        * The orientation of the icon. Defaults to 'up'.
        * `up, left, right, down`
        */
        orientation: {
            type: String,
            default: null,
            validator: (value) => value.match(
                /(up|down|left|right)/,
            ),
        },
        /**
        * Size of icon, defaults to medium
        * `xxs, xs, sm, md, lg, xl`)
        */
        size: {
            type: String,
            default: 'md',
            validator: (value) => value.match(/(xxs|xs|sm|md|lg|xl)/),
        },
        /**
        * Size of icon at small and extra small viewport, defaults to null,
        * the size falls back to the regular size if not set
        * `xxs, xs, sm, md, lg, xl`)
        */
        smallSize: {
            type: String,
            default: null,
            validator: (value) => value.match(/(xxs|xs|sm|md|lg|xl)/),
        },
    },
    data() {
        return {
            iconLookup: [
                {
                    key: 'accesstoliet',
                    value: 'accessible-toilet',
                },
                {
                    key: 'accessparkdrop',
                    value: 'facility-accessparkdrop',
                },
                {
                    key: 'acco',
                    value: 'product-accommodation',
                },
                {
                    key: 'acti',
                    value: 'product-activities',
                },
                {
                    key: 'attr',
                    value: 'product-attractions',
                },
                {
                    key: 'audioloop',
                    value: 'facility-audioloop',
                },
                {
                    key: 'cafereston',
                    value: 'cafe',
                },
                {
                    key: 'cate',
                    value: 'product-food-and-drink',
                },
                {
                    key: 'cities',
                    value: 'city',
                },
                {
                    key: 'cycling',
                    value: 'cycle',
                },
                {
                    key: 'dsblaccess',
                    value: 'facility-dsblaccess',
                },
                {
                    key: 'wheelchairaccess',
                    value: 'facility-dsblaccess',
                },
                {
                    key: 'even',
                    value: 'product-events',
                },
                {
                    key: 'familyev',
                    value: 'family',
                },
                {
                    key: 'filmev',
                    value: 'film-tv',
                },
                {
                    key: 'hottub',
                    value: 'hot-tub',
                },
                {
                    key: 'parking',
                    value: 'facility-parking',
                },
                {
                    key: 'petswelcom',
                    value: 'facility-petswelcom',
                },
                {
                    key: 'wifi',
                    value: 'facility-wifi',
                },
                {
                    key: 'public',
                    value: 'public-transport',
                },
                {
                    key: 'pubtranrte',
                    value: 'public-transport',
                },
                {
                    key: 'reta',
                    value: 'product-shopping',
                },
                {
                    key: 'spahealth',
                    value: 'wellness',
                },
                {
                    key: 'vege',
                    value: 'vegan-vegetarian',
                },
                {
                    key: 'walking',
                    value: 'walk',
                },
                {
                    key: 'transport',
                    value: 'transport',
                },
                {
                    key: 'brekavail',
                    value: 'breakfast-available',
                },
            ],
        };
    },
    computed: {
        path() {
            return iconPath + this.formattedName;
        },
        dimension() {
            return get(designTokens, `props.icon_size_${this.size}.value`, '40px');
        },
        formattedName() {
            /*
             * To facilitate more readable icon names and
             * organise / group icons within the design system
             * there is a lookup for how keys may be passed from the backend
             */
            const formattedNameLookup = this.iconLookup.find(({ key }) => key === this.name);

            return formattedNameLookup !== undefined ? formattedNameLookup.value : this.name;
        },
    },
};
</script>

<style lang="scss">

$sizes: (
    xxs: $icon-size-xxs,
    xs: $icon-size-xs,
    sm: $icon-size-sm,
    md: $icon-size-md,
    lg: $icon-size-lg,
    xl: $icon-size-xl,
);

$variants: (
    primary: $color-theme-primary,
    secondary: $color-theme-secondary,
    light: $color-theme-light,
    dark: $color-theme-dark,
    reverse-white: $color-white,
    secondary-teal: $color-theme-secondary-teal,
);

.vs-icon {
    fill: $color-black;
    overflow: visible;

    @each $size in map-keys($sizes) {
        $this-size: map-get($sizes, $size);

        &.vs-icon--size-#{$size} {
            height: $this-size;
            width: $this-size;
            font-size: $this-size;
            padding: 0;
        }
    }

    // This is awkward but these have to be two separate loops through
    // the sizes. If you have one loop generating both sets you run into
    // specificity issues as the classes go sm-xs xs sm-sm sm etc etc,
    // and the later non-sm classes override the earlier sm ones
    @each $size in map-keys($sizes) {
        $this-size: map-get($sizes, $size);

        @include media-breakpoint-down(sm) {
            &.vs-icon--sm-size-#{$size} {
                height: $this-size;
                width: $this-size;
                font-size: $this-size;
                padding: 0;
            }
        }
    }

    @each $variant in map-keys($variants) {
        &.vs-icon--variant-#{$variant} {
            fill: map-get($variants, $variant);

             &.icon--reverse {
                fill: $color-white;
                background: map-get($variants, $variant);
            }
        }
    }

    &.icon--down {
        transform: rotate(180deg);
    }

    &.icon--left {
        transform: rotate(270deg);
    }

    &.icon--right {
        transform: rotate(90deg);
    }
}
</style>

<docs>
```jsx
  <div>

    <h3>Default</h3>
    <VsIcon name="search" />

    <h3 class="mt-8">Variant</h3>
    <VsIcon name="user" variant="primary" />
    <VsIcon name="user" variant="secondary" />
    <VsIcon name="user" variant="light" />
    <VsIcon name="user" variant="dark" />
    <VsIcon name="user" variant="reverse-white" />
    <VsIcon name="user" variant="secondary-teal" />

    <h3 class="mt-8">Custom Colour</h3>
    <VsIcon name="user" customColour="#ff0000" />
    <VsIcon name="user" customColour="gold" />

    <h3 class="mt-8">Size</h3>

    <div class="d-flex">
        <div class="d-flex flex-column mr-3 align-items-center">
            <h4>xxs</h4>
            <VsIcon name="favourite" size="xxs" />
            </div>

            <div class="d-flex flex-column mr-3 align-items-center">
            <h4>xs</h4>
            <VsIcon name="favourite" size="xs" />
            </div>

            <div class="d-flex flex-column mr-3 align-items-center">
            <h4>sm</h4>
            <VsIcon name="favourite" size="sm" />
            </div>

            <div class="d-flex flex-column mr-3 align-items-center">
            <h4>md</h4>
            <VsIcon name="favourite" size="md" />
            </div>

            <div class="d-flex flex-column mr-3 align-items-center">
            <h4>lg</h4>
            <VsIcon name="favourite" size="lg" />
            </div>

            <div class="d-flex flex-column mr-3 align-items-center">
            <h4>xl</h4>
            <VsIcon name="favourite" size="xl" />
            </div>
        </div>

        <h3 class="mt-8">Orientation</h3>
        <div class="d-flex">
            <div class="d-flex flex-column mr-3 align-items-center">
                <h4>Up</h4>
                <VsIcon name="chevron" orientation="up" />
            </div>
            <div class="d-flex flex-column mr-3 align-items-center">
                <h4>Down</h4>
                <VsIcon name="chevron" orientation="down" />
            </div>
            <div class="d-flex flex-column mr-3 align-items-center">
                <h4>Left</h4>
                <VsIcon name="chevron" orientation="left" />
            </div>
            <div class="d-flex flex-column mr-3 align-items-center">
                <h4>Right</h4>
                <VsIcon name="chevron" orientation="right" />
            </div>
        </div>
    </div>
```
</docs>
