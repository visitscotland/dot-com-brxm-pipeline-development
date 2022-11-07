import { shallowMount } from '@vue/test-utils';
import VsMegaNavMobileMenu from '../MegaNavMobileMenu';

const factoryShallowMount = () => shallowMount(VsMegaNavMobileMenu, {
    slots: {
        default: '<p>Mobile nav content</p>',
    },
});

describe('VsMegaNavMobileMenu', () => {
    it('should render a component with the data-test attribute `vs-mega-nav-mobile-menu`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-mega-nav-mobile-menu');
    });

    describe(':slots', () => {
        it('renders content inserted in the default slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.html()).toContain('<p>Mobile nav content</p>');
        });
    });
});
