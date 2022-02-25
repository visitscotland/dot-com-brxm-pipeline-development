import { shallowMount } from '@vue/test-utils';

import VsFooterSocialItem from '../FooterSocialItem';

const factoryShallowMount = (propsData) => shallowMount(VsFooterSocialItem, {
    propsData: {
        ...propsData,
        href: 'https://facebook.com',
        icon: 'facebook',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsFooterSocialItem', () => {
    it('should render a component with the data-test attribute `vs-footer-social-item`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-footer-social-item');
    });

    describe(':props', () => {
        it(':icon - Should show correct social icon', () => {
            const icon = wrapper.find('[data-test="vs-footer-social-item__link"').find('vsicon-stub');

            expect(icon.attributes('name')).toBe('facebook');
        });

        it(':href - Should show the correct social URL on the link', () => {
            const link = wrapper.find('[data-test="vs-footer-social-item__link"');

            expect(link.attributes('href')).toBe('https://facebook.com');
        });
    });
});
