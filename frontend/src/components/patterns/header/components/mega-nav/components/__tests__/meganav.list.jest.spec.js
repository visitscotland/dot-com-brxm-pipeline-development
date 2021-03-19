import { shallowMount } from '@vue/test-utils';
import VsMegaNavList from '../MegaNavList';

const factoryShallowMount = () => shallowMount(VsMegaNavList, {
    slots: {
        navListHeading: '<li class="top-menu-item-heading">Cities</li>',
        navListItems: '<li class="top-menu-item-list">List Item</li>',
        navHeadingCtaLink: '<li class="top-menu-item-cta">CTA Link</li>',
    },
});

describe('VsMegaNavList', () => {
    it('should render a component with the data-test attribute `vs-mega-nav-list`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-mega-nav-list');
    });

    describe(':slots', () => {
        it('renders content inserted in a navListHeading slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-mega-nav-list"]').find('.top-menu-item-heading').text()).toBe('Cities');
        });

        it('renders content inserted in a navListItems slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-mega-nav-list"]').find('.top-menu-item-list').text()).toBe('List Item');
        });

        it('renders content inserted in a navHeadingCtaLink slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-mega-nav-list"]').find('.top-menu-item-cta').text()).toBe('CTA Link');
        });
    });
});
