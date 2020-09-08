'use strict';

const Gatherer = require('lighthouse').Gatherer;
const EnvironmentJSON = require('./env_url.json');
class CustomGatherer extends Gatherer {
  beforePass(options) {
    const driver = options.driver;
    const target = options.url;
    options.url = EnvironmentJSON.url+target;
     return driver.evaluateAsync('window.metric')
      .then(loadMetrics => {
        if (!loadMetrics) {
          throw new Error('Unable to find load metrics in page');
        }
        return loadMetrics;
      });
  }
}

module.exports = CustomGatherer;