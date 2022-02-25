import { shallowMount, mount } from '@vue/test-utils';

import VsFooterNavListItem from '../FooterNavListItem';

const slotContent = 'Some slot content';

const factoryShallowMount = (propsData) => shallowMount(VsFooterNavListItem, {
    propsData: {
        ...propsData,
    },
    slots: {
        default: slotContent,
    },
});

const factoryMount = (propsData) => mount(VsFooterNavListItem, {
    propsData: {
        ...propsData,
        href: 'https://google.com',
        type: 'external',
        linkText: 'This is a link',
    },
    slots: {
        default: slotContent,
    },
});

describe('VsFooterNavListItem', () => {
    it('should render a component with the data-test attribute `vs-footer-nav-list-item`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-footer-nav-list-item');
    });

    describe(':props', () => {
        it('should accept and render a `linkText` property', () => {
            const wrapper = factoryMount();

            expect(wrapper.text()).toContain('This is a link');
        });

        it('should pass the `href` property to the child link', () => {
            const wrapper = factoryMount();
            const vsLink = wrapper.find('[data-test="vs-footer-nav-list-item__link"');

            expect(vsLink.props().href).toBe('https://google.com');
        });

        it('should pass the `link-alt-text` property to the child link', () => {
            const wrapper = factoryMount();
            const vsLink = wrapper.find('[data-test="vs-footer-nav-list-item__link"');

            expect(vsLink.props().type).toBe('external');
        });
    });
});
