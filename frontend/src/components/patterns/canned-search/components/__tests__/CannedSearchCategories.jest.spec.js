import { shallowMount } from '@vue/test-utils';
import VsCannedSearchCategories from '../CannedSearchCategories';

const categories = [
    {
        name: 'Rural',
    },
    {
        name: 'Village Location',
    },
    {
        name: 'Mountains area',
    },
];

const factoryShallowMount = () => shallowMount(VsCannedSearchCategories, {
    propsData: {
        categories,
    },
});

describe('VsCannedSearchCategories', () => {
    describe(':props', () => {
        it('should render each item within the `categories` property', () => {
            const wrapper = factoryShallowMount();

            for (let x = 0; x < categories.length; x++) {
                expect(wrapper.html()).toContain(categories[x].name);
            }
        });

        it('should not render additional `categories` beyond three', async() => {
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
                categories: testCats,
            });

            await wrapper.vm.$nextTick();

            for (let x = 3; x < categories.length; x++) {
                expect(wrapper.html()).not.toContain(categories[x]);
            }
        });
    });
});
