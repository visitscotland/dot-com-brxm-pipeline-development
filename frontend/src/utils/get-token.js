import { get } from 'lodash';

import designTokens from '@/assets/tokens/tokens.raw.json';

export default (tokenName, defaultValue) => get(designTokens, `props.${tokenName}.value`, defaultValue);
