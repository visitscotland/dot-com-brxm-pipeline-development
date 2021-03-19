import { shallowMount } from '@vue/test-utils';

import VsSummaryBoxDisplay from '../SummaryBoxDisplay';

const factoryShallowMount = (propsData) => shallowMount(VsSummaryBoxDisplay, {
    propsData: {
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsSummaryBoxDisplay', () => {
    it('should render a component with the data-test attribute `vs-summary-box-display`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-summary-box-display');
    });

    describe(':props', () => {
        it('should accept and render a `text` property', async() => {
            await wrapper.setProps({
                text: 'Test text',
            });

            expect(wrapper.text()).toContain('Test text');
        });
    });
});
