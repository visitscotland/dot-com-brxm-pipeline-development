import { shallowMount } from '@vue/test-utils';
import VsCannedSearchCuisines from '../CannedSearchCuisines';

const cuisines = [
    {
        name: 'Scottish',
    },
    {
        name: 'Vegetarian/Vegan',
    },
    {
        name: '+ More',
    },
];

const factoryShallowMount = () => shallowMount(VsCannedSearchCuisines, {
    propsData: {
        cuisines,
    },
});

describe('VsCannedSearchCuisines', () => {
    describe(':props', () => {
        it('should render each item within the `cuisines` property', () => {
            const wrapper = factoryShallowMount();

            for (let x = 0; x < cuisines.length; x++) {
                expect(wrapper.html()).toContain(cuisines[x].name);
            }
        });

        it('should not render additional `cuisines` beyond three', async() => {
            const testCats = [
                {
                    name: 'a',
                },
                {
                    name: 'b',
                },
                {
                    name: 'c',
                },
                {
                    name: 'd',
                },
                {
                    name: 'e',
                },
            ];

            const wrapper = factoryShallowMount();

            wrapper.setProps({
                cuisines: testCats,
            });

            await wrapper.vm.$nextTick();

            for (let x = 3; x < cuisines.length; x++) {
                expect(wrapper.html()).not.toContain(cuisines[x]);
            }
        });
    });
});
