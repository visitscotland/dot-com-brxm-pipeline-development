/* eslint-disable import/no-import-module-exports */
/* eslint-disable import/no-extraneous-dependencies */
/**
 * This file outputs an object for consumption by webpack as its entry config.
 * The object lists components to build and their file paths.
 */

import glob from 'glob';
import path from 'path';

import { camelCase, upperFirst } from 'lodash-es';

const componentPaths = [
    './src/components/elements/**/*.vue',
    './src/components/patterns/**/*.vue',
    './src/components/modules/**/*.vue',
    './src/components/templates/**/*.vue',
    './src/components/examples/**/*.vue',
];

const storePattern = './src/components/**/*.store.js';

const components = {
    VsApp: './src/main.js',
};

const itemKey = (basename, store) => `Vs${store ? 'Store' : ''}${upperFirst(camelCase(basename))}`;

componentPaths
    .reduce((accumulator, pattern) => accumulator.concat(glob.sync(pattern)), [])
    .forEach((componentPath) => {
        components[itemKey(path.basename(componentPath, '.vue'))] = componentPath;
    });

glob.sync(storePattern).forEach((storePath) => {
    components[itemKey(path.basename(storePath, '.js'), true)] = storePath;
});

export default components;
