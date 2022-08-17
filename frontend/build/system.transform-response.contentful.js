/* eslint-disable import/no-import-module-exports */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-use-before-define */

import { documentToHtmlString } from '@contentful/rich-text-html-renderer';

import _ from 'lodash-es';

function transformRawResponse(raw) {
    const instance = _.get(raw, 'items.0');
    const relatedEntries = _.get(raw, 'includes.Entry');

    return {
    title: getContentfulFieldValue(instance, 'title'),
    sections: extractContentfulEntrySections(instance, relatedEntries),
    };
}

function getContentfulFieldValue(contentfulEntry, fieldPath) {
    return _.get(contentfulEntry, `fields.${ fieldPath}`);
}

function extractContentfulEntrySections(contentfulEntry, relatedEntries) {
    return _.filter(
        _.map(
      getContentfulFieldValue(contentfulEntry, 'sections'),
      _.partial(parseContentfulSection, _, relatedEntries),
        ),
    );
}

function parseContentfulSection(sectionLinkObj, relatedEntries) {
    const section = _.find(
        relatedEntries,
        _.matchesProperty('sys.id', _.get(sectionLinkObj, 'sys.id')),
    );

    if (!section) {
        return null;
    }

    const fields = _.get(section, 'fields');

    if (fields.content) {
        fields.content = documentToHtmlString(fields.content);
    }

    if (fields.sections) {
    fields.sections = extractContentfulEntrySections(section, relatedEntries);
    }

    return fields;
}

export default transformRawResponse;
