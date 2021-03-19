import { shallowMount, mount } from '@vue/test-utils';

import VsSocialCreditLink from '../SocialCreditLink';

const factoryShallowMount = (propsData) => shallowMount(VsSocialCreditLink, {
    propsData: {
        ...propsData,
        credit: 'Test credit',
        socialPostUrl: '#',
        source: 'instagram',
    },
});

const factoryMount = (propsData) => mount(VsSocialCreditLink, {
    propsData: {
        ...propsData,
        credit: 'Test credit',
        socialPostUrl: '#',
        source: 'instagram',
    },
});

describe('VsSocialCreditLink', () => {
    it('should render a component with the data-test attribute `vs-social-credit-link`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-social-credit-link');
    });

    describe(':props', () => {
        it('accepts and renders a `credit` prop', () => {
            const wrapper = factoryMount();

            expect(wrapper.text()).toContain('Test credit');
        });

        it('accepts `socialPostUrl` prop and passes it to a VsLink', () => {
            const wrapper = factoryMount();
            const link = wrapper.find('[data-test="vs-social-credit-link__link"');

            expect(link.props().href).toBe('#');
        });

        it('renders an icon if the `source` prop is defined', () => {
            const wrapper = factoryShallowMount();
            const icon = wrapper.find('[data-test="vs-social-credit-link__icon"');

            expect(icon.exists()).toBe(true);
        });

        it('passes the `source` prop to the icon', () => {
            const wrapper = factoryShallowMount();
            const icon = wrapper.find('[data-test="vs-social-credit-link__icon"');

            expect(icon.props().name).toBe('instagram');
        });
    });
});
