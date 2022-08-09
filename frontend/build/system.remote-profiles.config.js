/* eslint-disable import/extensions */
/* eslint-disable import/no-import-module-exports */
import { dirname } from 'path';
import { fileURLToPath } from 'url';
import { merge } from 'lodash-es';
import dotenv from 'dotenv';
import * as trHippo from './system.transform-response.hippo.js';
import * as trContentful from './system.transform-response.contentful.js';

const __dirname = dirname(fileURLToPath(import.meta.url));

if (!process.env.VS_DS_REMOTE_CONFIG_URL) {
    dotenv.config({
        path: `${__dirname}/./../../.env`,
    });
}

/**
 * Define connection details and response transformations for remote config here
 *
 * Export object, each key defines a named remote config profile.
 *
 * See Contentful example below for details:
 */

// module.exports = {
//  contentful: {
//     /**
//      * URI base is required and is the URL to poll for config, without query params
//      */
//     uriBase: "https://cdn.contentful.com/spaces/xxxxxxxxxxxx/environments/master/entries",
//
//     /**
//      * Map of query params to add to the request
//      */
//     uriParams: {
//       access_token: "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
//       content_type: "instance",
//       include: 5,
//     },
//
//     /**
//      * Options object passed to request-promise-native
//      */
//     requestOptions: {
//        strictSSL: true,
//        transform: require("../build/contentful-response-transform")
//     }
//   }
// }

const commonConfig = {
    uriBase: process.env.VS_DS_REMOTE_CONFIG_URL,
    requestOptions: {
        strictSSL: process.env.VS_DS_REMOTE_CONFIG_STRICT_SSL !== 'false',
    },
};

const hippo = merge({   
}, commonConfig, {
    requestOptions: {
        transform: trHippo.transformRawResponse,
    },
});

const contentful = merge({
}, commonConfig, {
    uriParams: {
        access_token: process.env.VS_DS_REMOTE_CONFIG_CONTENTFUL_TOKEN,
        content_type: 'instance',
        include: 5,
    },
    requestOptions: {
        transform: trContentful.transformRawResponse,
    },
});

export default {
    hippo,
    contentful,
};
