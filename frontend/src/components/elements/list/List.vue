<template>
    <Component
        :is="ordered ? 'ol' : 'ul'"
        class="vs-list"
        :class="{
            'vs-list--unstyled': unstyled,
            'vs-list--inline': inline,
        }"
        v-bind="$attrs"
    >
        <slot />
    </Component>
</template>

<script>
/**
 * Generic List Component that can be used for any kind of list needed given the right properties.
 *
 * @displayName List
 */
export default {
    name: 'VsList',
    status: 'prototype',
    release: '0.0.1',
    props: {
        /**
         * Option to choose the kind of list needed (ordered | unordered)
         */
        ordered: {
            type: Boolean,
            default: false,
        },
        /**
         * Option to add/remove bullet points
         */
        unstyled: {
            type: Boolean,
            default: false,
        },
        /**
         * Option to set the list as inline or normal
         */
        inline: {
            type: Boolean,
            default: false,
        },
    },
};
</script>

<style lang="scss">
.vs-list {
    &.vs-list--unstyled {
        padding: 0;
        margin: 0;
        list-style: none;
    }

    &.vs-list--inline {
        display: flex;
    }
}
</style>

<docs>
```jsx
    <h3>Default</h3>
    <VsList>
        <li class="mr-7">Test</li>
        <li class="mr-7">Test</li>
        <li class="mr-7">Test</li>
    </VsList>

    <h3 class="mt-8">Inline</h3>
    <VsList inline>
        <li class="mr-7">Test</li>
        <li class="mr-7">Test</li>
        <li class="mr-7">Test</li>
    </VsList>

    <h3 class="mt-8">Inline Unstyled</h3>
    <VsList unstyled inline>
        <li class="mr-7">Test</li>
        <li class="mr-7">Test</li>
        <li class="mr-7">Test</li>
    </VsList>

    <h3 class="mt-8">Unstyled</h3>
    <VsList unstyled>
        <li class="mr-7">Test</li>
        <li class="mr-7">Test</li>
        <li class="mr-7">Test</li>
    </VsList>

    <h3 class="mt-8">Ordered List</h3>
    <VsList ordered>
        <li>Test</li>
    </VsList>
```
</docs>
