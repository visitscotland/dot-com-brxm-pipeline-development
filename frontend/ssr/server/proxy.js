const httpProxy = require("http-proxy");
const webO = Object.values(require("http-proxy/lib/http-proxy/passes/web-outgoing"));
const { isFunction } = require("lodash");

let proxy;

const proxyOptions = {
    selfHandleResponse: true,
    ws: true,
};

const createOnResponse = (postProcess) => {
    return (proxyRes, req, res) => {
        const body = [];

        proxyRes.on("data", (chunk) => {
            body.push(chunk);
        });

        proxyRes.on("end", async () => {
            let response = Buffer.concat(body).toString();

            if (isFunction(postProcess)) {
                response = await postProcess(req.path, response);
            }

            proxyRes.pipe(res);

            res.end(response);
        });

        // Here we have to copy what the http-proxy library does for responses that
        // don't require custom handling. This is bad but necessary until the library
        // provides a way to conditionally apply custom handling per request.
        // https://github.com/http-party/node-http-proxy/issues/1451

        if (!res.headersSent) {
            for (let i = 0; i < webO.length; i++) {
                if (webO[i](req, res, proxyRes, proxyOptions)) { break }
            }
        }
    }
}

module.exports = (target, postProcess) => {
    proxy = httpProxy.createProxyServer({
        ...proxyOptions,
        target,
    });

    proxy.on("proxyRes", createOnResponse(postProcess))

    return proxy;
}
