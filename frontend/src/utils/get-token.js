import { get } from 'lodash';

import dTs from '#assets/tokens/tokens.raw.json';

export default (tokenName, defaultValue) => get(dTs, `props.${tokenName}.value`, defaultValue);
