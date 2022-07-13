const path = require('path');

const webpack = require('webpack');
const { merge } = require('webpack-merge');

const baseWebpackConfig = require('./base.webpack.conf');
const mergeIE11Fix = require('./webpack.ie11-fix');

const utils = require('./utils');

const styleRules = utils.styleLoaders({
    sourceMap: baseWebpackConfig.mode === 'development',
    extract: baseWebpackConfig.mode !== 'development',
    usePostCSS: true,
});

const config = {
    output: {
        // note that this folder is overriden by the `styleguideDir` vue-styleguidist option
        path: path.resolve(__dirname, '../dist/system'),
    },
    resolve: {
        alias: {
            'rsg-components/ReactComponent/ReactComponentRenderer':
                'rsg-components-default/ReactComponent/ReactComponentRenderer',
            'rsg-components/Playground/PlaygroundRenderer':
                'rsg-components-default/Playground/PlaygroundRenderer',
        },
    },
    module: {
        rules: [...styleRules],
    },
    plugins: [
        new webpack.ProvidePlugin({
            process: 'process/browser',
        }),
    ],
};

module.exports = merge(mergeIE11Fix(baseWebpackConfig), config);
