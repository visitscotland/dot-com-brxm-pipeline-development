/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/no-import-module-exports */
/* eslint-disable import/extensions */
import path from 'path';
import { fileURLToPath } from 'url';

import { get } from 'lodash-es';
import nodeExternals from 'webpack-node-externals';
import VueSSRServerPlugin from 'vue-server-renderer/server-plugin.js';
import { merge } from 'webpack-merge';

import base from './library.webpack.conf.js';

import * as sourceImports from './ssr.generate-component-library-map.js';

const sourceImportsMap = sourceImports.default(base.entry);

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const entry = {
    'main-server': path.resolve(__dirname, '../ssr/src/server-entry.js'),
};

console.log('sourceImportsMap', sourceImportsMap);


base.entry = {
    ...entry,
    ...base.entry,
};

delete base.optimization;

const ssrServerWebpackConfig = merge(base, {
    target: 'node',
    output: {
        path: path.resolve(__dirname, '../dist/ssr/server'),
        library: {
            type: 'commonjs2',
        }
    },
    // https://webpack.js.org/configuration/externals/#function
    // https://github.com/liady/webpack-node-externals
    // Externalize app dependencies. This makes the server build much faster
    // and generates a smaller bundle file.
    externals: nodeExternals({
        // do not externalize dependencies that need to be processed by webpack.
        // you can add more file types here e.g. raw *.vue files
        // you should also whitelist deps that modifies `global` (e.g. polyfills)
        // whitelist: /[\.css]$/
    }),
    module: {
        rules: [
            {
                test: /server-entry\.js$/,
                use: [
                    {
                        loader: path.resolve('build/ssr.dynamic-component-loader.cjs'),
                        options: {
                            componentMap: get(sourceImportsMap, 'components'),
                        },
                    },
                ],
            },
        ],
    },

    // This is the plugin that turns the entire output of the server build
    // into a single JSON file. The default file name is`vue-ssr-server-bundle.json`
  plugins: [
      new VueSSRServerPlugin(),
  ],
});

export default ssrServerWebpackConfig;
