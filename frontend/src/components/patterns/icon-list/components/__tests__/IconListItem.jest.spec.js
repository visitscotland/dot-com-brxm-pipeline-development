import { shallowMount } from '@vue/test-utils';

import VsIconListItem from '../IconListItem';

const factoryShallowMount = (propsData) => shallowMount(VsIconListItem, {
    propsData: {
        ...propsData,
        icon: 'facility-wifi',
        label: 'wifi',
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsIconListItem', () => {
    it('should render a component with the data-test attribute `vs-icon-list__item`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-icon-list__item');
    });

    it('should contain a `vs-icon` component', () => {
        const vsLink = wrapper.find('vsicon-stub');
        expect(vsLink.exists()).toBe(true);
    });

    describe(':props', () => {
        it('should accept and render a `label` property', async() => {
            expect(wrapper.text()).toContain('wifi');
        });

        it('should accept an `icon` property and pass it to the icon', async() => {
            const vsLink = wrapper.find('vsicon-stub');
            expect(vsLink.attributes('name')).toBe('facility-wifi');
        });
    });
});
