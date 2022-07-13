const path = require('path');
const { merge } = require('webpack-merge');
const VueSSRClientPlugin = require('vue-server-renderer/client-plugin');

const base = require('./library.webpack.conf');

const clientEntry = {
    VsApp: path.resolve(__dirname, '../ssr/src/client-entry.js'),
};

base.entry = {
    ...base.entry,
    ...clientEntry,
};

module.exports = merge(base, {
    output: {
        path: path.resolve(__dirname, '../dist/ssr/client'),
    },
    plugins: [new VueSSRClientPlugin()],
});
