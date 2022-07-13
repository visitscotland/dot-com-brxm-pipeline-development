#! /usr/bin/env node

/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-use-before-define */
/* eslint-disable no-param-reassign */
/* eslint-disable max-len */

const fs = require('fs');
const rm = require('rimraf');
const path = require('path');

const _ = require('lodash');
const ora = require('ora');
const chalk = require('chalk');
const requestPromise = require('request-promise-native');
const Turndown = require('turndown');
const turndownTablesPlugin = require('turndown-plugin-gfm').tables;

const getConfig = require('vue-styleguidist/scripts/config');
const StyleguidistError = require('react-styleguidist/lib/scripts/utils/error');

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

function _getTurndownService() {
    const outTurndownService = new Turndown();

    outTurndownService.use(turndownTablesPlugin);

    outTurndownService.addRule('transform-code', {
        filter: ['code', 'pre'],
        replacement(content) {
            return `\`\`\`\n${content}\n\`\`\``;
        },
    });

    return outTurndownService;
}

const turndownService = _getTurndownService();

function _makeRemoteUriFromRemoteConfig(config) {
    const uri = _.get(config, 'uriBase');
    let queryString = null;

    if (!uri) {
        throw new StyleguidistError('Remote config is missing uriBase');
    }

    queryString = _.join(
        _.map(_.get(config, 'uriParams'), (value, key) => `${key}=${value}`),
        '&'
    );

    return uri + (queryString ? `?${queryString}` : '');
}

function _makeRequestOptions(inpRemoteConfig) {
    const options = {
        uri: _makeRemoteUriFromRemoteConfig(inpRemoteConfig),
    };

    return _.defaults(options, _.get(inpRemoteConfig, 'requestOptions'), defaultRequestOptions);
}

function _mergeConfig(inpRemoteConfig, localConfig) {
    return _.assign({}, localConfig, inpRemoteConfig);
}

function _prepOutputDir(tempOutputPath) {
    if (!fs.existsSync(tempOutputPath)) {
        fs.mkdirSync(tempOutputPath, {
            recursive: true,
        });
    }
}

function _processSectionContent(section, tempOutputPath) {
    const outputMarkdownPath = tempOutputPath + _makeSectionMarkdownFileName(section);
    const htmlContent = _.get(section, 'content');
    const markdownContent = htmlContent ? turndownService.turndown(htmlContent) : '';

    fs.writeFileSync(outputMarkdownPath, markdownContent, 'utf8');

    return outputMarkdownPath;
}

function _processSection(section, tempOutputPath) {
    // process child sections first
    section = _processSections(section, tempOutputPath);

    return _.merge({}, section, {
        content: _processSectionContent(section, tempOutputPath),
    });
}

function _processSections(parentObject, tempOutputPath) {
    const sections = _.get(parentObject, 'sections');

    if (!sections) {
        return parentObject;
    }

    _prepOutputDir(tempOutputPath);

    parentObject.sections = _.map(sections, _.partial(_processSection, _, tempOutputPath));

    return parentObject;
}

function _makeSectionMarkdownFileName(section) {
    return `${_.kebabCase(_.get(section, 'name'))}.md`;
}

function _applyRemoteConfigDefaults(inpRemoteConfig) {
    return _.defaultsDeep({}, inpRemoteConfig, defaultRemoteConfig);
}

function _addPrivateComponents(mergedConfig) {
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

function _extractRemoteConfig(config, profileName) {
    const outRemoteConfig = _.get(config, KEY_REMOTE_CONFIG);

    _.unset(config, KEY_REMOTE_CONFIG);

    if (_.isEmpty(outRemoteConfig)) {
        console.log(chalk.red('No remote config defined'));

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
        Falling back to first profile in config`
            )
        );

        profileName = _.first(_.keys(outRemoteConfig));
    }

    console.log(chalk.cyan(`Selected remote profile - ${profileName}.`));

    return _.get(outRemoteConfig, profileName);
}

function getRemoteConfig(config, argv) {
    const selectedProfileName = _.get(argv, ARG_REMOTE_PROFILE, process.env.VS_DS_REMOTE_PROFILE);

    remoteConfig = _extractRemoteConfig(config, selectedProfileName);

    if (!remoteConfig) {
        return Promise.resolve(getConfig(config));
    }

    remoteConfig = _applyRemoteConfigDefaults(remoteConfig);

    const requestOptions = _makeRequestOptions(remoteConfig);

    spinner = ora(`Getting remote config from ${requestOptions.uri}...`);

    spinner.start();

    return requestPromise(requestOptions)
        .then(_.partial(_mergeConfig, _, config))
        .then(_.partial(_processSections, _, _.get(remoteConfig, 'tempPath')))
        .then(_addPrivateComponents)
        .then(mergedConfig => {
            spinner.stop();

            console.log(chalk.cyan('Remote config merged!'));

            return mergedConfig;
        })
        .catch(err => {
            spinner.stop();

            console.log(
                chalk.red(`Problem encountered getting remote config from ${requestOptions.uri}`)
            );
            console.log(err);

            // return the original static config on error
            console.log(chalk.cyan('Ignoring remote config'));

            return config;

            // throw err
        })
        .then(getConfig);
}

function cleanup(docsConfig) {
    const sectionMarkdownFiles = _.map(_.get(docsConfig, 'sections'), _makeSectionMarkdownFileName);

    const tempPath = _.get(_applyRemoteConfigDefaults(remoteConfig), 'tempPath');

    _.each(sectionMarkdownFiles, fileName => {
        rm(path.join(tempPath, fileName), err => {
            if (err) {
                console.log(
                    chalk.red('Cleanup: failed to remove temp markdown file', fileName, ':', err)
                );
            }
        });
    });
}

module.exports = {
    getRemoteConfig,
    cleanup,
};
