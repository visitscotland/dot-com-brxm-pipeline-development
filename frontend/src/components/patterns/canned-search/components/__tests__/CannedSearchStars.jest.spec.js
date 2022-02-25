import { shallowMount } from '@vue/test-utils';
import VsCannedSearchStars from '../CannedSearchStars';

const factoryShallowMount = (propsData) => shallowMount(VsCannedSearchStars, {
    propsData: {
        ...propsData,
    },
});

describe('VsCannedSearchStars', () => {
    describe(':props', () => {
        it('should render a number of stars equal to `min` if `min` and `max` are the same', () => {
            const wrapper = factoryShallowMount({
                min: 4,
                max: 4,
                gold: false,
            });

            expect(wrapper.findAll('.vs-canned-search-stars__star').length).toBe(4);
        });

        it('should render one star if `min` if `min` and `max` are different', () => {
            const wrapper = factoryShallowMount({
                min: 3,
                max: 4,
                gold: false,
            });

            expect(wrapper.findAll('.vs-canned-search-stars__star').length).toBe(1);
        });

        it('should say `min-max` if `min` and `max` are different', () => {
            const wrapper = factoryShallowMount({
                min: 2,
                max: 4,
                gold: false,
            });

            expect(wrapper.html()).toContain('2-4');
        });

        it('should render purple stars if `gold` is false', () => {
            const wrapper = factoryShallowMount({
                min: 4,
                max: 4,
                gold: false,
            });

            const star = wrapper.find('.vs-canned-search-stars__star');

            expect(star.classes('vs-canned-search-stars__star--gold')).toBe(false);
        });

        it('should render gold stars if `gold` is true', () => {
            const wrapper = factoryShallowMount({
                min: 4,
                max: 4,
                gold: true,
            });

            const star = wrapper.find('.vs-canned-search-stars__star');

            expect(star.classes('vs-canned-search-stars__star--gold')).toBe(true);
        });
    });
});
