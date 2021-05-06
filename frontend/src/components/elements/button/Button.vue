<template>
    <BButton
        :variant="variant"
        :href="href"
        :tabindex="tabindex"
        class="vs-button"
        :class="{
            'vs-button--animated': animate,
            'vs-button--is-animating': isAnimating,
            [backgroundClass]: backgroundClass,
            [textTransformClass]: textTransformClass,
        }"
        :size="size"
        v-bind="$attrs"
        @click="animateHandler"
        @mouseover="hovered = true"
        @focusin="hovered = true"
        @mouseleave="hovered = false"
        @focusout="hovered = false"
    >
        <VsIcon
            :class="{ 'mr-2': !iconOnly }"
            v-if="icon"
            :name="icon"
            :size="iconSizeOverride || calcIconSize"
            :padding="0"
            :orientation="iconOrientation"
            :variant="iconVariantOverride || calcIconVariant"
        />
        <!-- @slot The button content goes here -->
        <slot />
    </BButton>
</template>
<script>
import { BButton } from 'bootstrap-vue';
import VsIcon from '@components/elements/icon/Icon';

/**
 * TODO: Document usage.
 *
 * NOTE: When listening for the @click event you need to use the `native` modifier
 * in order to listen for the underlying `button`s native event. e.g.
 *
 * <vs-button @click.native="handler">Click me</vs-button>
 *
 * @displayName Button
 */

export default {
    name: 'VsButton',
    status: 'prototype',
    release: '0.0.1',
    components: {
        BButton,
        VsIcon,
    },
    props: {
        /**
         * Use this option to render the button as an anchor element with the given href.
         */
        href: {
            type: String,
            default: null,
        },
        /**
         * Tab index value - this is needed as tabindex attribute is sometimes stripped
         * from the button on first update with nested components
         */
        tabindex: {
            type: String,
            default: null,
        },
        /**
         * Style variation to give additional meaning.
         * `primary, secondary`
         */
        variant: {
            type: String,
            default: 'primary',
            validator: (value) => value.match(
                /(primary|secondary|transparent|dark|light)/,
            ),
        },
        /**
         * Background property used primarily for overrides.
         * Normally we use the above variant properties for applying background color, but
         * in cases where outline buttons are used and default transparent state needs to be
         * overridden, this background property can be applied
         * `white`
         */
        background: {
            type: String,
            default: null,
            validator: (value) => value.match(/(white)/),
        },
        /**
         * Style the button size.
         * `sm, md, lg`
         */
        size: {
            type: String,
            default: 'md',
            validator: (value) => value.match(/(sm|md|lg)/),
        },
        /**
         * By default, buttons have an animation behaviour on click.
         * To disable, add an animate=false property
         */
        animate: {
            type: Boolean,
            default: true,
        },
        /**
         * If you need a button with icon
         * just pass the icon name here.
         */
        icon: {
            type: String,
            default: '',
        },
        /**
         * By default, button text is uppercase
         * To disable, add an uppercase=false property
         */
        uppercase: {
            type: Boolean,
            default: true,
        },
        /**
         * The icon orientation
         * `up, down, left, right`
         */
        iconOrientation: {
            type: String,
            default: null,
            validator: (value) => value.match(/(up|down|left|right)/),
        },
        /**
         * If the button contains an icon and no text
         */
        iconOnly: {
            type: Boolean,
            default: false,
        },
        /**
         * The variant to be used for a contained icon, generally this is
         * automatically calculated based on the button variant but in a few
         * unusual cases it is desirable to manually set it
         */
        iconVariantOverride: {
            type: String,
            default: null,
            validator: (value) => value.match(
                /(primary|secondary|light|dark|reverse-white|secondary-teal)/,
            ),
        },
        /**
         * The size to be used for a contained icon, generally this is
         * automatically calculated based on the button size but in a few
         * unusual cases it is desirable to manually set it
         */
        iconSizeOverride: {
            type: String,
            default: null,
            validator: (value) => value.match(/(xxs|xs|sm|md|lg|xl)/),
        },
    },
    data() {
        return {
            isAnimating: false,
            hovered: false,
        };
    },
    computed: {
        backgroundClass() {
            return this.background ? [`btn-bg-${this.background}`] : null;
        },
        calcIconSize() {
            switch (this.size) {
            case 'sm':
                return 'xs';
            case 'md':
                return 'sm';
            case 'lg':
                return 'md';
            default:
                return 'md';
            }
        },
        textTransformClass() {
            return this.uppercase ? 'text-uppercase' : null;
        },
        calcIconVariant() {
            if (this.isOutline) {
                if (this.hovered) {
                    return 'light';
                }

                return this.outlineColour;
            }

            if (this.variant === 'transparent') {
                return 'primary';
            }

            return 'light';
        },
        isOutline() {
            return this.variant.match(/outline/) !== null;
        },
        outlineColour() {
            return this.variant.replace('outline-', '');
        },
    },
    methods: {
        animateHandler() {
            this.isAnimating = true;
            setTimeout(() => {
                this.isAnimating = false;
            }, 1000);
        },
    },
};
</script>

<style lang="scss">

