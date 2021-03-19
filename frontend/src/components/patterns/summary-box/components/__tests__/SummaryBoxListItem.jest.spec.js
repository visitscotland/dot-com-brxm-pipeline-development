import { shallowMount } from '@vue/test-utils';

import VsSummaryBoxListItem from '../SummaryBoxListItem';

const slotContent = 'Slot Content';

const factoryShallowMount = (propsData) => shallowMount(VsSummaryBoxListItem, {
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

describe('VsSummaryBoxListItem', () => {
    it('should render a component with the data-test attribute `vs-summary-box-list-item`', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-summary-box-list-item');
    });

    describe(':slots', () => {
        it('renders content inserted into the default `slot`', () => {
            expect(wrapper.text()).toContain(slotContent);
        });
    });
});
