<template>
    <div class="component-status">
        <ul class="status-list">
            <li>
                <VsIcon
                    name="docs/ready"
                    customColour="green"
                    size="xs"
                />
                <p class="mb-0">
                    Ready
                </p>
            </li>
            <li>
                <VsIcon
                    name="docs/review"
                    customColour="orange"
                    size="xs"
                />
                <p class="mb-0">
                    Under review
                </p>
            </li>
            <li>
                <VsIcon
                    name="docs/deprecated"
                    customColour="red"
                    size="xs"
                />
                <p class="mb-0">
                    Deprecated
                </p>
            </li>
            <li>
                <VsIcon
                    name="docs/prototype"
                    customColour="blue"
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
        <table>
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
                        <code class="name">{{ component.name }}</code>
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
                            customColour="green"
                            size="xs"
                        />
                        <VsIcon
                            v-if="component.status === 'under-review' ||
                                component.status === 'review'"
                            name="docs/review"
                            customColour="orange"
                            size="xs"
                        />
                        <VsIcon
                            v-if="component.status === 'prototype'"
                            name="docs/prototype"
                            customColour="blue"
                            size="xs"
                        />
                        <VsIcon
                            v-if="component.status === 'deprecated'"
                            name="docs/deprecated"
                            customColour="red"
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
import orderBy from "lodash/orderBy"
import get from "lodash/get"
import designTokens from "../../styles/docs.tokens.json"

export default {
    name: "Components",
    props: {
        show: {
            type: String,
            default: "all",
            validator: (value) => value.match(/(all|patterns|templates|elements|modules)/),
        },
    },
    data() {
        return {
            components: this.orderData(this.getComponents()),
            tokens: designTokens.props,
        }
    },
    methods: {
        getComponents() {
            let contexts

            if ((this.show === "all") || this.show === "modules") {
                contexts = [
                    require.context("@components/elements/", true, /\.vue$/),
                    require.context("@components/patterns/", true, /\.vue$/),
                    // require.context("@components/modules/", true, /\.vue$/),
                    require.context("@components/templates/", true, /\.vue$/),
                ]
            } else if (this.show === "elements") {
                contexts = [require.context("@components/elements/", true, /\.vue$/)]
            } else if (this.show === "patterns") {
                contexts = [require.context("@components/patterns/", true, /\.vue$/)]
                // } else if (this.show === "modules") {
                //   contexts = [require.context("@components/modules/", true, /\.vue$/)]
            } else if (this.show === "templates") {
                contexts = [require.context("@components/templates/", true, /\.vue$/)]
            }

            const components = []
            contexts.forEach((context) => {
                context.keys().forEach((key) => components.push(context(key).default))
            })

            return components
        },
        orderData(data) {
            return orderBy(data, "name", "asc")
        },
        getColour(colourName) {
            return get(this.tokens, `docs_color_${colourName}.value`, "#aaa")
        },
    },
}
</script>

<style lang="scss" scoped>
@import "../../styles/docs.tokens.scss";
@import "../../styles/docs.mixins.scss";
@import "../../styles/docs.functions.scss";

/* STYLES
--------------------------------------------- */

.component-status {
  @include reset;
  font-family: $font-heading;
  font-weight: $weight-normal;
  line-height: $line-height-xs;
  color: $docs-color-rich-black;
  margin-bottom: $space-s;
  font-style: normal;
  @media (max-width: 1000px) {
    overflow-x: auto;
  }
  table {
    border-collapse: collapse;
    border-spacing: 0;
    width: 100%;
  }
  thead th {
    padding: $space-s;
    background: $docs-color-cloud;
    font-size: $size-s;
    font-weight: $weight-bold;
    color: $docs-color-oxford-blue;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: $weight-semi-bold;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: left;
    // Chrome has a bug related to thead, this only works on th:
    position: -webkit-sticky;
    position: sticky;
    top: -1px;
    &:first-child {
      border-top-left-radius: $radius-default;
      border-bottom-left-radius: $radius-default;
    }
    &:last-child {
      border-top-right-radius: $radius-default;
      border-bottom-right-radius: $radius-default;
    }
  }
  tr {
    border-bottom: 1px solid #dfe3e6;
    &:last-child {
      border: 0;
    }
  }
  td {
    font-size: $size-s;
    padding: $space-s;
    &:first-child {
      font-weight: $weight-bold;
      white-space: nowrap;
    }
  }
  .status-list {
    margin: 0 0 $space-m;
    overflow: hidden;
    padding: 0;
    list-style: none;
    flex-direction: row;
    align-items: center;
    display: flex;
    @media (max-width: 1000px) {
      display: block;
    }
    li {
      margin: 0 $space-m 0 0;
      color: shade($docs-color-silver, 20%);
      font-size: $size-s;
      align-items: center;
      display: flex;
      @media (max-width: 1000px) {
        width: 50%;
        float: left;
        margin: 0;
      }
      svg,
      span {
        margin: 0 calc(#{$space-s} / 2) 0 0;
      }
      p {
        margin-bottom: 0;
        @media (max-width: 1000px) {
          margin: $space-xs;
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
