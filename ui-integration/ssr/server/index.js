const querystring = require("querystring");
const express = require("express");
const { some, startsWith, get } = require("lodash");
const getPort = require("get-port");
const proxy = require("express-http-proxy");

const { renderPage } = require("./ssr");

if (!process.env.VS_SSR_PROXY_TARGET_HOST) {
    require("dotenv").config();
}

// Port range for the app
const minPort = 8082;
const maxPort = 8082;

const app = express();

/**
 * Add the start of any URL path to be excluded from SSR here
 */
const pathsToExcludeFromSSR = [
    "/site/webfiles",
    "/site/binaries",
    "/site/autoreload",
    "/favicon.ico",
    "/cms",
    "/site/manifest.webmanifest",
];

/**
 * Returns true if the provided URL path is to be excluded from SSR
 * @param {String} urlPath
 */
const excludePathFromSSR = (urlPath) => some(
    pathsToExcludeFromSSR,
    (fragment) => startsWith(urlPath, fragment),
);

/**
 * Returns string of the request's path and params URL parts
 * @param {Object} request Request object
 */
const getRequestPathAndParams = (request) => {
    let fullPath = get(request, "path")
    const query = querystring.stringify(get(request, "query"))

    if(query) {
        fullPath += `?${query}`
    }

    return fullPath
}

/**
 * Handles all requests after they have been proxied and performs SSR on them if appropriate
 */
const postProxyHandler = async (proxyRes, proxyResData, userReq) => {
    const pathAndParams = getRequestPathAndParams(userReq);

    if (excludePathFromSSR(userReq.path)) {
        // console.log(`Proxying request to ${pathAndParams}`);
    } else {
        console.log(`Attempting SSR on request to ${pathAndParams}`);

        try {
            const renderedResponse = await renderPage(proxyResData.toString("utf8"));

            console.log(`Completed SSR on request to ${pathAndParams}`)

            return renderedResponse;
        } catch (error) {
            console.error(`Failed SSR on request to ${pathAndParams}, error to follow`);
            console.error(error);
        }
    }

    return proxyResData;
}

app.use(proxy(process.env.VS_SSR_PROXY_TARGET_HOST, {
    userResDecorator: postProxyHandler,
}));


(async () => {
    const port = await getPort({
        port: getPort.makeRange(minPort, maxPort),
    });

    app.listen(port, () => console.log(`Listening on: ${port}`));
})();
