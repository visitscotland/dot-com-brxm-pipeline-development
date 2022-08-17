/* eslint-disable no-underscore-dangle */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/extensions */
import path from 'path';
import { fileURLToPath } from 'url';

import baseWebpackConfig from './base.webpack.conf.js';

import * as utils from './utils.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const styleRules = utils.default.styleLoaders({
    sourceMap: baseWebpackConfig.mode === 'development',
    extract: baseWebpackConfig.mode !== 'development',
    usePostCSS: true,
});

export default {
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
        rules: [
            ...styleRules,
        ],
    },
};

