import { shallowMount, mount } from '@vue/test-utils';

import VsFooterSocialItem from '../FooterSocialItem';

const factoryShallowMount = (propsData) => shallowMount(VsFooterSocialItem, {
    propsData: {
        ...propsData,
        href: 'https://facebook.com',
        icon: 'facebook',
    },
});

const factoryMount = (propsData) => mount(VsFooterSocialItem, {
    propsData: {
        ...propsData,
        href: 'https://facebook.com',
        icon: 'facebook',
    },
});

describe('VsFooterSocialItem', () => {
    it('should render a component with the data-test attribute `vs-footer-social-item`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-footer-social-item');
    });

    describe(':props', () => {
        it('should pass the `href` property to the child link', () => {
            const wrapper = factoryMount();
            const vsLink = wrapper.find('[data-test="vs-footer-social-item__button"');

            expect(vsLink.props().href).toBe('https://facebook.com');
        });

        it('should pass the `link-alt-text` property to the child link', () => {
            const wrapper = factoryMount();
            const vsLink = wrapper.find('[data-test="vs-footer-social-item__button"');

            expect(vsLink.props().icon).toBe('facebook');
        });
    });
});
