import { shallowMount, mount } from '@vue/test-utils';
import VsMegaNavDropdown from '../MegaNavDropdown';

const factoryShallowMount = () => shallowMount(VsMegaNavDropdown, {
    propsData: {
        menuToggleAltText: 'Toggle menu',
    },
    slots: {
        buttonContent: '<div class="button-content"></div>',
        ctaLink: '<div class="cta-link"></div>',
        dropdownContent: '<div class="dropdown-content"></div>',
    },
});

const factoryMount = () => mount(VsMegaNavDropdown, {
    propsData: {
        menuToggleAltText: 'Toggle menu',
    },
});

describe('VsMegaNavDropdown', () => {
    it('should render a component with the data-test attribute `vs-mega-nav-dropdown`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-mega-nav-dropdown');
    });

    describe(':slots', () => {
        it('renders content inserted in a ctaLink slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.findAll('.cta-link').length).toBe(1);
        });

        it('renders content inserted in a dropdownContent slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.findAll('.dropdown-content').length).toBe(1);
        });
    });

    describe(':events', () => {
        it('emits `menuToggled` event when `bv::dropdown::show` is emitted from root', () => {
            const wrapper = factoryMount();
            wrapper.vm.$root.$emit('bv::dropdown::show');
            expect(wrapper.emitted().menuToggled).toBeTruthy();
        });

        it('emits `menuToggled` event when `bv::dropdown::hide` is emitted from root', () => {
            const wrapper = factoryMount();
            wrapper.vm.$root.$emit('bv::dropdown::hide');
            expect(wrapper.emitted().menuToggled).toBeTruthy();
        });
    });
});
