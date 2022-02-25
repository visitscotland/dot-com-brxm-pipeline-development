import { shallowMount, mount } from '@vue/test-utils';

import VsFooterCopyright from '../FooterCopyright';

const slotContent = 'Some copyright info';

const factoryShallowMount = (propsData) => shallowMount(VsFooterCopyright, {
    propsData: {
        ...propsData,
        href: 'https://google.com',
        linkAltText: 'A link to google',
    },
    slots: {
        copyright: slotContent,
    },
});

const factoryMount = (propsData) => mount(VsFooterCopyright, {
    propsData: {
        ...propsData,
        href: 'https://google.com',
        linkAltText: 'A link to google',
    },
    slots: {
        copyright: slotContent,
    },
});

describe('VsFooterCopyright', () => {
    it('should render a component with the data-test attribute `vs-footer-copyright`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-footer-copyright');
    });

    describe(':props', () => {
        it('should pass the `href` property to the logo', async() => {
            const wrapper = factoryMount();
            const vsLink = wrapper.find('[data-test="vs-footer-copyright-logo"');

            expect(vsLink.props().href).toBe('https://google.com');
        });

        it('should pass the `link-alt-text` property to the logo', async() => {
            const wrapper = factoryMount();
            const vsLink = wrapper.find('[data-test="vs-footer-copyright-logo"');

            expect(vsLink.props().linkAltText).toBe('A link to google');
        });
    });

    describe(':slots', () => {
        it('renders content inserted into the `copyright` slot', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
