import { shallowMount } from '@vue/test-utils';
import VsSkipTo from '../SkipTo';

const factoryShallowMount = () => shallowMount(VsSkipTo, {
    propsData: {
        skipToText: 'Skip to',
    },
    slots: {
        mainMenuText: 'Main menu',
        mainContentText: 'Main content',
        searchText: 'Search',
        footerText: 'Footer',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsSiteSearch', () => {
    it('should render a component with the data-test attribute `vs-skip-to`', () => {
        expect(wrapper.find('[data-test="vs-skip-to"]').exists()).toBe(true);
    });

    describe(':props', () => {
        it('should render the `skipToText` label', () => {
            expect(wrapper.attributes('aria-label')).toBe('Skip to');
            expect(wrapper.find('[data-test="vs-skip-to"]').find('.vs-skip-to__label').text()).toContain('Skip to');
        });
    });
    describe(':slots', () => {
        it('should render the content of the `mainMenuText` slot', () => {
            expect(wrapper.text()).toContain('Main menu');
        });

        it('should render the content of the `mainContentText` slot', () => {
            expect(wrapper.text()).toContain('Main content');
        });

        it('should render the content of the `searchText` slot', () => {
            expect(wrapper.text()).toContain('Search');
        });

        it('should render the content of the `footerText` slot', () => {
            expect(wrapper.text()).toContain('Footer');
        });
    });

    describe(':methods', () => {
        it('should call the `mainMenuFocus` method on the main menu link click', async() => {
            const mainMenuBtn = wrapper.find('[data-test="vs-skip-to-main-menu"]');
            const mockMethod = jest.spyOn(wrapper.vm, 'mainMenuFocus');
            mainMenuBtn.trigger('click');

            await wrapper.vm.$nextTick();

            expect(mockMethod).toHaveBeenCalled();
        });

        it('should call the `footerFocus` method on the footer link click', async() => {
            const footerBtn = wrapper.find('[data-test="vs-skip-to-footer"]');
            const mockMethod = jest.spyOn(wrapper.vm, 'footerFocus');
            footerBtn.trigger('click');

            await wrapper.vm.$nextTick();

            expect(mockMethod).toHaveBeenCalled();
        });
    });
});
