import { shallowMount } from '@vue/test-utils';
import VsCarousel from '../Carousel';

const factoryShallowMount = (slotsData) => shallowMount(VsCarousel, {
    data() {
        return {
            totalSlides: 20,
            currentPage: 0,
            maxPages: 1,
            nextDisabled: false,
            prevDisabled: true,
            currentWidth: 'lg',
        };
    },
    ...slotsData,
});

/* eslint-disable */
window.matchMedia = window.matchMedia || function() {
    return {
        matches: false,
        addListener: function() {},
        removeListener: function() {}
    };
};
/* eslint-enable */

describe('VsCarousel', () => {
    it('should render a carousel container element', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.find('[data-test="vs-carousel"]').exists()).toBe(true);
    });

    describe(':slots', () => {
        it('renders the mobile pagination correctly', async() => {
            const wrapper = factoryShallowMount({
                slots: {
                    vsCarouselOf: 'of',
                },
            });

            await wrapper.setData({
                currentPage: 1,
                totalSlides: 10,
                currentWidth: 'lg',
            });

            const pagintationText = wrapper.find('[data-test="vs-carousel__mobile-pagination"]').text();

            const formattedPagination = pagintationText.replace(/\s\s+/g, ' ');

            expect(formattedPagination).toBe('2 of 10');
        });

        it('renders the default slot correctly', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    default: '<div class="default-content">Default slot carousel content</div>',
                },
            });

            expect(wrapper.find('.default-content').exists()).toBe(true);
        });
    });

    describe(':methods', () => {
        it('sets the correct active page on arrow click', async() => {
            const wrapper = factoryShallowMount();

            await wrapper.setData({
                currentPage: 1,
                totalSlides: 10,
                currentWidth: 'lg',
            });

            await wrapper.vm.initNavigation();

            await wrapper.find('.vs-carousel__control--next').trigger('click');

            expect(wrapper.vm.currentPage).toBe(2);
        });

        it('throttles page navigation to not work within a 250ms period of a previous nav event', async() => {
            jest.useFakeTimers();

            const wrapper = factoryShallowMount();

            await wrapper.setData({
                currentPage: 1,
                totalSlides: 10,
                currentWidth: 'lg',
            });

            await wrapper.vm.initNavigation();

            await wrapper.find('.vs-carousel__control--next').trigger('click');

            expect(wrapper.vm.currentPage).toBe(2);

            await wrapper.find('.vs-carousel__control--prev').trigger('click');

            expect(wrapper.vm.currentPage).toBe(2);

            // Fast-forward throttle timer on sliderNavigate
            jest.runAllTimers();

            await wrapper.find('.vs-carousel__control--prev').trigger('click');

            expect(wrapper.vm.currentPage).toBe(1);
        });

        it('sets the correct active page on clicking navigation item', async() => {
            const wrapper = factoryShallowMount();

            await wrapper.setData({
                currentPage: 0,
                totalSlides: 10,
                currentWidth: 'lg',
                maxPages: 4,
            });

            wrapper.find('[data-test="vs-carousel__nav-3"]').trigger('click');

            expect(wrapper.vm.currentPage).toBe(2);
        });

        it('only renders the previous control if the prop is set to true', async() => {
            const wrapper = factoryShallowMount();

            await wrapper.setData({
                currentPage: 2,
                prevDisabled: false,
            });

            expect(wrapper.find('.vs-carousel__control--prev').exists()).toBe(true);

            await wrapper.setData({
                prevDisabled: true,
            });

            expect(wrapper.find('.vs-carousel__control--prev').exists()).toBe(false);
        });

        it('only renders the next control if the prop is set to true', async() => {
            const wrapper = factoryShallowMount();

            await wrapper.setData({
                nextDisabled: false,
            });

            expect(wrapper.find('.vs-carousel__control--next').exists()).toBe(true);

            await wrapper.setData({
                nextDisabled: true,
            });

            expect(wrapper.find('.vs-carousel__control--next').exists()).toBe(false);
        });
    });
});
