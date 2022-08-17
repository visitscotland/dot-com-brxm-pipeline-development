/* eslint-disable no-underscore-dangle */
/* eslint-disable import/first */
/* eslint-disable import/extensions */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/no-import-module-exports */
/* eslint-disable func-names */
/* eslint-disable no-param-reassign */
/* eslint-disable global-require */
/* eslint-disable no-shadow */
/* eslint-disable func-names */
/* eslint-disable no-restricted-syntax */
/* eslint-disable guard-for-in */

import path from 'path';
import MiniCssExtractPlugin from 'mini-css-extract-plugin';
import { cloneDeep, find } from 'lodash-es';
import sass from 'sass';
import { fileURLToPath } from 'url';
import buildMode from './base.build-mode.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const cssLoaders = function (options) {
    options = options || {
    };

    const cssLoader = {
        loader: 'css-loader',
        options: {
            sourceMap: options.sourceMap,
        },
    };

    const cssLoaderForModulesOptions = {
        modules: true,
        localIdentName: buildMode === 'development'
            ? '[path][name]__[local]' : '[hash:base64]',
    };

    const postcssLoader = {
        loader: 'postcss-loader',
        options: {
            sourceMap: options.sourceMap,
        },
    };

    const baseSassLoaderOptions = {
        sassOptions: {
            style: 'compressed',
        },
        implementation: 'sass',
        sourceMap: options.sourceMap,
    };

    const sassResourcesLoader = {
        loader: 'sass-resources-loader',
        options: {
            resources: [
                path.resolve(__dirname, '../src/assets/tokens/tokens.scss'),
                path.resolve(__dirname, '../src/assets/tokens/tokens.map.scss'),
                path.resolve(__dirname, '../src/styles/resources.scss'),
            ],
        },
    };

    function generateLoadersForCssModules(loaders) {
        const cssModulesLoaders = cloneDeep(loaders);

        const cssLoader = find(cssModulesLoaders, {
            loader: 'css-loader',
        });

        cssLoader.options = {
            ...cssLoader.options,
            ...cssLoaderForModulesOptions,
        };

        return cssModulesLoaders;
    }

    // generate loader string to be used with extract text plugin
    function generateLoaders(sassLoader, extraSassLoaderOptions) {
        const loaders = [];

        // Extract CSS when that option is specified
        // (which is the case during production build)
        if (options.extract) {
            loaders.push(MiniCssExtractPlugin.loader);
        } else {
            loaders.push('vue-style-loader');
        }

        loaders.push(cssLoader);

        if (options.usePostCSS) {
            loaders.push(postcssLoader);
        }

        if (sassLoader) {
            loaders.push({
                loader: 'sass-loader',
                options: {
                    ...baseSassLoaderOptions,
                    ...extraSassLoaderOptions || {
                    },
                },
            });

            loaders.push(sassResourcesLoader);
        }

        // For CSS Modules we need to return an array with 2 options -
        // one for CSS modules style blocks and the other for all
        // other style blocks
        return [
            {
                resourceQuery: /module/,
                use: generateLoadersForCssModules(loaders),
            },
            {
                use: loaders,
            },
        ];
    }

    // https://vue-loader.vuejs.org/guide/extract-css.html
    return {
        css: generateLoaders(),
        postcss: generateLoaders(),
        sass: generateLoaders(true, {
            indentedSyntax: true,
        }),
        scss: generateLoaders('sass'),
    };
};

// Generate loaders for standalone style files
const styleLoaders = function (options) {
    const output = [];
    const loaders = cssLoaders(options);

    for (const extension in loaders) {
        const loader = loaders[extension];
        output.push({
            test: new RegExp(`\\.${extension}$`),
            // use: loader,
            oneOf: loader,
        });
    }

    return output;
};

const packageName = 'vs-dotcom-frontend';

export default {
    packageName,
  styleLoaders,
  cssLoaders,
};
