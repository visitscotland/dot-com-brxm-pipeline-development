import { shallowMount } from '@vue/test-utils';
import VsMegaNavTopMenu from '../MegaNavTopMenu';

const factoryShallowMount = () => shallowMount(VsMegaNavTopMenu, {
    slots: {
        default: '<li class="top-menu-item"></li>',
    },
});

describe('VsMegaNavTopMenu', () => {
    it('should render a component with the data-test attribute `vs-mega-nav-top-menu`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-mega-nav-top-menu');
    });

    describe(':slots', () => {
        it('renders a list item in the default slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.findAll('.top-menu-item').length).toBe(1);
        });
    });
});
