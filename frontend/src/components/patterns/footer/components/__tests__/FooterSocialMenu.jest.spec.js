import { shallowMount } from '@vue/test-utils';

import VsFooterSocialMenu from '../FooterSocialMenu';

const slotContent = 'Some slot content';
const titleSlotContent = 'A title';

const factoryShallowMount = (propsData) => shallowMount(VsFooterSocialMenu, {
    propsData: {
        ...propsData,
    },
    slots: {
        default: slotContent,
        title: titleSlotContent,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsFooterSocialMenu', () => {
    it('should render a component with the data-test attribute `vs-footer-social-menu`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-footer-social-menu');
    });

    describe(':slots', () => {
        it('renders content inserted into the `default` slot', () => {
            expect(wrapper.text()).toContain(slotContent);
        });

        it('renders content inserted into the `title` slot', () => {
            expect(wrapper.text()).toContain(titleSlotContent);
        });
    });
});
