import { shallowMount } from '@vue/test-utils';

import VsFooterNavList from '../FooterNavList';

const slotContent = 'Some slot content';

const factoryShallowMount = (propsData) => shallowMount(VsFooterNavList, {
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

describe('VsFooterNavList', () => {
    it('should render a component with the data-test attribute `vs-footer-nav-list`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-footer-nav-list');
    });

    describe(':slots', () => {
        it('renders content inserted into the `default` slot', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
