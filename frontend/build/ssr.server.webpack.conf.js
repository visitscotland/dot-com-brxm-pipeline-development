const path = require('path');

const { get } = require('lodash');
const nodeExternals = require('webpack-node-externals');
const VueSSRServerPlugin = require('vue-server-renderer/server-plugin');
const merge = require('webpack-merge');

const base = require('./library.webpack.conf');

const entry = {
    'main-server': path.resolve(__dirname, '../ssr/src/server-entry.js'),
};

const sourceImports = require('./ssr.generate-component-library-map')(base.entry);

base.entry = {
    ...entry,
    ...base.entry,
};

delete base.optimization;

module.exports = merge(base, {
    target: 'node',
    output: {
        path: path.resolve(__dirname, '../dist/ssr/server'),
        libraryTarget: 'commonjs2',
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
                        loader: path.resolve(__dirname, './ssr.dynamic-component-loader'),
                        options: {
                            componentMap: get(sourceImports, 'components'),
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
