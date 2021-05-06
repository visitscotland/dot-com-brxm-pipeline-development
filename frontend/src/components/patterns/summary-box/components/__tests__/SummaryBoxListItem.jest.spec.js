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

describe('VsSummaryBoxListItem', () => {
    it('should render a component with the data-test attribute `vs-summary-box-list-item`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-summary-box-list-item');
    });

    describe(':props', () => {
        it('should accept and render a `text` property', () => {
            const wrapper = factoryShallowMount({
                text: 'Test text',
            });

            expect(wrapper.text()).toContain('Test text');
        });

        it('should accept and render a `label` property', () => {
            const wrapper = factoryShallowMount({
                label: 'Test label',
            });

            expect(wrapper.text()).toContain('Test label');
        });

        it('should accept and render an `icon` property', () => {
            const wrapper = factoryShallowMount({
                icon: 'car',
            });

            const vsIcon = wrapper.find('vsicon-stub');

            expect(vsIcon.attributes('name')).toBe('car');
        });

        it('if an `icon` property is set, should accept and render an `iconLabel` properly', () => {
            const wrapper = factoryShallowMount({
                icon: 'car',
                iconLabel: 'A car',
            });

            expect(wrapper.text()).toContain('A car');
        });
    });
});
