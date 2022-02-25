/* eslint-disable func-names */
/* eslint-disable import/no-extraneous-dependencies */

const os = require('os');
const path = require('path');

const { getOptions } = require('loader-utils');
const { get, map, keys } = require('lodash');

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

    return `import ${componentName} from "${relativePath}"`;
};

const insertComponentImports = (subject, componentsMap) => subject.replace(
    importsPlaceholder,
    map(componentsMap, generateComponentImportStatement).join(os.EOL)
);

const insertComponentRegistrations = (subject, componentsMap) => subject.replace(
    registrationsPlaceholder,
    keys(componentsMap).join(`,${os.EOL}`)
);

const transformSource = (subject, componentsMap) => {
    const transformedSource = insertComponentImports(subject, componentsMap);

    return insertComponentRegistrations(transformedSource, componentsMap);
};

module.exports = function(source) {
    const options = getOptions(this);
    const componentsMap = get(options, 'componentMap');

    if (componentsMap) {
        return transformSource(source, componentsMap);
    }

    return source;
};
