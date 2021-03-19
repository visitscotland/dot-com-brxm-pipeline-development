import { shallowMount } from '@vue/test-utils';

import VsDropdownItem from '../DropdownItem';

const slotContent = 'Slot Content';

const loadedAttrs = {
    href: 'https://google.com',
};

const factoryShallowMount = (propsData) => shallowMount(VsDropdownItem, {
    propsData: {
        ...propsData,
    },
    slots: {
        default: slotContent,
    },
    attrs: loadedAttrs,
});

let wrapper;

beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsDropdownItem', () => {
    it('should render a bdropdownitem-stub', () => {
        expect(wrapper.element.tagName).toBe('BDROPDOWNITEM-STUB');
    });

    describe(':attrs', () => {
        it('should bind the attributes passed in as `attrs`', () => {
            expect(wrapper.attributes().href).toEqual(loadedAttrs.href);
        });
    });

    describe(':slots', () => {
        it('renders content inserted into the default `slot`', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
