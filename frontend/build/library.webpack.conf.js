const path = require('path');

const webpack = require('webpack');
const { merge } = require('webpack-merge');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const OptimizeCSSPlugin = require('optimize-css-assets-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const SafeParser = require('postcss-safe-parser');
const { WebpackManifestPlugin } = require('webpack-manifest-plugin');
const baseWebpackConfig = require('./base.webpack.conf');

const utils = require('./utils');
const generateManifest = require('./library.generate-manifest');

baseWebpackConfig.entry = require('./library.entry');

// Remove the CSS extract from the base config to prevent duplicate CSS file
baseWebpackConfig.plugins = baseWebpackConfig.plugins.filter(
    plugin => !(plugin instanceof MiniCssExtractPlugin)
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
        filename:
            baseWebpackConfig.mode === 'development'
                ? 'scripts/[name].js'
                : 'scripts/[chunkhash].js',
        publicPath: '../',
        library: '[name]',
        libraryTarget: 'umd',
    },
    optimization: {
        splitChunks: {
            chunks: 'all',
            minSize: 0,
            maxInitialRequests: Infinity,
        },
        runtimeChunk: 'single',
        concatenateModules: false,
        moduleIds: 'deterministic',
    },
    plugins: [
        // extract css into its own file
        new MiniCssExtractPlugin({
            filename:
                baseWebpackConfig.mode === 'development'
                    ? 'styles/[name].css'
                    : 'styles/[chunkhash].css',
        }),

        // Compress and dedupe extracted CSS
        new OptimizeCSSPlugin({
            cssProcessorOptions: {
                parser: SafeParser,
            },
        }),

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

module.exports = webpackConfig;
