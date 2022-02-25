import VueLogger from 'vuejs-logger';
import Vue from 'vue';

import buildMode from '../../build/base.build-mode';

const isProduction = buildMode === 'production';

const options = {
    isEnabled: true,
    logLevel: isProduction ? 'error' : 'debug',
    stringifyArguments: false,
    showLogLevel: true,
    showMethodName: true,
    separator: '|',
    showConsoleColors: true,
};

Vue.use(VueLogger, options);

export default Vue.$log;
