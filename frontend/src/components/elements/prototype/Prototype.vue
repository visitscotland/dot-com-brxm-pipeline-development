<template>
    <div class="vs-prototype p-0 m-0">
        <slot />
    </div>
</template>

<script>
/**
 * Prototypes are wrappers used to visually communicate parts
 * of the product or actions that are still unavailable.
 *
 * This is a development only tool to help visualise
 * components/functionalities to be finished.
 *
 * @displayName Prototype
 */

export default {
    name: 'VsPrototype',
    status: 'prototype',
    release: '0.0.1',
};
</script>

<style lang="scss">
.vs-prototype {
    opacity: $opacity-50;

    & > * {
        color: darken(grey, 70%);
        border-color: darken(grey, 70%);
        background: lighten(grey, 30%);
    }

    :hover,
    &:hover {
        cursor: not-allowed;
    }
}
</style>

<docs>
```jsx
    <h4>Originals</h4>
    <BsWrapper class="d-flex mb-4">
        <VsButton class="mr-2 mb-2">Button</VsButton>
    </BsWrapper>

    <BsWrapper class="d-flex mb-4">
        <VsIcon name="search" />
    </BsWrapper>

    <h4 class="mt-8">Prototypes</h4>
    <BsWrapper class="d-flex mb-4">
        <VsPrototype>
            <VsButton class="mr-2 mb-2">Button</VsButton>
        </VsPrototype>
    </BsWrapper>

    <BsWrapper class="d-flex mb-4">
        <VsPrototype>
            <VsIcon name="search" />
        </VsPrototype>
    </BsWrapper>
```
</docs>
