/* eslint-disable import/no-import-module-exports */
/* eslint-disable func-names */
/* eslint-disable import/no-extraneous-dependencies */

import os from 'os';
import path from 'path';
import { fileURLToPath } from 'url';

import { getOptions } from 'loader-utils';
import { get, map, keys } from 'lodash-es';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const importsPlaceholder = '/** PLACEHOLDER: COMPONENT IMPORTS */';
const registrationsPlaceholder = '/** PLACEHOLDER: COMPONENT REGISTRATION */';
const ssrAppPath = './ssr/src/';

/**
 * This webpack loader, when given a componentMap option, inserts imports
 * and component registrations into the source file at the relevent
 * placeholder text.
 *
 * The componentMap option should be a map with component names as keys and
 * values of the component path relative to the main parent (frontend) folder.
 */

// TODO: add schema for options shape validation

const generateComponentImportStatement = (modulePath, componentName) => {
    const relativePath = path.posix.relative(ssrAppPath, modulePath);

    return `const ${componentName} = () => await import("${relativePath}")`;
};

const insertComponentImports = (subject, componentsMap) => subject.replace(
    importsPlaceholder,
  map(componentsMap, generateComponentImportStatement).join(os.EOL),
);

const insertComponentRegistrations = (subject, componentsMap) => subject.replace(
    registrationsPlaceholder,
  keys(componentsMap).join(`,${os.EOL}`),
);

const transformSource = (subject, componentsMap) => {
    const transformedSource = insertComponentImports(subject, componentsMap);

    console.log(transformSource);

    return insertComponentRegistrations(transformedSource, componentsMap);
};

export default function(source) {
    const options = getOptions(this);
    const componentsMap = get(options, 'componentMap');

    if (componentsMap) {
        return transformSource(source, componentsMap);
    }

    return source;
}
