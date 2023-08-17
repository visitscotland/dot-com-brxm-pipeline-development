const CopyPlugin = require("copy-webpack-plugin")
const { CleanWebpackPlugin } = require("clean-webpack-plugin")
const path = require("path")

const DEFAULT_SOURCE_PATH = "./node_modules/@visitscotland/component-library/dist/client"
const DEFAULT_OUTPUT_PATH_ROOT = "../repository-data/webfiles/src/main/resources/site/"

const ASSET_OUTPUT_PATH = "frontend"
const TEMPLATE_OUTPUT_PATH = "freemarker/frontend"
const FTL_IMPORTS_FILE_PATH = "../../include/imports.ftl"
const VUE_APP_MOUNT_TARGET = "[data-vue-app-init]"

module.exports = function(env, argv) {
  const srcPath = path.resolve(__dirname, argv["src-path"] || DEFAULT_SOURCE_PATH)
  const outputPath = path.resolve(__dirname, argv["output-path"] || DEFAULT_OUTPUT_PATH_ROOT)
  const assetOutputPath = path.join(outputPath, ASSET_OUTPUT_PATH)
  const templateOutputPath = path.join(outputPath, TEMPLATE_OUTPUT_PATH)

  return {
    mode: "production",
    entry: path.join(srcPath, "/manifest.json"),
    output: {
      path: assetOutputPath,
    },
    plugins: [
        new CopyPlugin([
            {
                from: './embed/processed-styles/*.css',
                to: '../static/third-party/',
            },
        ]),
        new CopyPlugin([
            {
            from: srcPath,
            to: assetOutputPath,
            },
        ]),
        new CleanWebpackPlugin(),
    ],
    module: {
      rules: [
        {
          test: /\.(\.)$/,
          use: [
            {
              loader: "file-loader",
              options: {},
            },
          ],
        },
        {
          test: /\.json$/,
          use: [
            {
              loader: path.resolve("./build/generateFreemarkerTemplate.js"),
              options: {
                webfilesPath: ASSET_OUTPUT_PATH,
                freemarkerTargetPath: templateOutputPath,
                importsPath: FTL_IMPORTS_FILE_PATH,
                appMountTarget: VUE_APP_MOUNT_TARGET,
              },
            },
          ],
        },
      ],
    },
  }
}
