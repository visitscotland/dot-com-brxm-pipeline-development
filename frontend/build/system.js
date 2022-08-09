/* eslint-disable import/no-unresolved */
/* eslint-disable import/extensions */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/no-dynamic-require */

import path from 'path';

import minimist from 'minimist';
import { partial } from 'lodash-es';
import chalk from 'chalk';
import ora from 'ora';

import StyleguidistError from 'react-styleguidist/lib/scripts/utils/error.js';
import styleguidist from 'vue-styleguidist';

import { getRemoteConfig, cleanup } from './system.remote.utils.js';

import config from './system.config.js';

let systemConfig = config;

const argv = minimist(process.argv.slice(2));
const command = argv._[0];

// This will be used if NODE_ENV isn't already set
if (!process.env.NODE_ENV) {
    process.env.NODE_ENV = command === 'build' ? 'production' : 'development';
}

const spinner = ora('Building design system...');

// Do not show nasty stack traces for Styleguidist errors
process.on('uncaughtException', (err) => {
    if (err.code === 'EADDRINUSE') {
        console.log(
      `Another server is running at ${
        systemConfig ? `port ${ systemConfig.serverPort}` : 'the port'
      } already. Please stop it or change the default port to continue. You can change the port using the \`serverPort\` option in the styleguide config`,
        );
    } else {
        console.error(err.toString());
        console.error(err.stack);
    }
    process.exit(1);
});

function callbackCommon(err, stats) {
    spinner.stop();
    if (err) throw err;

    if (stats) {
        process.stdout.write(
            `${stats.toString({
                colors: true,
                modules: false,
                children: false,
                chunks: false,
                chunkModules: false,
            }) }\n\n`,
        );

        if (stats.hasErrors()) {
            console.log(chalk.red('Design System build failed with errors.\n'));
            process.exit(1);
        }
    }

    console.log(chalk.cyan('Design System build complete.\n'));
}

function buildCallback(err, inpConfig, stats) {
    callbackCommon(err, stats);

    cleanup(inpConfig);
}

function serverCallback(err, inpConfig, stats) {
    callbackCommon(err, stats);

    const url = `http://${inpConfig.serverHost === '0.0.0.0' ? 'localhost' : inpConfig.serverHost}:${
    systemConfig.serverPort
  }`;

    console.log(chalk.yellow(inpConfig.title, `running at ${url}`));

    // can't cleanup until after the server has stopped
    // cleanupRemoteBuild(remoteConfig, inpConfig)
}

function run(inpCommand, returnedConfig) {
    systemConfig = returnedConfig;
    spinner.start();

    const styleguide = styleguidist(systemConfig);
    console.log('STYLE',styleguide);

    switch (inpCommand) {
    case 'server':
        styleguide.server(serverCallback);
        break;
    case 'build':
    default:
        styleguide.build(buildCallback);
    }
}

getRemoteConfig(config, argv)
    .then(partial(run, command))
    .catch((err) => {
        console.log(chalk.red('Problem getting remote config:', err));
    });
