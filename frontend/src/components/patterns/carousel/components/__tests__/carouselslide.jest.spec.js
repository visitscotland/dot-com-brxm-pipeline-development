import { shallowMount } from '@vue/test-utils';
import VsCarouselSlide from '../CarouselSlide';

const imgSrc = 'https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm';

const factoryShallowMount = () => shallowMount(VsCarouselSlide, {
    propsData: {
        imgSrc,
        imgAlt: 'Img alt',
        linkType: 'internal',
        linkUrl: 'http://www.visitscotland.com',
        category: 'Category Name',
        slideIndex: '4',
    },
    slots: {
        vsCarouselSlideHeading: 'Slide heading',
    },
    provide: () => ({
        slideCols: {
            xs: '12',
            sm: '6',
            md: '4',
            lg: '3',
        },
        visibleSlides: [4, 5, 6],
    }),
});

describe('VsCarouselSlide', () => {
    it('should render a component with the column attributes defined by injected values', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.find('[data-test="vs-carousel-slide"]').attributes('cols')).toBe('12');
        expect(wrapper.find('[data-test="vs-carousel-slide"]').attributes('sm')).toBe('6');
        expect(wrapper.find('[data-test="vs-carousel-slide"]').attributes('lg')).toBe('4');
        expect(wrapper.find('[data-test="vs-carousel-slide"]').attributes('xl')).toBe('3');
    });

    it('should contain an active class if the index value matches a visibleSlide prop', async() => {
        const wrapper = factoryShallowMount();

        expect(wrapper.find('[data-test="vs-carousel-card"').classes()).toContain('vs-carousel-slide__card--active');
    });

    describe(':slots', () => {
        it('renders default vsCarouselSlideHeading slot content', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.find('[data-test="vs-carousel-slide__title"]').text()).toContain('Slide heading');
        });
    });
});
