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

  setup();

    return {
    title: getFieldValue(instance, 'title'),
    sections: extractEntrySections(instance),
    };
}

function setup() {
    projectName = process.env.VS_DS_REMOTE_CONFIG_HIPPO_PROJECT_NAME || projectName;
    sectionsFieldTitle = (
        process.env.VS_DS_REMOTE_CONFIG_HIPPO_SECTIONS_FIELD_TITLE || sectionsFieldTitle
    );
    sectionsContentTitle = (
        process.env.VS_DS_REMOTE_CONFIG_HIPPO_SECTIONS_CONTENT_FIELD_TITLE || sectionsContentTitle
    );
}

function getFieldValue(entry, fieldPath) {
    return _.get(entry, `items.${ projectName }:${ fieldPath}`);
}

function extractEntrySections(entry) {
  return _.filter(_.map(getFieldValue(entry, sectionsFieldTitle), _.partial(parseSection)));
}

function extractSectionContent(section) {
  const contentObject = getFieldValue(section, sectionsContentTitle);
    const raw = _.get(contentObject, 'content');
    let cons = false;

    if (section.name === 'Grid') {
        cons = true;
    }

    return raw.replace(
        /data-hippo-link="([^"]*)"/gm,
    _.partial(replaceHippoLink, _, _, _.get(contentObject, 'links'), cons),
    );
}

function replaceHippoLink(match, linkId, contentLinks) {
    let srcAttribute = 'src="';

    /**
   * NOTE: the replace here rmeoves the port number from the URLs. This solution
   * needs to be replaced with something more stable via Hippo configuration.
   */
    srcAttribute += _.get(contentLinks, [linkId, 'url'], '').replace(/:[0-9]{4}\//, '/');

    srcAttribute += '"';

    return srcAttribute;
}

function parseSection(section) {
    const fields = _.mapKeys(_.get(section, 'items'), (value, key) => _.last(_.split(key, ':')));

    if (fields[sectionsContentTitle]) {
    fields.content = extractSectionContent(section);
    }

    if (fields[sectionsFieldTitle]) {
    fields.sections = extractEntrySections(section);
    }

    return fields;
}

export default transformRawResponse;
