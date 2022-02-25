import { shallowMount } from '@vue/test-utils';

import VsSummaryBoxList from '../SummaryBoxList';

const slotContent = 'Slot Content';

const factoryShallowMount = (propsData) => shallowMount(VsSummaryBoxList, {
    propsData: {
        ...propsData,
    },
    slots: {
        default: slotContent,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsSummaryBoxList', () => {
    it('should render a component with the data-test attribute `vs-summary-box-list`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-summary-box-list');
    });

    describe(':slots', () => {
        it('renders content inserted into the default `slot`', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
