import { shallowMount } from '@vue/test-utils';

import VsDropdown from '../Dropdown';

const slotContent = 'Slot Content';

const factoryShallowMount = (propsData) => shallowMount(VsDropdown, {
    propsData: {
        ...propsData,
    },
    slots: {
        default: slotContent,
    },
});

let wrapper;

beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VSDropdown', () => {
    it('should render a bdropdown-stub', () => {
        expect(wrapper.element.tagName).toBe('BDROPDOWN-STUB');
    });
});
