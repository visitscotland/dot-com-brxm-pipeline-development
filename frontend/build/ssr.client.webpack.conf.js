/* eslint-disable no-underscore-dangle */
/* eslint-disable import/extensions */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/no-import-module-exports */
import path from 'path';
import VueSSRClientPlugin from 'vue-server-renderer/client-plugin.js';
import { fileURLToPath } from 'url';

import { merge } from 'webpack-merge';
import base from './library.webpack.conf.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const clientEntry = {
    VsApp: path.resolve(__dirname, '../ssr/src/client-entry.js'),
};

base.entry = {
    ...base.entry,
    ...clientEntry,
};

export default merge(base, {
    output: {
        path: path.resolve(__dirname, '../dist/ssr/client'),
    },
    plugins: [
        new VueSSRClientPlugin(),
    ],
});
