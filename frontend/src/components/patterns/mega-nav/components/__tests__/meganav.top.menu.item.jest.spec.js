import { shallowMount, mount } from '@vue/test-utils';
import VsMegaNavTopMenuItem from '../MegaNavTopMenuItem';

const factoryMount = (values) => mount(VsMegaNavTopMenuItem, {
    propsData: {
        ...values,
    },
    slots: {
        buttonContent: 'Dropdown Toggle',
        dropdownContent: '<li class="dropdown-list-item"></li>',
        navFeaturedItem: '<div>Test featured item</div>',
    },
});

describe('VsMegaNavTopMenuItem', () => {
    it('should render a component with the data-test attribute `vs-mega-nav-top-menu-item`', () => {
        const wrapper = shallowMount(VsMegaNavTopMenuItem);
        expect(wrapper.attributes('data-test')).toBe('vs-mega-nav-top-menu-item');
    });

    describe(':props', () => {
        it('should not render CTA link by default when no props have been provided', () => {
            const wrapper = factoryMount();
            expect(wrapper.find('[data-test="vs-mega-nav-top-menu-item__cta-link"]').exists()).toBeFalsy();
        });

        it('should not render CTA link if only `ctaText` prop has been provided', () => {
            const wrapper = factoryMount({
                ctaText: 'Explore the Map of Scotland',
            });
            expect(wrapper.find('[data-test="vs-mega-nav-top-menu-item__cta-link"]').exists()).toBeFalsy();
        });

        it('should not render CTA link if only `href` prop has been provided', () => {
            const wrapper = factoryMount({
                href: 'https://www.visitscotland.com/destinations',
            });
            expect(wrapper.find('[data-test="vs-mega-nav-top-menu-item__cta-link"]').exists()).toBeFalsy();
        });

        it('should render CTA link if both `ctaText` and `href` props have been provided', () => {
            const wrapper = factoryMount({
                ctaText: 'Explore the Map of Scotland',
                href: 'https://www.visitscotland.com/destinations',
            });
            expect(wrapper.find('[data-test="vs-mega-nav-top-menu-item__cta-link"]').exists()).toBeTruthy();
        });

        it('should display CTA link text `Explore the Map of Scotland` when passed as `ctaText` prop', () => {
            const wrapper = factoryMount({
                ctaText: 'Explore the Map of Scotland',
                href: 'https://www.visitscotland.com/destinations',
            });
            expect(wrapper.find('[data-test="vs-mega-nav-top-menu-item__cta-link"]').text()).toBe('Explore the Map of Scotland');
        });

        it('should display the URL `https://www.visitscotland.com/destinations` on the cta link when passed as `href` prop', () => {
            const wrapper = factoryMount({
                ctaText: 'Explore the Map of Scotland',
                href: 'https://www.visitscotland.com/destinations',
            });
            expect(wrapper.find('[data-test="vs-mega-nav-top-menu-item__cta-link"]').attributes().href).toBe('https://www.visitscotland.com/destinations');
        });

        it('should show the appropriate class if the `align` prop is set to `bottom`', () => {
            const wrapper = factoryMount({
                align: 'bottom',
            });
            expect(wrapper.find('.vs-mega-nav-top-menu-item__featured--bottom').exists()).toBe(true);
        });
    });

    describe(':slots', () => {
        it('renders content inserted in a buttonContent slot', () => {
            const wrapper = factoryMount();
            expect(wrapper.find('[data-test="vs-mega-nav-dropdown"]').find('.dropdown-toggle').text()).toBe('Dropdown Toggle');
        });

        it('renders content inserted in a dropdownContent slot', () => {
            const wrapper = factoryMount();
            expect(wrapper.findAll('.dropdown-list-item').length).toBe(1);
        });

        it('renders content inserted in a navFeaturedItems slot', () => {
            const wrapper = factoryMount();
            expect(wrapper.find('[data-test="vs-mega-nav-top-menu-item__featured"]').text()).toBe('Test featured item');
        });
    });
});
