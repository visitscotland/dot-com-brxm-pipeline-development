/* eslint-disable import/no-import-module-exports */
/* eslint-disable no-use-before-define */

import { documentToHtmlString } from '@contentful/rich-text-html-renderer';

import _ from 'lodash-es';

function transformRawResponse(raw) {
    const instance = _.get(raw, 'items.0');
    const relatedEntries = _.get(raw, 'includes.Entry');

    return {
        title: _getContentfulFieldValue(instance, 'title'),
        sections: _extractContentfulEntrySections(instance, relatedEntries),
    };
}

function _getContentfulFieldValue(contentfulEntry, fieldPath) {
    return _.get(contentfulEntry, `fields.${ fieldPath}`);
}

function _extractContentfulEntrySections(contentfulEntry, relatedEntries) {
    return _.filter(
        _.map(
            _getContentfulFieldValue(contentfulEntry, 'sections'),
            _.partial(_parseContentfulSection, _, relatedEntries),
        ),
    );
}

function _parseContentfulSection(sectionLinkObj, relatedEntries) {
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
        fields.sections = _extractContentfulEntrySections(section, relatedEntries);
    }

    return fields;
}

export default transformRawResponse;
