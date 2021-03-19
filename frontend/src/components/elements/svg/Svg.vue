<template>
    <!-- eslint-disable vue/no-v-html -->
    <svg
        v-bind="attributes"
        v-html="children"
    />
    <!-- eslint-enable vue/no-v-html -->
</template>

<script>
import {
    first,
    split,
    map,
    partial,
    fromPairs,
    replace,
    mapValues,
    extend,
    has,
    ary,
    join,
    toPairs,
    nth,
} from 'lodash';
import svgContext from '@/utils/svg-context';

/**
 * SVGs are used to display vector images
 *
 * The display name being set to just "SVG" throws a console error
 *
 * @displayName SVG Wrapper
 */
export default {
    name: 'VsSvg',
    release: '0.1.1',
    status: 'prototype',
    props: {
        /**
         * The path of the svg to display, relative to /src/assets
         */
        path: {
            type: String,
            required: true,
        },
        /**
     * The fill color of the SVG.
     */
        fill: {
            type: String,
            default: '',
        },
        /**
     * The height attributeof the svg
     */
        height: {
            type: [Number, String],
            default: '',
        },
        /**
     * The width attributeof the svg
     */
        width: {
            type: [Number, String],
            default: '',
        },
    },
    computed: {
        svg() {
            return svgContext(`./${this.path}.svg`);
        },
        nativeAttrs() {
            const tag = first(this.svg.match(/<svg[^>]+.*?>/));

            const attributes = tag.match(/(\S+)=["']?((?:.(?!["']?\s+(?:\S+)=|[>"']))+.)["']?/g);
            const attributesMap = fromPairs(map(attributes, partial(split, partial.placeholder, '=', 2)));

            return mapValues(attributesMap, partial(replace, partial.placeholder, /"/g, ''));
        },
        attributes() {
            const extraAttributes = {
            };
            let styleMap = {
            };

            if (this.fill) {
                if (has(this.nativeAttrs, 'style')) {
                    styleMap = this.nativeStyleAttrMap;

                    styleMap.fill = this.fill;
                } else {
                    styleMap = {
                        fill: this.fill,
                    };
                }

                extraAttributes.style = join(
                    map(toPairs(styleMap), partial(join, partial.placeholder, ':')),
                    ';',
                );
            }

            if (this.height) {
                extraAttributes.height = this.height;
            }

            if (this.width) {
                extraAttributes.width = this.width;
            }

            return extend({
            }, this.nativeAttrs, extraAttributes);
        },
        nativeStyleAttrMap() {
            const styleArray = map(
                split(this.nativeAttrs.style, ';'),
                ary(partial(split, partial.placeholder, ':'), 1),
            );

            return fromPairs(styleArray);
        },
        children() {
            return nth(this.svg.match(/(<svg[^>]+.*?>)([\s\S]*)(<\/svg>)/), 2);
        },
    },
};
</script>

<style lang="scss"></style>

<docs>
  ```jsx
  <div>
    <pre>&lt;vs-svg path="svg/visitscotland" /&gt; </pre>
    <VsSvg path="visitscotland" />

    <br /><hr /><br />

    <pre>height="50"</pre>
    <VsSvg path="visitscotland" height="50" />

    <br /><hr /><br />

    <pre>width="110"</pre>
    <VsSvg path="visitscotland" width="110" />

    <br /><hr /><br />

    <pre>fill="red"</pre>
    <VsSvg path="visitscotland" fill="red" />

  </div>
  ```
</docs>
