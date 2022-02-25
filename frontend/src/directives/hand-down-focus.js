import { get, isFunction } from 'lodash';

/**
 * When an element with this directive is focused on, the
 * directive passes focus to the element passed as the value,
 * or the element's first child if no value is given. The
 * element needs to be given a tabindex value to work.
 */

export default function(el, binding) {
    el.onfocus = () => { // eslint-disable-line no-param-reassign
        const $target = binding.value || get(el, 'childNodes[0]');
        if (isFunction($target.focus)) {
            $target.focus();
        }
    };
}
