#! /usr/bin/env node
/* eslint-disable no-console */

/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-use-before-define */
/* eslint-disable no-param-reassign */

import fs from 'fs';
import rm from 'rimraf';
import path from 'path';

import _ from 'lodash-es';
import ora from 'ora';
import chalk from 'chalk';
import requestPromise from 'request-promise-native';
import { micromark } from 'micromark';

import getConfig from 'vue-styleguidist';
import StyleguidistError from 'react-styleguidist';

const KEY_REMOTE_CONFIG = 'remoteProfiles';
const ARG_REMOTE_PROFILE = 'remote-profile';

const defaultRemoteConfig = {
  tempPath: './.tmp/',
};

const defaultRequestOptions = {
  json: true,
};

let remoteConfig = null;
let spinner;

// function getMicromarkService() {
//   const outMicromarkService = micromark(content, );

//   outMicromarkService.use(MicromarkTablesPlugin);

//   outMicromarkService.addRule('transform-code', {
//     filter: ['code', 'pre'],
//     replacement(content) {
//       return `\`\`\`\n${content}\n\`\`\``;
//     },
//   });

//   return outMicromarkService;
// }

// const micromarkService = getMicromarkService();

function makeRemoteUriFromRemoteConfig(config) {
  const uri = _.get(config, 'uriBase');
  let queryString = null;

  if (!uri) {
    throw new StyleguidistError('Remote config is missing uriBase');
  }

  queryString = _.join(
    _.map(_.get(config, 'uriParams'), (value, key) => `${key}=${value}`),
    '&',
  );

  return uri + (queryString ? `?${queryString}` : '');
}

function makeRequestOptions(inpRemoteConfig) {
  const options = {
    uri: makeRemoteUriFromRemoteConfig(inpRemoteConfig),
  };

  return _.defaults(options, _.get(inpRemoteConfig, 'requestOptions'), defaultRequestOptions);
}

function mergeConfig(inpRemoteConfig, localConfig) {
  return _.assign({
  }, localConfig, inpRemoteConfig);
}

function prepOutputDir(tempOutputPath) {
  if (!fs.existsSync(tempOutputPath)) {
    fs.mkdirSync(tempOutputPath, {
      recursive: true,
    });
  }
}

function processSectionContent(section, tempOutputPath) {
  const outputMarkdownPath = tempOutputPath + makeSectionMarkdownFileName(section);
  const htmlContent = _.get(section, 'content');
  const markdownContent = htmlContent ? micromark(htmlContent) : '';

  fs.writeFileSync(outputMarkdownPath, markdownContent, 'utf8');

  return outputMarkdownPath;
}

function processSection(section, tempOutputPath) {
  // process child sections first
  section = processSections(section, tempOutputPath);

  return _.merge({
  }, section, {
    content: processSectionContent(section, tempOutputPath),
  });
}

function processSections(parentObject, tempOutputPath) {
  const sections = _.get(parentObject, 'sections');

  if (!sections) {
    return parentObject;
  }

  prepOutputDir(tempOutputPath);

  parentObject.sections = _.map(sections, _.partial(processSection, _, tempOutputPath));

  return parentObject;
}

function makeSectionMarkdownFileName(section) {
  return `${_.kebabCase(_.get(section, 'name'))}.md`;
}

function applyRemoteConfigDefaults(inpRemoteConfig) {
  return _.defaultsDeep({
  }, inpRemoteConfig, defaultRemoteConfig);
}

function addPrivateComponents(mergedConfig) {
  if (!mergedConfig.sections || !_.isArray(mergedConfig.sections)) {
    mergedConfig.sections = [];
  }

  mergedConfig.sections.push({
    name: 'Private Components',
    exampleMode: 'hide',
    usageMode: 'hide',
    components: ['./src/**/*.vue', './docs/components/**/*.vue'],
  });

  return mergedConfig;
}

function extractRemoteConfig(config, profileName) {
  const outRemoteConfig = _.get(config, KEY_REMOTE_CONFIG);

  _.unset(config, KEY_REMOTE_CONFIG);

  if (_.isEmpty(outRemoteConfig)) {
    console.log(
      chalk.red('No remote config defined'),
    );

    return null;
  }

  if (!profileName) {
    profileName = _.first(_.keys(outRemoteConfig));
    console.log(chalk.red('No profile chosen, falling back to first profile in config'));
  } else if (!_.has(outRemoteConfig, profileName)) {
    console.log(
      chalk.red(
        `Profile ${profileName} not included in remote config -
        add at least one profile to the "remoteProfile" key of the config.
        Falling back to first profile in config`,
      ),
    );

    profileName = _.first(_.keys(outRemoteConfig));
  }

  console.log(chalk.cyan(`Selected remote profile - ${profileName}.`));

  return _.get(outRemoteConfig, profileName);
}

export function getRemoteConfig(config, argv) {
  const selectedProfileName = _.get(argv, ARG_REMOTE_PROFILE, process.env.VS_DS_REMOTE_PROFILE);

  remoteConfig = extractRemoteConfig(config, selectedProfileName);

  if (!remoteConfig) {
    return Promise.resolve(getConfig(config));
  }

  remoteConfig = applyRemoteConfigDefaults(remoteConfig);

  const requestOptions = makeRequestOptions(remoteConfig);

  spinner = ora(`Getting remote config from ${requestOptions.uri}...`);

  spinner.start();

  return requestPromise(requestOptions)
    .then(_.partial(mergeConfig, _, config))
    .then(_.partial(processSections, _, _.get(remoteConfig, 'tempPath')))
    .then(addPrivateComponents)
    .then((mergedConfig) => {
      spinner.stop();

      console.log(chalk.cyan('Remote config merged!'));

      return mergedConfig;
    })
    .catch((err) => {
      spinner.stop();

      console.log(chalk.red(`Problem encountered getting remote config from ${requestOptions.uri}`));
      console.log(err);

      // return the original static config on error
      console.log(chalk.cyan('Ignoring remote config'));

      return config;

      // throw err
    })
    .then(getConfig);
}

export function cleanup(docsConfig) {
  const sectionMarkdownFiles = _.map(_.get(docsConfig, 'sections'), makeSectionMarkdownFileName);

  const tempPath = _.get(applyRemoteConfigDefaults(remoteConfig), 'tempPath');

  _.each(sectionMarkdownFiles, (fileName) => {
    rm(path.join(tempPath, fileName), (err) => {
      if (err) {
        console.log(chalk.red('Cleanup: failed to remove temp markdown file', fileName, ':', err));
      }
    });
  });
}
