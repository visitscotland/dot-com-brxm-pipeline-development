<template>
    <div class="spacing">
        <div
            v-for="(prop, index) in spacingTokens"
            :key="index"
        >
            <div class="spacing-label">${{ prop.name.replace(/_/g, "-") }} <span>({{ prop.value }}) ({{ prop.pixelHeight }}px*)</span></div>
            <div
                class="space"
                :style="{height: prop.calcHeight }">
            </div>
        </div>
        <p>*Pixel values calculated at 1rem = 16px</p>
    </div>
</template>

<script>
import { orderBy, filter } from "lodash"
import designTokens from "@/assets/tokens/tokens.raw.json"

/**
 * A framework for creating a predictable and harmonious spacing system. These
 * tokens are used for padding, margins, and position together with
 * [spacing utilities](https://github.com/viljamis/vue-design-system/wiki/spacing).
 * To edit spacing, see
 * [/src/tokens/spacing.yml](https://github.com/viljamis/vue-design-system/blob/master/src/tokens/spacing.yml).
 */
export default {
    name: "Spacing",
    data() {
        return {
            tokens: designTokens.props,
        }
    },
    computed: {
        spacingTokens() {
            let filteredTokens = filter(this.tokens, ["category", "space"])

            filteredTokens.forEach(element => {
                try {
                    element.arrayIndex = parseInt(element.name.split("_")[1])
                    element.calcHeight = "calc(" + element.value + ")"
                    element.pixelHeight = this.calculatePixelHeight(element.value)
                } catch (error) {
                    // Spacer element named incorrectly
                    element.calcHeight = "-"
                    element.pixelHeight = "-"

                }
            })

            return this.orderData(filteredTokens)
        },
    },
    methods: {
        orderData(data) {
            const order = orderBy(data, "arrayIndex", "asc")
            return order
        },
        calculatePixelHeight(remInput) {
            let input = remInput.replace(/rem/g, " * 16")
            return eval(input)
        }
    },
}
</script>

<style lang="scss" scoped>
@import "../../styles/docs.tokens.scss";
@import "../../styles/docs.mixins.scss";

/* STYLES
--------------------------------------------- */

.spacing {
    margin-top: $space-l;
    overflow: hidden;
    max-width: 1176px;
    width: 100%;
}
.spacing-label {
    span {
        margin-left: .5rem;
        font-size: .8rem;
    }
}
.space {
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    border-radius: $radius-default;
    box-shadow: $shadow-s-inset;
    margin-bottom: $space-xs;
    font-size: $size-s;
    font-family: $font-text;
    color: $docs-color-rich-black;
    background: tint(#c4cdd5, 85%);
    text-align: center;
    position: relative;
    float: left;
    width: 100%;
}
</style>

<docs>
  ```jsx
  <spacing/>
  ```
</docs>
