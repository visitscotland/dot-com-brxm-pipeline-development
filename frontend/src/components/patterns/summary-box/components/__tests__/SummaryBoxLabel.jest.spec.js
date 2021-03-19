import { shallowMount } from '@vue/test-utils';

import VsSummaryBoxLabel from '../SummaryBoxLabel';

const factoryShallowMount = (propsData) => shallowMount(VsSummaryBoxLabel, {
    propsData: {
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsSummaryBoxLabel', () => {
    it('should render a component with the data-test attribute `vs-summary-box-label`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-summary-box-label');
    });

    describe(':props', () => {
        it('should accept and render a `label` property', async() => {
            await wrapper.setProps({
                label: 'Test label',
            });

            expect(wrapper.text()).toContain('Test label');
        });
    });
});
