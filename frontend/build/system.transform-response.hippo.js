/* eslint-disable import/no-import-module-exports */
/* eslint-disable no-use-before-define */

import _ from 'lodash-es';

// Specifies the project name stub, which is needed to extract field values
// Set the VS_DS_REMOTE_CONFIG_HIPPO_PROJECT_NAME environment variable to override
let projectName = 'myhippoproject';

// The case-sensitive title of the sections field in which a document's sub-sections are defined
// Set the VS_DS_REMOTE_CONFIG_HIPPO_SECTIONS_FIELD_TITLE environment variable to override
let sectionsFieldTitle = 'Sections';

// The case-sensitive title of the content field in which the section document's textual content
// is stored
// Set the VS_DS_REMOTE_CONFIG_HIPPO_SECTIONS_CONTENT_FIELD_TITLE environment variable to override
let sectionsContentTitle = 'content';

function transformRawResponse(raw) {
    const instance = raw;

    _setup();

    return {
        title: _getFieldValue(instance, 'title'),
        sections: _extractEntrySections(instance),
    };
}

function _setup() {
    projectName = process.env.VS_DS_REMOTE_CONFIG_HIPPO_PROJECT_NAME || projectName;
    sectionsFieldTitle = (
        process.env.VS_DS_REMOTE_CONFIG_HIPPO_SECTIONS_FIELD_TITLE || sectionsFieldTitle
    );
    sectionsContentTitle = (
        process.env.VS_DS_REMOTE_CONFIG_HIPPO_SECTIONS_CONTENT_FIELD_TITLE || sectionsContentTitle
    );
}

function _getFieldValue(entry, fieldPath) {
    return _.get(entry, `items.${ projectName }:${ fieldPath}`);
}

function _extractEntrySections(entry) {
    return _.filter(_.map(_getFieldValue(entry, sectionsFieldTitle), _.partial(_parseSection)));
}

function _extractSectionContent(section) {
    const contentObject = _getFieldValue(section, sectionsContentTitle);
    const raw = _.get(contentObject, 'content');
    let cons = false;

    if (section.name === 'Grid') {
        cons = true;
    }

    return raw.replace(
        /data-hippo-link="([^"]*)"/gm,
        _.partial(_replaceHippoLink, _, _, _.get(contentObject, 'links'), cons)
    );
}

function _replaceHippoLink(match, linkId, contentLinks) {
    let srcAttribute = 'src="';

    /**
   * NOTE: the replace here rmeoves the port number from the URLs. This solution
   * needs to be replaced with something more stable via Hippo configuration.
   */
    srcAttribute += _.get(contentLinks, [linkId, 'url'], '').replace(/:[0-9]{4}\//, '/');

    srcAttribute += '"';

    return srcAttribute;
}

function _parseSection(section) {
    const fields = _.mapKeys(_.get(section, 'items'), (value, key) => _.last(_.split(key, ':')));

    if (fields[sectionsContentTitle]) {
        fields.content = _extractSectionContent(section);
    }

    if (fields[sectionsFieldTitle]) {
        fields.sections = _extractEntrySections(section);
    }

    return fields;
}

export default transformRawResponse;
