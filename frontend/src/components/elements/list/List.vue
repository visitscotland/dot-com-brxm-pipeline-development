<template>
    <Component
        :is="ordered ? 'ol' : 'ul'"
        class="vs-list"
        :class="{
            'vs-list--unstyled': unstyled,
            'vs-list--inline': inline,
            'vs-list--ordered': ordered,
        }"
        v-bind="$attrs"
    >
        <slot />
    </Component>
</template>

<script>
/**
 * The List component can be used for unordered, ordered, unstyled, inline or nested lists.
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
    margin: 0 0 $spacer-5 $spacer-6;
    padding: 0;
    list-style: none;

    li{
        margin-bottom: $spacer-1;

        &::before{
            content: "• ";
            color: $color-pink;
            padding-right: 0.7rem;
            margin-left: -$spacer-6;
            font-size: $h2-font-size;
            line-height: $line-height-xs;
        }

        ul{
            list-style: none;
            margin: $spacer-4 0 $spacer-5 $spacer-6;

            li{
                &::before{
                    content: "– ";
                    padding-right: 0.6rem;
                    font-size: $h3-font-size;
                }
            }
        }
    }

    &.vs-list--inline {
        display: flex;
    }

    &.vs-list--ordered {
        margin: 0 0 $spacer-5 $spacer-6;
        list-style: none;
        counter-reset: list-counter;

        li{
            margin-bottom: 0;

            &::before{
                color: $color-pink;
                padding-right: 0.5rem;
                margin-left: -$spacer-6;
                font-size: inherit;
                line-height: $line-height-xs;
                counter-increment: list-counter;
                content: counter(list-counter) ". ";
            }

            ol{
                list-style: none;
                counter-reset: list-counter;
                margin: $spacer-4 0 $spacer-5 $spacer-6;

                li{
                    margin-bottom: 0;

                    &::before{
                        content: counter(list-counter, lower-alpha)  ". ";
                    }
                }
            }
        }
    }

    &.vs-list--unstyled,
    &.vs-list--inline {
        margin: 0;

        li{
            margin-bottom: 0;

            &::before{
                display: none;
            }
        }
    }
}
</style>

<docs>
```jsx
    <h3>Unordered List</h3>
    <VsList>
        <li>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
            <ul>
                <li>Lorem ipsum dolor sit amet, consectetur adipiscing elit. </li>
                <li>Maecenas nec massa placerat, bibendum ex sit amet, blandit dolor.</li>
                <li>Curabitur tempus quam et dolor venenatis blandit. Nulla et erat orci.
                Suspendisse consequat ipsum et ex molestie viverra.
                In faucibus eget nisi eu lobortis.</li>
                <li>Integer efficitur lacus id mi scelerisque, porta feugiat leo egestas. </li>
            </ul>
        </li>
        <li>Curabitur tempus quam et dolor venenatis blandit. Nulla et erat orci.
        Suspendisse consequat ipsum et ex molestie viverra. In faucibus eget nisi eu lobortis.</li>
        <li>Integer efficitur lacus id mi scelerisque, porta feugiat leo egestas. </li>
    </VsList>

    <h3 class="mt-8">Ordered List</h3>
    <VsList ordered>
        <li>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
            <ol>
                <li>Lorem ipsum dolor sit amet, consectetur adipiscing elit. </li>
                <li>Maecenas nec massa placerat, bibendum ex sit amet, blandit dolor.</li>
                <li>Curabitur tempus quam et dolor venenatis blandit. Nulla et erat orci.
                Suspendisse consequat ipsum et ex molestie viverra.
                In faucibus eget nisi eu lobortis.</li>
                <li>Integer efficitur lacus id mi scelerisque, porta feugiat leo egestas. </li>
            </ol>
        </li>
        <li>Curabitur tempus quam et dolor venenatis blandit. Nulla et erat orci.
        Suspendisse consequat ipsum et ex molestie viverra. In faucibus eget nisi eu lobortis.</li>
        <li>Integer efficitur lacus id mi scelerisque, porta feugiat leo egestas. </li>
    </VsList>

    <h3 class="mt-8">Unstyled List</h3>
    <VsList unstyled>
        <li>Car</li>
        <li>Ferry</li>
        <li>Bike</li>
        <li>Plane</li>
    </VsList>

    <h3 class="mt-8">Inline Unstyled List</h3>
    <VsList unstyled inline>
        <li class="mr-7">Car</li>
        <li class="mr-7">Ferry</li>
        <li class="mr-7">Bike</li>
        <li class="mr-7">Plane</li>
    </VsList>
```
</docs>
