import { shallowMount } from '@vue/test-utils';
import VsHeader from '../Header';

const factoryShallowMount = () => shallowMount(VsHeader, {
    slots: {
        globalMenu: '<div class="global-menu"></div>',
        megaNav: '<div class="mega-nav"></div>',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsHeader', () => {
    it('should render a component with the data-test attribute `vs-header`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-header');
    });

    describe(':slots', () => {
        it('renders content inserted in a globalMenu slot', () => {
            expect(wrapper.findAll('.global-menu').length).toBe(1);
        });

        it('renders content inserted in a megaNav slot', () => {
            expect(wrapper.findAll('.mega-nav').length).toBe(1);
        });
    });
});
