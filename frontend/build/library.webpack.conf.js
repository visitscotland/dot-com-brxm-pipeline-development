/* eslint-disable no-underscore-dangle */
/* eslint-disable import/extensions */
/* eslint-disable import/no-extraneous-dependencies */
import path from 'path';

import webpack from 'webpack';
import { merge } from 'webpack-merge';
import MiniCssExtractPlugin from 'mini-css-extract-plugin';
import CssMinimizerPlugin from 'css-minimizer-webpack-plugin';
import { CleanWebpackPlugin } from 'clean-webpack-plugin';
import { WebpackManifestPlugin } from 'webpack-manifest-plugin';
import { fileURLToPath } from 'url';
import baseWebpackConfig from './base.webpack.conf.js';

import components from './library.entry.js';
import utils from './utils.js';
import generateManifest from './library.generate-manifest.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

baseWebpackConfig.entry = components;

// Remove the CSS extract from the base config to prevent duplicate CSS file
baseWebpackConfig.plugins = baseWebpackConfig.plugins.filter(
    (plugin) => !(plugin instanceof MiniCssExtractPlugin),
);

const webpackConfig = merge(baseWebpackConfig, {
    module: {
        rules: utils.styleLoaders({
            sourceMap: baseWebpackConfig.mode === 'development',
            extract: true,
            usePostCSS: true,
        }),
    },
    devtool: false,
    output: {
        path: path.resolve(__dirname, '../dist', 'library'),
        filename: baseWebpackConfig.mode === 'development' ? 'scripts/[name].js' : 'scripts/[chunkhash].js',
        publicPath: '../',
        library: {
            name: '[name]',
            type: 'commonjs2',
        },
    },
    optimization: {
        splitChunks: {
            chunks: 'all',
            minSize: 0,
            maxInitialRequests: Infinity,
        },
        runtimeChunk: 'single',
        concatenateModules: false,
    },
    plugins: [
        // extract css into its own file
        new MiniCssExtractPlugin({
            filename: baseWebpackConfig.mode === 'development' ? 'styles/[name].css' : 'styles/[chunkhash].css',
        }),

        // Compress and dedupe extracted CSS
        new CssMinimizerPlugin(),

        // Keep module.id stable when vendor modules does not change
        new webpack.ids.HashedModuleIdsPlugin(),

        // Generate custom manifest.json
        new WebpackManifestPlugin({
            generate: generateManifest,
            fileName: 'manifest.json',
        }),

        new CleanWebpackPlugin(),
    ],
});

export default webpackConfig;
