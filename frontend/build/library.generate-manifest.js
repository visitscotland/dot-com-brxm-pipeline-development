import {
    extend,
    reduce,
    invoke,
    mapKeys,
    mapValues,
    filter,
    partial,
    ary,
    endsWith,
    remove,
    isEmpty,
} from 'lodash-es';

function extractEntriesFromOutputFile(outputFile) {
    const entries = Array.from(invoke(outputFile, 'chunk._groups.values'));

    return entries.length ? mapKeys(entries, 'options.name') : {
    };
}

function extractAllEntriesFromFileDescriptor(FileDescriptor) {
    return reduce(
        filter(FileDescriptor, 'isInitial'),
        (acc, val) => extend({
        }, acc, extractEntriesFromOutputFile(val)),
        {
    },
    );
}

function mapComponentEntryFiles(component) {
    const files = new Set(component.chunks[0].files); //lodash uniq filemap functions don't work with webpack 5 chunk sets
    const scripts = remove(files, ary(partial(endsWith, partial.placeholder, '.js'), 1));
    const styles = remove(files, ary(partial(endsWith, partial.placeholder, '.css'), 1));
    const headingFonts = [
        'eveleth-clean-regular.woff',
        'eveleth-clean-regular.woff2',
        'eveleth-clean-thin.woff',
        'eveleth-clean-thin.woff2',
    ];
    const bodyFonts = [
        'source-sans-pro-bold.woff',
        'source-sans-pro-bold.woff2',
        'source-sans-pro-light.woff',
        'source-sans-pro-light.woff2',
        'source-sans-pro-regular.woff',
        'source-sans-pro-regular.woff2',
        'source-sans-pro-semibold.woff',
        'source-sans-pro-semibold.woff2',
    ];

    const assetMap = {
    };

    assetMap.headingFonts = headingFonts;
    assetMap.bodyFonts = bodyFonts;

    if (!isEmpty(scripts)) {
        assetMap.scripts = scripts;
    }

    if (!isEmpty(styles)) {
        assetMap.styles = styles;
    }

    if (!isEmpty(files)) {
        assetMap.other = files;
    }

    return assetMap;
}

/**
 * Generates manifest file for system component build
 */
export default (FileDescriptor) => {
    const componentEntries = extractAllEntriesFromFileDescriptor(FileDescriptor);
    return mapValues(componentEntries, mapComponentEntryFiles);
};
