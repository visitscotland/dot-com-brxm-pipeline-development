import { shallowMount, mount } from '@vue/test-utils';
import VsMegaNav from '../MegaNav';

const factoryShallowMount = (propsData) => shallowMount(VsMegaNav, {
    propsData: {
        href: 'https://www.visitscotland.com',
        menuToggleAltText: 'Open Menu',
        searchButtonText: 'Search',
        ...propsData,
    },
    slots: {
        megaNavTopMenuItems: '<div class="mega-nav-top-menu-items"></div>',
    },
});

const factoryMount = () => mount(VsMegaNav, {
    propsData: {
        href: 'https://www.visitscotland.com',
        menuToggleAltText: 'Open Menu',
        searchButtonText: 'Search',
    },
    slots: {
        megaNavTopMenuItems: '<div class="mega-nav-top-menu-items"></div>',
    },
});

describe('VsMegaNav', () => {
    it('should render a component with the data-test attribute `vs-mega-nav`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.find('[data-test=vs-mega-nav]').exists()).toBe(true);
    });

    it('should render a button with an `vs-icon--bars-mobile-menu` icon', () => {
        const wrapper = factoryMount();
        const dropdownToggle = wrapper.find('[data-test="vs-mega-nav-dropdown"]').find('.dropdown-toggle');

        expect(dropdownToggle.exists()).toBe(true);
        expect(dropdownToggle.html()).toContain('vs-icon--bars-mobile-menu');
    });

    describe(':props', () => {
        it('should display the URL `https://www.visitscotland.com` on the logo link when passed as `href` prop', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-mega-nav__logo"]').attributes().href).toBe('https://www.visitscotland.com');
        });

        it('should display sr-only `Open Menu` text within toggle button', () => {
            const wrapper = factoryMount();
            const dropdownToggle = wrapper.find('[data-test="vs-mega-nav-dropdown"]').find('.dropdown-toggle');

            expect(dropdownToggle.html()).toContain('Open Menu');
        });

        // it('should display `Search` text within the search button', () => {
        //     const wrapper = factoryShallowMount();
        //     const siteSearchStub = wrapper.find('vssitesearch-stub');

        //     expect(siteSearchStub.text()).toContain('Search');
        // });

        it('should pass `searchLabelText` prop to search form', () => {
            const wrapper = factoryShallowMount();
            const siteSearchFormStub = wrapper.find('vssitesearchform-stub');

            expect(siteSearchFormStub.attributes().labeltext).toBe('What are you looking for?');
        });

        it('should pass `searchButtonText` prop to search form', () => {
            const wrapper = factoryShallowMount();
            const siteSearchFormStub = wrapper.find('vssitesearchform-stub');

            expect(siteSearchFormStub.attributes().submitbuttontext).toBe('Search');
        });

        it('should pass `searchClearButtonText` prop to search form', () => {
            const wrapper = factoryShallowMount();
            const siteSearchFormStub = wrapper.find('vssitesearchform-stub');

            expect(siteSearchFormStub.attributes().clearbuttontext).toBe('Clear form');
        });

        it('should pass `searchCloseButtonText` prop to search form', () => {
            const wrapper = factoryShallowMount();
            const siteSearchFormStub = wrapper.find('vssitesearchform-stub');

            expect(siteSearchFormStub.attributes().closebuttontext).toBe('Close search form');
        });
    });

    describe(':slots', () => {
        it('renders content inserted in a megaNavTopMenuItems slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.findAll('.mega-nav-top-menu-items').length).toBe(1);
        });
    });

    // describe(':methods', () => {
    //     it('toggles the search form when search button is clicked', async() => {
    //         const wrapper = factoryMount();
    //         const siteSearchBtn = wrapper.find('[data-test=vs-site-search]');

    //         const siteSearchForm = wrapper.find('[data-test=vs-site-search-form]');
    //         expect(siteSearchForm.attributes('style')).toBe('display: none;');

    //         siteSearchBtn.trigger('click');
    //         await wrapper.vm.$nextTick();

    //         setTimeout(() => {
    //             expect(siteSearchForm.attributes('style')).toBe('');
    //         }, 100);
    //     });
    // });
});