.vs-button.btn {
    font-family: $font-family-base;
    font-weight: $font-weight-light;
    transition: $transition-base;
    text-decoration: none;
    letter-spacing: 2px;
    position: relative;
    overflow: hidden;

    .btn-dark {
        &:hover {
            background-color: $color-gray-shade-5;
        }
    }

    &.btn-bg-white:not(:hover) {
        background-color: $color-white;
    }

    &.btn-light,
    &.btn-transparent {
        &:focus {
            box-shadow: 0 0 0 0.2rem rgba(0, 0, 0, 0.25);
        }

        &::after {
            background: rgba(0, 0, 0, 0.2);
        }
    }

    &.vs-button--animated {
        @keyframes bubble {
            0% {
                transform: scale(0, 0);
                opacity: 1;
            }
            100% {
                opacity: 0;
                transform: scale(100, 100);
            }
        }

        &::after {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 50%;
            bottom: 0;
            content: "";
            height: 5px;
            opacity: 0;
            position: absolute;
            right: 0;
            transform-origin: 50% 50%;
            transform: scale(1, 1) translate(-50%);
            width: 5px;
        }

        &.vs-button--is-animating::after {
            animation: bubble 500ms ease-in-out;
        }
    }

    .vs-icon {
        margin-top: -.05em;
    }
}
</style>

<docs>
```jsx
    <h4>Types</h4>
    <BsWrapper class="d-flex flex-wrap mb-4">
      <VsButton class="mr-2 mb-2">Button</VsButton>
      <VsButton :animate=false class="mr-2 mb-2">Button with no animation</VsButton>
      <VsButton class="mr-2 mb-2" href="https://www.visitscotland.com">Link</VsButton>
    </BsWrapper>

    <h4>With Icons</h4>
    <BsWrapper class="d-flex flex-wrap mb-4">
      <VsButton
        class="mr-2 mb-2"
        icon="food"
        size="sm"
      >
        Nearby Places to Eat
      </VsButton>
    </BsWrapper>
    <BsWrapper class="d-flex flex-wrap mb-4">
      <VsButton
        class="mr-2 mb-2"
        icon="map"
        size="md"
      >
        Map View
      </VsButton>
    </BsWrapper>
    <BsWrapper class="d-flex flex-wrap mb-4">
      <VsButton
        class="mr-2 mb-2"
        icon="external-link"
        size="lg"
      >
        Open in a new tab
      </VsButton>
    </BsWrapper>

    <h4>Icon Only</h4>
    <BsWrapper class="d-flex flex-wrap mb-4">
      <VsButton
        class="mr-2 mb-2"
        icon="external-link"
        size="sm"
        icon-only
      />
    </BsWrapper>

    <h4>Variants</h4>
    <BsWrapper class="d-flex flex-wrap mb-4">
      <VsButton variant="primary" class="mr-2 mb-2">Primary (default)</VsButton>
      <VsButton variant="secondary" class="mr-2 mb-2">Secondary</VsButton>
      <VsButton variant="transparent" class="mr-2 mb-2">Transparent</VsButton>
      <VsButton variant="dark" class="mr-2 mb-2">Dark</VsButton>
      <VsButton variant="light" class="mr-2 mb-2">Light</VsButton>
    </BsWrapper>
    <h4>Outline Color Variants</h4>
    <BsWrapper class="d-flex flex-wrap mb-4">
      <VsButton variant="outline-primary" class="mr-2 mb-2">Primary</VsButton>
      <VsButton variant="outline-secondary" class="mr-2 mb-2">Secondary</VsButton>
      <VsButton variant="outline-transparent" class="mr-2 mb-2">Transparent</VsButton>
      <VsButton variant="outline-dark" class="mr-2 mb-2">Dark</VsButton>
      <VsButton variant="outline-light" class="mr-2 mb-2">Light</VsButton>
    </BsWrapper>
    <h4>Outline Variants with Icons</h4>
    <BsWrapper class="d-flex flex-wrap mb-4">
      <VsButton
        variant="outline-primary"
        class="mr-2 mb-2"
        icon="external-link"
      >Primary</VsButton>
      <VsButton
        variant="outline-secondary"
        class="mr-2 mb-2"
        icon="external-link"
      >Secondary</VsButton>
    </BsWrapper>
    <h4>Outline Color Variants - override transparent background</h4>
    <BsWrapper class="d-flex flex-wrap mb-4 bg-dark p-3">
      <VsButton background="white" variant="outline-primary" class="mr-2 mb-2">Primary</VsButton>
    </BsWrapper>
    <h4>Sizes</h4>
    <BsWrapper>
      <VsButton class="mr-2 mb-2" size="sm">Small</VsButton>
      <VsButton class="mr-2 mb-2" size="md">Medium</VsButton>
      <VsButton class="mr-2 mb-2" size="lg">Large</VsButton>
      <VsButton block class="mr-2 mb-2" size="md">Block</VsButton>
    </BsWrapper>

    <h4>Disabled States</h4>
    <BsWrapper class="d-flex flex-wrap mb-4">
        <VsButton disabled class="mr-2 mb-2" variant="primary" size="md">
            Disabled primary
        </VsButton>
        <VsButton disabled class="mr-2 mb-2" variant="secondary" size="md">
            Disabled secondary
        </VsButton>
    </BsWrapper>

```
</docs>
