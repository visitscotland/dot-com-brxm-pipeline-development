const { flow } = require("lodash");

if (!process.env.VS_SSR_PROXY_TARGET_HOST) {
    require("dotenv").config();
}

const targetHostWithoutProtocol = process.env.VS_SSR_PROXY_TARGET_HOST.replace(/[a-z]+:\/\//, "");

// This function prepends the target host address to all script and link href and src values,
// respectively, that begin with "/" - workaround instead of outputting absolute
// URLs from the target site
const correctLocalAssetUrls = (inputHtml) => inputHtml.replace(
    /(<(?:script|link)\s+(?:[^>]*?\s+)?(?:href|src)=(?:["']))([\.]*\/)/g,
    `$1${process.env.VS_SSR_PROXY_TARGET_HOST}/`,
);

const correctHippAutoreloadUrl = (inputHtml) => inputHtml.replace(
    /("ws:\/\/)" \+ document\.location\.host/g,
    `$1${targetHostWithoutProtocol}"`,
);

module.exports = flow(
    correctLocalAssetUrls,
    correctHippAutoreloadUrl,
);
