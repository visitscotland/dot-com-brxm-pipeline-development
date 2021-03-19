import { shallowMount } from '@vue/test-utils';

import VsSummaryBoxIconWithLabel from '../SummaryBoxIconWithLabel';

const factoryShallowMount = (propsData) => shallowMount(VsSummaryBoxIconWithLabel, {
    propsData: {
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsSummaryBoxIconWithLabel', () => {
    it('should render a component with the data-test attribute `vs-summary-box-icon-with-label`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-summary-box-icon-with-label');
    });

    it('should contain a component with the data-test attribute `vs-icon`', () => {
        const vsIcon = wrapper.find('vsicon-stub');

        expect(vsIcon.exists()).toBe(true);
    });

    describe(':props', () => {
        it('should accept and render a `label` property', async() => {
            await wrapper.setProps({
                label: 'Test label',
            });

            expect(wrapper.text()).toContain('Test label');
        });

        it('should accept an `icon` property and pass it to the child vs-icon', async() => {
            await wrapper.setProps({
                icon: 'product-activities',
            });

            const vsIcon = wrapper.find('vsicon-stub');

            expect(vsIcon.attributes('name')).toBe('product-activities');
        });
    });
});
