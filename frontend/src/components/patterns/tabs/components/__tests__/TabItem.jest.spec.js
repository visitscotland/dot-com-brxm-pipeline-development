import { shallowMount } from '@vue/test-utils';

import VsTabItem from '../TabItem';

const factoryShallowMount = (propsData) => shallowMount(VsTabItem, {
    propsData: {
        ...propsData,
        title: 'First tab',
    },
});

describe('VsTabItem', () => {
    it('should render a TabItem', () => {
        const wrapper = factoryShallowMount();
        const tabItem = wrapper.find('[data-test=vs-tab__item]');

        expect(tabItem.exists()).toBe(true);
    });

    describe(':props', () => {
        it(':title should be assigned to panel id', () => {
            const wrapper = factoryShallowMount();
            const tabItem = wrapper.find('[data-test=vs-tab__item]');

            expect(tabItem.attributes('title')).toBe('First tab');
        });
    });
});
