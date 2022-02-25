<template>
    <div class="component-status">
        <ul class="status-list">
            <li>
                <VsIcon
                    name="docs/ready"
                    size="xs"
                />
                <p class="mb-0">
                    Ready
                </p>
            </li>
            <li>
                <VsIcon
                    name="docs/review"
                    size="xs"
                />
                <p class="mb-0">
                    Under review
                </p>
            </li>
            <li>
                <VsIcon
                    name="docs/deprecated"
                    size="xs"
                />
                <p class="mb-0">
                    Deprecated
                </p>
            </li>
            <li>
                <VsIcon
                    name="docs/prototype"
                    size="xs"
                />
                <p class="mb-0">
                    Prototype
                </p>
            </li>
            <li>
                <span>—</span>
                <p class="mb-0">
                    Not applicable
                </p>
            </li>
        </ul>
        <table class="rsg--table">
            <thead>
                <tr>
                    <th v-if="show === 'all'">
                        Component Name
                    </th>
                    <th v-if="show === 'elements'">
                        Element Name
                    </th>
                    <th v-if="show === 'patterns'">
                        Pattern Name
                    </th>
                    <th v-if="show === 'templates'">
                        Template Name
                    </th>
                    <th>Released in</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <tr
                    v-for="(component, index) in components"
                    :key="index"
                    class="component"
                >
                    <td v-if="component.name">
                        {{ component.name }}
                    </td>
                    <td v-else>
                        N/A
                    </td>
                    <td v-if="component.release">
                        {{ component.release }}
                    </td>
                    <td v-else>
                        N/A
                    </td>
                    <td v-if="component.status">
                        <VsIcon
                            v-if="component.status === 'docs/ready'"
                            name="docs/ready"
                            custom-colour="green"
                            size="xs"
                        />
                        <VsIcon
                            v-if="component.status === 'under-review' ||
                                component.status === 'review'"
                            name="docs/review"
                            custom-colour="orange"
                            size="xs"
                        />
                        <VsIcon
                            v-if="component.status === 'prototype'"
                            name="docs/prototype"
                            custom-colour="blue"
                            size="xs"
                        />
                        <VsIcon
                            v-if="component.status === 'deprecated'"
                            name="docs/deprecated"
                            custom-colour="red"
                            size="xs"
                        />
                    </td>
                    <td v-else>
                        —
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
// If you want to use your own tokens here, change the following line to:
// import designTokens from "@/assets/tokens/tokens.raw.json"
import orderBy from 'lodash/orderBy';
import get from 'lodash/get';
import designTokens from '../../styles/docs.tokens.json';

export default {
    name: 'Components',
    props: {
        show: {
            type: String,
            default: 'all',
            validator: (value) => value.match(/(all|patterns|templates|elements|modules)/),
        },
    },
    data() {
        return {
            components: this.orderData(this.getComponents()),
            tokens: designTokens.props,
        };
    },
    methods: {
        getComponents() {
            let contexts;

            if ((this.show === 'all') || this.show === 'modules') {
                contexts = [
                    require.context('@components/elements/', true, /\.vue$/),
                    require.context('@components/patterns/', true, /\.vue$/),
                    // require.context('@components/modules/', true, /\.vue$/),
                    require.context('@components/templates/', true, /\.vue$/),
                ];
            } else if (this.show === 'elements') {
                contexts = [require.context('@components/elements/', true, /\.vue$/)];
            } else if (this.show === 'patterns') {
                contexts = [require.context('@components/patterns/', true, /\.vue$/)];
                // } else if (this.show === 'modules') {
                //   contexts = [require.context('@components/modules/', true, /\.vue$/)]
            } else if (this.show === 'templates') {
                contexts = [require.context('@components/templates/', true, /\.vue$/)];
            }

            const components = [];
            contexts.forEach((context) => {
                context.keys().forEach((key) => components.push(context(key).default));
            });

            return components;
        },
        orderData(data) {
            return orderBy(data, 'name', 'asc');
        },
        getColour(colourName) {
            return get(this.tokens, `docs_color_${colourName}.value`, '#aaa');
        },
    },
};
</script>

<style lang='scss'>
    @import '../../styles/docs.tokens.scss';
    @import '../../styles/docs.mixins.scss';
    @import '../../styles/docs.functions.scss';

    /* STYLES
    --------------------------------------------- */

    .component-status {
        .status-list {
            margin: 0 0 $space-base;
            overflow: hidden;
            padding: 0;
            list-style: none;
            flex-direction: row;
            align-items: center;
            display: block;

            @media (min-width: $docs-breakpoint-lg) {
                display: flex;
            }

            li {
                color: $docs-color-grey-dark;
                font-size: $size-s;
                align-items: center;
                display: flex;
                width: 50%;
                float: left;
                margin: 0;

                @media (min-width: $docs-breakpoint-lg) {
                    width: auto;
                    margin: 0 $space-m 0 0;
                    float: none;
                }

                svg,
                span {
                    margin: 0 calc(#{$space-s} / 4) 0 0;
                }

                p {
                    margin: $space-xs $space-xs 0;

                    @media (min-width: $docs-breakpoint-lg) {
                        margin: 0;
                    }
                }
            }
        }
    }
</style>

<docs>
  ```jsx
  <components />
  ```
</docs>
