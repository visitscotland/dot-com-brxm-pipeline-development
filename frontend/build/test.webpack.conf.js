/* eslint-disable import/extensions */
/* eslint-disable import/no-extraneous-dependencies */
// This is the webpack config used for unit tests.

import merge from 'webpack-merge';
import utils from './utils.js';
import baseWebpackConfig from './base.webpack.conf.js';

const webpackConfig = merge(baseWebpackConfig, {
    // use inline sourcemap for karma-sourcemap-loader
    module: {
        rules: utils.styleLoaders(),
    },
    devtool: '#inline-source-map',
    resolveLoader: {
        alias: {
            // necessary to to make lang="scss" work in test when using vue-loader's ?inject option
            // see discussion at https://github.com/vuejs/vue-loader/issues/724
            'scss-loader': 'sass-loader',
        },
    },
});

// no need for app entry during tests
delete webpackConfig.entry;

export default webpackConfig;
