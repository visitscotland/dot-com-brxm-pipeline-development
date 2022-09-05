import {
    pickBy,
    startsWith,
    map,
    get,
    partial,
    keys,
    replace,
    zipObject,
} from 'lodash';

import designTokens from '@assets/tokens/tokens.raw.json';

const breakpointTokens = pickBy(get(designTokens, 'props'), (value, name) => startsWith(name, 'breakpoint_'));

const breakpointNames = map(
    keys(breakpointTokens),
    partial(replace, partial.placeholder, 'breakpoint_', ''),
);

export default zipObject(breakpointNames, map(breakpointTokens, 'value'));
