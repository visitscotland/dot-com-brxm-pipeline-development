/* eslint-disable import/extensions */
/* eslint-disable no-console */
/* eslint-disable import/no-extraneous-dependencies */

import path from 'path';
import chalk from 'chalk';
import vueDocgenApi from 'vue-docgen-api';
import { readFileSync } from 'fs';
import { fileURLToPath } from 'url';

import webpackConfig from './system.webpack.config.js';
import parseChildComponents from './system.parse-child-components.js';
import remoteProfiles from './system.remote-profiles.config.js';
import * as packageConfig from '../package.json' assert {type: 'json'};

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const config = {
  version: packageConfig.version,
  /**
     * Most of the styles are defined in /docs/styles/docs.styles.scss
     */
  theme: {
    maxWidth: '100%',
    sidebarWidth: 260,
    fontFamily: {
      base: ['Source Sans Pro', 'Helvetica', 'Arial', 'sans-serif'],
      heading: ['evelethclean-regular', 'Helvetica', 'Arial', 'sans-serif'],
    },
  },
  renderRootJsx: path.join(__dirname, '../docs/components/Preview.js'),
  assetsDir: path.join(__dirname, '../src/assets'),
  /**
     * Enabling the below option will break things!
     */
  skipComponentsWithoutExample: false,
  /**
     * We’re defining below JS and SCSS required globally.
     */
  require: [
    path.join(__dirname, '../docs/docs.helper.js'),
    path.join(__dirname, '../docs/styles/docs.styles.scss'),
    path.join(__dirname, '../src/assets/fixtures/index.js'),
    path.join(__dirname, '../src/styles/core.styles.scss'),
  ],
  /**
     * Enabling the following option splits sections into separate views.
     */
  pagePerSection: true,
  /**
     * Custom wrapper template for the design system site.
     */
  template: {
    title: 'Example — Design System',
    lang: 'en',
    trimWhitespace: true,
    head: {
      meta: [
        {
          name: 'viewport',
          content: 'width=device-width,initial-scale=1.0',
        },
        {
          name: 'format-detection',
          content: 'telephone=no',
        },
      ],
    },
    favicon: 'https://www.visitscotland.com/favicon.ico',
  },
  ignore: [
    '**/App.vue',
    '**/__tests__/**',
    '**/*.test.js',
    '**/*.test.jsx',
    '**/*.spec.js',
    '**/*.spec.jsx',
    '**/ExampleComponent.vue',
  ],
  webpackConfig,
  compilerConfig: {
    target: {
      ie: '11',
      chrome: '71',
      firefox: '64',
      safari: '11',
      edge: '17',
    },
  },
  styleguideDir: 'dist/system',
  printServerInstructions() {
  },
  printBuildInstructions(passedConfig) {
    console.log(chalk.cyan('\n  Design System Docs build finished succesfully!\n'));
    console.log(
      chalk.yellow(
        '  Tip: You can now deploy the docs as a static website.\n'
                + '  Copy the build files from '
          + `${passedConfig.styleguideDir}\n`,
      ),
    );
  },
  styleguideComponents: {
    LogoRenderer: path.join(__dirname, '../docs/components/rsg-components/LogoRenderer'),
    Playground: path.join(__dirname, '../docs/components/rsg-components/Playground'),
    StyleGuideRenderer: path.join(__dirname, '../docs/components/rsg-components/StyleGuideRenderer'),
    ComponentsRenderer: path.join(__dirname, '../docs/components/rsg-components/ComponentsRenderer'),
    TabButton: path.join(__dirname, '../docs/components/rsg-components/TabButton'),
    Usage: path.join(__dirname, '../docs/components/rsg-components/Usage'),
  },
  propsParser(filePath) {
    return vueDocgenApi.parse(filePath, {
      resolve: webpackConfig.resolve,
      alias: webpackConfig.resolve.alias,
      addScriptHandlers: [parseChildComponents.default],
    });
  },
  /**
     * Details of any remote config profiles
     */
  remoteProfiles,

  /**
   * Configure docs server to redirect asset queries
   */
  // configureServer(app) {
  //   // `app` is the instance of the express server running the docs
  //   app.get('/assets/:file', (req, res) => {
  //     res.redirect(req.params.file)
  //   })
  // },
};

export default config;
