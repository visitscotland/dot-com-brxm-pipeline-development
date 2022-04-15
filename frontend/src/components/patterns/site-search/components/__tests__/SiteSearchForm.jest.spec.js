import { shallowMount, mount } from '@vue/test-utils';
import VsSiteSearchForm from '../SiteSearchForm';

const factoryShallowMount = (propsData) => shallowMount(VsSiteSearchForm, {
    propsData: {
        ...propsData,
    },
});

const factoryMount = (propsData) => mount(VsSiteSearchForm, {
    propsData: {
        ...propsData,
    },
});

describe('VsSiteSearchForm', () => {
    it('should render a component with the data-test attribute `vs-site-search-form`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.find('[data-test=vs-site-search-form]').exists()).toBe(true);
    });

    describe(':props', () => {
        it('should display `labelText` within the input', () => {
            const wrapper = factoryShallowMount();
            const label = wrapper.find('.vs-site-search-form__label');
            expect(label.text()).toContain('What are you looking for?');
        });

        it('should display `submitButtonText` within the submit button', () => {
            const wrapper = factoryShallowMount();
            const searchBtn = wrapper.find('.vs-site-search-form__search-button');
            expect(searchBtn.text()).toContain('Search');
        });

        it('should display `clearButtonText` (sr-only) within clear button', async() => {
            const wrapper = factoryMount();
            const searchInput = wrapper.find('input[type="search"]');
            await searchInput.setValue('Test');
            const clearBtn = wrapper.find('.vs-site-search-form__clear-button');

            expect(clearBtn.text()).toContain('Clear form');
        });

        it('should display `closeButtonText` (sr-only) within close button', () => {
            const wrapper = factoryShallowMount();
            const closeBtn = wrapper.find('.vs-site-search-form__close-button');
            expect(closeBtn.text()).toContain('Close search form');
        });
    });

    describe(':methods', () => {
        it('clears search form value when clear button clicked', async() => {
            const wrapper = factoryMount();
            const searchInput = wrapper.find('input[type="search"]');
            await searchInput.setValue('Test');
            expect(searchInput.element.value).toBe('Test');

            const clearBtn = wrapper.find('.vs-site-search-form__clear-button');
            await clearBtn.trigger('click');
            expect(searchInput.element.value).toBe('');
        });
        it('invalidates search form on button click when nothing has been entered', async() => {
            const wrapper = factoryMount();
            const submitBtn = wrapper.find('.vs-site-search-form__search-button');
            const searchInput = wrapper.find('input[type="search"]');
            submitBtn.trigger('click');
            await wrapper.vm.$nextTick();

            setTimeout(() => {
                expect(searchInput.classes()).toContain('is-invalid');
            }, 1000);
        });
        it('validates form on input', async() => {
            const wrapper = factoryMount();
            const submitBtn = wrapper.find('.vs-site-search-form__search-button');
            const searchInput = wrapper.find('input[type="search"]');
            submitBtn.trigger('click');
            await wrapper.vm.$nextTick();

            await searchInput.setValue('Test');

            setTimeout(() => {
                expect(searchInput.classes()).not.toContain('is-invalid');
            }, 100);
        });
        it('emits `toggleAction` event when close button clicked', async() => {
            const wrapper = factoryShallowMount();
            const closeBtn = wrapper.find('.vs-site-search-form__close-button');
            closeBtn.trigger('click');
            await wrapper.vm.$nextTick();

            expect(wrapper.emitted().toggleAction).toBeTruthy();
        });
    });
});
