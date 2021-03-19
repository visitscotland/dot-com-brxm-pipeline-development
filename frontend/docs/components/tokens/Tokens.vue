<template>
    <div>
        <div
            v-for="(thisType, index1) in sortedTypes"
            :key="index1"
            class="token-type"
        >
            <table>
                <thead>
                    <tr>
                        <th colspan="3">
                            <!-- eslint-disable vue/component-name-in-template-casing -->
                            <component
                                v-if="sortedTypes.length > 1"
                                :is="typeHeadingElement"
                            >
                                {{ thisType === "..." ? "No type" : thisType }}
                            </component>
                            <!-- eslint-enable vue/component-name-in-template-casing -->
                        </th>
                    </tr>
                    <tr>
                        <th>Token Name</th>
                        <th>Value</th>
                        <th>Category</th>
                    </tr>
                </thead>
                <tbody>
                    <tr
                        v-once
                        v-for="(token, index2) in filterBy('type', thisType)"
                        :key="index2"
                        class="token"
                    >
                        <td v-if="token.name">
                            <code class="name">${{ token.name.replace(/_/g, "-") }}</code>
                        </td>
                        <td v-else>
                            N/A
                        </td>
                        <td v-if="token.value">
                            <div
                                v-if="token.type === 'color'"
                                class="example color"
                                :style="{ backgroundColor: token.value }"
                            />
                            <div
                                v-if="token.category === 'border-radius'"
                                class="example border-radius"
                                :style="{ borderRadius: token.value }"
                            />
                            <div
                                v-if="token.category === 'box-shadow'"
                                class="example box-shadow"
                                :style="{ boxShadow: token.value }"
                            />
                            <code
                                v-if="token.type === 'color'"
                                class="type"
                            >{{ token.originalValue }}</code>
                            <code
                                v-else
                                class="type"
                            >{{ token.value }}</code>
                        </td>
                        <td v-else>
                            N/A
                        </td>
                        <td v-if="token.category">
                            {{ token.category }}
                        </td>
                        <td v-else>
                            N/A
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import {
    orderBy,
    filter,
    map,
    uniq,
    castArray,
} from "lodash"
import designTokens from "@/assets/tokens/tokens.raw.json"

/**
 * A list of available tokens in Vue Design System. Use these tokens in place
 * of hard-coded values in order to maintain a scalable and consistent system.
 * To edit these tokens and add more, see
 * [/src/tokens/](https://github.com/viljamis/vue-design-system/blob/master/src/tokens).
 */
export default {
    name: "Tokens",
    props: {
        types: {
            type: [String, Array],
            default: null,
        },
        typeHeadingElement: {
            type: String,
            default: "h2",
            validator: (value) => value.match(/(h1|h2|h3|h4|h5|h6)/),
        },
    },
    data() {
        return {
            tokens: this.orderData(designTokens.props),
        }
    },
    computed: {
        sortedTypes() {
            if (this.types) {
                return castArray(this.types)
            }

            return orderBy(uniq(map(this.tokens, "type")))
        },
    },
    methods: {
        orderData(data) {
            const byName = orderBy(data, "name", "asc")
            const byCategoryAndName = orderBy(byName, "category")
            return byCategoryAndName
        },
        filterBy(filterOn, term) {
            return filter(this.tokens, [filterOn, term])
        },
    },
}
</script>

<style lang="scss" scoped>
@import "../../styles/docs.tokens.scss";
@import "../../styles/docs.mixins.scss";

/* STYLES
--------------------------------------------- */

.token-type {
  @include reset;
  margin-top: $space-l;
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
    margin-bottom: 2em;
    &:last-of-type {
      margin-bottom: 0;
    }
  }
  thead {
    tr {
      th {
        padding: $space-s $space-l $space-s $space-s;
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
        * {
          margin-bottom: 1.5em;
        }
      }
    }
    tr:first-of-type {
      th {
        padding: 0;
      }
      th,
      &:hover {
        background: initial;
      }
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
    padding: $space-s $space-l $space-s $space-s;
    &:first-child {
      font-weight: $weight-bold;
      white-space: nowrap;
    }
  }
  .type {
    line-height: $line-height-s;
    max-width: calc(100% - #{$space-m});
    float: left;
  }
  .example {
    @include inline-space($space-xs);
    border-radius: $radius-default;
    background: $docs-color-white;
    box-shadow: $shadow-s-inset, $shadow-s-inset, $shadow-s-inset;
    margin-top: $space-xx-small;
    width: $space-s;
    height: $space-s;
    float: left;
  }
}
</style>

<docs>
  ```jsx
  <tokens/>

  <tokens type="color" />
  ```
</docs>
